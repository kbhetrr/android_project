package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity // AppCompatActivity 상속
import androidx.lifecycle.lifecycleScope
import com.example.softwareproject.com.example.softwareproject.presentation.room.CsBattleActivity
import com.example.softwareproject.com.example.softwareproject.presentation.room.PsBattleActivity
import com.example.softwareproject.com.example.softwareproject.presentation.room.viewmodel.RoomViewModel
import com.example.softwareproject.util.RoomType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BattleLoadingActivity : AppCompatActivity() {

    private val viewModel: RoomViewModel by viewModels()
    private lateinit var roomListener: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_loading) // activity_battle_loading.xml 설정

        val btnGoBack: Button = findViewById(R.id.btn_go_back)
        val roomId = intent.getStringExtra("roomId") ?: return

        btnGoBack.setOnClickListener {
            lifecycleScope.launch {
                viewModel.deleteRoom(roomId)
                Log.d("RoomViewModel", "방 삭제 완료: $roomId")
                finish()
            }
        }

        // 여기에 실제 상대방을 기다리는 로직을 시작하거나,
        // ViewModel을 통해 상태를 업데이트하는 코드를 넣을 수 있습니다.
        // 예: 매칭 서버에 요청 보내기, 응답 대기 등
        val roomRef = FirebaseFirestore.getInstance()
            .collection("room")
            .document(roomId)

        roomListener = roomRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("BattleLoading", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val roomState = snapshot.getString("roomState")
                val roomType = snapshot.getString("roomType") ?: "CS" // 기본값

                Log.d("BattleLoading", "방 상태 감지: $roomState")

                if (roomState == "PROGRESS") {
                    // 리스너 제거
                    roomListener.remove()

                    val intent = when (RoomType.valueOf(roomType)) {
                        RoomType.CS -> Intent(this, CsBattleActivity::class.java)
                        RoomType.PS -> Intent(this, PsBattleActivity::class.java)
                    }
                    intent.putExtra("roomId", roomId)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    // 사용자가 시스템의 뒤로가기 버튼을 눌렀을 때도 finish()와 동일하게 동작
    override fun onBackPressed() {
        super.onBackPressed() // 기본 동작 (finish() 호출)
    }
}