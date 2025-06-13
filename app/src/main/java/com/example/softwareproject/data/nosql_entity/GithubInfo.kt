package com.example.softwareproject.data.nosql_entity

import com.google.firebase.Timestamp

data class GithubInfo(
    var userId : String = "",
    var avatarUrl: String? = null,
    var githubName: String? = null,
    var email: String? = null,
    var bio: String? = null,
    var followers: Int? = null,
    var following: Int? = null,
    var firebaseUid: String? = null
)
