package com.example.softwareproject.data.dto.problem

import com.example.softwareproject.com.example.softwareproject.data.dto.problem.TagDto

data class BaekjoonProblemDto (
    val problemId: String,
    val titleKo: String,
    val acceptedUserCount: Int,
    val averageTries: Double,
    val tags: List<TagDto>

)