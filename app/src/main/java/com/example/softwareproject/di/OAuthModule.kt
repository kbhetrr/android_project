package com.example.softwareproject.di

import dagger.Module
import dagger.Provides
import com.example.softwareproject.data.remote.GitHubOAuthApi
import com.example.softwareproject.BuildConfig
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OAuthModule {

    @Provides
    @Named("githubClientId")
    fun provideClientId(): String = BuildConfig.GITHUB_CLIENT_ID

    @Provides
    @Named("githubClientSecret")
    fun provideClientSecret(): String = BuildConfig.GITHUB_CLIENT_SECRET

    @Provides
    @Named("githubRedirectUri")
    fun provideRedirectUri(): String = "myapp://auth-callback"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://github.com/")
        .client(OkHttpClient())
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideGitHubApi(retrofit: Retrofit): GitHubOAuthApi =
        retrofit.create(GitHubOAuthApi::class.java)
}
