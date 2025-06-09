package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import java.util.Date

open class BaseEntity {
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date()
    @ColumnInfo(name = "updated_at")
    var updatedAt: Date = Date()
}