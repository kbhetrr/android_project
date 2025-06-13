package com.example.softwareproject.data.nosql_entity


import com.example.softwareproject.util.RoomType
import com.google.firebase.Timestamp

data class ParticipantProblemStatus(
    var roomParticipantId: String = "",
    var userId: String="",
    var problemId: Int = 0,
    var problemType: RoomType = RoomType.CS,
    var isSolved: Boolean = false,
    var createdAt: Timestamp? = null,
    var updatedAt: Timestamp? = null
)