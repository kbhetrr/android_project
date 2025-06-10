package com.example.softwareproject.data.remote

import com.squareup.moshi.Json

data class GitHubTokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "token_type")   val tokenType: String,
    val scope: String
)