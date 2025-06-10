package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.softwareproject.util.RoomType
@Entity(tableName = "participant_problem_status",
    foreignKeys = [
        ForeignKey(
            entity = RoomParticipant::class,
            parentColumns = ["room_participant_id"],
            childColumns = ["room_participant_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ],

)

data class ParticipantProblemStatus (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "participant_problem_status")
    val id : Int,
    @ColumnInfo(name = "room_participant_id")
    val roomParticipantId : Int,
    @ColumnInfo(name = "problem_id")
    var problemId : Int,
    @ColumnInfo(name = "problem_type")
    var problemType : RoomType,
    @ColumnInfo(name = "is_solved")
    val isSolved : Boolean
)
