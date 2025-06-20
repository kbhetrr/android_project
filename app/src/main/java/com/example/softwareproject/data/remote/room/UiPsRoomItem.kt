package com.example.softwareproject.data.remote.room

import com.example.softwareproject.util.DifficultyCs
import com.example.softwareproject.util.DifficultyPs
import com.example.softwareproject.util.Topic

data class UiPsRoomItem (
    val roomId: String,
    val roomTitle: String,
    val difficulty: DifficultyPs,
    val githubName: String?,
    val description: String?
)