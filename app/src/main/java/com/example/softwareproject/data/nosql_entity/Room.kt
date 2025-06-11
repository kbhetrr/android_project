package com.example.softwareproject.com.example.softwareproject.data.nosql_entity

import com.example.softwareproject.util.RoomState
import com.example.softwareproject.util.RoomType
import com.google.firebase.Timestamp

data class Room(
    var room_id: String = "",
    var room_title: String = "",
    var problem_count: Int = 0,
    var room_type: RoomType = RoomType.CS,
    var room_state: RoomState = RoomState.WAITING,
    var description: String = "",
    var created_at: Timestamp? = null,
    var updated_at: Timestamp? = null
)