package com.example.softwareproject // 실제 패키지 이름으로 변경

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.softwareproject.presentation.room.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MakeRoomActivity : AppCompatActivity() {
    private lateinit var makeButton: Button
    private lateinit var backButton: Button

    private val viewModel: RoomViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_room) // activity_battle_loading.xml 설정

        makeButton = findViewById(R.id.make_button)
        backButton = findViewById(R.id.back_button)
        val roomTitle : EditText = findViewById(R.id.edit_room_title)
        val spinner_types: Spinner = findViewById(R.id.spinner_types)
        ArrayAdapter.createFromResource(
            this,
            R.array.types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner_types.adapter = adapter
        }

        val spinner_tiers: Spinner = findViewById(R.id.spinner_tiers)
        ArrayAdapter.createFromResource(
            this,
            R.array.tier,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner_tiers.adapter = adapter
        }

        val spinner_counts: Spinner = findViewById(R.id.spinner_counts)
        ArrayAdapter.createFromResource(
            this,
            R.array.count,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner_counts.adapter = adapter
        }

        makeButton.setOnClickListener{
            // BattleLoadingActivity 시작
            val title = roomTitle.text.toString()
            val type = spinner_types.selectedItem.toString()     // 문제 유형
            val difficulty = spinner_tiers.selectedItem.toString()     // 난이도
            val problemCount = spinner_counts.selectedItem.toString()   // 문제 개수

            viewModel.makeRoom(
                title = title,
                type = type,
                difficulty =difficulty,
                problemCount =problemCount
            ).observe(this) { roomId ->
                val intent = Intent(this, BattleLoadingActivity::class.java)
                intent.putExtra("roomId", roomId)
                startActivity(intent)
            }
        }

        backButton.setOnClickListener{
            // 현재 액티비티 종료하여 이전 화면으로 돌아가기
            finish()
        }

//        val btnGoBack: Button = findViewById(R.id.btn_go_back)
//        btnGoBack.setOnClickListener {
//            // 현재 액티비티 종료하여 이전 화면으로 돌아가기
//            finish()
//        }

        // 여기에 실제 상대방을 기다리는 로직을 시작하거나,
        // ViewModel을 통해 상태를 업데이트하는 코드를 넣을 수 있습니다.
        // 예: 매칭 서버에 요청 보내기, 응답 대기 등
    }

    // 사용자가 시스템의 뒤로가기 버튼을 눌렀을 때도 finish()와 동일하게 동작
    override fun onBackPressed() {
        super.onBackPressed() // 기본 동작 (finish() 호출)
    }
}