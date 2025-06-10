package com.example.softwareproject.data.remote

import retrofit2.http.*

interface GitHubOAuthApi {
    @Headers("Accept: application/json")
    @POST("login/oauth/access_token")
    @FormUrlEncoded
    suspend fun fetchAccessToken(
        @Field("client_id")     clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code")          code: String,
        @Field("redirect_uri")  redirectUri: String,
        @Field("state")         state: String
    ): GitHubTokenResponse
}