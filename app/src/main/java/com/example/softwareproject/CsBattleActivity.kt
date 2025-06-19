package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity // AppCompatActivity 상속
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.softwareproject.com.example.softwareproject.SelectableItem

class CsBattleActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var radioAdapter: RadioSelectionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_cs) // activity_battle_loading.xml 설정

        recyclerView = findViewById(R.id.problem_view) // RecyclerView ID
        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemList = listOf(
            SelectableItem("id_1", "옵션 1"),
            SelectableItem("id_2", "옵션 2"),
            SelectableItem("id_3", "옵션 3"),
            SelectableItem("id_4", "옵션 4")
        )

        radioAdapter = RadioSelectionAdapter(itemList) { selectedItem ->
            // 아이템 선택 시 처리할 로직
            // Toast.makeText(this, "${selectedItem.name} 선택됨", Toast.LENGTH_SHORT).show()
            // 예: ViewModel에 선택된 값 저장
        }
        recyclerView.adapter = radioAdapter

        val homeBtn: Button = findViewById(R.id.home_btn)
        homeBtn.setOnClickListener {
            // 현재 액티비티 종료하여 이전 화면으로 돌아가기
            finish()
        }

        // 여기에 실제 상대방을 기다리는 로직을 시작하거나,
        // ViewModel을 통해 상태를 업데이트하는 코드를 넣을 수 있습니다.
        // 예: 매칭 서버에 요청 보내기, 응답 대기 등
    }

    // 사용자가 시스템의 뒤로가기 버튼을 눌렀을 때도 finish()와 동일하게 동작
    override fun onBackPressed() {
        super.onBackPressed() // 기본 동작 (finish() 호출)
    }
}