package com.example.softwareproject.module

import com.example.softwareproject.data.repository.BattleRepositoryImpl
import com.example.softwareproject.domain.repository.BattleRepository

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BattleRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBattleRepository(
        impl: BattleRepositoryImpl
    ): BattleRepository
}