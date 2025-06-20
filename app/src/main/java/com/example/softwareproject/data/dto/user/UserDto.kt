package com.example.softwareproject.data.dto.user

import com.google.firebase.Timestamp

data class UserDto(
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
    val userId: String = ""
)
