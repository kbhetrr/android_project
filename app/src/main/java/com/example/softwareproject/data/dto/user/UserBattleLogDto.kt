package com.example.softwareproject.data.dto.user

data class UserBattleLogDto(
    val lose: Int = 0,
    val match: Int = 0,
    val rate: Int = 0,
    val userId: String = "",
    val win: Int = 0
)
