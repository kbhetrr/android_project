package com.example.softwareproject.data.dto.room

import com.example.softwareproject.util.DifficultyCs
import com.example.softwareproject.util.Topic

data class CsRoomDto(
    val csRoomId: String = "",
    val difficultyLevel: DifficultyCs = DifficultyCs.EASY,
    val roomId: String = "",
    val topic: Topic = Topic.OPERATINGSYSTEM
)
