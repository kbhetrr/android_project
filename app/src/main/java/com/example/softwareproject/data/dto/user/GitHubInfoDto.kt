package com.example.softwareproject.com.example.softwareproject.data.dto.user

import kotlinx.metadata.internal.metadata.serialization.StringTable

data class GitHubInfoDto (
    val avatarUrl: String,
    val bio :String,
    val email: String,
    val firebaseUid : String,
    val followers: Int,
    val following : Int,
    val githubName: String,
    val userId: String
)
