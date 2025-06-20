package com.example.softwareproject.data.dto.room

import com.example.softwareproject.util.UserRole
import com.google.firebase.Timestamp

data class RoomParticipantDto(
    val userId: String = "",
    val attack: Int = 0,
    val hp: Int = 0,
    val shield: Int = 0,
    val role: UserRole = UserRole.GUEST, // 기본 역할 지정 (enum)
    val roomId: String = "",
    val solvedProblem: Int = 0,
    val createdAt: Timestamp? = null,     // Firestore Timestamp는 nullable 처리
    val updatedAt: Timestamp? = null
)
