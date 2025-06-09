package com.example.softwareproject.data.entity.base

enum class UserRole(val role: Int) {
    HOST(1),
    GUEST(2);

    companion object {
        fun fromRole(role: Int): UserRole {
            return entries.firstOrNull { it.role == role } ?: GUEST
        }
    }
}
