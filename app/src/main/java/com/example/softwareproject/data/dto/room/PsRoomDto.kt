package com.example.softwareproject.com.example.softwareproject.data.dto

import com.example.softwareproject.com.example.softwareproject.util.DifficultyPs
import com.example.softwareproject.util.Topic

data class PsRoomDto (
    val codingRoomId : String,
    val difficultyLevel : DifficultyPs,
    val roomId : String
)