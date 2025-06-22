package com.example.softwareproject.com.example.softwareproject.presentation.room // 실제 패키지 이름으로 변경

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity // AppCompatActivity 상속
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.softwareproject.R
import com.example.softwareproject.RadioSelectionAdapter
import com.example.softwareproject.ResultActivity
import com.example.softwareproject.ResultDefeatActivity
import com.example.softwareproject.SelectableItem
import com.example.softwareproject.com.example.softwareproject.presentation.room.viewmodel.PsBattleViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PsBattleActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var radioAdapter: RadioSelectionAdapter
    private lateinit var ProblemLinkButton: Button
    private val psBattleViewModel: PsBattleViewModel by viewModels()
    private var isUiReset = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_ps) // activity_battle_loading.xml 설정


        if (!isUiReset) {
            resetUi()
            isUiReset = true
        }
        recyclerView = findViewById(R.id.problem_view) // RecyclerView ID
        recyclerView.layoutManager = LinearLayoutManager(this)

        ProblemLinkButton = findViewById(R.id.problem_link)

        val roomId = intent.getStringExtra("roomId") ?: return

        psBattleViewModel.loadAbility(roomId)
        psBattleViewModel.observeParticipantHp(roomId)

        val yourHpText = findViewById<TextView>(R.id.your_hp_text)
        val yourHpBar = findViewById<ProgressBar>(R.id.xp_progress_bar)

        val opponentHpText = findViewById<TextView>(R.id.opponent_hp_text)
        val opponentHpBar = findViewById<ProgressBar>(R.id.opponent_xp_progress_bar)


        psBattleViewModel.yourHpStatus.observe(this) { (currentHp, maxHp) ->
            Log.d("TabCsFragment", "최대 체력: $maxHp")
            yourHpText.text = "$currentHp / $maxHp"
            yourHpBar.max = maxHp
            yourHpBar.progress = currentHp
        }

        psBattleViewModel.opponentHpStatus.observe(this) { (currentHp, maxHp) ->
            Log.d("TabCsFragment", "상대 최대 체력: $maxHp")
            opponentHpText.text = "$currentHp / $maxHp"
            opponentHpBar.max = maxHp
            opponentHpBar.progress = currentHp
        }

        psBattleViewModel.loadProblemCount(roomId)
        psBattleViewModel.observeRoomState(roomId)

        psBattleViewModel.problemCount.observe(this) { count ->
            val itemList = List(count) { index ->
                SelectableItem("id_${index + 1}", "문제 ${index + 1}")
            }

            psBattleViewModel.battleResult.observe(this) { result ->
                result?.let {
                    val intent = when (result) {
                        "WIN" -> Intent(this, ResultActivity::class.java)
                        "LOSE" -> Intent(this, ResultDefeatActivity::class.java)
                        else -> return@let  // 다른 값은 무시
                    }.apply {
                        putExtra("result", result)
                    }

                    startActivity(intent)
                    finish()
                }
            }

            radioAdapter = RadioSelectionAdapter(itemList) { selectedItem ->
                val selectedIndex = selectedItem.id.removePrefix("id_").toInt()
                psBattleViewModel.loadProblem(roomId, selectedIndex)
            }

            recyclerView.adapter = radioAdapter

            // 🔘 문제 정보 Observe해서 문제 UI 갱신
            psBattleViewModel.currentProblem.observe(this) { problem ->
                ProblemLinkButton.visibility = Button.VISIBLE
                findViewById<TextView>(R.id.problem_title).text = "문제 ${problem?.problemIndex} - ${problem?.title}"
                findViewById<TextView>(R.id.problem_description).text = problem?.title
                findViewById<TextView>(R.id.problem_baekjoon_id).text = "백준 ${problem?.problemId}번"
                findViewById<TextView>(R.id.user_count).text = "푼 유저 수: ${problem?.acceptedUserCount}"
                findViewById<TextView>(R.id.try_chance).text = "평균 시도 횟수: ${problem?.averageTries}"

                ProblemLinkButton.setOnClickListener{
                    val problemUrl = "https://www.acmicpc.net/problem/${problem?.problemId}" // 여기에 실제 문제 링크 URL을 넣으세요.
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(problemUrl)

                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "웹 링크를 열 수 있는 앱이 없습니다.", Toast.LENGTH_LONG).show()
                    }
                }
            }


            val homeBtn: Button = findViewById(R.id.home_btn)
            homeBtn.setOnClickListener {
                lifecycleScope.launch {
                    psBattleViewModel.giveUp(roomId)  // 비동기 작업 다 끝날 때까지 기다림
                    finish() // 그 다음 종료
                }
            }
            val attackBtn: Button = findViewById(R.id.attack_btn)
            attackBtn.setOnClickListener {
                lifecycleScope.launch {
                    psBattleViewModel.attackOpponent(roomId)
                }
            }

        }
        // 사용자가 시스템의 뒤로가기 버튼을 눌렀을 때도 finish()와 동일하게 동작
    }

    override fun onBackPressed() {
        super.onBackPressed() // 기본 동작 (finish() 호출)
    }
    private fun resetUi() {
        // 문제 정보 초기화
        findViewById<TextView>(R.id.problem_title).text = ""
        findViewById<TextView>(R.id.problem_description).text = ""
        findViewById<TextView>(R.id.problem_baekjoon_id).text = ""
        findViewById<TextView>(R.id.user_count).text = ""
        findViewById<TextView>(R.id.try_chance).text = ""

        // 링크 버튼 초기 숨김
        findViewById<Button>(R.id.problem_link).visibility = Button.INVISIBLE

        // 체력 초기화
        findViewById<TextView>(R.id.your_hp_text).text = "0 / 0"
        findViewById<TextView>(R.id.opponent_hp_text).text = "0 / 0"
        findViewById<ProgressBar>(R.id.xp_progress_bar).progress = 0
        findViewById<ProgressBar>(R.id.opponent_xp_progress_bar).progress = 0
    }

}