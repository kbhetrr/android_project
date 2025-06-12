package com.example.softwareproject.com.example.softwareproject.data.remote.user

data class UserSaveInfo (
    val userId: String,
    val name: String,
    val email:String,
    val bio: String,
    val avatarUrl : String,
    val followers: Int,
    val followings: Int,
    val firebaseUid: String
)