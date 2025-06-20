package com.example.softwareproject.data.dto.room

import com.example.softwareproject.util.DifficultyPs

data class PsRoomDto (
    val codingRoomId : String,
    val difficultyLevel : DifficultyPs,
    val roomId : String
)