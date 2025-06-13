package com.example.softwareproject.com.example.softwareproject.data.remote.room

import com.example.softwareproject.util.BaekjoonTier
import com.example.softwareproject.util.DifficultyCs
import com.example.softwareproject.util.RoomState
import com.example.softwareproject.util.RoomType
import com.example.softwareproject.util.Topic
import com.google.firebase.Timestamp

data class CodingRoomSaveInfo (
    val roomId: String,
    val description: String,
    val problemCount: Int,
    val roomState: RoomState,
    val roomTitle: String,
    val roomType: RoomType,
    val userId: String,
    var difficultyLevel: BaekjoonTier,
    val createdAt: Timestamp,
    val updatedAt: Timestamp
)