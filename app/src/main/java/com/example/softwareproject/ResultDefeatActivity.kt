package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity // AppCompatActivity 상속
import androidx.lifecycle.lifecycleScope
import com.example.softwareproject.com.example.softwareproject.presentation.room.viewmodel.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ResultDefeatActivity : AppCompatActivity() {

//    private val viewModel: RoomViewModel by viewModels()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_defeat) // activity_battle_loading.xml 설정

//        val btnGoBack: Button = findViewById(R.id.homeButton)
//        btnGoBack.setOnClickListener {
//            finish()
//        }
//        val roomId = intent.getStringExtra("roomId") ?: return
//
//        btnGoBack.setOnClickListener {
//            lifecycleScope.launch {
//                viewModel.deleteRoom(roomId)
//                Log.d("RoomViewModel", "방 삭제 완료: $roomId")
//                finish()
//            }
//        }
        val btnHome: Button = findViewById(R.id.homeButton)
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료 (선택사항)
        }
    }

    // 사용자가 시스템의 뒤로가기 버튼을 눌렀을 때도 finish()와 동일하게 동작
    override fun onBackPressed() {
        super.onBackPressed() // 기본 동작 (finish() 호출)
    }
}