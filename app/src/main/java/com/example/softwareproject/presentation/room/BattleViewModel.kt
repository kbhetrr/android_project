package com.example.softwareproject.com.example.softwareproject.presentation.room

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.softwareproject.com.example.softwareproject.domain.usecase.room.BattleUseCase
import com.example.softwareproject.domain.usecase.room.RoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BattleViewModel @Inject constructor(
    private val roomUseCase: RoomUseCase,
    private val battleUseCase: BattleUseCase
) :ViewModel()
{
    suspend fun createCsProblem(roomId : String){
        battleUseCase.createCsProblem(roomId)
    }
}