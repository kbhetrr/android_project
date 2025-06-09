package com.example.softwareproject.data.entity.base

import androidx.room.ColumnInfo

open class BattleStats {
    @ColumnInfo(name = "hp")
    var hp: Int = 0

    @ColumnInfo(name = "attack")
    var attack: Int = 0

    @ColumnInfo(name = "shield")
    var shield: Int = 0

    @ColumnInfo(name = "role")
    var role : UserRole = UserRole.GUEST
}
