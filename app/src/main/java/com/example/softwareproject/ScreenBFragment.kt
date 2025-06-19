package com.example.softwareproject // 자신의 패키지 이름으로 변경

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.semantics.text
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.softwareproject.com.example.softwareproject.model.DamageCalculator
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenBFragment : Fragment() { // 클래스 이름을 FragmentB로 (기존 ScreenBFragment에서 변경 가정)

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var myViewPagerAdapter: RoomViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_b.xml을 inflate 합니다.
        val view = inflater.inflate(R.layout.fragment_b, container, false)

        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.view_pager)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewPager2 어댑터 설정
        // requireActivity()를 사용하여 FragmentActivity 컨텍스트를 전달합니다.
        myViewPagerAdapter = RoomViewPagerAdapter(requireActivity())
        viewPager.adapter = myViewPagerAdapter

        // TabLayout과 ViewPager2를 연결
        // TabLayoutMediator를 사용하여 탭 이름 등을 설정할 수 있습니다.
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "CS 배틀" // 첫 번째 탭 이름
                1 -> "PS 배틀"   // 두 번째 탭 이름
                else -> null
            }
            // 필요하다면 탭 아이콘 등도 설정 가능
            // tab.setIcon(R.drawable.ic_tab_one)
        }.attach() // 반드시 attach()를 호출해야 연결됩니다.

        // Damage Calculator Test
        val TAG = "DamageTest"
        // 테스트할 샘플 입력값 정의
        val testTier = 3f
        val testSolvers = 800f
        val testAverageTries = 3.5f

        Log.d(TAG, "데미지 계산을 시작합니다...")
        Log.d(TAG, "입력값 -> Tier: $testTier, Solvers: $testSolvers, AvgTries: $testAverageTries")

        try {
            // 3. DamageCalculator 인스턴스 생성 및 결과 계산
            // 'use' 블록을 사용하면 블록이 끝날 때 calculator.close()가 자동으로 호출되어 안전합니다.
            val damage = DamageCalculator(requireContext()).use { calculator ->
                calculator.predictDamage(testTier, testSolvers, testAverageTries)
            }

            // 4. 계산된 결과를 Logcat에 출력 (Debug 레벨)
            Log.d(TAG, "계산 결과 -> 예측 데미지: ${damage.toInt()}")

        } catch (e: Exception) {
            // 모델 로딩 실패 등 예외 발생 시 로그 출력
            Log.e(TAG, "데미지 계산 중 오류 발생", e)
        }
    }
}