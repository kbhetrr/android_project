package com.example.softwareproject.di

import com.example.softwareproject.domain.repository.AuthRepository
import com.example.softwareproject.data.repository.login.GithubAuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(
        impl: GithubAuthRepositoryImpl
    ): AuthRepository
}
