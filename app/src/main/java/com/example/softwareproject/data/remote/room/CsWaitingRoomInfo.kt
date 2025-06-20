package com.example.softwareproject.data.remote.room


import com.example.softwareproject.util.DifficultyCs
import com.example.softwareproject.util.RoomType

data class CsWaitingRoomInfo(
    val userId: String,
    val title: String,
    val type: RoomType,
    val difficultyCs: DifficultyCs,
    val problemCount: Int
)