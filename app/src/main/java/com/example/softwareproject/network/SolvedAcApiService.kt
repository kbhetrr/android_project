package com.example.softwareproject.network // 적절한 패키지로 변경

import com.example.softwareproject.model.SolvedAcUser
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SolvedAcApiService {
    @GET("user/show") // 기본 URL 뒤에 붙는 경로
    suspend fun getUserProfile(
        @Query("handle") handle: String
    ): Response<SolvedAcUser> // Coroutine 지원을 위해 suspend 및 Response 사용
}