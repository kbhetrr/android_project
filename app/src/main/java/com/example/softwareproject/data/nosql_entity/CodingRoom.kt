package com.example.softwareproject.com.example.softwareproject.data.nosql_entity


import com.example.softwareproject.util.BaekjoonTier

data class CodingRoom(
    var coding_room_id: String = "",
    var room_id: String = "",
    var difficulty_level: BaekjoonTier = BaekjoonTier.GOLD_1
)