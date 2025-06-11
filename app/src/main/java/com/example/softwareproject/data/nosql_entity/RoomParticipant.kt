package com.example.softwareproject.com.example.softwareproject.data.nosql_entity

import com.google.firebase.Timestamp
import com.example.softwareproject.util.UserRole

data class RoomParticipant(
    var room_participant_id: String = "",
    var room_id: String = "",
    var user_id: String = "",
    var solved_problem: Int = 0,
    var hp: Int = 0,
    var attack: Int = 0,
    var shield: Int = 0,
    var rold: UserRole = UserRole.GUEST,
    var created_at: Timestamp? = null,
    var updated_at: Timestamp? = null
)
