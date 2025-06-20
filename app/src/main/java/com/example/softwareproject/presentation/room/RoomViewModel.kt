package com.example.softwareproject.presentation.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softwareproject.util.DifficultyPs
import com.example.softwareproject.data.remote.room.CsWaitingRoomInfo
import com.example.softwareproject.data.remote.room.PsWaitingRoomInfo
import com.example.softwareproject.data.remote.room.UiCsRoomItem
import com.example.softwareproject.data.remote.room.UiPsRoomItem
import com.example.softwareproject.domain.usecase.room.RoomUseCase

import com.example.softwareproject.util.DifficultyCs
import com.example.softwareproject.util.RoomType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomUseCase: RoomUseCase
) : ViewModel() {

    private val _csRooms = MutableLiveData<List<UiCsRoomItem>>()
    private val _psRooms = MutableLiveData<List<UiPsRoomItem>>()
    val csRooms: LiveData<List<UiCsRoomItem>> = _csRooms
    val psRooms: LiveData<List<UiPsRoomItem>> = _psRooms

    fun loadCsRooms() {
        viewModelScope.launch {
            val rooms = roomUseCase.listCsRoom()
            Log.d("TabCsFragment", "받은 방 개수: ${rooms.size}")
            _csRooms.value = rooms
        }
    }

    fun loadPsRooms() {
        viewModelScope.launch {
            val rooms = roomUseCase.listPsRoom()
            Log.d("TabPsFragment", "받은 방 개수: ${rooms.size}")
            _psRooms.value = rooms
        }
    }

//    fun createRoomPs(
//        userId : String,
//        title: String,
//        difficulty: String,
//        problemCount: String
//    ) {
//        viewModelScope.launch {
//            val roomType = RoomType.PS
//            var level: DifficultyPs? = null
//            if (difficulty == "브론즈") {
//                level = DifficultyPs.BRONZE
//            } else if (difficulty == "실버") {
//                level = DifficultyPs.SILVER
//            } else if (difficulty == "골드") {
//                level = DifficultyPs.GOLD
//            } else {
//                level = DifficultyPs.PLATINUM
//            }
//            roomRepository.createWaitingPsRoom(
//                PsWaitingRoomInfo(
//                    userId = userId,
//                    title = title,
//                    type = roomType,
//                    difficultyPs = level,
//                    problemCount = problemCount.toInt()
//                )
//            )
//        }
//    }
//
//    fun createRoomCs(
//        userId: String,
//        title: String,
//        difficulty: String,
//        problemCount: String
//    ) {
//        viewModelScope.launch {
//            val roomType = RoomType.CS
//            var level: DifficultyCs? = null
//            if (difficulty == "쉬움") {
//                level = DifficultyCs.EASY
//            } else if (difficulty == "중간") {
//                level = DifficultyCs.MIDDLE
//            } else {
//                level = DifficultyCs.HARD
//            }
//            roomRepository.createWaitingCsRoom(
//                CsWaitingRoomInfo(
//                    userId = userId,
//                    title = title,
//                    type = roomType,
//                    difficultyCs = level,
//                    problemCount = problemCount.toInt()
//                )
//            )
//        }
//    }
}