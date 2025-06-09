package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.softwareproject.data.entity.base.BaseEntity
import com.example.softwareproject.data.entity.base.RoomState
import com.example.softwareproject.data.entity.base.RoomType



data class Room (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "room_id")
    val id : Int,
    @ColumnInfo(name="room_title")
    val roomTitle : String,
    @ColumnInfo(name = "problem_count")
    val problemCount : Int,
    @ColumnInfo(name = "room_type")
    val roomType: RoomType,
    @ColumnInfo(name = "room_state")
    val roomState: RoomState,
    @ColumnInfo(name = "description")
    val description : String,
    @Embedded
    var baseEntity : BaseEntity = BaseEntity()
)