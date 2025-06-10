package com.example.softwareproject.domain.usecase.login.github

import com.example.softwareproject.domain.repository.AuthRepository
import javax.inject.Inject

class BuildAuthUrlUseCase @Inject constructor(
    private val authRepo: AuthRepository
){
    operator fun invoke(state: String): String =
        authRepo.buildAuthUrl(state)
}