package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw // 뷰의 크기를 정확히 알기 위해 사용
import androidx.fragment.app.Fragment

import dagger.hilt.android.AndroidEntryPoint

import androidx.viewpager2.widget.ViewPager2
import com.example.softwareproject.util.UserPreferences

// import com.google.android.material.imageview.ShapeableImageView


@AndroidEntryPoint
class ScreenAFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var carouselFragmentAdapter: CarouselAdapter
    // private lateinit var userProfileImageView: ShapeableImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // view.findViewById<ShapeableImageView>(R.id.user_profile_image)?.let {
        //     userProfileImageView = it
        // }

        viewPager = view.findViewById(R.id.carousel_view_pager)
        carouselFragmentAdapter = CarouselAdapter(this) // 어댑터는 무한 스크롤 없는 버전
        viewPager.adapter = carouselFragmentAdapter

        // ViewPager2가 그려지기 직전에 크기를 얻어와서 PageTransformer와 Padding을 설정합니다.
        // 이렇게 하면 ViewPager2의 실제 너비를 기준으로 계산할 수 있어 더 정확합니다.
        viewPager.doOnPreDraw {
            setupCarouselPeekEffect()
        }

        // 초기 페이지 설정 (0번 인덱스, 즉 첫 번째 페이지로 시작)
        // setCurrentItem은 PageTransformer와 Padding이 설정된 후 호출되는 것이 좋습니다.
        // doOnPreDraw 콜백 내에서 호출하거나, 그 이후에 호출합니다.
        // 여기서는 setupCarouselPeekEffect 내부에서 처리하지 않으므로 onViewCreated 마지막에 호출.
        if (carouselFragmentAdapter.itemCount > 0) {
            viewPager.setCurrentItem(0, false)
        }
    }

    private fun setupCarouselPeekEffect() {
        if (!isAdded || viewPager.width == 0) { // 프래그먼트가 추가되지 않았거나, 너비가 아직 0이면 실행하지 않음
            return
        }

        // 1. ViewPager2의 클리핑 비활성화
        viewPager.clipToPadding = false
        viewPager.clipChildren = false

        // 2. ViewPager2의 offscreenPageLimit 설정
        viewPager.offscreenPageLimit = 1 // 양옆 페이지를 미리 로드

        // 3. 페이지 간 간격 및 양옆에 보여질 너비 정의 (dimens.xml 또는 직접 값 사용)
        // res/values/dimens.xml 에 정의했다고 가정
        // <dimen name="viewpager_page_margin">16dp</dimen>
        // <dimen name="viewpager_peek_offset">32dp</dimen>
        val pageMarginPx = 16
        val peekOffsetPx = 32

        // 4. ViewPager2 자체에 패딩 설정
        // 첫 번째 페이지의 왼쪽과 마지막 페이지의 오른쪽에도 peekOffsetPx 만큼의 공간을 확보하고,
        // 페이지 사이의 간격(pageMarginPx)의 절반을 추가로 확보합니다.
        // 이렇게 하면 첫/마지막 페이지에서도 양옆으로 동일한 시각적 여백/미리보기가 가능해집니다.
        val horizontalPadding = peekOffsetPx + (pageMarginPx / 2)
        viewPager.setPadding(horizontalPadding, 0, horizontalPadding, 0)

        // 5. PageTransformer 설정
        viewPager.setPageTransformer { page, position ->
            // position: 현재 페이지가 중앙에 가까울수록 0에 가까워짐.
            // 오른쪽으로 스와이프하면 왼쪽 페이지가 나타나고 position은 0에서 -1로 감.
            // 왼쪽으로 스와이프하면 오른쪽 페이지가 나타나고 position은 0에서 1로 감.

            // ViewPager2의 너비에서 좌우 패딩을 제외한 실제 컨텐츠 영역의 너비
            // 이 너비는 페이지들이 실제로 배치되고 움직일 수 있는 공간입니다.
            val viewPagerContentWidth =
                viewPager.width - viewPager.paddingLeft - viewPager.paddingRight
        }
    }
}
// 각 페이지 아이템이 차지하는 너비 (PageTransformer가 적용되기 전의 기본 너비)
// 일반적으로 페이지는 ViewPager2의 너비만큼을 차지하려고 합니다.
// val pageFullWidth = page.width // 이 시점에서는 page.width가 정확하지 않을 수 있음

// 페이지를 얼마나 이동시킬 것인가?
// 목표: 현재 페이지(position 0)는 중앙에,
// 옆 페이지(position 1 또는 -1)는 peekOffsetPx 만큼만 보이도록.
// 페이지 사이에는 pageMarginPx 만큼의 간격.

// 페이지 이동량을 계산합니다.
// position이 1일 때 (오른쪽 페이지가 중앙으로 올 때), 해당 페이지는 왼쪽으로 이동해야 합니다.
// 이동량 = (ViewPager2 컨텐츠 너비 - peekOffsetPx) + pageMarginPx
// 하지만 이 계산은 페이지가 완전히 화면 밖으로 나갔다가 들어오는 것을 기준으로 하므로,
// 좀 더 간단하게는, 페이지 간의 상대적인 위치를 조절하는 데 집중합니다.

// `offset`은 각 페이지가 자신의 원래 위치에서 얼마나 이동해야 하는지를 나타냅니다.
// `position`이 0일 때 `offset`은 0이 되어야 합니다 (중앙 페이지는 이동 없음).
// `position`이 1 또는 -1일 때, 페이지는 `peekOffsetPx`만큼 보이도록 이동해야 합니다.
// 이 이동은 `pageMarginPx`도 고려해야 합니다.
// `-(peekOffsetPx * 2 + pageMarginPx)` 와 유사한 값을 `position`에 곱하는 방식이 일반적입니다.
// 하지만 이 방식은 ViewPager2의 패딩과 결합될 때 미세 조정이 필요할 수 있습니다.

// 여기서는 ViewPager2의 패딩을 통해 이미 양옆 공간이 확보되었으므로,
// PageTransformer는 페이지들을 "서로 가깝게" 만드는 역할을 합니다.
// 페이지들이 서로 (페이지 너비 - peekOffset * 2 - margin) 만큼 겹치도록 이동합니다.

// `offsetPosition`은 페이지가 얼마나 이동해야 하는지를 결정하는 핵심 값입니다.
// 페이지가 화면 중앙에서 멀어질수록(position의 절대값이 커질수록) 더 많이 이동합니다.
// `pageMarginPx`는 페이지 사이의 간격을 만듭니다.
// `peekOffsetPx`는 옆 페이지가 얼마나 보일지를 결정합니다.
// `viewPager.paddingLeft` (또는 `horizontalPadding`)은 첫 페이지의 시작 위치에 영향을 줍니다.

// 이 값은 페이지가 `position`에 따라 이동해야 하는 총 거리를 나타냅니다.
// 페이지가 중앙(position=0)에 있을 때는 이동하지 않습니다.
// 페이지가 완전히 옆(position=1 또는 -1)으로 갔을 때,
//