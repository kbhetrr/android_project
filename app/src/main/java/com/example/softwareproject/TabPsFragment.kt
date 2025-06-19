package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.softwareproject.presentation.room.RoomViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabPsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: RoomRecyclerAdapter // 어댑터 타입

    private val viewModel: RoomViewModel by viewModels()//임준식 추가

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab_ps, container, false) // fragment_tab_one.xml 사용
        recyclerView = view.findViewById(R.id.recycler_view_tab_two)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(context)
        myAdapter = RoomRecyclerAdapter(emptyList()) // 🔥 어댑터 초기화
        recyclerView.adapter = myAdapter

        // 더미 데이터 생성 (실제로는 ViewModel 등에서 가져옴)
//        val dummyItems = List(20) { MyItem("탭 1 아이템 ${it + 1}", "설명 ${it + 1}") }
//        myAdapter = RoomRecyclerAdapter(dummyItems)
//        recyclerView.adapter = myAdapter

        //임준식 추가
        // 🔥 LiveData 관찰
        viewModel.csRooms.observe(viewLifecycleOwner) { rooms ->
            myAdapter.submitList(rooms)
        }

        // 방 리스트 로드
        viewModel.loadCodingRooms()

    }

    companion object {
        fun newInstance() = TabPsFragment()
    }
}