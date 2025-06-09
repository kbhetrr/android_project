package com.example.softwareproject.data.entity.base

enum class RoomType(val type: Int) {
    CS(1),
    CODING(2);

    companion object {
        fun fromType(type: Int): RoomType {
            return entries.firstOrNull { it.type == type } ?: CS
        }
    }
}

