package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.Matrix
import androidx.fragment.app.Fragment
import coil.decode.SvgDecoder
import coil.load
import com.example.softwareproject.util.UserPreferences
import kotlin.math.roundToInt
import kotlin.text.toFloat


class CarouselPageOneFragment : Fragment() {

    private lateinit var githubImageView: ImageView
    private lateinit var githubIdView: TextView
    //private val githubChartUrl = "https://ghchart.rshah.org/kbhetrr" // 사용자 이름 변경 가능
    private lateinit var githubChartUrl: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 이 프래그먼트를 위한 레이아웃을 인플레이트합니다.
        // 예시로 간단한 TextView만 있는 레이아웃을 사용합니다.
        // 실제로는 res/layout/fragment_carousel_page_one.xml 같은 별도의 레이아웃 파일을 만듭니다.
        val view = inflater.inflate(R.layout.fragment_carousel_page_one, container, false) // XML 레이아웃 필요

        // (선택 사항) 프래그먼트 내의 뷰에 데이터 설정 또는 로직 추가
        // val titleTextView = view.findViewById<TextView>(R.id.page_one_title)
        // titleTextView.text = "첫 번째 캐러셀 페이지"

        return view
    }

    // 필요하다면 뷰 초기화 등을 위해 onViewCreated 사용
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 예: view.findViewById<TextView>(R.id.some_text_view).text = "내용"
        var githubId: String? = ""
        context?.let {
            githubId = UserPreferences.getGithubId(it)
            githubChartUrl = "https://ghchart.rshah.org/" + githubId
        }
        githubIdView = view.findViewById(R.id.github_id)
        githubIdView.setText("@" + githubId)
        githubImageView = view.findViewById(R.id.github_svg)

        Log.d("CarouselPageOne", "githubImageView is null: ${githubImageView == null}") // 로그 추가

        // Coil을 사용하여 SVG 이미지 로드
        githubImageView.load(githubChartUrl) {
            // SvgDecoder를 명시적으로 추가해야 하는 경우 (Coil 버전에 따라 다를 수 있음)
            // Coil 2.x 이상이고 coil-svg 의존성이 제대로 추가되었다면,
            // SvgDecoder는 자동으로 ComponentRegistry에 등록되어 별도 설정이 필요 없을 수 있습니다.
            // 만약 SVG가 로드되지 않는다면 아래 decoderFactory 라인을 시도해 보세요.
            decoderFactory(SvgDecoder.Factory()) // Coil 2.x
            // 또는 이전 버전 Coil의 경우:
            // decoder(SvgDecoder(requireContext()))
            size(1326, 208)
            // 로딩 중 또는 에러 시 보여줄 플레이스홀더 및 에러 이미지 (선택 사항)

            placeholder(R.drawable.sword_icon) // 예시: res/drawable/ic_placeholder.xml
            error(R.drawable.shield_sedo_line_icon)         // 예시: res/drawable/ic_error.xml
            // crossfade(true) // 부드러운 이미지 전환 효과 (선택 사항)

            listener(
                onSuccess = { _, result ->
                    val drawable = result.drawable
                    if (drawable != null) {
                        // ImageView의 크기가 확정된 후에 Matrix를 설정하기 위해 post 사용
                        githubImageView.post {
                            configureMatrix(githubImageView, drawable)
                        }
                    }
                },
                onError = { _, throwable ->
                    // 에러 처리
                    Log.e("MatrixImageView", "Image load error")
                }
            )
        }
    }

    // Matrix를 계산하고 적용하는 함수
    private fun configureMatrix(imageView: ImageView, drawable: Drawable) {
        val viewWidth = imageView.width.toFloat()
        val viewHeight = imageView.height.toFloat()

        if (viewWidth == 0f || viewHeight == 0f) {
            // 뷰의 크기가 아직 결정되지 않았으면 아무것도 하지 않음
            return
        }

        val drawableWidth = drawable.intrinsicWidth.toFloat()
        val drawableHeight = drawable.intrinsicHeight.toFloat()

        if (drawableWidth <= 0 || drawableHeight <= 0) {
            // Drawable의 크기가 유효하지 않으면 아무것도 하지 않음
            return
        }

        val matrix = android.graphics.Matrix()

        // 1. 스케일 계산: 이미지 높이를 ImageView 높이에 맞춤
        val scale = viewHeight / drawableHeight

        matrix.setScale(scale, scale)

        // 2. 가로 이동(Translation X) 계산
        val scaledImageWidth = drawableWidth * scale
        var dx = 0f

        if (scaledImageWidth > viewWidth) {
            // 스케일된 이미지 너비가 ImageView 너비보다 크면,
            // 이미지의 오른쪽 끝이 ImageView의 오른쪽 끝에 오도록 dx 계산
            dx = viewWidth - scaledImageWidth
        } else {
            // 스케일된 이미지 너비가 ImageView 너비보다 작거나 같으면,
            // 이미지를 ImageView의 가로 중앙에 배치 (선택 사항, 또는 왼쪽 정렬 dx = 0)
            dx = (viewWidth - scaledImageWidth) / 2f
        }

        // 3. 세로 이동(Translation Y) 계산: 이 경우 0
        val dy = 0f // 이미지 높이가 뷰 높이에 맞춰졌으므로 y축 이동은 없음

        matrix.postTranslate(dx.roundToInt().toFloat(), dy.roundToInt().toFloat())
        imageView.imageMatrix = matrix
    }
}