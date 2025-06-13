package com.example.softwareproject.data.nosql_entity


import com.example.softwareproject.util.BaekjoonTier

data class CodingRoom(
    var codingRoomId: String = "",
    var roomId: String = "",
    var difficultyLevel: BaekjoonTier = BaekjoonTier.GOLD_1
)