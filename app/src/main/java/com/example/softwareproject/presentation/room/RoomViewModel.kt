package com.example.softwareproject.presentation.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softwareproject.data.remote.room.UiCodingRoomItem
import com.example.softwareproject.data.remote.room.UiCsRoomItem
import com.example.softwareproject.domain.repository.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _csRooms = MutableLiveData<List<UiCsRoomItem>>()
    private val _codingRooms = MutableLiveData<List<UiCodingRoomItem>>()
    val csRooms: LiveData<List<UiCsRoomItem>> = _csRooms
    val codingRooms: LiveData<List<UiCodingRoomItem>> = _codingRooms
    fun loadCsRooms() {
        viewModelScope.launch {
            val rooms = roomRepository.listCsRoom()
            _csRooms.value = rooms
        }
    }

    fun loadCodingRooms(){
        viewModelScope.launch {
            val rooms = roomRepository.listCodingRoom()
            _codingRooms.value = rooms
        }
    }
}