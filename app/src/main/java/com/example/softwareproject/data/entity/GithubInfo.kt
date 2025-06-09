package com.example.softwareproject.data.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.softwareproject.data.entity.base.BaseEntity

@Entity(tableName = "github_info",
    foreignKeys = [
        ForeignKey(
            entity = UserInfo::class,
            parentColumns = ["user_info_id"],
            childColumns = ["user_info_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_info_id"], unique = true)]
)


data class GithubInfo(
    @PrimaryKey
    @ColumnInfo(name = "github_user_id")
    val githubUserid : Long,
    @ColumnInfo(name = "user_info_id")
    val userInfoId : Int,
    @ColumnInfo(name = "avatar_url")
    var avatarUrl : String? = null,
    @ColumnInfo(name = "github_name")
    var githubName : String? = null,
    @ColumnInfo(name = "email")
    var email : String? = null,
    @ColumnInfo(name = "bio")
    var bio : String? = null,
    @ColumnInfo(name = "followers")
    var followers : Int ? = null,
    @ColumnInfo(name = "following")
    var following : Int ? = null,
    @Embedded
    var baseEntity : BaseEntity = BaseEntity()
)