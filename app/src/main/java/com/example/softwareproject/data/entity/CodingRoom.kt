package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.softwareproject.data.entity.base.BaekjoonTier

@Entity(tableName = "coding_room",
    foreignKeys = [
        ForeignKey(
            entity = Room::class,
            parentColumns = ["room_id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["room_id"], unique = true)]
)

data class CodingRoom (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "coding_room_id")
    val id : Int,

    @ColumnInfo(name = "room_id")
    val roomId : Int,

    @ColumnInfo(name = "difficulty_level")
    var difficulty : BaekjoonTier
)