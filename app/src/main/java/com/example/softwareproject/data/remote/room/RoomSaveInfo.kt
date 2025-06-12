package com.example.softwareproject.com.example.softwareproject.data.remote.room

import com.google.firebase.Timestamp

data class RoomSaveInfo (
    val roomId: String,
    val description: String,
    val problemCount: Int,
    val roomState: String,
    val roomTitle: String,
    val roomType: String,
    val userId: String,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
)