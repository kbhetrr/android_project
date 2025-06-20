package com.example.softwareproject.data.dto.user

data class UserAbilityDto(
    val userId: String = "",
    val attack: Int = 0,
    val level: Int = 1,
    val hp: Int = 100,
    val shield: Int = 0,
    val exp: Int = 0,
    val targetExp: Int = 100
)
