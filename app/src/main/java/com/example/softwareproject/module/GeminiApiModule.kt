package com.example.softwareproject.com.example.softwareproject.module

import com.example.softwareproject.domain.repository.GeminiApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GeminiApiModule {

    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    @Provides
    @Singleton
    fun provideGeminiApi(): GeminiApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeminiApi::class.java)
    }
}
