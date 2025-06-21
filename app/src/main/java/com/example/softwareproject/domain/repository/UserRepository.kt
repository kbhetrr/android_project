package com.example.softwareproject.domain.repository

import com.example.softwareproject.com.example.softwareproject.data.dto.user.BaekjoonInfoDto
import com.example.softwareproject.data.dto.user.GitHubInfoDto
import com.example.softwareproject.data.dto.user.UserAbilityDto
import com.example.softwareproject.data.dto.user.UserBattleLogDto
import com.example.softwareproject.data.dto.user.UserDto
import com.example.softwareproject.data.remote.user.UserFullInfo
import com.example.softwareproject.data.remote.user.UserSaveInfo

interface UserRepository {
    suspend fun getUserFullInfo(userId: String) : UserFullInfo
    suspend fun createUserInfo(userSaveInfo: UserSaveInfo)
    suspend fun isUserExists(userId: String): Boolean
    suspend fun getUserInfoByGithubId(githubId: String): UserFullInfo

    suspend fun createUser(user:UserDto)
    suspend fun createUserAbility(userAbility: UserAbilityDto)
    suspend fun createUserBattleLog(userBattleLogDto: UserBattleLogDto)
    suspend fun createUserGithubInfo(githubInfo:GitHubInfoDto)
    suspend fun createUserBaekjoonInfo(baekjoonInfo: BaekjoonInfoDto)
    suspend fun saveBaekjoonInfo(userId: String, solvedAcHandle: String)

    suspend fun getUserInfo(userId: String) : UserDto?
    suspend fun getUserAbilityInfo(userId: String) : UserAbilityDto?
    suspend fun getUserBattleLogInfo(userId: String) : UserBattleLogDto?
    suspend fun getUSerGithubInfo(userId: String) : GitHubInfoDto?
    suspend fun getUserGithubInfoByFirebaseUid(firebaseUid: String) : GitHubInfoDto?
    suspend fun getUserBaekjoonInfoByUserId(userId: String) : BaekjoonInfoDto?

    suspend fun updateBaekjoonInfo(baekjoonInfoDto: BaekjoonInfoDto)

}