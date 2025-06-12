package com.example.softwareproject.com.example.softwareproject.data.nosql_entity

data class CodingProblem(
    var codingRoomId: String = "",
    var problemId: Int = 0,
    var problemIndex: Int = 0,
    var title: String = "",
    var averageTries: Double = 0.0,
    var acceptedUserCount: Int = 0,
    var tag: String = ""
)
