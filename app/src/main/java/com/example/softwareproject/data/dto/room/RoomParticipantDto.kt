package com.example.softwareproject.data.dto.room

import com.example.softwareproject.util.UserRole
import com.google.firebase.Timestamp
import java.sql.Time

data class RoomParticipantDto (
    val userId: String,
    val attack: Int,
    val hp: Int,
    val shield: Int,
    val role : UserRole,
    val roomId: String,
    val solvedProblem: Int,
    val createdAt: Timestamp,
    val updatedAt: Timestamp,
)