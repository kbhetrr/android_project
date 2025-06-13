package com.example.softwareproject.com.example.softwareproject.domain.repository.solvedac

import com.google.gson.annotations.SerializedName
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    @SerializedName("isSolvable") val isSolvable: Boolean,
    @SerializedName("acceptedUserCount") val acceptedUserCount: Int?
)

data class SolvedAcTag(
    @SerializedName("key") val key: String
)

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {
    val solvedAcApi: SolvedAcApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://solved.ac/api/v3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SolvedAcApi::class.java)
    }
}