package com.example.softwareproject.data.nosql_entity


data class UserAbility(
    var userId : String = "",
    var exp: Int = 0,
    var level: Int = 1,
    var hp: Int = 100,
    var attack: Int = 10,
    var shield: Int = 5,
)