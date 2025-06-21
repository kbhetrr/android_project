package com.example.softwareproject.domain.repository

import com.example.softwareproject.data.dto.problem.CsProblemDto
import com.example.softwareproject.data.dto.problem.PsProblemDto


interface ProblemRepository {
    suspend fun createCsProblem(csProblemDto: CsProblemDto)
    suspend fun createPsProblem(psProblemDto: PsProblemDto)

    suspend fun getCsProblem(csRoomId: String, problemIndex: Int) : CsProblemDto?
    suspend fun getPsProblem(psRoomId: String, problemIndex: Int) : PsProblemDto?
    suspend fun getCsProblemList(csRoomId: String) :  List<CsProblemDto>
    suspend fun getPsProblemList(psRoomId: String) : List<PsProblemDto>
    suspend fun getCsProblemByIndex(roomId: String, index: Int): CsProblemDto
    suspend fun getPsProblemByIndex(roomId: String, index: Int): PsProblemDto
}