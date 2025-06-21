package com.example.softwareproject.com.example.softwareproject.presentation.room // 실제 패키지 이름으로 변경

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.softwareproject.R
import com.example.softwareproject.RadioSelectionAdapter
import com.example.softwareproject.ResultActivity
import com.example.softwareproject.SelectableItem
import com.example.softwareproject.presentation.room.viewmodel.CsBattleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CsBattleActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var radioAdapter: RadioSelectionAdapter
    private val csBattleViewModels: CsBattleViewModel by viewModels()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_cs) // activity_battle_loading.xml 설정


        recyclerView = findViewById(R.id.problem_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val roomId = intent.getStringExtra("roomId") ?: return

        csBattleViewModels.loadAbility(roomId)
        csBattleViewModels.observeParticipants(roomId)

        val yourHpText = findViewById<TextView>(R.id.your_hp_text)
        val yourHpBar = findViewById<ProgressBar>(R.id.xp_progress_bar)

        val opponentHpText = findViewById<TextView>(R.id.opponent_hp_text)
        val opponentHpBar = findViewById<ProgressBar>(R.id.opponent_xp_progress_bar)


        csBattleViewModels.yourHpStatus.observe(this) { (currentHp, maxHp) ->
            Log.d("TabCsFragment", "최대 체력: $maxHp")
            yourHpText.text = "$currentHp / $maxHp"
            yourHpBar.max = maxHp
            yourHpBar.progress = currentHp
        }

        csBattleViewModels.opponentHpStatus.observe(this) { (currentHp, maxHp) ->
            Log.d("TabCsFragment", "상대 최대 체력: $maxHp")
            opponentHpText.text = "$currentHp / $maxHp"
            opponentHpBar.max = maxHp
            opponentHpBar.progress = currentHp
        }

        csBattleViewModels.loadProblemCount(roomId)

        csBattleViewModels.problemCount.observe(this) { count ->
            val itemList = List(count) { index ->
                SelectableItem("id_${index + 1}", "문제 ${index + 1}")
            }

            csBattleViewModels.battleResult.observe(this) { result ->
                result?.let {
                    val intent = Intent(this, ResultActivity::class.java).apply {
                        putExtra("result", result) // "WIN" 또는 "LOSE"
                    }
                    startActivity(intent)
                    finish()
                }
            }
            radioAdapter = RadioSelectionAdapter(itemList) { selectedItem ->
                val selectedIndex = selectedItem.id.removePrefix("id_").toInt()
                csBattleViewModels.loadProblem(roomId, selectedIndex)
            }

            recyclerView.adapter = radioAdapter

            // 🔘 문제 정보 Observe해서 문제 UI 갱신
            csBattleViewModels.currentProblem.observe(this) { problem ->
                findViewById<TextView>(R.id.problem_title).text = "문제 ${problem?.problemIndex}"
                findViewById<TextView>(R.id.problem_description).text = problem?.question

                val options = listOf(
                    problem?.choice1,
                    problem?.choice2,
                    problem?.choice3,
                    problem?.choice4
                )

                listOf(
                    findViewById<RadioButton>(R.id.radio_button_option1),
                    findViewById<RadioButton>(R.id.radio_button_option2),
                    findViewById<RadioButton>(R.id.radio_button_option3),
                    findViewById<RadioButton>(R.id.radio_button_option4)
                ).forEachIndexed { i, btn ->
                    btn.text = options[i]
                }
                val radioButtons = listOf(
                    findViewById<RadioButton>(R.id.radio_button_option1),
                    findViewById<RadioButton>(R.id.radio_button_option2),
                    findViewById<RadioButton>(R.id.radio_button_option3),
                    findViewById<RadioButton>(R.id.radio_button_option4)
                )

                radioButtons.forEachIndexed { index, btn ->
                    btn.setOnClickListener {
                        csBattleViewModels.selectAnswer(index + 1) // 1~4번 보기 선택
                    }
                }
            }

            val homeBtn: Button = findViewById(R.id.home_btn)
            homeBtn.setOnClickListener {
                lifecycleScope.launch {
                    csBattleViewModels.giveUp(roomId)
                    finish()
                }
            }
            val attackBtn: Button = findViewById(R.id.attack_btn)
            attackBtn.setOnClickListener {
                lifecycleScope.launch {
                    csBattleViewModels.attackOpponent(roomId)
                }
            }

            // TODO: 상대방 대기, 상태 갱신 로직 여기에 추가 가능
        }
        // 사용자가 시스템의 뒤로가기 버튼을 눌렀을 때도 finish()와 동일하게 동작
    }
    override fun onBackPressed() {
        super.onBackPressed() // 기본 동작 (finish() 호출)
    }
}