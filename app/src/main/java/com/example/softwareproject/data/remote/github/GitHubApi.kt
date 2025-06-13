package com.example.softwareproject.data.remote.github

import com.example.softwareproject.data.remote.user.GitHubUser
import retrofit2.http.GET
import retrofit2.http.Header

interface GitHubApi {
    @GET("user")
    suspend fun getUserInfo(
        @Header("Authorization") authHeader: String
    ): GitHubUser
}