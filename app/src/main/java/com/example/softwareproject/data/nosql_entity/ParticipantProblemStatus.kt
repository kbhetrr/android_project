package com.example.softwareproject.com.example.softwareproject.data.nosql_entity


import com.example.softwareproject.util.RoomType
import com.google.firebase.Timestamp

data class ParticipantProblemStatus(
    var participant_problem_status_id: String = "",
    var room_participant_id: String = "",
    var problem_id: Int = 0,
    var problem_type: RoomType = RoomType.CS,
    var is_solved: Boolean = false,
    var created_at: Timestamp? = null,
    var updated_at: Timestamp? = null
)