package com.example.softwareproject.domain.repository

import com.example.softwareproject.data.entity.User

interface UserRepository {
    suspend fun getUserInfo(userId: Int): User
}