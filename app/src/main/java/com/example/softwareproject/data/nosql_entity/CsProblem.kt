package com.example.softwareproject.data.nosql_entity

data class CsProblem(
    var csRoomId: String = "",
    var problemIndex: Int = 0,
    var question: String = "",
    var choice1: String = "",
    var choice2: String = "",
    var choice3: String = "",
    var choice4: String = "",
    var correctChoice: Int = 1
)
