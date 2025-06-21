package com.example.softwareproject.com.example.softwareproject.module


import com.example.softwareproject.data.dto.problem.BaekjoonApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BaekjoonApi {
    @GET("v1/problem/search")
    suspend fun getProblemsByTag(
        @Query("tierFrom") tierFrom: Int,
        @Query("tierTo") tierTo: Int,
        @Query("page") page: Int
    ): BaekjoonApiResponse



}