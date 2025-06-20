package com.example.softwareproject.module

import com.example.softwareproject.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProblemRepositoryModule {
    @Binds
    abstract fun bindProblemRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): ProblemRepositoryModule
}