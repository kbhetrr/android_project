package com.example.softwareproject.domain.repository

import com.example.softwareproject.com.example.softwareproject.data.remote.user.UserFullInfo
import com.example.softwareproject.com.example.softwareproject.data.remote.user.UserSaveInfo

interface UserRepository {
    suspend fun getUserInfo(userId: String) : UserFullInfo
    suspend fun createUserInfo(userSaveInfo:UserSaveInfo)
    suspend fun isUserExists(userId: String): Boolean
}