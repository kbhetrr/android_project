package com.example.softwareproject.com.example.softwareproject.presentation.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softwareproject.data.dto.problem.CsProblemDto
import com.example.softwareproject.domain.repository.ProblemRepository
import com.example.softwareproject.domain.usecase.room.ProblemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.utils.io.tryCopyException
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CsBattleViewModel @Inject constructor(
    private val problemUseCase: ProblemUseCase
) : ViewModel() {


}