package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "coding_problem",
    foreignKeys = [
        ForeignKey(
            entity = CodingRoom::class,
            parentColumns = ["coding_room_id"],
            childColumns = ["coding_room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)

data class CodingProblem (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "coding_problem_id")
    val id : Int,

    @ColumnInfo(name = "coding_room_id")
    val codingRoomId : Int,

    @ColumnInfo(name = "problem_id")
    val problemId : Int,

    @ColumnInfo(name = "problem_index")
    val problemIndex : Int,

    @ColumnInfo(name = "title")
    val title : String,

    @ColumnInfo(name = "average_tries")
    val averageTry : Double,

    @ColumnInfo(name = "accepted_user_count")
    val acceptUserCount : Int,

    @ColumnInfo(name = "tag")
    val tag : String
    )