package com.example.softwareproject.data.entity.base

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Room (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "room_id")
    val id : Int
    room_
)