package com.example.softwareproject.com.example.softwareproject.data.dto.user

data class BaekjoonInfoDto(
    val userId: String = "",
    val baekjoonId: String = "",
    val count: Int = 0,
    val items: List<String> = emptyList()
)