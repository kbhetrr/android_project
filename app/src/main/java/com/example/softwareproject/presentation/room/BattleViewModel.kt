package com.example.softwareproject.com.example.softwareproject.presentation.room

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.softwareproject.BuildConfig
import com.example.softwareproject.com.example.softwareproject.domain.usecase.room.BattleUseCase
import com.example.softwareproject.domain.usecase.room.RoomUseCase
import com.example.softwareproject.util.RoomType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BattleViewModel @Inject constructor(
    private val roomUseCase: RoomUseCase,
    private val battleUseCase: BattleUseCase
) :ViewModel()
{
    suspend fun createProblem(roomId : String){

        val roomType = roomUseCase.getRoomType(roomId)
        if(roomType == RoomType.CS){
            Log.d("GeminiAPI", "BattleViewModel : CreateProblem 시작")
            battleUseCase.createCsProblem(roomId)
        }
        else{
            battleUseCase.createPsProblem(roomId)
            Log.d("GeminiAPI", "BattleViewModel : CreateProblem-PS 시작")
        }
    }

    suspend fun getRoomType(roomId: String) : RoomType{
        return roomUseCase.getRoomType(roomId)
    }
    suspend fun createRoomParticipant(roomId: String) {
        battleUseCase.createRoomParticipant(roomId)
    }
    suspend fun createParticipantProblemState(roomId: String){
        battleUseCase.createParticipantProblemState()
    }
}