package com.example.softwareproject.domain.repository

import com.example.softwareproject.data.remote.room.CodingRoomSaveInfo
import com.example.softwareproject.data.remote.room.CsRoomSaveInfo
import com.example.softwareproject.data.remote.room.UiCodingRoomItem
import com.example.softwareproject.data.remote.room.UiCsRoomItem

interface RoomRepository {
    suspend fun getRoomInfo(roomId: String)
    suspend fun createCsRoom(csRoomSaveInfo:  CsRoomSaveInfo)
    suspend fun createCodingRoom(codingRoomSaveInfo: CodingRoomSaveInfo)
    suspend fun listCsRoom() : List<UiCsRoomItem>
    suspend fun listCodingRoom() : List<UiCodingRoomItem>
}