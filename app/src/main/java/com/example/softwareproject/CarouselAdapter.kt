package com.example.softwareproject // 실제 패키지 이름으로 변경

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

// 실제 페이지 수만 정의
private const val NUM_PAGES = 2 // CarouselPageOneFragment, CarouselPageTwoFragment

class CarouselAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return NUM_PAGES // 실제 페이지 수 반환
    }

    override fun createFragment(position: Int): Fragment {
        // position은 이제 0 또는 1만 가능
        return when (position) {
            0 -> CarouselPageOneFragment()
            1 -> CarouselPageTwoFragment()
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}