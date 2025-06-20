package com.example.softwareproject.domain.usecase.room


import android.util.Log
import com.example.softwareproject.data.remote.room.UiPsRoomItem
import com.example.softwareproject.data.remote.room.UiCsRoomItem
import com.example.softwareproject.domain.repository.RoomRepository
import com.example.softwareproject.domain.repository.UserRepository
import com.example.softwareproject.util.RoomType
import javax.inject.Inject

class RoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
){

    suspend fun listCsRoom(): List<UiCsRoomItem> {
        val rooms = roomRepository.roomList()

        val roomList = roomRepository.roomList().filter { it.roomType == RoomType.CS }
        Log.d("TabCsFragment", "불러온 총 방의수: ${rooms.size}")
        Log.d("TabCsFragment", "불러온 총 방의수: ${rooms.get(0)}")
        Log.d("DEBUG", "roomType: ${rooms.get(0).roomType}, isEnum: ${rooms.get(0).roomType::class}")
        val roomFirst = roomRepository.roomList().first()
        Log.d("DEBUG", "roomType class = ${roomFirst.roomType::class}")
        Log.d("DEBUG", "roomType value = ${roomFirst.roomType}")
        Log.d("DEBUG", "equals RoomType.CS? ${roomFirst.roomType == RoomType.CS}")

        val result = mutableListOf<UiCsRoomItem>()

        for (room in roomList) {
            val csRoom = roomRepository.getCsRoomInfo(room.roomId)
            val user = userRepository.getUSerGithubInfo(room.userId)

            Log.d("DEBUG", "roomId: ${room.roomId}, csRoom: $csRoom, user: $user")

            if (csRoom != null && user != null) {
                result.add(
                    UiCsRoomItem(
                        roomId = room.roomId,
                        roomTitle = room.roomTitle,
                        topic = csRoom.topic,
                        difficulty = csRoom.difficultyLevel,
                        githubName = user.githubName,
                        description = room.description
                    )
                )
            }
        }

        return result
    }

    suspend fun listPsRoom(): List<UiPsRoomItem> {
        val roomList = roomRepository.roomList().filter { it.roomType == RoomType.PS }
        val result = mutableListOf<UiPsRoomItem>()

        for (room in roomList) {
            val psRoom = roomRepository.getPsRoomInfo(room.roomId)
            val user = userRepository.getUSerGithubInfo(room.userId)

            if (psRoom != null && user != null) {
                result.add(
                    UiPsRoomItem(
                        roomId = room.roomId,
                        roomTitle = room.roomTitle,
                        difficulty = psRoom.difficultyLevel,
                        githubName = user.githubName,
                        description = room.description
                    )
                )
            }
        }

        return result
    }

}