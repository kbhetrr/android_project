package com.example.softwareproject.data.repository

import android.util.Log
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


    override suspend fun getCsRoomInfo(csRoomId: String): CsRoomDto? {
        return try {
            val snapshot = firebaseStore.collection("cs_room")
                .whereEqualTo("csRoomId", csRoomId)
                .get()
                .await()

            snapshot.documents.firstOrNull()?.toObject(CsRoomDto::class.java)
        } catch (e: Exception) {
            Log.e("Repository", "getCsRoomInfo failed: ${e.message}")
            null
        }
    }

    override suspend fun getPsRoomInfo(psRoomId: String): PsRoomDto? {
        return try {
            val snapshot = firebaseStore.collection("coding_room")
                .whereEqualTo("psRoomId", psRoomId)
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
            firebaseStore.collection("room")
                .document(roomId)
                .delete()
                .await()
            roomId
        } catch (e: Exception) {
            Log.e("Repository", "deleteRoom failed: ${e.message}")
            null
        }
    }

    override suspend fun deleteCsRoom(roomId: String): String? {
        return try {
            firebaseStore.collection("cs_room")
                .document(roomId)
                .delete()
                .await()
            roomId
        } catch (e: Exception) {
            Log.e("Repository", "deleteCsRoom failed: ${e.message}")
            null
        }
    }

    override suspend fun deletePsRoom(roomId: String): String? {
        return try {
            firebaseStore.collection("coding_room")
                .document(roomId)
                .delete()
                .await()
            roomId
        } catch (e: Exception) {
            Log.e("Repository", "deletePsRoom failed: ${e.message}")
            null
        }
    }

    override suspend fun deleteRoomParticipant(roomId: String): String? {
        return try {
            firebaseStore.collection("room_participant")
                .document(roomId)
                .delete()
                .await()
            roomId
        } catch (e: Exception) {
            Log.e("Repository", "deleteRoomParticipant failed: ${e.message}")
            null
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


//    override suspend fun createCsRoom(csRoomSaveInfo: CsRoomSaveInfo) {
//        val user: UserFullInfo = userRepository.getUserInfo(csRoomSaveInfo.userId)
//
//        val roomParticipant = RoomParticipant(
//            userId = csRoomSaveInfo.userId,
//            solvedProblem = 0,
//            hp = user.userAbility.hp,
//            attack = user.userAbility.attack,
//            shield = user.userAbility.shield,
//            role = UserRole.HOST,
//            createdAt = Timestamp.now(),
//            updatedAt = Timestamp.now()
//        )
//
//        val roomRef = FirebaseFirestore.getInstance()
//            .collection("room")
//            .document()
//        val csRoomRef = FirebaseFirestore.getInstance()
//            .collection("cs_room")
//            .document()
//
//        val room = Room(
//            roomId = roomRef.id,
//            roomTitle = csRoomSaveInfo.roomTitle,
//            problemCount = csRoomSaveInfo.problemCount,
//            roomType = csRoomSaveInfo.roomType,
//            roomState = csRoomSaveInfo.roomState,
//            description = csRoomSaveInfo.description,
//            createdBy = csRoomSaveInfo.userId,
//            createdAt = Timestamp.now(),
//            updatedAt = Timestamp.now()
//        )
//
//
//        val csRoom = CsRoom(
//            csRoomId = csRoomRef.id,
//            roomId = roomRef.id,
//            topic = csRoomSaveInfo.topic,
//            difficultyLevel = csRoomSaveInfo.difficultyLevel
//        )
//
//
//        val prompt = """
//        ${{csRoomSaveInfo.problemCount}}개의 ${csRoomSaveInfo.difficultyLevel} 수준 CS 문제를 만들어줘.
//        형식: JSON 배열로 출력하고, 각 문제는 다음 키를 포함해야 해:
//        question, choice1, choice2, choice3, choice4, correctChoice (1~4 중 정답 번호)
//        """.trimIndent()
//
//        val request = GeminiRequest(
//            contents = listOf(
//                Content(parts = listOf(Part(text = prompt)))
//            )
//        )
//
//        val geminiApi = Retrofit.Builder()
//            .baseUrl("https://generativelanguage.googleapis.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(GeminiApi::class.java)
//
//        val geminiResponse = geminiApi.generateCSQuestions(
//            request= request,
//            apiKey = BuildConfig.GEMINI_API_KEY)
//        val generatedText = geminiResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
//            ?: throw Exception("Gemini 응답 없음")
//
//        val gson = Gson()
//        val csProblems: List<CsProblem> = gson.fromJson(generatedText, object : TypeToken<List<CsProblem>>() {}.type)
//
//        csProblems.forEachIndexed { index, problem ->
//            val csProblem = problem.copy(csRoomId = csRoom.csRoomId, problemIndex = index)
//            firebaseStore.collection("cs_problem").document().set(csProblem).await()
//
//            val problemStatus = ParticipantProblemStatus(
//                roomParticipantId = roomParticipant.userId,
//                userId = roomParticipant.userId,
//                problemId = index + 1,
//                problemType = room.roomType,
//                isSolved = false,
//                createdAt = Timestamp.now(),
//                updatedAt = Timestamp.now()
//            )
//            firebaseStore.collection("participant_problem_status").document().set(problemStatus).await()
//        }
//
//
//
//        firebaseStore.collection("room_participant").document().set(roomParticipant).await()
//        firebaseStore.collection("room").document(roomRef.id).set(room).await()
//        firebaseStore.collection("cs_room").document(csRoomRef.id).set(csRoom).await()
//    }
//
//
//
//    override suspend fun createCodingRoom(codingRoomSaveInfo: CodingRoomSaveInfo) {
//        val user: UserFullInfo = userRepository.getUserInfo(codingRoomSaveInfo.userId)
//
//        val roomParticipant = RoomParticipant(
//            userId = codingRoomSaveInfo.userId,
//            solvedProblem = 0,
//            hp = user.userAbility.hp,
//            attack = user.userAbility.attack,
//            shield = user.userAbility.shield,
//            role = UserRole.HOST,
//            createdAt = Timestamp.now(),
//            updatedAt = Timestamp.now()
//        )
//
//        val roomRef = FirebaseFirestore.getInstance()
//            .collection("room")
//            .document()
//        val codingRoomRef = FirebaseFirestore.getInstance()
//            .collection("coding_room")
//            .document()
//
//        val room = Room(
//            roomId = roomRef.id,
//            roomTitle = codingRoomSaveInfo.roomTitle,
//            problemCount = codingRoomSaveInfo.problemCount,
//            roomType = codingRoomSaveInfo.roomType,
//            roomState = codingRoomSaveInfo.roomState,
//            description = codingRoomSaveInfo.description,
//            createdBy = codingRoomSaveInfo.userId,
//            createdAt = Timestamp.now(),
//            updatedAt = Timestamp.now()
//        )
//
//
//        val csRoom = CodingRoom(
//            codingRoomId = codingRoomRef.id,
//            roomId = roomRef.id,
//            difficultyLevel = codingRoomSaveInfo.difficultyLevel
//        )
//
//        val tier = codingRoomSaveInfo.difficultyLevel.tierValue
//
//        val selectedProblems = fetchProblemsByTierFromApi(
//            tier = tier,
//            count = codingRoomSaveInfo.problemCount,
//            codingRoomId = codingRoomRef.id)
//
//        firebaseStore.collection("room_participant").document().set(roomParticipant).await()
//        firebaseStore.collection("room").document(roomRef.id).set(room).await()
//        firebaseStore.collection("coding_room").document(codingRoomRef.id).set(csRoom).await()
//
//        for (problem in selectedProblems) {
//            firebaseStore.collection("coding_problem").document().set(problem).await()
//        }
//    }

    suspend fun fetchProblemsByTierFromApi(
        tier: Int,
        count: Int,
        codingRoomId: String
    ): List<CodingProblem> {
        val response = RetrofitInstance.solvedAcApi.searchProblemsByLevel("level:$tier")
        val allProblems = response.items.filter { it.isSolvable }

        return allProblems.shuffled().take(count).mapIndexed { index, problem ->
            CodingProblem(
                codingRoomId = codingRoomId,
                problemId = problem.problemId,
                problemIndex = index + 1,
                title = problem.title,
                averageTries = 0.0,
                acceptedUserCount = problem.acceptedUserCount ?: 0,
                tag = problem.tags.firstOrNull()?.key ?: "etc"
            )
        }
    }




}