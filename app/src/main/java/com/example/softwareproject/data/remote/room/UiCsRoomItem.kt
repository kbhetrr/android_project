package com.example.softwareproject.data.remote.room

import com.example.softwareproject.util.DifficultyCs
import com.example.softwareproject.util.Topic

data class UiCsRoomItem(
    val roomId: String,
    val roomTitle: String,
    val topic: Topic,
    val difficulty: DifficultyCs,
    val githubName: String?,
    val description: String?

)