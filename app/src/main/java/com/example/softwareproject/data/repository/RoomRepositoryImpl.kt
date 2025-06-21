package com.example.softwareproject.data.repository

import android.util.Log
import com.example.softwareproject.com.example.softwareproject.data.dto.room.ParticipantProblemState
import com.example.softwareproject.data.dto.room.RoomDto
import com.example.softwareproject.data.dto.room.RoomParticipantDto
import com.example.softwareproject.data.dto.room.CsRoomDto
import com.example.softwareproject.data.dto.room.PsRoomDto
import com.example.softwareproject.data.nosql_entity.CodingProblem
import com.example.softwareproject.data.remote.room.CsWaitingRoomInfo
import com.example.softwareproject.data.remote.room.PsWaitingRoomInfo
import com.example.softwareproject.domain.repository.RoomRepository
import com.example.softwareproject.domain.repository.UserRepository
import com.example.softwareproject.domain.repository.solvedac.RetrofitInstance
import com.example.softwareproject.util.RoomState
import com.example.softwareproject.util.RoomType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val firebaseStore: FirebaseFirestore,
    private val userRepository: UserRepository
) : RoomRepository{

    override suspend fun createRoom(room : RoomDto) : RoomDto {
        return try {
            firebaseStore.collection("room")
                .document(room.roomId)
                .set(room)
                .await()
            Log.d("Firestore", "room 저장 성공: ${room.roomId}")
            room
        } catch (e: Exception) {
            Log.e("Firestore", "room 저장 실패: ${e.message}")
            throw e
        }
    }

    override suspend fun createCsRoom(csRoom: CsRoomDto): CsRoomDto {
        return try {
            firebaseStore.collection("cs_room")
                .document(csRoom.csRoomId)
                .set(csRoom)
                .await()
            Log.d("Firestore", "cs_room 저장 성공: ${csRoom.csRoomId}")
            csRoom
        } catch (e: Exception) {
            Log.e("Firestore", "cs_room 저장 실패: ${e.message}")
            throw e
        }
    }


    override suspend fun createPsRoom(psRoom: PsRoomDto): PsRoomDto {
        return try {
            firebaseStore.collection("coding_room")
                .document(psRoom.codingRoomId)
                .set(psRoom)
                .await()

            Log.d("Firestore", "coding_room 저장 성공: ${psRoom.codingRoomId}")
            return psRoom
        } catch (e: Exception) {
            Log.e("Firestore", "coding_room 저장 실패: ${e.message}")
            throw e  // 필요 시 예외를 던지거나 Result로 감싸도 됨
        }
    }

    override suspend fun createRoomParticipant(roomParticipant: RoomParticipantDto): RoomParticipantDto {
        try {
            firebaseStore.collection("room_participant")
                .document("${roomParticipant.roomId}_${roomParticipant.userId}")
                .set(roomParticipant)
                .await()

            Log.d("Firestore", "room_participant 저장 성공")
            return roomParticipant
        } catch (e: Exception) {
            Log.e("Firestore", "room_participant 저장 실패: ${e.message}")
            throw e
        }
    }

    override suspend fun createParticipantProblemState(participantProblemState: ParticipantProblemState): ParticipantProblemState {
        return try {
            val firestore = FirebaseFirestore.getInstance()
            val docId = "${participantProblemState.roomId}_${participantProblemState.userId}_${participantProblemState.problemIndex}"

            firestore.collection("participant_problem_status")
                .document(docId)
                .set(participantProblemState)
                .await()

            Log.d("Firestore", "participant_problem_state 저장 성공: $docId")
            participantProblemState
        } catch (e: Exception) {
            Log.e("Firestore", "participant_problem_state 저장 실패: ${e.message}")
            throw e
        }
    }

    override suspend fun getRoomInfo(roomId: String): RoomDto? {
        return try {
            val snapshot = firebaseStore.collection("room")
                .whereEqualTo("roomId", roomId)
                .get()
                .await()

            snapshot.documents.firstOrNull()?.toObject(RoomDto::class.java)
        } catch (e: Exception) {
            Log.e("Repository", "getRoomInfo failed: ${e.message}")
            null
        }
    }


    override suspend fun getCsRoomInfoByRoomId(csRoomId: String): CsRoomDto? {
        return try {
            val snapshot = firebaseStore.collection("cs_room")
                .whereEqualTo("roomId", csRoomId)
                .get()
                .await()

            snapshot.documents.firstOrNull()?.toObject(CsRoomDto::class.java)
        } catch (e: Exception) {
            Log.e("Repository", "getCsRoomInfo failed: ${e.message}")
            null
        }
    }

    override suspend fun getPsRoomInfoByRoomId(psRoomId: String): PsRoomDto? {
        return try {
            val snapshot = firebaseStore.collection("coding_room")
                .whereEqualTo("roomId", psRoomId)
                .get()
                .await()

            snapshot.documents.firstOrNull()?.toObject(PsRoomDto::class.java)
        } catch (e: Exception) {
            Log.e("Repository", "getPsRoomInfo failed: ${e.message}")
            null
        }
    }


    override suspend fun getRoomParticipantInfo(userId: String, roomId: String): RoomParticipantDto? {
        return try {
            val snapshot = firebaseStore.collection("room_participant")
                .whereEqualTo("userId", userId)
                .whereEqualTo("roomId", roomId)
                .get()
                .await()

            if (!snapshot.isEmpty)
                snapshot.documents[0].toObject(RoomParticipantDto::class.java)
            else
                null
        } catch (e: Exception) {
            Log.e("Repository", "getRoomParticipantInfo failed: ${e.message}")
            null
        }
    }


    override suspend fun getRoomParticipantList(roomId: String): List<RoomParticipantDto> {
        return try {
            val snapshot = firebaseStore.collection("room_participant")
                .whereEqualTo("roomId", roomId)
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(RoomParticipantDto::class.java) }
        } catch (e: Exception) {
            Log.e("Repository", "getRoomParticipantList failed: ${e.message}")
            emptyList()
        }
    }

    override suspend fun roomList(): List<RoomDto> {
        return try {
            val snapshot = firebaseStore.collection("room")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(RoomDto::class.java) }
        } catch (e: Exception) {
            Log.e("Repository", "roomList failed: ${e.message}")
            emptyList()
        }
    }

    override suspend fun csRoomList(): List<CsRoomDto> {
        return try {
            val snapshot = firebaseStore.collection("cs_room")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(CsRoomDto::class.java) }
        } catch (e: Exception) {
            Log.e("Repository", "csRoomList failed: ${e.message}")
            emptyList()
        }
    }

    override suspend fun psRoomList(): List<PsRoomDto> {
        return try {
            val snapshot = firebaseStore.collection("ps_room")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(PsRoomDto::class.java) }
        } catch (e: Exception) {
            Log.e("Repository", "psRoomList failed: ${e.message}")
            emptyList()
        }
    }
    override suspend fun deleteRoom(roomId: String): String? {
        return try {
            val snapshot = firebaseStore.collection("room")
                .whereEqualTo("roomId", roomId)
                .get()
                .await()

            for (doc in snapshot.documents) {
                doc.reference.delete().await()
            }

            roomId
        } catch (e: Exception) {
            Log.e("Repository", "deleteRoom failed: ${e.message}")
            null
        }
    }

    override suspend fun deleteCsRoom(roomId: String): String? {
        return try {
            val snapshot = firebaseStore.collection("cs_room")
                .whereEqualTo("roomId", roomId)
                .get()
                .await()

            for (doc in snapshot.documents) {
                doc.reference.delete().await()
            }

            roomId
        } catch (e: Exception) {
            Log.e("Repository", "deleteCsRoom failed: ${e.message}")
            null
        }
    }

    override suspend fun deletePsRoom(roomId: String): String? {
        return try {
            val snapshot = firebaseStore.collection("coding_room")
                .whereEqualTo("roomId", roomId)
                .get()
                .await()

            for (doc in snapshot.documents) {
                doc.reference.delete().await()
            }

            roomId
        } catch (e: Exception) {
            Log.e("Repository", "deletePsRoom failed: ${e.message}")
            null
        }
    }

    override suspend fun deleteRoomParticipant(roomId: String): String? {
        return try {
            val snapshot = firebaseStore.collection("room_participant")
                .whereEqualTo("roomId", roomId)
                .get()
                .await()

            for (doc in snapshot.documents) {
                doc.reference.delete().await()
            }

            roomId
        } catch (e: Exception) {
            Log.e("Repository", "deleteRoomParticipant failed: ${e.message}")
            null
        }
    }

    override suspend fun roomStateChange(roomId: String, roomState: RoomState) {
        try {
            val snapshot = firebaseStore.collection("room")
                .whereEqualTo("roomId", roomId)
                .get()
                .await()

            val doc = snapshot.documents.firstOrNull()
            if (doc != null) {
                val documentId = doc.id
                val stateString = when (roomState) {
                    RoomState.WAITING -> "WAITING"
                    RoomState.PROGRESS -> "PROGRESS"
                    RoomState.FINISHED -> "FINISHED"
                }

                firebaseStore.collection("room")
                    .document(documentId)
                    .update("roomState", stateString)
                    .await()

                Log.d("RoomRepository", "방 상태 업데이트 성공: $stateString")
            } else {
                Log.e("RoomRepository", "roomId: $roomId 문서를 찾을 수 없음")
            }
        } catch (e: Exception) {
            Log.e("RoomRepository", "방 상태 업데이트 실패: ${e.message}")
        }
    }



    override suspend fun createWaitingCsRoom(csWaitingRoomInfo: CsWaitingRoomInfo) {

    }

    override suspend fun createWaitingPsRoom(psWaitingRoomInfo: PsWaitingRoomInfo) {

    }

    override fun observeRoomList(
        roomType: RoomType,
        onChanged: (List<RoomDto>) -> Unit
    ): ListenerRegistration {
        return firebaseStore.collection("room")
            .whereEqualTo("roomType", roomType.name)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val rooms = snapshot.documents.mapNotNull { it.toObject(RoomDto::class.java) }
                onChanged(rooms)
            }
    }




}