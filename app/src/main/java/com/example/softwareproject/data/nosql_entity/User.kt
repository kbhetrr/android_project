package com.example.softwareproject.com.example.softwareproject.data.nosql_entity
import com.google.firebase.Timestamp

data class User(
    var user_id: String = "",
    var created_at: Timestamp? = null,
    var updated_at: Timestamp? = null
)