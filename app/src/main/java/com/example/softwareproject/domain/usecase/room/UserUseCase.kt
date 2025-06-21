package com.example.softwareproject.com.example.softwareproject.domain.usecase.room

import com.example.softwareproject.com.example.softwareproject.data.dto.user.BaekjoonInfoDto
import com.example.softwareproject.data.dto.user.GitHubInfoDto
import com.example.softwareproject.data.dto.user.UserAbilityDto
import com.example.softwareproject.domain.repository.ProblemRepository
import com.example.softwareproject.domain.repository.RoomRepository
import com.example.softwareproject.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserUseCase@Inject constructor(
    private val problemRepository: ProblemRepository,
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
)  {

    suspend fun getUserAbility(userId : String) : UserAbilityDto? {
        return userRepository.getUserAbilityInfo(userId)
    }
    suspend fun getCurrentGitHubInfo() : GitHubInfoDto? {
        val uid = FirebaseAuth.getInstance().uid.toString()
        return userRepository.getUserGithubInfoByFirebaseUid(uid)
    }
    suspend fun getCurrentUserAbility() :UserAbilityDto? {
        val uid = FirebaseAuth.getInstance().uid.toString()
        val gitHubInfo = userRepository.getUserGithubInfoByFirebaseUid(uid)
        return gitHubInfo?.userId?.let { userRepository.getUserAbilityInfo(it) }
    }
    suspend fun getOpponentUserAbility(roomId: String): UserAbilityDto? {
        val myFirebaseUid = FirebaseAuth.getInstance().uid.toString()


        val myGitHubInfo = userRepository.getUserGithubInfoByFirebaseUid(myFirebaseUid)
        val myUserId = myGitHubInfo?.userId ?: return null

        val participantList = roomRepository.getRoomParticipantList(roomId)

        val opponent = participantList.firstOrNull { it.userId != myUserId } ?: return null

        return userRepository.getUserAbilityInfo(opponent.userId)
    }

    suspend fun saveBaekjoonInfo(solvedAcHandle: String) {
        val firebaseUid = FirebaseAuth.getInstance().uid ?: return

        val githubInfo = userRepository.getUserGithubInfoByFirebaseUid(firebaseUid)
            ?: return

        val userId = githubInfo.userId

        val existingInfo = userRepository.getUserBaekjoonInfoByUserId(userId)

        if (existingInfo == null) {
            userRepository.saveBaekjoonInfo(
                userId = userId,
                solvedAcHandle = solvedAcHandle
            )
        }
    }

}