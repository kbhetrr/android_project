package com.example.softwareproject.com.example.softwareproject.data.dto

import com.example.softwareproject.com.example.softwareproject.util.DifficultyPs
import com.example.softwareproject.util.DifficultyCs
import com.example.softwareproject.util.Topic

data class CsRoomDto (
    val csRoomId: String,
    val difficultyLevel: DifficultyPs,
    val roomId : String,
    val topic : Topic
)