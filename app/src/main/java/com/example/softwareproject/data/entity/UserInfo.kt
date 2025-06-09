package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.softwareproject.data.entity.base.BaseEntity

@Entity(tableName = "user_info",
        foreignKeys = [
            ForeignKey(
                entity = User::class,
                parentColumns = ["user_id"],
                childColumns = ["user_id"],
                onDelete = ForeignKey.CASCADE
            )
        ],
    indices = [Index(value = ["user_id"], unique = true)]
    )
data class UserInfo (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_info_id")
    val id : Int,

    @ColumnInfo(name = "user_id")
    val userId : Int,

    @Embedded
    var baseEntity : BaseEntity = BaseEntity()
)