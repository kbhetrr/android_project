package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.softwareproject.data.entity.base.BaseEntity
import com.example.softwareproject.data.entity.base.BattleStats

@Entity(tableName = "room_participant",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Room::class,
            parentColumns = ["room_id"],
            childColumns = ["room_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],

)
data class RoomParticipant (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "room_participant_id")
    val id : Int,
    @ColumnInfo(name = "room_id")
    val roomId : Int,
    @ColumnInfo(name = "user_id")
    val userId : Int,
    @ColumnInfo(name = "solved_problem")
    var solvedProblem : Int,
    @Embedded
    var battleStats: BattleStats = BattleStats(),
    @Embedded
    var baseEntity: BaseEntity = BaseEntity(),
    )