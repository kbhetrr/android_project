package com.example.softwareproject.com.example.softwareproject.module


import com.example.softwareproject.data.dto.problem.BaekjoonApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BaekjoonApi {
    @GET("v3/search/problem")
    suspend fun getProblemsByTag(
        @Query("query") query: String,
        @Query("page") page: Int
    ): BaekjoonApiResponse
}