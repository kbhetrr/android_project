package com.example.softwareproject.domain.repository

import com.example.softwareproject.data.dto.room.RoomParticipantDto
import kotlinx.coroutines.flow.Flow

interface BattleRepository {
    suspend fun updateParticipantHp(userId: String, roomId: String, newHp: Int)
    fun observeRoomParticipants(roomId: String): Flow<List<RoomParticipantDto>>
}