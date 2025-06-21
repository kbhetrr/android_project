package com.example.softwareproject.data.repository

import com.example.softwareproject.domain.repository.BattleRepository
import com.example.softwareproject.data.dto.room.RoomParticipantDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class BattleRepositoryImpl @Inject constructor(
    private val fireBaseStore : FirebaseFirestore
) :BattleRepository {

    override suspend fun updateParticipantHp(userId: String, roomId: String, newHp: Int) {
        val doc = fireBaseStore.collection("room_participant")
            .whereEqualTo("userId", userId)
            .whereEqualTo("roomId", roomId)
            .get()
            .await()
            .documents
            .firstOrNull()

        doc?.reference?.update("hp", newHp)?.await()
    }

    override fun observeRoomParticipants(roomId: String): Flow<List<RoomParticipantDto>> = callbackFlow {
        val listener = fireBaseStore.collection("room_participant")
            .whereEqualTo("roomId", roomId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull {
                    it.toObject(RoomParticipantDto::class.java)
                } ?: emptyList()

                trySend(list).isSuccess // 안전 전송
            }

        awaitClose { listener.remove() }
    }
}