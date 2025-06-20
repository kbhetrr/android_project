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
import com.google.firebase.firestore.ListenerRegistration
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

    private var csRoomListener: ListenerRegistration? = null
    private var psRoomListener: ListenerRegistration? = null


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

    fun makeRoom(title: String,
                 type: String,
                 difficulty: String,
                 problemCount: String){

        val roomType = extractRoomTypeFromLabel(type)

        if (roomType == null) {
            Log.e("RoomViewModel", "방 종류를 파악할 수 없습니다: $type")
            return
        }
        viewModelScope.launch {
            when (roomType) {
                RoomType.PS -> {
                    roomUseCase.createPsRoom(title, difficulty, problemCount)
                    loadPsRooms()
                }
                RoomType.CS -> {
                    roomUseCase.createCsRoom(title, difficulty, problemCount)
                    loadCsRooms()
                }
            }
        }
    }

    private fun extractRoomTypeFromLabel(label: String): RoomType? {
        return when {
            label.contains("(PS)") -> RoomType.PS
            label.contains("(CS)") -> RoomType.CS
            else -> null
        }
    }
    fun observeCsRooms() {
        if (csRoomListener != null) return

        csRoomListener = roomUseCase.observeUiCsRooms { roomList ->
            _csRooms.value = roomList
        }
    }

    fun observePsRooms() {
        if (psRoomListener != null) return

        psRoomListener = roomUseCase.observeUiPsRooms { psRooms ->
            _psRooms.value = psRooms
        }
    }


    fun removeCsRoomListener() {
        csRoomListener?.remove()
        csRoomListener = null
    }
    fun removePsRoomListener() {
        psRoomListener?.remove()
        psRoomListener = null
    }

}
