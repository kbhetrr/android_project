package com.example.softwareproject.com.example.softwareproject.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.softwareproject.CsBattleActivity
import com.example.softwareproject.PsBattleActivity
import com.example.softwareproject.R
import com.example.softwareproject.com.example.softwareproject.domain.usecase.room.BattleUseCase
import com.example.softwareproject.com.example.softwareproject.presentation.room.BattleViewModel
import com.example.softwareproject.presentation.room.RoomViewModel
import com.example.softwareproject.util.RoomType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BattleWaitingActivity : AppCompatActivity(){
    private val roomViewModel: RoomViewModel by viewModels()
    private val battleViewModel : BattleViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_waiting)

        val roomId = intent.getStringExtra("roomId") ?: return


        lifecycleScope.launch {
            try {

                roomViewModel.battleStart(roomId)

                battleViewModel.createProblem(roomId)

                battleViewModel.createRoomParticipant(roomId)

                b
                val roomType = battleViewModel.getRoomType(roomId)

                val intent = when (roomType) {
                    RoomType.CS -> Intent(this@BattleWaitingActivity, CsBattleActivity::class.java)
                    RoomType.PS -> Intent(this@BattleWaitingActivity, PsBattleActivity::class.java)
                }
                intent.putExtra("roomId", roomId)
                startActivity(intent)
                finish()

            } catch (e: Exception) {
                Log.e("BattleWaiting", "문제 생성 실패: ${e.message}")
                Toast.makeText(this@BattleWaitingActivity, "문제 생성 실패", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }


    override fun onBackPressed() {
        super.onBackPressed() // 기본 동작 (finish() 호출)
    }
}