package com.example.softwareproject.com.example.softwareproject.data.remote.user

import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.GithubInfo
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.User
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.UserAbility
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.UserBattleLog

data class UserFullInfo(
    val user: User,
    val githubInfo: GithubInfo,
    val userAbility: UserAbility,
    val userBattleLog: UserBattleLog
)