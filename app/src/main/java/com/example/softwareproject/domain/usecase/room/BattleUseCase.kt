package com.example.softwareproject.com.example.softwareproject.domain.usecase.room

import android.util.Log
import com.example.softwareproject.BuildConfig
import com.example.softwareproject.data.dto.room.ParticipantProblemState
import com.example.softwareproject.domain.repository.BattleRepository
import com.example.softwareproject.com.example.softwareproject.module.BaekjoonApi
import com.example.softwareproject.data.dto.problem.BaekjoonProblemDto
import com.example.softwareproject.data.dto.problem.CsProblemDto
import com.example.softwareproject.data.dto.problem.PsProblemDto
import com.example.softwareproject.data.dto.room.RoomParticipantDto
import com.example.softwareproject.domain.repository.Content
import com.example.softwareproject.domain.repository.GeminiApi
import com.example.softwareproject.domain.repository.GeminiRequest
import com.example.softwareproject.domain.repository.Part
import com.example.softwareproject.domain.repository.ProblemRepository
import com.example.softwareproject.domain.repository.RoomRepository
import com.example.softwareproject.domain.repository.UserRepository
import com.example.softwareproject.domain.usecase.room.ProblemUseCase
import com.example.softwareproject.domain.usecase.room.RoomUseCase
import com.example.softwareproject.util.RoomState
import com.example.softwareproject.util.RoomType
import com.example.softwareproject.util.UserRole
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BattleUseCase@Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val problemRepository: ProblemRepository,
    private val geminiApiService: GeminiApi,
    private val battleRepository: BattleRepository,
    private val roomUseCase: RoomUseCase,
    private val problemUseCase: ProblemUseCase,
    private val baekjoonApi: BaekjoonApi

) {

    suspend fun createCsProblem(roomId: String) {
        val roomInfo = roomRepository.getRoomInfo(roomId)
        val csRoomInfo = roomRepository.getCsRoomInfoByRoomId(roomId)
        val problemCount = roomInfo?.problemCount ?: 1
        val difficultyLevel = csRoomInfo?.difficultyLevel
        val topic = csRoomInfo?.topic ?: "컴퓨터공학"
        val csRoomId = csRoomInfo?.csRoomId ?:"0"
        Log.d("GeminiAPI", "createCsProblem 시작!")
        val prompt = buildPrompt(
            count = problemCount,
            topic = topic.toString(),
            level =  difficultyLevel.toString())

        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt))))
        )
        Log.d("GeminiAPI", "API Key = ${BuildConfig.GEMINI_API_KEY}")
        try {
            val response = geminiApiService.generateCSQuestions(request, BuildConfig.GEMINI_API_KEY)

            val responseText = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: throw Exception("Gemini 응답에 텍스트 없음")

            Log.d("GeminiAPI", "응답 원본:\n$responseText")

            val problems = parseGeminiToCsProblemList(responseText, csRoomId)
            problems.forEachIndexed { i, problem ->
                try {
                    problemRepository.createCsProblem(problem)
                } catch (e: Exception) {
                    Log.e("GeminiAPI", "문제 저장 실패 at index $i: ${e.localizedMessage}")
                }
            }
        } catch (e: retrofit2.HttpException) {
            Log.e("GeminiAPI", "HTTP 에러 - 코드: ${e.code()}, 메시지: ${e.message()}, 오류 바디: ${e.response()?.errorBody()?.string()}")
        } catch (e: Exception) {
            Log.e("GeminiAPI", "일반 예외 발생: ${e.localizedMessage}")
        }
    }
    private fun buildPrompt(count: Int, topic: String, level: String): String {
        val randomizer = System.currentTimeMillis()
        return """
        $topic 분야의 $level 수준 컴퓨터공학 객관식 퀴즈를 총 $count 문제 생성해줘.

        문제 유형은 정의형, 응용형, 사례형, 오류 찾기형, 출력 결과 예측형 등을 골고루 섞어서 구성해줘.
        문제마다 내용이 겹치지 않도록 주의하고, 다양한 개념과 난이도를 포함시켜줘.

        형식은 아래와 같이:
        문제: ...
        1. ...
        2. ...
        3. ...
        4. ...
        정답: (1~4 중 하나)

        각 문제는 줄바꿈으로 구분하고, 문제와 문제 사이에는 '---' 기호 3개로 나눠줘.

        문제에 랜덤성을 부여하기 위한 시드값: $randomizer
    """.trimIndent()
    }
    private fun parseGeminiToCsProblemList(response: String, csRoomId: String): List<CsProblemDto> {
        val problems = mutableListOf<CsProblemDto>()

        // --- 기준으로 문제 블록 분리
        val blocks = response.trim().split(Regex("-{3,}")).map { it.trim() }.filter { it.isNotEmpty() }

        for ((index, block) in blocks.withIndex()) {
            val lines = block.lines().map { it.trim() }.filter { it.isNotBlank() }

            if (lines.size < 6) continue

            val question = lines[0].removePrefix("문제:").trim()
            val choice1 = lines[1].removePrefix("1.").trim()
            val choice2 = lines[2].removePrefix("2.").trim()
            val choice3 = lines[3].removePrefix("3.").trim()
            val choice4 = lines[4].removePrefix("4.").trim()
            val correctRaw = lines[5].removePrefix("정답:").trim()
            val correct = correctRaw.filter { it.isDigit() }.take(1)

            problems.add(
                CsProblemDto(
                    question = question,
                    choice1 = choice1,
                    choice2 = choice2,
                    choice3 = choice3,
                    choice4 = choice4,
                    correctChoice = correct,
                    csRoomId = csRoomId,
                    problemIndex = (index + 1).toString()
                )
            )
        }

        return problems
    }

    suspend fun createPsProblem(roomId: String) {
        val roomInfo = roomRepository.getRoomInfo(roomId)
        val psRoomInfo = roomRepository.getPsRoomInfoByRoomId(roomId)

        val problemCount = roomInfo?.problemCount ?: 5
        val difficultyLevel = psRoomInfo?.difficultyLevel ?: "silver"  // silver, bronze, etc.
        Log.e("BattleWaiting", "문제 레벨: $difficultyLevel")
        val problems = fetchBaekjoonProblems(difficultyLevel.toString(), problemCount)

        problems.forEachIndexed { index, problem ->
            val psDto = PsProblemDto(
                acceptedUserCount = problem.acceptedUserCount,
                averageTries = problem.averageTries,
                codingRoomId = psRoomInfo?.codingRoomId ?: "1",
                problemId = problem.problemId,
                problemIndex = (index + 1).toString(),
                tag = problem.tags.firstOrNull()?.key ?: "etc",
                title = problem.titleKo
            )
            problemRepository.createPsProblem(psDto)
        }
    }

    private suspend fun fetchBaekjoonProblems(level: String, count: Int): List<BaekjoonProblemDto> {
        Log.e("BattleWaiting", "문제 레벨: $level")
        val levelRange = when (level.lowercase()) {
            "bronze" -> 1..5
            "silver" -> 6..10
            "gold" -> 11..15
            "platinum" -> 16..20
            else -> 6..10
        }

        val query = "tier:${levelRange.first}..${levelRange.last}"
        Log.e("BattleWaiting", "문제 레벨: ${levelRange.first}..${levelRange.last}")
        val result = mutableListOf<BaekjoonProblemDto>()
        val seenIds = mutableSetOf<Int>()  // 중복 방지
        var page = 1
        val maxPage = 20  // 무한루프 방지

        while (result.size < count && page <= maxPage) {
            val response = baekjoonApi.getProblemsByTag(query = query, page = page)

            if (response.items.isEmpty()) {
                Log.w("BAEKJOON", "Page $page - 빈 응답")
                break
            }

            val filtered = response.items.filter {
                it.level != null && it.level in levelRange && seenIds.add(it.problemId.toInt())
            }

            Log.d("BAEKJOON", "Page $page - 필터 전: ${response.items.size}, 필터 후: ${filtered.size}")

            result.addAll(filtered)
            page++
        }

        return result.shuffled().take(count)
    }

    suspend fun createRoomParticipant(roomId: String){
        val roomInfo =roomRepository.getRoomInfo(roomId)

        val hostUserId = roomInfo?.userId ?: "0"
        Log.d("BattleWaiting", "hostUserId: $hostUserId")
        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val participantUserId = userRepository.getUserGithubInfoByFirebaseUid(uid)

        val hostUser = userRepository.getUserInfo(hostUserId) // 방장
        val participantUser = participantUserId?.let { userRepository.getUserInfo(it.userId) } // 참가자

        val hostUserAbility = userRepository.getUserAbilityInfo(hostUserId)
        val participantUserAbility = participantUser?.let { userRepository.getUserAbilityInfo(it.userId) }
        Log.d("BattleWaiting", "hostUserId: $hostUserId $hostUser $hostUserAbility")

        if(hostUserAbility != null && hostUser != null)
        {
            Log.e("BattleWaiting", " 호스트유저 정보 생성!")
            roomRepository.createRoomParticipant(RoomParticipantDto(
                userId = hostUserId,
                attack = hostUserAbility.attack,
                hp = hostUserAbility.hp,
                maxHp = hostUserAbility.hp,
                shield = hostUserAbility.shield,
                role = UserRole.HOST,
                roomId = roomId,
                solvedProblem = 0,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
                ))
        }
        if(participantUserAbility != null && participantUser != null)
        {
            Log.e("BattleWaiting", " 참가 유저 정보 생성!")
            roomRepository.createRoomParticipant(RoomParticipantDto(
                userId = participantUser.userId,
                attack = participantUserAbility.attack,
                hp = participantUserAbility.hp,
                maxHp = participantUserAbility.hp,
                shield = participantUserAbility.shield,
                role = UserRole.GUEST,
                roomId = roomId,
                solvedProblem = 0,
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now()
            ))
        }


    }
    suspend fun finishGame(roomId: String, winnerUserId: String, losserUserId: String) {
        try {
            val winnerLog = userRepository.getUserBattleLogInfo(winnerUserId)
            val loserLog = userRepository.getUserBattleLogInfo(losserUserId)

            val winnerAbility = userRepository.getUserAbilityInfo(winnerUserId)
            val loserAbility = userRepository.getUserAbilityInfo(losserUserId)

            // 1. 전적 처리
            winnerLog?.let {
                val updatedWin = it.win + 1
                val updatedMatch = it.match + 1
                val updatedRate = if (updatedMatch > 0) updatedWin * 100 / updatedMatch else 0

                userRepository.updateBattleLogInfo(
                    it.copy(
                        win = updatedWin,
                        match = updatedMatch,
                        rate = updatedRate
                    )
                )
            }

            loserLog?.let {
                val updatedLose = it.lose + 1
                val updatedMatch = it.match + 1
                val updatedRate = if (it.win > 0) it.win * 100 / updatedMatch else 0

                userRepository.updateBattleLogInfo(
                    it.copy(
                        lose = updatedLose,
                        match = updatedMatch,
                        rate = updatedRate
                    )
                )
            }

            // 2. 능력치 처리
            winnerAbility?.let {
                var newExp = it.exp + 10
                var newLevel = it.level
                var newTargetExp = it.targetExp

                if (newExp >= newTargetExp) {
                    newLevel += 1
                    newExp = 0
                    newTargetExp += 20 // 레벨업마다 경험치 요구 증가 가능
                }

                userRepository.updateUserAbilityInfo(
                    it.copy(
                        exp = newExp,
                        level = newLevel,
                        targetExp = newTargetExp
                    )
                )
            }

            loserAbility?.let {
                var newExp = it.exp + 2
                var newLevel = it.level
                var newTargetExp = it.targetExp

                if (newExp >= newTargetExp) {
                    newLevel += 1
                    newExp = 0
                    newTargetExp += 20
                }

                userRepository.updateUserAbilityInfo(
                    it.copy(
                        exp = newExp,
                        level = newLevel,
                        targetExp = newTargetExp
                    )
                )
            }

            // 3. 게임 종료 정리
            val room = roomRepository.getRoomInfo(roomId)
            roomRepository.roomStateChange(roomId, RoomState.FINISHED)

//            roomUseCase.deleteRoomParticipant(roomId)
//            roomUseCase.deleteParticipantProblemStatus(roomId)
            if (room != null) {
                if(room.roomType == RoomType.PS)
                {
//                    roomUseCase.deletePsRoom(roomId)
                    problemUseCase.deletePsProblem(roomId)
                }
                else{
//                    roomUseCase.deleteCsRoom(roomId)
                    problemUseCase.deleteCsProblem(roomId)
                }
            }
//            roomUseCase.deleteRoom(roomId)

        } catch (e: Exception) {
            Log.e("finishGame", "게임 종료 처리 실패: ${e.message}")
        }
    }
    suspend fun createParticipantProblemState(roomId: String) {
        val roomInfo = roomRepository.getRoomInfo(roomId)
        val hostUserId = roomInfo?.userId ?: "0"
        val problemCount = roomInfo?.problemCount ?: 1  // 기본값 1

        val uid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val participantUserId = userRepository.getUserGithubInfoByFirebaseUid(uid)
        val participantId = participantUserId?.userId ?: "0"

        for (index in 1..problemCount) {
            Log.e("BattleWaiting", "roomParticipant생성")
            roomRepository.createParticipantProblemState(
                ParticipantProblemState(
                    createdAt = Timestamp.now(),
                    updatedAt = Timestamp.now(),
                    isSolved = false,
                    problemIndex = index,
                    roomId = roomId,
                    userId = hostUserId
                )
            )

            // 참가자용 저장
            roomRepository.createParticipantProblemState(
                ParticipantProblemState(
                    createdAt = Timestamp.now(),
                    updatedAt = Timestamp.now(),
                    isSolved = false,
                    problemIndex = index,
                    roomId = roomId,
                    userId = participantId
                )
            )
        }
    }
    suspend fun getCurrentRoomParticipant(roomId: String): RoomParticipantDto? {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        val githubInfo = userRepository.getUserGithubInfoByFirebaseUid(uid) ?: return null
        val userId = githubInfo.userId

        return try {
            roomRepository.getRoomParticipantInfo(userId, roomId)
        } catch (e: Exception) {
            Log.e("UserUseCase", "getCurrentRoomParticipant failed: ${e.message}")
            null
        }
    }

    suspend fun getOpponentRoomParticipant(roomId: String): RoomParticipantDto? {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null
        val githubInfo = userRepository.getUserGithubInfoByFirebaseUid(uid) ?: return null
        val myUserId = githubInfo.userId

        return try {
            val participantList = roomRepository.getRoomParticipantList(roomId) ?: return null
            participantList.firstOrNull { it.userId != myUserId }
        } catch (e: Exception) {
            Log.e("UserUseCase", "getOpponentRoomParticipant failed: ${e.message}")
            null
        }
    }
    suspend fun updateParticipantHp(userId :String, roomId: String, newHp:Int){
        battleRepository.updateParticipantHp(
            userId = userId,
            roomId = roomId,
            newHp = newHp
            )
    }

    fun observeRoomParticipants(roomId: String): Flow<List<RoomParticipantDto>> {
        return battleRepository.observeRoomParticipants(roomId)
    }




}