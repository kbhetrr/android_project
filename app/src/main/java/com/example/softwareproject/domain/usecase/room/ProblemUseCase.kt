package com.example.softwareproject.domain.usecase.room

import com.example.softwareproject.domain.repository.ProblemRepository
import javax.inject.Inject

class ProblemUseCase @Inject constructor(
    private val problemRepository: ProblemRepository
) {


}