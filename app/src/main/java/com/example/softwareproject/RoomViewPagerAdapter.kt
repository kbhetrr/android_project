package com.example.softwareproject // 실제 패키지 이름으로 변경

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity // FragmentActivity 사용
import androidx.viewpager2.adapter.FragmentStateAdapter

class RoomViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        TabCsFragment.newInstance(), // 탭 1에 표시할 프래그먼트
        TabPsFragment.newInstance()  // 탭 2에 표시할 프래그먼트
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}