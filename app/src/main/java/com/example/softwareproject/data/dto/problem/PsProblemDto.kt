package com.example.softwareproject.data.dto.problem

data class PsProblemDto (
    val acceptedUserCount: Int,
    val averageTries: Int,
    val codingRoomId: String,
    val problemId: String,
    val problemIndex : String,
    val tag : String,
    val title: String
    )