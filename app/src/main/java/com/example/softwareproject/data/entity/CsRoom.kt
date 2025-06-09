package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.softwareproject.data.entity.base.DifficultyCs
import com.example.softwareproject.data.entity.base.Topic

@Entity(tableName = "cs_room",
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
data class CsRoom (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cs_room_id")
    val id : Int,
    @ColumnInfo(name = "room_id")
    val roomId : Int,
    @ColumnInfo(name = "topic")
    var topic : Topic,
    @ColumnInfo(name = "difficulty_level")
    var difficultyCs: DifficultyCs
)