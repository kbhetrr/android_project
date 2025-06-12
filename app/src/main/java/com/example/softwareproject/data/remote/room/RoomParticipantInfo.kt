package com.example.softwareproject.com.example.softwareproject.data.remote.room

import com.google.firebase.Timestamp

data class RoomParticipantInfo (
    val attack: Int,
    val hp: Int,
    val shield: Int,
    val roomId: String,
    val solvedProblem: Int,
    val userId: String,
    val role: String,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
)