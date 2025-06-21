package com.example.softwareproject.presentation.room.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softwareproject.com.example.softwareproject.domain.usecase.room.BattleUseCase
import com.example.softwareproject.com.example.softwareproject.domain.usecase.room.UserUseCase
import com.example.softwareproject.data.dto.problem.CsProblemDto
import com.example.softwareproject.data.dto.room.RoomParticipantDto
import com.example.softwareproject.domain.usecase.room.ProblemUseCase
import com.example.softwareproject.domain.usecase.room.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CsBattleViewModel @Inject constructor(
    private val problemUseCase: ProblemUseCase,
    private val battleUseCase : BattleUseCase,
    private val userUseCase: UserUseCase,
    private val roomUseCase: RoomUseCase
) : ViewModel() {
    private val _battleResult = MutableLiveData<String?>()
    val battleResult: LiveData<String?> = _battleResult

    private val _selectedAnswerIndex = MutableLiveData<Int>() // 1~4
    val selectedAnswerIndex: LiveData<Int> = _selectedAnswerIndex

    private val _problemCount = MutableLiveData<Int>()
    val problemCount: LiveData<Int> = _problemCount

    private val _currentProblem = MutableLiveData<CsProblemDto?>()
    val currentProblem: LiveData<CsProblemDto?> = _currentProblem

    private val _yourHp = MutableLiveData<Int>()
    val yourHp: LiveData<Int> = _yourHp

    private val _yourMaxHp = MutableLiveData<Int>()
    val yourMaxHp: LiveData<Int> = _yourMaxHp

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
            val csRoom = roomUseCase.getCsRoomInfo(roomId)
            val problem = csRoom?.let { problemUseCase.getCsProblemByIndex(it.csRoomId, problemIndex) }
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
            problemUseCase.deleteCsProblem(roomId)
//            roomUseCase.deleteCsRoom(roomId)
            roomUseCase.deleteRoomParticipant(roomId)
            roomUseCase.deleteParticipantProblemStatus(roomId)
//            roomUseCase.deleteRoom(roomId)
            }
        }
    }
    fun attackOpponent(roomId: String) {
        viewModelScope.launch {

            val selected = _selectedAnswerIndex.value
            val problem = _currentProblem.value

            if(selected == null || problem == null) return@launch

            val isCorrect = selected == problem.correctChoice.toInt()

            if (isCorrect) {

                val opponent = battleUseCase.getOpponentRoomParticipant(roomId)
                val me = battleUseCase.getCurrentRoomParticipant(roomId)
                val newHp = (opponent?.hp ?: 0) - 1
                battleUseCase.updateParticipantHp(opponent?.userId ?: return@launch, roomId, newHp.coerceAtLeast(0))

                if (newHp == 0) {
                    me?.let { battleUseCase.finishGame(roomId, winnerUserId = it.userId,losserUserId = opponent.userId) }
                    _battleResult.value = "WIN"
                }
            } else {
                val me = battleUseCase.getCurrentRoomParticipant(roomId)
                val opponent = battleUseCase.getOpponentRoomParticipant(roomId)
                val newHp = (me?.hp ?: 0) - 1
                battleUseCase.updateParticipantHp(me?.userId ?: return@launch, roomId, newHp.coerceAtLeast(0))

                if (newHp == 0) {
                    if (opponent != null) {
                        battleUseCase.finishGame(roomId, winnerUserId = opponent.userId, losserUserId = me.userId)
                        _battleResult.value = "LOSE"
                    }
                    // ➕ 여기서 UI에 알림 보내는 LiveData도 emit 가능
                }
            }
        }
    }

    fun selectAnswer(index: Int) {
        _selectedAnswerIndex.value = index
    }

    fun observeParticipants(roomId: String) {
        viewModelScope.launch {
            battleUseCase.observeRoomParticipants(roomId).collect { participants ->
                val currentUserId = userUseCase.getCurrentUserAbility()?.userId
                participants.forEach { participant ->
                    if (participant.userId == currentUserId) {
                        _yourHp.postValue(participant.hp)
                        _yourMaxHp.postValue(participant.maxHp)
                    } else {
                        _opponentHp.postValue(participant.hp)
                        _opponentMaxHp.postValue(participant.maxHp)
                    }
                }
            }
        }
    }

}
