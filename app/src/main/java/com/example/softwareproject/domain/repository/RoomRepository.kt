package com.example.softwareproject.domain.repository

import com.example.softwareproject.data.dto.room.RoomDto
import com.example.softwareproject.data.dto.room.RoomParticipantDto
import com.example.softwareproject.data.dto.room.CsRoomDto
import com.example.softwareproject.data.dto.room.PsRoomDto
import com.example.softwareproject.data.remote.room.CsWaitingRoomInfo
import com.example.softwareproject.data.remote.room.PsWaitingRoomInfo


interface RoomRepository {
    suspend fun createRoom(room : RoomDto)
    suspend fun createCsRoom(csRoom : CsRoomDto)
    suspend fun createPsRoom(psRoom : PsRoomDto)
    suspend fun createRoomParticipant(roomParticipant: RoomParticipantDto)

    suspend fun getRoomInfo(roomId: String) : RoomDto?
    suspend fun getCsRoomInfo(csRoomId: String) : CsRoomDto?
    suspend fun getPsRoomInfo(psRoomId: String) : PsRoomDto?
    suspend fun getRoomParticipantInfo(userId: String, roomId:String) : RoomParticipantDto?
    suspend fun getRoomParticipantList(roomId: String) : List<RoomParticipantDto>


    suspend fun roomList() : List<RoomDto>
    suspend fun csRoomList() : List<CsRoomDto>
    suspend fun psRoomList() : List<PsRoomDto>

    suspend fun createWaitingCsRoom(csWaitingRoomInfo: CsWaitingRoomInfo)
    suspend fun createWaitingPsRoom(psWaitingRoomInfo: PsWaitingRoomInfo)
}