package com.example.softwareproject.domain.usecase.room


import android.util.Log
import com.example.softwareproject.data.dto.room.CsRoomDto
import com.example.softwareproject.data.dto.room.PsRoomDto
import com.example.softwareproject.data.dto.room.RoomDto
import com.example.softwareproject.data.entity.Room
import com.example.softwareproject.data.remote.room.UiPsRoomItem
import com.example.softwareproject.data.remote.room.UiCsRoomItem
import com.example.softwareproject.domain.repository.RoomRepository
import com.example.softwareproject.domain.repository.UserRepository
import com.example.softwareproject.util.DifficultyCs
import com.example.softwareproject.util.DifficultyPs
import com.example.softwareproject.util.RoomState
import com.example.softwareproject.util.RoomType
import com.example.softwareproject.util.Topic
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomUseCase @Inject constructor(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
){
    private var csRoomListener: ListenerRegistration? = null

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
            val csRoom = roomRepository.getCsRoomInfoByRoomId(room.roomId)
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
        val rooms = roomRepository.roomList()
        val roomList = roomRepository.roomList().filter { it.roomType == RoomType.PS }

        Log.d("TabCsFragment", "불러온 총 방의수: ${rooms.size}")
        Log.d("TabCsFragment", "불러온 총 방의수: ${rooms.get(0)}")
        Log.d("DEBUG", "roomType: ${rooms.get(0).roomType}, isEnum: ${rooms.get(0).roomType::class}")
        val roomFirst = roomRepository.roomList().first()
        Log.d("DEBUG", "roomType class = ${roomFirst.roomType::class}")
        Log.d("DEBUG", "roomType value = ${roomFirst.roomType}")
        Log.d("DEBUG", "equals RoomType.CS? ${roomFirst.roomType == RoomType.PS}")
        val result = mutableListOf<UiPsRoomItem>()

        for (room in roomList) {
            val psRoom = roomRepository.getPsRoomInfoByRoomId(room.roomId)
            val user = userRepository.getUSerGithubInfo(room.userId)

            Log.d("DEBUG", "roomId: ${room.roomId}, csRoom: $psRoom, user: $user")

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

    suspend fun createPsRoom(roomTitle:String,
                             difficulty: String,
                             problemCount:String) :String {
        val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid ?: return "실패입니다."

        val githubInfo = userRepository.getUserGithubInfoByFirebaseUid(firebaseUid)
            ?: throw IllegalStateException("GitHub info not found for current user.")

        val userId = githubInfo.userId
        val number = problemCount.filter { it.isDigit() }.toInt()

        val problemDiff = when (difficulty) {
            "브론즈" -> DifficultyPs.BRONZE
            "실버" -> DifficultyPs.SILVER
            "골드" -> DifficultyPs.GOLD
            else -> DifficultyPs.PLATINUM
        }

        val roomId = FirebaseFirestore.getInstance().collection("room").document().id

        val createdRoom = roomRepository.createRoom(
            RoomDto(
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now(),
                roomType = RoomType.PS,
                roomTitle = roomTitle,
                roomState = RoomState.WAITING,
                roomId = roomId,
                problemCount = number,
                userId = userId,
                description = "",
                createBy = userId
            )
        )

        val createdPsRoom = roomRepository.createPsRoom(
            PsRoomDto(
                codingRoomId = FirebaseFirestore.getInstance().collection("coding_room").document().id,
                difficultyLevel = problemDiff,
                roomId = createdRoom.roomId,
            )
        )
        return roomId
    }

    suspend fun createCsRoom(
        roomTitle: String,
        difficulty: String,
        problemCount: String
    ) : String {
        val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid ?: return "실패입니다."

        val githubInfo = userRepository.getUserGithubInfoByFirebaseUid(firebaseUid)
            ?: throw IllegalStateException("GitHub info not found for current user.")

        val userId = githubInfo.userId
        val number = problemCount.filter { it.isDigit() }.toInt()

        val problemDiff = when (difficulty) {
            "쉬움" -> DifficultyCs.EASY
            "중간" -> DifficultyCs.MIDDLE
            else -> DifficultyCs.HARD
        }

        val topic = Topic.entries.random()
        val roomId = FirebaseFirestore.getInstance().collection("room").document().id

        val createdRoom = roomRepository.createRoom(
            RoomDto(
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now(),
                roomType = RoomType.CS,
                roomTitle = roomTitle,
                roomState = RoomState.WAITING,
                roomId = roomId,
                problemCount = number,
                userId = userId,
                description = "",
                createBy = userId
            )
        )

        val createdCsRoom = roomRepository.createCsRoom(
            CsRoomDto(
                csRoomId = FirebaseFirestore.getInstance().collection("cs_room").document().id,
                difficultyLevel = problemDiff,
                roomId = createdRoom.roomId,
                topic = topic
            )
        )
        return roomId
    }

    suspend fun deleteRoom(roomId: String){
        roomRepository.deleteRoom(roomId)
        roomRepository.deleteCsRoom(roomId)
        roomRepository.deletePsRoom(roomId)
    }

    suspend fun battleStart(roomId: String){
        roomRepository.roomStateChange(roomId,RoomState.PROGRESS)
    }

    suspend fun battleFinish(roomId: String){
        roomRepository.roomStateChange(roomId, RoomState.FINISHED)
    }
    fun observeUiCsRooms(onChanged: (List<UiCsRoomItem>) -> Unit): ListenerRegistration {
        return roomRepository.observeRoomList(RoomType.CS) { rooms ->
            CoroutineScope(Dispatchers.IO).launch {
                val result = rooms.mapNotNull { room ->
                    val csRoom = roomRepository.getCsRoomInfoByRoomId(room.roomId)
                    val user = userRepository.getUSerGithubInfo(room.userId)

                    if (csRoom != null && user != null) {
                        UiCsRoomItem(
                            roomId = room.roomId,
                            roomTitle = room.roomTitle,
                            topic = csRoom.topic,
                            difficulty = csRoom.difficultyLevel,
                            githubName = user.githubName,
                            description = room.description
                        )
                    } else null
                }

                withContext(Dispatchers.Main) {
                    onChanged(result)
                }
            }
        }
    }
    fun observeUiPsRooms(onChanged: (List<UiPsRoomItem>) -> Unit): ListenerRegistration {
        return roomRepository.observeRoomList(RoomType.PS) { rooms ->
            CoroutineScope(Dispatchers.IO).launch {
                val result = rooms.mapNotNull { room ->
                    val psRoom = roomRepository.getPsRoomInfoByRoomId(room.roomId)
                    val user = userRepository.getUSerGithubInfo(room.userId)

                    if (psRoom != null && user != null) {
                        UiPsRoomItem(
                            roomId = room.roomId,
                            roomTitle = room.roomTitle,
                            difficulty = psRoom.difficultyLevel,
                            githubName = user.githubName,
                            description = room.description
                        )
                    } else null
                }
                withContext(Dispatchers.Main) {
                    onChanged(result)
                }
            }
        }
    }

    suspend fun getRoomType(roomId: String): RoomType {
        val room = roomRepository.getRoomInfo(roomId)
        return room?.roomType ?: RoomType.CS
    }

    suspend fun getCsRoomInfo(csRoomId: String): CsRoomDto? {
        return roomRepository.getCsRoomInfoByRoomId(csRoomId)
    }
    suspend fun getRoom(roomId: String) : RoomDto? {
        return roomRepository.getRoomInfo(roomId)
    }
}