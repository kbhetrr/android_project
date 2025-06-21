package com.example.softwareproject.domain.usecase.room

import com.example.softwareproject.data.dto.problem.CsProblemDto
import com.example.softwareproject.data.dto.room.RoomDto
import com.example.softwareproject.data.entity.Room
import com.example.softwareproject.domain.repository.ProblemRepository
import com.example.softwareproject.domain.repository.RoomRepository
import javax.inject.Inject

class ProblemUseCase @Inject constructor(
    private val problemRepository: ProblemRepository,
    private val roomRepository: RoomRepository
) {
    suspend fun getRoomInfo(roomId : String) : RoomDto? {
        return roomRepository.getRoomInfo(roomId)
    }
    suspend fun getProblemByIndex(roomId: String, index: Int): CsProblemDto {
        return problemRepository.getProblemByIndex(roomId, index)
    }

}