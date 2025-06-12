package com.example.softwareproject.com.example.softwareproject.data.nosql_entity
import com.google.firebase.Timestamp

data class User(
    var userId: String = "",
    var createdAt: Timestamp? = null,
    var updatedAt: Timestamp? = null
)