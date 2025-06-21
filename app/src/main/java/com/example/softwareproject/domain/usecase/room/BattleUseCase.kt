package com.example.softwareproject.com.example.softwareproject.domain.usecase.room

import com.example.softwareproject.BuildConfig
import com.example.softwareproject.com.example.softwareproject.module.BaekjoonApi
import com.example.softwareproject.com.example.softwareproject.module.GeminiApiService
import com.example.softwareproject.data.dto.problem.BaekjoonProblemDto
import com.example.softwareproject.data.dto.problem.CsProblemDto
import com.example.softwareproject.data.dto.problem.PsProblemDto
import com.example.softwareproject.domain.repository.Content
import com.example.softwareproject.domain.repository.GeminiApi
import com.example.softwareproject.domain.repository.GeminiRequest
import com.example.softwareproject.domain.repository.Part
import com.example.softwareproject.domain.repository.ProblemRepository
import com.example.softwareproject.domain.repository.RoomRepository
import com.example.softwareproject.domain.repository.UserRepository
import com.example.softwareproject.util.RoomType
import javax.inject.Inject

class BattleUseCase@Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val problemRepository: ProblemRepository,
    private val geminiApiService: GeminiApi,
    private val baekjoonApi: BaekjoonApi

) {

    suspend fun createCsProblem(roomId: String) {
        val roomInfo = roomRepository.getRoomInfo(roomId)
        val csRoomInfo = roomRepository.getCsRoomInfoByRoomId(roomId)
        val problemCount = roomInfo?.problemCount ?: 1
        val difficultyLevel = csRoomInfo?.difficultyLevel
        val topic = csRoomInfo?.topic ?: "컴퓨터공학"
        val csRoomId = csRoomInfo?.csRoomId ?:"0"
        val prompt = buildPrompt(
            count = problemCount,
            topic = topic.toString(),
            level =  difficultyLevel.toString())

        val request = GeminiRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt))))
        )

        val response = geminiApiService.generateCSQuestions(request, BuildConfig.GEMINI_API_KEY)


        val responseText = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: throw Exception("Gemini 응답에 텍스트 없음")

        val problems = parseGeminiToCsProblemList(responseText, csRoomId)
        problems.forEach { problem ->
            problemRepository.createCsProblem(problem)
        }
    }
    private fun buildPrompt(count: Int, topic: String, level: String): String {
        return """
        $topic 분야의 $level 수준 컴퓨터공학 객관식 퀴즈를 총 $count 문제 생성해줘.

        형식은 아래와 같이:
        문제: ...
        1. ...
        2. ...
        3. ...
        4. ...
        정답: (1~4 중 하나)

        각 문제는 줄로 나누고, 문제 사이엔 빈 줄로 구분해줘.
    """.trimIndent()
    }
    private fun parseGeminiToCsProblemList(response: String, csRoomId: String): List<CsProblemDto> {
        val blocks = response.trim().split("\n\n")

        return blocks.mapIndexed { index, block ->
            val lines = block.lines().map { it.trim() }

            val question = lines.getOrNull(0)?.removePrefix("문제: ") ?: ""
            val choice1 = lines.getOrNull(1)?.removePrefix("1. ") ?: ""
            val choice2 = lines.getOrNull(2)?.removePrefix("2. ") ?: ""
            val choice3 = lines.getOrNull(3)?.removePrefix("3. ") ?: ""
            val choice4 = lines.getOrNull(4)?.removePrefix("4. ") ?: ""
            val correctRaw = lines.getOrNull(5)?.removePrefix("정답: ") ?: "1"
            val correct = correctRaw.filter { it.isDigit() }.take(1) // 첫 숫자 하나만

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
        }
    }

    suspend fun createPsProblem(roomId: String) {
        val roomInfo = roomRepository.getRoomInfo(roomId)
        val psRoomInfo = roomRepository.getPsRoomInfoByRoomId(roomId)

        val problemCount = roomInfo?.problemCount ?: 5
        val difficultyLevel = psRoomInfo?.difficultyLevel ?: "silver"  // silver, bronze, etc.

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
        val levelRange = when (level.lowercase()) {
            "bronze" -> 1..5
            "silver" -> 6..10
            "gold" -> 11..15
            "platinum" -> 16..20
            else -> 6..10
        }

        val result = mutableListOf<BaekjoonProblemDto>()
        var page = 1

        while (result.size < count) {
            val response = baekjoonApi.getProblemsByTag(
                tierFrom = levelRange.first,
                tierTo = levelRange.last,
                page = page
            )

            if (response.items.isEmpty()) break
            result.addAll(response.items)
            page++
        }

        return result.shuffled().take(count)
    }
}