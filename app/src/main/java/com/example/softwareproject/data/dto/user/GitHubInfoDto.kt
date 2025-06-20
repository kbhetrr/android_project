package com.example.softwareproject.data.dto.user

data class GitHubInfoDto(
    val avatarUrl: String = "",
    val bio: String = "",
    val email: String = "",
    val firebaseUid: String = "",
    val followers: Int = 0,
    val following: Int = 0,
    val githubName: String = "",
    val userId: String = ""
)
