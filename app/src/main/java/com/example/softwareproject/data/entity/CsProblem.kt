package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "cs_problem",
    foreignKeys = [
        ForeignKey(
            entity = CsRoom::class,
            parentColumns = ["cs_room_id"],
            childColumns = ["cs_room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
)

data class CsProblem (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cs_problem_id")
    val id : Int,
    @ColumnInfo(name = "cs_room_id")
    val csRoomId : Int,
    @ColumnInfo(name = "problem_index")
    val problemIndex : Int,
    @ColumnInfo(name = "question")
    val question : String,
    @ColumnInfo(name = "choice_1")
    val choice1 : String,
    @ColumnInfo(name = "choice_2")
    val choice2 : String,
    @ColumnInfo(name = "choice_3")
    val choice3 : String,
    @ColumnInfo(name = "choice_4")
    val choice4 : String,
    @ColumnInfo(name = "correct_choice")
    val correctChoice : Int
)