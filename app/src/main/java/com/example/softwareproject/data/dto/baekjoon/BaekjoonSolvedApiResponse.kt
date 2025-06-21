package com.example.softwareproject.data.dto.baekjoon

data class BaekjoonSolvedApiResponse(
    val count: Int,
    val items: List<ProblemItem>
)

data class ProblemItem(
    val problemId: Int
)