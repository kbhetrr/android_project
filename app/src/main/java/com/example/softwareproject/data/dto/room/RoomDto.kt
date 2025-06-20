package com.example.softwareproject.data.dto.room

import com.example.softwareproject.util.RoomState
import com.example.softwareproject.util.RoomType
import com.google.firebase.Timestamp

data class RoomDto(
    val createBy: String = "",
    val description: String = "",
    val userId: String = "",
    val problemCount: Int = 0,
    val roomId: String = "",
    val roomState: RoomState = RoomState.WAITING, // 기본 상태
    val roomTitle: String = "",
    val roomType: RoomType = RoomType.CS, // 기본 타입
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)
