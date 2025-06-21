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
import com.example.softwareproject.domain.usecase.room.ProblemUseCase
import com.example.softwareproject.domain.usecase.room.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CsBattleViewModel @Inject constructor(
    private val problemUseCase: ProblemUseCase,
    private val battleUseCase : BattleUseCase,
    private val userUseCase: UserUseCase,
    private val roomUseCase: RoomUseCase
) : ViewModel() {

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
}
