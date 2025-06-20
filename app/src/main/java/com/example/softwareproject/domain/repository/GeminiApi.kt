package com.example.softwareproject.domain.repository

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


interface GeminiApi {
    @POST("v1beta/models/gemini-pro:generateContent")
    suspend fun generateCSQuestions(
        @Body request: GeminiRequest,
        @Query("key") apiKey: String
    ): GeminiResponse
}


data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String
)


data class GeminiResponse(
    val candidates: List<Candidate>
)

data class Candidate(
    val content: Content
)
