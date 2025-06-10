package com.example.softwareproject.util

enum class DifficultyCs(val level: Int) {
    EASY(1),
    MIDDLE(2),
    HARD(3);

    companion object {
        fun fromLevel(level: Int): DifficultyCs {
            return entries.firstOrNull { it.level == level } ?: EASY
        }
    }
}
