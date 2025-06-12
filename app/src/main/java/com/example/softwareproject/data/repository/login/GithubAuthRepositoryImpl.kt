package com.example.softwareproject.data.repository.login

import com.example.softwareproject.domain.repository.AuthRepository

import com.example.softwareproject.data.remote.GitHubOAuthApi
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Named

class GithubAuthRepositoryImpl @Inject constructor(
    private val api: GitHubOAuthApi,               // ← api 주입
    @Named("githubClientId")     private val clientId: String,
    @Named("githubClientSecret") private val clientSecret: String,
    @Named("githubRedirectUri")  private val redirectUri: String

) : AuthRepository {
    override fun buildAuthUrl(state: String): String =
        HttpUrl.Builder()
            .scheme("https")
            .host("github.com")
            .addPathSegments("login/oauth/authorize")
            .addQueryParameter("client_id",     clientId)
            .addQueryParameter("redirect_uri",  redirectUri)
            .addQueryParameter("scope",         "read:user,user:email")
            .addQueryParameter("state",         state)
            .build()
            .toString()

    override suspend fun exchangeCodeForToken(code: String, state: String): String =
        api.fetchAccessToken(
            clientId     = clientId,
            clientSecret = clientSecret,
            code         = code,
            redirectUri  = redirectUri,
            state = state
        ).accessToken
}