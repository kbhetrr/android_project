package com.example.softwareproject.data.remote.room

import com.example.softwareproject.util.DifficultyPs
import com.example.softwareproject.util.DifficultyCs
import com.example.softwareproject.util.RoomType

data class PsWaitingRoomInfo (
    val userId: String,
    val title: String,
    val type: RoomType,
    val difficultyPs: DifficultyPs,
    val problemCount: Int
)