package com.example.softwareproject.com.example.softwareproject.module

import androidx.room.Query
import retrofit2.http.GET

interface BaekjoonApi {
    @GET("v1/problem/search")
    suspend fun getProblemsByTag(
        @Query("query") query: String,
        @Query("tierFrom") tierFrom: Int,
        @Query("tierTo") tierTo: Int,
        @Query("page") page: Int
    ): BaekjoonApiResponse
}