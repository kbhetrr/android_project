package com.example.softwareproject.data.repository

import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.GithubInfo
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.User
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.UserAbility
import com.example.softwareproject.com.example.softwareproject.data.nosql_entity.UserBattleLog
import com.example.softwareproject.com.example.softwareproject.data.remote.user.UserFullInfo
import com.example.softwareproject.com.example.softwareproject.data.remote.user.UserSaveInfo
import com.example.softwareproject.domain.repository.UserRepository
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val fireBaseStore : FirebaseFirestore
) : UserRepository{

    override suspend fun getUserInfo(userId: String) : UserFullInfo{
        val userDoc = fireBaseStore.collection("user").document(userId).get().await()
        val githubDoc = fireBaseStore.collection("github_info").document(userId).get().await()
        val abilityDoc = fireBaseStore.collection("user_ability").document(userId).get().await()
        val battleDoc = fireBaseStore.collection("user_battle_log").document(userId).get().await()

        if (!userDoc.exists() || !githubDoc.exists() || !abilityDoc.exists() || !battleDoc.exists()) {
            throw Exception("유저 정보 중 일부가 존재하지 않습니다.")
        }

        val user = userDoc.toObject(User::class.java)!!
        val github = githubDoc.toObject(GithubInfo::class.java)!!
        val ability = abilityDoc.toObject(UserAbility::class.java)!!
        val battle = battleDoc.toObject(UserBattleLog::class.java)!!

        return UserFullInfo(user, github, ability, battle)
    }

    override suspend fun createUserInfo(userSaveInfo: UserSaveInfo){
        val userData = User(
            userId = userSaveInfo.userId,
            createdAt = Timestamp.now(),
            updatedAt = Timestamp.now(),
        )

        val userAbility = UserAbility(
            userId = userSaveInfo.userId,
            exp = 0,
            level = 1,
            hp = 10,
            attack = 10,
            shield = 10,
        )

        val userBattleLog = UserBattleLog(
            userId = userSaveInfo.userId,
            match = 0,
            win = 0,
            lose = 0,
            rate = 0.0
        )
        val githubInfo = GithubInfo(
            userId = userSaveInfo.userId,
            avatarUrl = userSaveInfo.avatarUrl,
            githubName = userSaveInfo.name,
            email = userSaveInfo.email,
            bio = userSaveInfo.bio,
            followers = userSaveInfo.followings,
            following = userSaveInfo.followings
        )
        fireBaseStore.collection("user").document(userSaveInfo.userId).set(userData).await()
        fireBaseStore.collection("user_ability").document(userSaveInfo.userId).set(userAbility).await()
        fireBaseStore.collection("user_battle_Log").document(userSaveInfo.userId).set(userBattleLog).await()
        fireBaseStore.collection("github_info").document(userSaveInfo.userId).set(githubInfo).await()
    }

    override suspend fun isUserExists(userId: String): Boolean {
        val doc = fireBaseStore
            .collection("user")
            .document(userId)
            .get()
            .await()
        return doc.exists()
    }

}