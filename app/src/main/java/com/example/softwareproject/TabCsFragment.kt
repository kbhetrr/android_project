package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent // Intent import
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TabCsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: RoomRecyclerAdapter // 어댑터 타입
    private lateinit var fabPs: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab_cs, container, false) // fragment_tab_one.xml 사용
        recyclerView = view.findViewById(R.id.recycler_view_tab_one)
        fabPs = view.findViewById(R.id.fab_cs)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 더미 데이터 생성 (실제로는 ViewModel 등에서 가져옴)
        val dummyItems = List(20) { MyItem("탭 1 아이템 ${it + 1}", "설명 ${it + 1}") }
        myAdapter = RoomRecyclerAdapter(dummyItems)
        recyclerView.adapter = myAdapter

        fabPs.setOnClickListener {
            // BattleLoadingActivity 시작
            val intent = Intent(activity, BattleLoadingActivity::class.java)
            // 필요하다면 intent에 데이터 추가 가능
            // intent.putExtra("KEY_BATTLE_ID", battleId)
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance() = TabCsFragment()
    }
}