package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.softwareproject.presentation.room.RoomViewModel
import com.example.softwareproject.presentation.room.adapter.PsRoomAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabPsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter:  PsRoomAdapter // 어댑터 타입
    private lateinit var fabPs: FloatingActionButton

    private var isFirstLoaded = false
    private val viewModel: RoomViewModel by viewModels()//임준식 추가


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tab_ps, container, false) // fragment_tab_one.xml 사용
        recyclerView = view.findViewById(R.id.recycler_view_tab_two)
        fabPs = view.findViewById(R.id.fab_cs)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(context)
        myAdapter = PsRoomAdapter(emptyList())
        recyclerView.adapter = myAdapter



        //임준식 추가
        viewModel.psRooms.observe(viewLifecycleOwner) { rooms ->
            Log.d("TabPsFragment", "받은 방 개수: ${rooms.size}")
            myAdapter.submitList(rooms)
        }

        // 방 리스트 로드

        if (!isFirstLoaded) {
            viewModel.loadPsRooms()
            viewModel.observePsRooms()
            isFirstLoaded = true
        }
        fabPs.setOnClickListener {
            // BattleLoadingActivity 시작
            val intent = Intent(activity, MakeRoomActivity::class.java)
            // 필요하다면 intent에 데이터 추가 가능
            // intent.putExtra("KEY_BATTLE_ID", battleId)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeCsRoomListener()
        isFirstLoaded = false
    }

    companion object {
        fun newInstance() = TabPsFragment()
    }
}