package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity // AppCompatActivity 상속

class BattleLoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_loading) // activity_battle_loading.xml 설정

        val btnGoBack: Button = findViewById(R.id.btn_go_back)
        btnGoBack.setOnClickListener {
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