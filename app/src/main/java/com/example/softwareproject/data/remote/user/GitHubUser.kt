package com.example.softwareproject.com.example.softwareproject.data.remote.user

data class GitHubUser(
    val id: Long,
    val login: String,
    val name: String?,
    val email: String?,
    val avatarUrl: String?,
    val bio: String?,
    val followers: Int,
    val following: Int
)