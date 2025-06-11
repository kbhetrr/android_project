package com.example.softwareproject.com.example.softwareproject.data.nosql_entity

data class CsProblem(
    var cs_problem_id: String = "",
    var cs_room_id: String = "",
    var problem_index: Int = 0,
    var question: String = "",
    var choice_1: String = "",
    var choice_2: String = "",
    var choice_3: String = "",
    var choice_4: String = "",
    var correct_choice: Int = 1
)
