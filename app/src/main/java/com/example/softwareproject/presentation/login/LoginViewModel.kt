package com.example.softwareproject.presentation.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.softwareproject.com.example.softwareproject.data.remote.github.GitHubApi
import com.example.softwareproject.com.example.softwareproject.data.remote.user.GitHubUser
import com.example.softwareproject.com.example.softwareproject.data.remote.user.UserSaveInfo
import com.example.softwareproject.domain.repository.UserRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val _loginResult = MutableLiveData<FirebaseUser?>()
    val loginResult: LiveData<FirebaseUser?> = _loginResult
    private val _githubUser = MutableLiveData<GitHubUser>()
    val githubUser: LiveData<GitHubUser> = _githubUser

    fun loginWithGitHub(activity: Activity) {
        val provider = OAuthProvider.newBuilder("github.com")
        provider.setScopes(listOf("read:user", "user:email"))

        val pending = auth.pendingAuthResult
        if (pending != null) {
            pending
                .addOnSuccessListener { handleLoginSuccess(it) }
                .addOnFailureListener { _loginResult.value = null }
        } else {
            auth.startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener { handleLoginSuccess(it) }
                .addOnFailureListener { _loginResult.value = null }
        }
    }

    private fun handleLoginSuccess(authResult: AuthResult) {
        val credential = authResult.credential as? OAuthCredential
        val accessToken = credential?.accessToken

        Log.d("GitHub", "Access Token: $accessToken")

        _loginResult.value = authResult.user

        accessToken?.let { token ->
            viewModelScope.launch {
                try {
                    val retrofit = Retrofit.Builder()
                        .baseUrl("https://api.github.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    val api = retrofit.create(GitHubApi::class.java)
                    val user = api.getUserInfo("Bearer $token")
                    _githubUser.postValue(user)
                    Log.d("GitHub", "유저 정보: $user")
                } catch (e: Exception) {
                    Log.e("GitHub", "API 호출 실패: ${e.message}")
                }
            }
        }
    }
    fun createUser(githubUser: GitHubUser, firebaseUser: FirebaseUser) {
        viewModelScope.launch {
            userRepository.createUserInfo(
                userSaveInfo = UserSaveInfo(
                    userId = githubUser.id.toString(),
                    name = githubUser.name ?: githubUser.login,
                    email = githubUser.email ?: firebaseUser.email ?: "",
                    bio = githubUser.bio ?: "",
                    avatarUrl = githubUser.avatarUrl ?: "",
                    followers = githubUser.followers,
                    followings = githubUser.following,
                    firebaseUid = firebaseUser.uid
                )
            )
        }
    }
    suspend fun checkUserExist(userId: String): Boolean {
        return userRepository.isUserExists(userId)
    }

}

//수정된 로직
