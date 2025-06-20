package com.example.softwareproject.data.dto.room

import com.example.softwareproject.util.DifficultyPs
import com.example.softwareproject.util.Topic

data class CsRoomDto (
    val csRoomId: String,
    val difficultyLevel: DifficultyPs,
    val roomId : String,
    val topic : Topic
)