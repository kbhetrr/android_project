package com.example.softwareproject.util

enum class DifficultyPs(val min: Int, val max: Int, val displayName: String) {
    BRONZE(1, 5, "BRONZE"),
    SILVER(6, 10, "SILVER"),
    GOLD(11, 15, "GOLD"),
    PLATINUM(16, 20, "PLATINUM");

    override fun toString(): String {
        return displayName
    }

}