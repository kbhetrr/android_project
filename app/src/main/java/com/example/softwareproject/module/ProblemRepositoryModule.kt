package com.example.softwareproject.module

import com.example.softwareproject.com.example.softwareproject.data.repository.ProblemRepositoryImpl
import com.example.softwareproject.domain.repository.ProblemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ProblemRepositoryModule {

    @Binds
    abstract fun bindProblemRepository(
        impl: ProblemRepositoryImpl
    ): ProblemRepository //
}
