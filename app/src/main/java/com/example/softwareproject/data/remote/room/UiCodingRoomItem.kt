package com.example.softwareproject.data.remote.room

import com.example.softwareproject.util.BaekjoonTier

data class UiCodingRoomItem(
    val roomId: String,
    val roomTitle: String,
    val difficulty: BaekjoonTier,
    val githubName: String?,
    val description: String?

)