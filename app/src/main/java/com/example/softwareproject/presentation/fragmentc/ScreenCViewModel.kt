package com.example.softwareproject.com.example.softwareproject.presentation.fragmentc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softwareproject.com.example.softwareproject.domain.usecase.room.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenCViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel(){
    fun saveBaekjoonInfo(solvedAcHandle: String) {
        viewModelScope.launch {
            userUseCase.saveBaekjoonInfo(solvedAcHandle)
        }
    }
}