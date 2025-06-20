package com.example.softwareproject.util

enum class DifficultyPs(val min: Int, val max: Int, val displayName: String) {
    BRONZE(1, 5, "브론즈"),
    SILVER(6, 10, "실버"),
    GOLD(11, 15, "골드"),
    PLATINUM(16, 20, "플래티넘");

    override fun toString(): String {
        return displayName
    }

}