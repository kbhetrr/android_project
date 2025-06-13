package com.example.softwareproject.data.remote.participant

import com.google.firebase.Timestamp

data class ParticipantProblemStatusInfo (
    val isSolved: Boolean,
    val problemId: Int,
    val problemType: Int,
    val roomParticipantId : String,
    val userId: String,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
)