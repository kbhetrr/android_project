package com.example.softwareproject.com.example.softwareproject.data.nosql_entity

import com.google.firebase.Timestamp

data class UserAbility(
    var user_ability_id: String = "",
    var user_info_id: String = "",
    var exp: Int = 0,
    var level: Int = 1,
    var hp: Int = 100,
    var attack: Int = 10,
    var shield: Int = 5,
    var created_at: Timestamp? = null,
    var updated_at: Timestamp? = null
)