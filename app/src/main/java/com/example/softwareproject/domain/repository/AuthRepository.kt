package com.example.softwareproject.domain.repository

interface AuthRepository {

    fun buildAuthUrl(state: String): String

    suspend fun exchangeCodeForToken(code: String, state: String): String
}