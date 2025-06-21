package com.example.softwareproject.com.example.softwareproject.module

import com.google.inject.Singleton
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object BaekjoonApiModule {

    private const val BASE_URL = "https://solved.ac/api/"

    @Provides
    @Singleton
    fun provideBaekjoonApi(): BaekjoonApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BaekjoonApi::class.java)
    }
}