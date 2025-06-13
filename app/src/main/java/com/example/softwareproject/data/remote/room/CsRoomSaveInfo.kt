package com.example.softwareproject.com.example.softwareproject.data.remote.room

import com.example.softwareproject.util.RoomState
import com.example.softwareproject.util.RoomType
import com.google.firebase.Timestamp

data class RoomSaveInfo (
    val roomId: String,
    val description: String,
    val problemCount: Int,
    val roomState: RoomState,
    val roomTitle: String,
    val roomType: RoomType,
    val userId: String,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
)