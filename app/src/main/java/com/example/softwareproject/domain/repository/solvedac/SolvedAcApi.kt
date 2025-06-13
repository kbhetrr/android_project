package com.example.softwareproject.com.example.softwareproject.domain.repository

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

interface SolvedAcApi {
    @GET("search/problem")
    suspend fun searchProblemsByLevel(
        @Query("query") query: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 100
    ): SolvedAcResponse
}

data class SolvedAcResponse(
    @SerializedName("items")
    val items: List<SolvedAcProblem>
)

data class SolvedAcProblem(
    @SerializedName("problemId") val problemId: Int,
    @SerializedName("titleKo") val title: String,
    @SerializedName("level") val tier: Int,
    @SerializedName("tags") val tags: List<SolvedAcTag>,
    @SerializedName("isSolvable") val isSolvable: Boolean
)

data class SolvedAcTag(
    @SerializedName("key") val key: String
)