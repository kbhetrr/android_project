package com.example.softwareproject.util

enum class UserRole(val role: Int) {
    HOST(1),
    GUEST(2);

    companion object {
        fun fromRole(role: Int): UserRole {
            return entries.firstOrNull { it.role == role } ?: GUEST
        }
    }
}
