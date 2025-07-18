package com.example.softwareproject.domain.repository

import com.example.softwareproject.data.dto.room.ParticipantProblemState
import com.example.softwareproject.data.dto.problem.CsProblemDto
import com.example.softwareproject.data.dto.room.RoomDto
import com.example.softwareproject.data.dto.room.RoomParticipantDto
import com.example.softwareproject.data.dto.room.CsRoomDto
import com.example.softwareproject.data.dto.room.PsRoomDto
import com.example.softwareproject.data.remote.room.CsWaitingRoomInfo
import com.example.softwareproject.data.remote.room.PsWaitingRoomInfo
import com.example.softwareproject.util.RoomState
import com.example.softwareproject.util.RoomType
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.Flow


interface RoomRepository {
    suspend fun createRoom(room : RoomDto) : RoomDto
    suspend fun createCsRoom(csRoom : CsRoomDto) : CsRoomDto
    suspend fun createPsRoom(psRoom : PsRoomDto) : PsRoomDto
    suspend fun createRoomParticipant(roomParticipant: RoomParticipantDto) : RoomParticipantDto
    suspend fun createParticipantProblemState(participantProblemState: ParticipantProblemState) : ParticipantProblemState

    suspend fun getRoomInfo(roomId: String) : RoomDto?
    suspend fun getCsRoomInfoByRoomId(csRoomId: String) : CsRoomDto?
    suspend fun getPsRoomInfoByRoomId(psRoomId: String) : PsRoomDto?
    suspend fun getRoomParticipantInfo(userId: String, roomId:String) : RoomParticipantDto?
    suspend fun getRoomParticipantList(roomId: String) : List<RoomParticipantDto>
    suspend fun getParticipantProblemStatusByUserIdAndRoomId(roomId: String, userId: String) : List<ParticipantProblemState>
    suspend fun getParticipantProblemStatusByUserIdAndProblemIndex(problemIndex: String, userId: String) : ParticipantProblemState?

    suspend fun roomList() : List<RoomDto>
    suspend fun csRoomList() : List<CsRoomDto>
    suspend fun psRoomList() : List<PsRoomDto>

    suspend fun createWaitingCsRoom(csWaitingRoomInfo: CsWaitingRoomInfo)
    suspend fun createWaitingPsRoom(psWaitingRoomInfo: PsWaitingRoomInfo)

    suspend fun deleteRoom(roomId: String) : String?
    suspend fun deleteCsRoom(roomId: String) : String?
    suspend fun deletePsRoom(roomId: String) : String?
    suspend fun deleteRoomParticipant(roomId: String) : String?
    suspend fun deleteParticipantProblemStatus(roomId: String) : String?


    suspend fun isAllSolved(roomId: String, userId: String): Boolean
    suspend fun roomStateChange(roomId: String, roomState: RoomState)
    suspend fun updateParticipantProblemStatus(participantProblemState: ParticipantProblemState)
    fun observeRoomList(roomType: RoomType, onChanged: (List<RoomDto>) -> Unit): ListenerRegistration
    fun observeRoomState(roomId: String): Flow<RoomState>
}