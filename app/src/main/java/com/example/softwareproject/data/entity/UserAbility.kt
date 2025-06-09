package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.softwareproject.data.entity.base.BaseEntity

@Entity(tableName = "user_ability",
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

data class UserAbility (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_ability_id")
    val id : Int,

    @ColumnInfo(name = "user_info_id")
    val userInfoId : Int,

    @ColumnInfo(name = "exp")
    var exp : Int,

    @ColumnInfo(name = "level")
    var level : Int,

    @ColumnInfo(name = "hp")
    var hp : Int,

    @ColumnInfo(name = "attack")
    var attack : Int,

    @ColumnInfo(name = "shield")
    var shield : Int,

    @Embedded
    var baseEntity : BaseEntity = BaseEntity()
)