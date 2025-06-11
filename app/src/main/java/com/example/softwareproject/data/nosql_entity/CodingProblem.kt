package com.example.softwareproject.com.example.softwareproject.data.nosql_entity

data class CodingProblem(
    var coding_problem_id: String = "",
    var coding_room_id: String = "",
    var problem_id: Int = 0,
    var problem_index: Int = 0,
    var title: String = "",
    var average_tries: Double = 0.0,
    var accepted_user_count: Int = 0,
    var tag: String = ""
)
