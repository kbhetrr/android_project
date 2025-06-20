package com.example.softwareproject.data.dto.room

import com.example.softwareproject.util.RoomState
import com.example.softwareproject.util.RoomType
import com.google.firebase.Timestamp

data class RoomDto (
    val createBy: String,
    val description : String,
    val userId : String,
    val problemCount : Int,
    val roomId : String,
    val roomState: RoomState,
    val roomTitle: String,
    val roomType: RoomType,
    val createdAt : Timestamp ?= null,
    val updatedAt : Timestamp ?= null
)
