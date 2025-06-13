package com.example.softwareproject.domain.usecase.login.github

import com.example.softwareproject.data.remote.github.GitHubApi
import com.example.softwareproject.data.remote.user.GitHubUser
import javax.inject.Inject

class GetGitHubUserInfoUseCase @Inject constructor(
    private val api: GitHubApi
) {
    suspend operator fun invoke(accessToken: String): GitHubUser {
        return api.getUserInfo("Bearer $accessToken")
    }
}