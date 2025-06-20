package com.example.softwareproject.com.example.softwareproject.module

import com.example.softwareproject.domain.repository.GeminiApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeminiApiService {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    fun create(): GeminiApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApi::class.java)
    }
}
