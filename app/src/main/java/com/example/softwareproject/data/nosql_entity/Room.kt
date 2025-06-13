package com.example.softwareproject.com.example.softwareproject.data.nosql_entity

import com.example.softwareproject.util.RoomState
import com.example.softwareproject.util.RoomType
import com.google.firebase.Timestamp

data class Room(
    var roomId: String = "",
    var roomTitle: String = "",
    var problemCount: Int = 0,
    var roomType: RoomType = RoomType.CS,
    var roomState: RoomState = RoomState.WAITING,
    var description: String = "",
    var createdBy: String = "",
    var createdAt: Timestamp? = null,
    var updatedAt: Timestamp? = null
)