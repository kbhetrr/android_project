package com.example.softwareproject.com.example.softwareproject.module

import com.example.softwareproject.domain.repository.GeminiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeminiApiModule {

    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    @Provides
    @Singleton
    fun provideGeminiApi(): GeminiApi {
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // 연결 타임아웃
            .readTimeout(30, TimeUnit.SECONDS)     // 읽기 타임아웃
            .writeTimeout(30, TimeUnit.SECONDS)    // 쓰기 타임아웃
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)  // 👉 이걸 붙여야 타임아웃이 적용됨
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApi::class.java)
    }
}
