package com.example.softwareproject.util

enum class RoomState(val level: Int) {
    WAITING(1),
    PROGRESS(2),
    FINISHED(3);

    companion object {
        fun fromLevel(level: Int): RoomState {
            return entries.firstOrNull { it.level == level } ?: WAITING
        }
    }
}
