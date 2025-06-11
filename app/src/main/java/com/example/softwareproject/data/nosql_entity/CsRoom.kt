package com.example.softwareproject.com.example.softwareproject.data.nosql_entity

import com.example.softwareproject.util.Topic
import com.example.softwareproject.util.DifficultyCs

data class CsRoom(
    var cs_room_id: String = "",
    var room_id: String = "",
    var topic: Topic = Topic.NETWORK,
    var difficulty_level: DifficultyCs = DifficultyCs.EASY
)
