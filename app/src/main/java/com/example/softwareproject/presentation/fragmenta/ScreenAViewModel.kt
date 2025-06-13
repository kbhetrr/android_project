package com.example.softwareproject.com.example.softwareproject.presentation.fragmenta

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softwareproject.data.nosql_entity.GithubInfo
import com.example.softwareproject.data.remote.user.GitHubUser
import com.example.softwareproject.data.remote.user.UserFullInfo
import com.example.softwareproject.domain.repository.UserRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class ScreenAViewModel @Inject constructor(
    private val userRepository: UserRepository
):ViewModel(){
    private val _userData = MutableLiveData<UserFullInfo>()
    val userData: LiveData<UserFullInfo> = _userData
    private val _githubUser = MutableLiveData<GitHubUser>()

    fun loadUserDataByUid(userId: String) {
        viewModelScope.launch {
            try {
                val firestore = FirebaseFirestore.getInstance()

                val githubInfoSnap = firestore.collection("github_info")
                    .whereEqualTo("firebaseUid", userId)
                    .limit(1)
                    .get()
                    .await()

                val githubInfo = githubInfoSnap.documents.firstOrNull()
                    ?.toObject(GithubInfo::class.java)
                    ?: throw Exception("GitHub 정보 없음")

                val githubUserId = githubInfo.userId
                    ?: throw Exception("GitHub ID 누락됨")

                val userInfo = userRepository.getUserInfoByGithubId(githubUserId)
                _userData.postValue(userInfo)

            } catch (e: Exception) {
                Log.e("ScreenAViewModel", "유저 정보 로딩 실패: ${e.message}")
            }
        }
    }





}