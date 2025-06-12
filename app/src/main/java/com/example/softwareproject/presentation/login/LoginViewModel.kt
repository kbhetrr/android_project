package com.example.softwareproject.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import com.example.softwareproject.domain.usecase.login.github.BuildAuthUrlUseCase
import com.example.softwareproject.domain.usecase.login.github.ExchangeGithubCodeUseCase

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val buildAuthUrl: BuildAuthUrlUseCase,
    private val exchangeCode: ExchangeGithubCodeUseCase.ExchangeCodeForTokenUseCase
) : ViewModel() {

    private val _authUrl = MutableStateFlow("")//CustomTabs에서 열어줄 인증 URL
    val authUrl: StateFlow<String> = _authUrl

    private var currentState: String = "" //currentState: CSRF 방지용 state. URL에 담아 보낸 후, redirect 시 검증.

    fun startLogin() {
        currentState = UUID.randomUUID().toString()
        _authUrl.value = buildAuthUrl(currentState) //로그인 URL 생성
    }

    fun handleRedirect(code: String?, state: String?) {
        if (code != null && state == currentState) {
            viewModelScope.launch {
                val token = exchangeCode(code, state)//엑세스 토큰을 획득한다.
            }
        }
    }
}