package com.example.softwareproject.model // 적절한 패키지로 변경

import com.google.gson.annotations.SerializedName

data class SolvedAcUser(
    @SerializedName("handle")
    val handle: String?,
    @SerializedName("bio")
    val bio: String?,
    @SerializedName("badgeId")
    val badgeId: String?, // 뱃지 ID (뱃지 이미지 URL 구성에 사용될 수 있음)
    @SerializedName("profileImageUrl")
    val profileImageUrl: String?, // 프로필 이미지 URL (있다면)
    @SerializedName("solvedCount")
    val solvedCount: Int?,
    @SerializedName("tier")
    val tier: Int?, // 티어 (숫자로 표현됨, 예: 1-30)
    @SerializedName("arenaTier")
    val arenaTier: Int?,
    // ... 필요한 다른 필드들 추가
)

// 만약 뱃지 이미지 URL을 직접 제공하지 않고, ID로 구성해야 한다면,
// 뱃지 이미지 URL을 만드는 로직이 필요할 수 있습니다.
// 예: "https://static.solved.ac/badges/FILENAME.svg"
// FILENAME을 badgeId로 추정하거나, 다른 규칙이 있는지 API 문서 확인 필요.package com.example.softwareproject.model // 적절한 패키지로 변경
//
//   import com.google.gson.annotations.SerializedName
//
//   data class SolvedAcUser(
//       @SerializedName("handle")
//       val handle: String?,
//       @SerializedName("bio")
//       val bio: String?,
//       @SerializedName("badgeId")
//       val badgeId: String?, // 뱃지 ID (뱃지 이미지 URL 구성에 사용될 수 있음)
//       @SerializedName("profileImageUrl")
//       val profileImageUrl: String?, // 프로필 이미지 URL (있다면)
//       @SerializedName("solvedCount")
//       val solvedCount: Int?,
//       @SerializedName("tier")
//       val tier: Int?, // 티어 (숫자로 표현됨, 예: 1-30)
//       // ... 필요한 다른 필드들 추가
//   )
//
//   // 만약 뱃지 이미지 URL을 직접 제공하지 않고, ID로 구성해야 한다면,
//   // 뱃지 이미지 URL을 만드는 로직이 필요할 수 있습니다.
//   // 예: "https://static.solved.ac/badges/FILENAME.svg"
//   // FILENAME을 badgeId로 추정하거나, 다른 규칙이 있는지 API 문서 확인 필요.