package com.example.softwareproject.data.nosql_entity

import com.google.firebase.Timestamp
import com.example.softwareproject.util.UserRole

data class RoomParticipant(
    var roomId: String = "",
    var userId: String = "",
    var solvedProblem: Int = 0,
    var hp: Int = 0,
    var attack: Int = 0,
    var shield: Int = 0,
    var role: UserRole = UserRole.GUEST,
    var createdAt: Timestamp? = null,
    var updatedAt: Timestamp? = null
)
