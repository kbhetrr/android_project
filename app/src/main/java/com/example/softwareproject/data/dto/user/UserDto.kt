package com.example.softwareproject.data.dto.user

import com.google.firebase.Timestamp

data class UserDto (
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
    val userId: String
)