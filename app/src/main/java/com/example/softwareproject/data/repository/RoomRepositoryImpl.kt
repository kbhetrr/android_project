package com.example.softwareproject.com.example.softwareproject.data.repository

import com.example.softwareproject.BuildConfig
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.CodingProblem
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.CodingRoom
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.CsProblem
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.CsRoom
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.GithubInfo
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.ParticipantProblemStatus
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.Room
import com.example.softwareproject.com.example.softwareproject.data.remote.room.CsRoomSaveInfo
import com.example.softwareproject.com.example.softwareproject.domain.repository.RoomRepository
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.RoomParticipant
import com.example.softwareproject.com.example.softwareproject.data.remote.room.CodingRoomSaveInfo
import com.example.softwareproject.com.example.softwareproject.data.remote.room.UiCodingRoomItem
import com.example.softwareproject.com.example.softwareproject.data.remote.room.UiCsRoomItem
import com.example.softwareproject.com.example.softwareproject.data.remote.user.UserFullInfo
import com.example.softwareproject.com.example.softwareproject.domain.repository.Content
import com.example.softwareproject.com.example.softwareproject.domain.repository.GeminiApi
import com.example.softwareproject.com.example.softwareproject.domain.repository.GeminiRequest
import com.example.softwareproject.com.example.softwareproject.domain.repository.Part
import com.example.softwareproject.com.example.softwareproject.domain.repository.solvedac.RetrofitInstance
import com.example.softwareproject.domain.repository.UserRepository
import com.example.softwareproject.util.UserRole
import com.google.firebase.Timestamp
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RoomRepositoryImpl @Inject constructor(
    private val firebaseStore: FirebaseFirestore,
    private val userRepository: UserRepository
) : RoomRepository{




    override suspend fun getRoomInfo(roomId: String) {

    }

    override suspend fun createCsRoom(csRoomSaveInfo: CsRoomSaveInfo) {
        val user: UserFullInfo = userRepository.getUserInfo(csRoomSaveInfo.userId)

        val roomParticipant = RoomParticipant(
            userId = csRoomSaveInfo.userId,
            solvedProblem = 0,
            hp = user.userAbility.hp,
            attack = user.userAbility.attack,
            shield = user.userAbility.shield,
            role = UserRole.HOST,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )

        val roomRef = FirebaseFirestore.getInstance()
            .collection("room")
            .document()
        val csRoomRef = FirebaseFirestore.getInstance()
            .collection("cs_room")
            .document()

        val room = Room(
            roomId = roomRef.id,
            roomTitle = csRoomSaveInfo.roomTitle,
            problemCount = csRoomSaveInfo.problemCount,
            roomType = csRoomSaveInfo.roomType,
            roomState = csRoomSaveInfo.roomState,
            description = csRoomSaveInfo.description,
            createdBy = csRoomSaveInfo.userId,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )


        val csRoom = CsRoom(
            csRoomId = csRoomRef.id,
            roomId = roomRef.id,
            topic = csRoomSaveInfo.topic,
            difficultyLevel = csRoomSaveInfo.difficultyLevel
        )


        val prompt = """
        ${{csRoomSaveInfo.problemCount}}개의 ${csRoomSaveInfo.difficultyLevel} 수준 CS 문제를 만들어줘.
        형식: JSON 배열로 출력하고, 각 문제는 다음 키를 포함해야 해:
        question, choice1, choice2, choice3, choice4, correctChoice (1~4 중 정답 번호)
        """.trimIndent()

        val request = GeminiRequest(
            contents = listOf(
                Content(parts = listOf(Part(text = prompt)))
            )
        )

        val geminiApi = Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApi::class.java)

        val geminiResponse = geminiApi.generateCSQuestions(
            request= request,
            apiKey = BuildConfig.GEMINI_API_KEY)
        val generatedText = geminiResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("Gemini 응답 없음")

        val gson = Gson()
        val csProblems: List<CsProblem> = gson.fromJson(generatedText, object : TypeToken<List<CsProblem>>() {}.type)

        csProblems.forEachIndexed { index, problem ->
            val csProblem = problem.copy(csRoomId = csRoom.csRoomId, problemIndex = index)
            firebaseStore.collection("cs_problem").document().set(csProblem).await()

            val problemStatus = ParticipantProblemStatus(
                roomParticipantId = roomParticipant.userId,
                userId = roomParticipant.userId,
                problemId = index + 1,
                problemType = room.roomType,
                isSolved = false,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            )
            firebaseStore.collection("participant_problem_status").document().set(problemStatus).await()
        }



        firebaseStore.collection("room_participant").document().set(roomParticipant).await()
        firebaseStore.collection("room").document(roomRef.id).set(room).await()
        firebaseStore.collection("cs_room").document(csRoomRef.id).set(csRoom).await()
    }

    override suspend fun createCodingRoom(codingRoomSaveInfo: CodingRoomSaveInfo) {
        val user: UserFullInfo = userRepository.getUserInfo(codingRoomSaveInfo.userId)

        val roomParticipant = RoomParticipant(
            userId = codingRoomSaveInfo.userId,
            solvedProblem = 0,
            hp = user.userAbility.hp,
            attack = user.userAbility.attack,
            shield = user.userAbility.shield,
            role = UserRole.HOST,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )

        val roomRef = FirebaseFirestore.getInstance()
            .collection("room")
            .document()
        val codingRoomRef = FirebaseFirestore.getInstance()
            .collection("coding_room")
            .document()

        val room = Room(
            roomId = roomRef.id,
            roomTitle = codingRoomSaveInfo.roomTitle,
            problemCount = codingRoomSaveInfo.problemCount,
            roomType = codingRoomSaveInfo.roomType,
            roomState = codingRoomSaveInfo.roomState,
            description = codingRoomSaveInfo.description,
            createdBy = codingRoomSaveInfo.userId,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now()
        )


        val csRoom = CodingRoom(
            codingRoomId = codingRoomRef.id,
            roomId = roomRef.id,
            difficultyLevel = codingRoomSaveInfo.difficultyLevel
        )

        val tier = codingRoomSaveInfo.difficultyLevel.tierValue

        val selectedProblems = fetchProblemsByTierFromApi(
            tier = tier,
            count = codingRoomSaveInfo.problemCount,
            codingRoomId = codingRoomRef.id)

        firebaseStore.collection("room_participant").document().set(roomParticipant).await()
        firebaseStore.collection("room").document(roomRef.id).set(room).await()
        firebaseStore.collection("coding_room").document(codingRoomRef.id).set(csRoom).await()

        for (problem in selectedProblems) {
            firebaseStore.collection("coding_problem").document().set(problem).await()
        }
    }

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

    override suspend fun listCsRoom(): List<UiCsRoomItem> {
        val result = mutableListOf<UiCsRoomItem>()

        val roomSnapshots = firebaseStore.collection("room")
            .whereEqualTo("roomType", "CS")
            .get()
            .await()

        for (roomDoc in roomSnapshots.documents) {
            val room = roomDoc.toObject(Room::class.java) ?: continue

            val csRoomSnapshot = firebaseStore.collection("cs_room")
                .whereEqualTo("roomId", room.roomId)
                .get()
                .await()

            val csRoom = csRoomSnapshot.documents.firstOrNull()?.toObject(CsRoom::class.java) ?: continue

            val githubInfoSnapshot = firebaseStore.collection("github_info")
                .whereEqualTo("userId", roomDoc.getString("createdBy") ?: continue)
                .get()
                .await()

            val githubInfo = githubInfoSnapshot.documents.firstOrNull()?.toObject(GithubInfo::class.java)

            result.add(
                UiCsRoomItem(
                    roomId = room.roomId,
                    roomTitle = room.roomTitle,
                    topic = csRoom.topic,
                    difficulty = csRoom.difficultyLevel,
                    githubName = githubInfo?.githubName
                )
            )
        }

        return result
    }

    override suspend fun listCodingRoom(): List<UiCodingRoomItem> {
        val result = mutableListOf<UiCodingRoomItem>()

        val roomSnapshots = firebaseStore.collection("room")
            .whereEqualTo("roomType", "CODING")
            .get()
            .await()

        for (roomDoc in roomSnapshots.documents) {
            val room = roomDoc.toObject(Room::class.java) ?: continue

            val codingRoomSnapshot = firebaseStore.collection("coding_room")
                .whereEqualTo("roomId", room.roomId)
                .get()
                .await()

            val codingRoom = codingRoomSnapshot.documents.firstOrNull()
                ?.toObject(CodingRoom::class.java) ?: continue

            // GitHub 프로필 가져오기
            val createdBy = roomDoc.getString("createdBy") ?: continue

            val githubInfoSnapshot = firebaseStore.collection("github_info")
                .whereEqualTo("userId", createdBy)
                .get()
                .await()

            val githubInfo = githubInfoSnapshot.documents.firstOrNull()
                ?.toObject(GithubInfo::class.java)

            result.add(
                UiCodingRoomItem(
                    roomId = room.roomId,
                    roomTitle = room.roomTitle,
                    difficulty = codingRoom.difficultyLevel,
                    githubName = githubInfo?.githubName
                )
            )
        }

        return result
    }

}