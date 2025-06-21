package com.example.softwareproject.data.dto.room

import com.example.softwareproject.util.UserRole
import com.google.firebase.Timestamp


data class RoomParticipantDto(
    val userId: String = "",
    val attack: Int = 0,
    val hp: Int = 0,
    val maxHp: Int = 100,
    val shield: Int = 0,
    val role: UserRole = UserRole.GUEST,
    val roomId: String = "",
    val solvedProblem: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now()
)
