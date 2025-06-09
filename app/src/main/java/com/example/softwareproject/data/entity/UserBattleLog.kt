package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.softwareproject.data.entity.base.BaseEntity

@Entity(tableName = "user_battle_log",
    foreignKeys = [
        ForeignKey(
            entity = UserInfo::class,
            parentColumns = ["user_info_id"],
            childColumns = ["user_info_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_info_id"], unique = true)]
)

data class UserBattleLog (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_battle_log_id")
    val id : Int,

    @ColumnInfo(name = "user_info_id")
    val userInfoId : Int,
    @ColumnInfo(name = "match")
    var match : Int,
    @ColumnInfo(name = "win")
    var win : Int,
    @ColumnInfo(name = "lose")
    var lose : Int,
    @ColumnInfo(name = "rate")
    var rate: Double,
    @Embedded
    var baseEntity: BaseEntity = BaseEntity()
)