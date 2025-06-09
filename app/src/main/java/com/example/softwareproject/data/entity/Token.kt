package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.softwareproject.data.entity.base.BaseEntity
import com.example.softwareproject.data.entity.base.TokenSource
import java.util.Date


@Entity(tableName = "token",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Token (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "token_id")
    val id : Int,
    @ColumnInfo(name = "token_source")
    val tokenSource : TokenSource,
    @ColumnInfo(name = "token_type")
    val tokenType : String,
    @ColumnInfo(name = "user_id")
    val userId : Int,
    @ColumnInfo(name = "access_token")
    val accessToken: String,
    @ColumnInfo(name = "refresh_token")
    val refreshToken: String ?= null,
    @ColumnInfo(name = "expires_at")
    val expiresAt : Date ?= null,
    @Embedded
    var baseEntity: BaseEntity = BaseEntity(),
    )