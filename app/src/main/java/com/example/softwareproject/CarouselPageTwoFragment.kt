package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.text
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.decode.SvgDecoder // SVG를 사용하므로 필요
import com.example.softwareproject.model.SolvedAcUser // 이전 단계에서 만든 데이터 모델
import com.example.softwareproject.network.RetrofitClient // 이전 단계에서 만든 Retrofit 클라이언트
import com.example.softwareproject.util.UserPreferences
import kotlinx.coroutines.launch

class CarouselPageTwoFragment : Fragment() {

    private lateinit var solvedImageView: ImageView
    private lateinit var SolvedIdView: TextView
    private lateinit var arenaImageView: ImageView
//    private lateinit var profileImageView: ImageView // 프로필 이미지용
//    private lateinit var handleTextView: TextView
//    private lateinit var solvedCountTextView: TextView
//    private lateinit var bioTextView: TextView

    // API에서 가져올 사용자 핸들 (예시, 실제로는 동적으로 설정하거나 인자로 받을 수 있음)
    // private val userHandle = "kimkh7534"
    private lateinit var userHandle: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 이 프래그먼트를 위한 레이아웃을 인플레이트합니다.
        return inflater.inflate(R.layout.fragment_carousel_page_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // XML 레이아웃에서 ID를 사용하여 뷰 초기화
        solvedImageView = view.findViewById(R.id.solved)
        arenaImageView = view.findViewById(R.id.arena)
        SolvedIdView = view.findViewById(R.id.solved_id)
        context?.let {
            userHandle = UserPreferences.getSolvedAcHandle(it).toString()
        }
        SolvedIdView.setText("@" + userHandle)
//        profileImageView = view.findViewById(R.id.profile_image_view)
//        handleTextView = view.findViewById(R.id.handle_text_view)
//        solvedCountTextView = view.findViewById(R.id.solved_count_text_view)
//        bioTextView = view.findViewById(R.id.bio_text_view)

        // 데이터 가져오기 시작
        fetchUserProfileData()
    }

    private fun fetchUserProfileData() {
        lifecycleScope.launch {
            try {
                // Retrofit을 사용하여 API 호출
                val response = RetrofitClient.instance.getUserProfile(userHandle)
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    if (userProfile != null) {
                        updateUI(userProfile)
                    } else {
                        Log.e("PageTwoFragment", "API Response body is null")
                        showError("사용자 정보를 가져오지 못했습니다 (null body).")
                    }
                } else {
                    Log.e("PageTwoFragment", "API Error: ${response.code()} - ${response.message()}")
                    showError("API 오류 발생: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("PageTwoFragment", "Network request failed", e)
                showError("네트워크 요청 실패: ${e.localizedMessage}")
            }
        }
    }

    private fun updateUI(user: SolvedAcUser) {
        // TextViews 업데이트
//        handleTextView.text = user.handle ?: "핸들 정보 없음"
//        solvedCountTextView.text = "푼 문제: ${user.solvedCount?.toString() ?: "N/A"}"
//        bioTextView.text = user.bio ?: "소개 없음"

        // 프로필 이미지 로드 (URL이 있다면)
//        user.profileImageUrl?.let { url ->
//            if (url.isNotEmpty()) {
//                profileImageView.visibility = View.VISIBLE
//                profileImageView.load(url) {
//                    placeholder(R.drawable.ic_placeholder) // 적절한 플레이스홀더 이미지로 변경
//                    error(R.drawable.ic_error)         // 적절한 에러 이미지로 변경
//                    // SVG인 경우 SvgDecoder 필요 (Coil 2.x에서는 coil-svg 추가 시 자동 감지될 수 있음)
//                    // 만약 profileImageUrl도 SVG라면 아래와 같이 명시
//                    // decoderFactory(SvgDecoder.Factory())
//                }
//            } else {
//                profileImageView.visibility = View.GONE
//            }
//        } ?: run {
//            profileImageView.visibility = View.GONE
//        }

        // 티어 이미지 로드
        user.tier?.let { tierNumber ->
            // solved.ac 티어 번호: 0 (Unrated), 1-5 (Bronze V-I), 6-10 (Silver V-I), ..., 26-30 (Ruby V-I)
            if (tierNumber >= 0) { // Unrated가 아닌 경우에만 티어 이미지 표시
                val tierImageUrl = "https://static.solved.ac/tier_small/${tierNumber}.svg"
                solvedImageView.visibility = View.VISIBLE
                solvedImageView.load(tierImageUrl) {
                    decoderFactory(SvgDecoder.Factory()) // SVG 이미지이므로 SvgDecoder 사용
                    placeholder(R.drawable.iconmonstr_github_1) // 티어용 플레이스홀더
                    error(R.drawable.sword_icon)         // 티어용 에러 이미지
                }
            } else {
                solvedImageView.visibility = View.GONE // Unrated 또는 티어 정보 없을 시 숨김
            }
        } ?: run {
            solvedImageView.visibility = View.GONE
        }

        // 아레나 티어 로드
        user.arenaTier?.let { tierNumber ->
            // solved.ac 티어 번호: 0 (Unrated), 1-5 (Bronze V-I), 6-10 (Silver V-I), ..., 26-30 (Ruby V-I)
            if (tierNumber >= 0) { // Unrated가 아닌 경우에만 티어 이미지 표시
                val tierImageUrl = "https://static.solved.ac/tier_arena/${tierNumber}.svg"
                arenaImageView.visibility = View.VISIBLE
                arenaImageView.load(tierImageUrl) {
                    decoderFactory(SvgDecoder.Factory()) // SVG 이미지이므로 SvgDecoder 사용
                    placeholder(R.drawable.iconmonstr_github_1) // 티어용 플레이스홀더
                    error(R.drawable.sword_icon)         // 티어용 에러 이미지
                }
            } else {
                arenaImageView.visibility = View.GONE // Unrated 또는 티어 정보 없을 시 숨김
            }
        } ?: run {
            arenaImageView.visibility = View.GONE
        }

        // 뱃지 이미지 로드 (badgeId가 있다면)
//        user.badgeId?.let { badge ->
//            if (badge.isNotEmpty()) {
//                // 예시: 뱃지 ID가 "tricolor_ambigram" 이면 파일명은 "tricolor_ambigram.svg"
//                // 실제 뱃지 이미지 URL 규칙을 정확히 확인해야 합니다.
//                // 만약 badgeId가 이미 전체 URL이거나 파일명만 제공하는 등 규칙이 다를 수 있습니다.
//                // 여기서는 badgeId가 파일명(확장자 제외)이라고 가정합니다.
//                val badgeImageUrl = "https://static.solved.ac/badges/${badge}.svg"
//                badgeImageView.visibility = View.VISIBLE
//                badgeImageView.load(badgeImageUrl) {
//                    decoderFactory(SvgDecoder.Factory()) // SVG 이미지이므로 SvgDecoder 사용
//                    placeholder(R.drawable.ic_placeholder_badge) // 뱃지용 플레이스홀더
//                    error(R.drawable.ic_error_badge)         // 뱃지용 에러 이미지
//                }
//            } else {
//                badgeImageView.visibility = View.GONE
//            }
//        } ?: run {
//            badgeImageView.visibility = View.GONE
//        }
    }

    private fun showError(message: String) {
        // 사용자에게 오류 메시지를 보여주는 간단한 방법 (Toast 사용)
        // 실제 앱에서는 더 정교한 오류 처리 UI를 사용할 수 있습니다.
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        // 필요하다면, 오류 발생 시 기본 이미지나 텍스트를 UI에 설정할 수 있습니다.
        // 예: tierImageView.setImageResource(R.drawable.default_tier_error)
    }
}