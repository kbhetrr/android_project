package com.example.softwareproject.com.example.softwareproject.data.dto.room

import com.google.firebase.Timestamp


data class ParticipantProblemState(
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
    val isSolved: Boolean = false,
    val problemIndex: Int = 0,
    val roomId: String = "",
    val userId: String = ""
)