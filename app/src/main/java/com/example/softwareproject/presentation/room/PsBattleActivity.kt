package com.example.softwareproject.com.example.softwareproject.presentation.room // 실제 패키지 이름으로 변경

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity // AppCompatActivity 상속
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.softwareproject.R
import com.example.softwareproject.RadioSelectionAdapter
import com.example.softwareproject.SelectableItem

class PsBattleActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var radioAdapter: RadioSelectionAdapter
    private lateinit var ProblemLinkButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_ps) // activity_battle_loading.xml 설정

        recyclerView = findViewById(R.id.problem_view) // RecyclerView ID
        recyclerView.layoutManager = LinearLayoutManager(this)
        val itemList = listOf(
            SelectableItem("id_1", "옵션 1"),
            SelectableItem("id_2", "옵션 2"),
            SelectableItem("id_3", "옵션 3"),
            SelectableItem("id_4", "옵션 4")
        )

        // 초기 선택할 아이템의 ID (예: "id_2"를 초기에 선택)
        val initialSelectedItem = "id_1"

        // 어댑터 생성 시 초기 선택 ID 전달
        radioAdapter = RadioSelectionAdapter(itemList, initialSelectedItem) { selectedItem ->
            // 아이템 선택 시 처리할 로직
            //Toast.makeText(this, "${selectedItem.name} 선택됨", Toast.LENGTH_SHORT).show()
            // 예: ViewModel에 선택된 값 저장
        }
        recyclerView.adapter = radioAdapter

        val homeBtn: Button = findViewById(R.id.home_btn)
        homeBtn.setOnClickListener {
            // 현재 액티비티 종료하여 이전 화면으로 돌아가기
            finish()
        }

        ProblemLinkButton = findViewById(R.id.problem_link)
        ProblemLinkButton.setOnClickListener{
            val problemUrl = "https://www.acmicpc.net/problem/1111" // 여기에 실제 문제 링크 URL을 넣으세요.
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(problemUrl)

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "웹 링크를 열 수 있는 앱이 없습니다.", Toast.LENGTH_LONG).show()
            }
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