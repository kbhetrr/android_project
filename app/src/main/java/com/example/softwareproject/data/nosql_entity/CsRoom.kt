package com.example.softwareproject.data.nosql_entity

import com.example.softwareproject.util.Topic
import com.example.softwareproject.util.DifficultyCs

data class CsRoom(
    var csRoomId: String = "",
    var roomId: String = "",
    var topic: Topic = Topic.NETWORK,
    var difficultyLevel: DifficultyCs = DifficultyCs.EASY
)
