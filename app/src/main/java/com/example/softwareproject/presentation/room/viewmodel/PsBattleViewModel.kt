package com.example.softwareproject.com.example.softwareproject.presentation.room.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softwareproject.com.example.softwareproject.domain.usecase.room.BattleUseCase
import com.example.softwareproject.com.example.softwareproject.domain.usecase.room.UserUseCase
import com.example.softwareproject.data.dto.problem.CsProblemDto
import com.example.softwareproject.data.dto.problem.PsProblemDto
import com.example.softwareproject.domain.usecase.room.ProblemUseCase
import com.example.softwareproject.domain.usecase.room.RoomUseCase
import com.example.softwareproject.util.RoomState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PsBattleViewModel @Inject constructor(
    private val problemUseCase: ProblemUseCase,
    private val battleUseCase : BattleUseCase,
    private val userUseCase: UserUseCase,
    private val roomUseCase: RoomUseCase
) : ViewModel()
{

    private val _battleResult = MutableLiveData<String?>()
    val battleResult: LiveData<String?> = _battleResult

    private val _problemCount = MutableLiveData<Int>()
    val problemCount: LiveData<Int> = _problemCount

    private val _currentProblem = MutableLiveData<PsProblemDto?>()
    val currentProblem: LiveData<PsProblemDto?> = _currentProblem

    private val _yourHp = MutableLiveData<Int>()
    val yourHp: LiveData<Int> = _yourHp

    private val _yourMaxHp = MutableLiveData<Int>()
    val yourMaxHp: LiveData<Int> = _yourMaxHp

    private val _ToastData = MutableLiveData<String>()
    val toastData: LiveData<String> get() = _ToastData

    private val _solvedProblems = MutableLiveData<MutableSet<Int>>(mutableSetOf())
    val solvedProblems: MutableLiveData<MutableSet<Int>> get() = _solvedProblems

    private val _hideProblemUi = MutableLiveData<Boolean>()
    val hideProblemUi: LiveData<Boolean> get() = _hideProblemUi

    val yourHpStatus = MediatorLiveData<Pair<Int, Int>>().apply {
        addSource(_yourHp) { hp ->
            val max = _yourMaxHp.value ?: 100
            value = Pair(hp ?: 0, max)
        }
        addSource(_yourMaxHp) { max ->
            val hp = _yourHp.value ?: 0
            value = Pair(hp, max ?: 100)
        }
    }

    private val _opponentHp = MutableLiveData<Int>()
    val opponentHp: LiveData<Int> = _opponentHp

    private val _opponentMaxHp = MutableLiveData<Int>()
    val opponentMaxHp: LiveData<Int> = _opponentMaxHp

    val opponentHpStatus = MediatorLiveData<Pair<Int, Int>>().apply {
        addSource(_opponentHp) { hp ->
            val max = _opponentMaxHp.value ?: 100
            value = Pair(hp ?: 0, max)
        }
        addSource(_opponentMaxHp) { max ->
            val hp = _opponentHp.value ?: 0
            value = Pair(hp, max ?: 100)
        }
    }


    fun loadProblemCount(roomId: String) {
        viewModelScope.launch {
            val roomInfo = problemUseCase.getRoomInfo(roomId)
            _problemCount.value = roomInfo?.problemCount ?: 10
        }
    }

    fun loadProblem(roomId: String, problemIndex: Int) {
        viewModelScope.launch {
            val psRoom = roomUseCase.getPsRoomInfo(roomId)
            val problem = psRoom?.let { problemUseCase.getPsProblemByIndex(it.codingRoomId, problemIndex) }
            _currentProblem.value = problem
        }
    }
    fun loadAbility(roomId: String) {
        viewModelScope.launch {
            val room = roomUseCase.getRoom(roomId) ?: return@launch
            val currentUser = userUseCase.getCurrentUserAbility()
            val opponentUser = userUseCase.getOpponentUserAbility(roomId)
            val currentParticipantUser = battleUseCase.getCurrentRoomParticipant(roomId)
            val opponentParticipantUser = battleUseCase.getOpponentRoomParticipant(roomId)


            _yourHp.value = currentParticipantUser?.hp
            _yourMaxHp.value = currentUser?.hp

            _opponentHp.value = opponentParticipantUser?.hp
            _opponentMaxHp.value = opponentUser?.hp

            Log.d("TabCsFragment", "받은 방 개수: ${_yourHp.value} ${yourMaxHp.value} ${_opponentHp.value} ${_opponentMaxHp.value}")
        }
    }
    fun giveUp(roomId: String) {
        viewModelScope.launch {
            withContext(NonCancellable) {
                val currentUser = battleUseCase.getCurrentRoomParticipant(roomId)
                val opponentUser = battleUseCase.getOpponentRoomParticipant(roomId)

                if (opponentUser != null) {
                    if (currentUser != null) {
                        battleUseCase.finishGame(
                            roomId = roomId,
                            winnerUserId = opponentUser.userId,
                            losserUserId = currentUser.userId
                        )
                    }
                }
            }
        }
    }
    fun attackOpponent(roomId: String, damage: Int) {
        viewModelScope.launch {
            val problem = _currentProblem.value ?: return@launch
            val problemId = problem.problemId.toString()

            val currentUser = battleUseCase.getCurrentRoomParticipant(roomId) ?: return@launch
            val opponentUser = battleUseCase.getOpponentRoomParticipant(roomId) ?: return@launch

            val baekjoonInfo = userUseCase.getBaekjoonInfo(currentUser.userId)
            val baekjoonId = baekjoonInfo?.baekjoonId


            val latestSolvedProblemIds = baekjoonId?.let { userUseCase.getLatestSolvedProblemIds(it) }
            latestSolvedProblemIds?.let {
                userUseCase.isExistedBaekjoonInfo(
                    userId = currentUser.userId,
                    newSolvedProblems = it
                )
            }
            val hasSolved = latestSolvedProblemIds?.contains(problemId) == true
            if (hasSolved) {

                markProblemAsSolved(problem.problemIndex.toInt())

                val newOpponentHp = (opponentUser.hp - damage).coerceAtLeast(0)
                battleUseCase.updateParticipantHp(opponentUser.userId, roomId, newOpponentHp)
                _ToastData.value = "공격 성공! 데미지: ${damage}"
                _hideProblemUi.value = true
                if(newOpponentHp <= 0)
                {
                    _battleResult.value = "WIN"
                    battleUseCase.finishGame(roomId, winnerUserId = currentUser.userId, losserUserId = opponentUser.userId)
                }
                val allSolved =
                    currentUser.let { roomUseCase.isAllSolved(roomId = roomId, userId = it.userId) }
                if (allSolved) {
                    markProblemAsSolved(problem.problemIndex.toInt())

                    currentUser.let {
                        battleUseCase.finishGame(
                            roomId,
                            winnerUserId = it.userId,
                            losserUserId = it.userId
                        )
                    }
                }
            } else {
                val newYourHp = (currentUser.hp - damage).coerceAtLeast(0)
                battleUseCase.updateParticipantHp(currentUser.userId, roomId, newYourHp)
                _ToastData.value = "공격 실패... 데미지: ${damage}"
                if(newYourHp <= 0){
                    _battleResult.value = "LOSE"
                    battleUseCase.finishGame(roomId, winnerUserId = opponentUser.userId, losserUserId = currentUser.userId)
                }
            }
        }
    }
    fun markProblemAsSolved(index: Int) {
        val updated = _solvedProblems.value ?: mutableSetOf()
        updated.add(index)
        _solvedProblems.value = updated
    }

    fun observeParticipantHp(roomId: String) {
        viewModelScope.launch {
            battleUseCase.observeRoomParticipants(roomId).collect { participants ->
                val currentUser = userUseCase.getCurrentUserAbility()
                val currentParticipant = participants.find { it.userId == currentUser?.userId }
                val opponentParticipant = participants.find { it.userId != currentUser?.userId }

                _yourHp.value = currentParticipant?.hp
                _opponentHp.value = opponentParticipant?.hp
            }
        }
    }
    fun observeRoomState(roomId: String) {
        viewModelScope.launch {
            roomUseCase.observeRoomState(roomId)
                .collect { state ->
                    if (state == RoomState.FINISHED) {
                        checkResult(roomId)
                    }
                }
        }
    }
    private suspend fun checkResult(roomId: String) {
        val uid = userUseCase.getCurrentUserAbility()?.userId ?: return
        val participant = battleUseCase.getCurrentRoomParticipant(roomId)
        val opponent = battleUseCase.getOpponentRoomParticipant(roomId)

        if (opponent != null) {
            if (participant != null) {
                when {
                    participant.hp <= 0 -> _battleResult.value = "LOSE"
                    opponent.hp <= 0 -> _battleResult.value = "WIN"
                    else -> _battleResult.value = "DRAW" // 혹시 모를 무승부 대비
                }
            }
        }
    }
}