package com.example.softwareproject.com.example.softwareproject.data.nosql_entity

import com.google.firebase.Timestamp

data class GithubInfo(
    var github_user_id: String = "",
    var user_info_id: String = "",
    var avatar_url: String? = null,
    var github_name: String? = null,
    var email: String? = null,
    var bio: String? = null,
    var followers: Int? = null,
    var following: Int? = null,
    var created_at: Timestamp? = null,
    var updated_at: Timestamp? = null
)
