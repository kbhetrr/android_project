package com.example.softwareproject.domain.usecase.login.github

import com.example.softwareproject.domain.repository.AuthRepository
import javax.inject.Inject

class ExchangeGithubCodeUseCase (
    private val authRepo: AuthRepository
) {
    class ExchangeCodeForTokenUseCase @Inject constructor(
        private val repo: AuthRepository
    ) {
        suspend operator fun invoke(code: String, state: String): String =
            repo.exchangeCodeForToken(code, state)
    }
}
