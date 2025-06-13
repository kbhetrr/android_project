package com.example.softwareproject.module


import com.example.softwareproject.domain.repository.RoomRepository
import com.example.softwareproject.data.repository.RoomRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRoomRepository(
        impl: RoomRepositoryImpl
    ): RoomRepository
}