package com.example.softwareproject // 자신의 패키지 이름으로 변경

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.semantics.text
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
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
    }
}