package com.example.softwareproject.com.example.softwareproject.presentation.room // 실제 패키지 이름으로 변경

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.softwareproject.ResultDefeatActivity
import com.example.softwareproject.SelectableItem
import com.example.softwareproject.presentation.room.viewmodel.CsBattleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CsBattleActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var radioAdapter: RadioSelectionAdapter
    private val csBattleViewModels: CsBattleViewModel by viewModels()
    private var isUiReset = false
    private var currentCount = 0
    private var currentSolvedSet: Set<Int> = emptySet()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_cs)

        if (!isUiReset) {
            resetUi()
            isUiReset = true
        }

        recyclerView = findViewById(R.id.problem_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val roomId = intent.getStringExtra("roomId") ?: return

        csBattleViewModels.loadAbility(roomId)
        csBattleViewModels.observeParticipants(roomId)
        csBattleViewModels.loadProblemCount(roomId)
        csBattleViewModels.observeRoomState(roomId)

        val yourHpText = findViewById<TextView>(R.id.your_hp_text)
        val yourHpBar = findViewById<ProgressBar>(R.id.xp_progress_bar)
        val opponentHpText = findViewById<TextView>(R.id.opponent_hp_text)
        val opponentHpBar = findViewById<ProgressBar>(R.id.opponent_xp_progress_bar)

        csBattleViewModels.yourHpStatus.observe(this) { (currentHp, maxHp) ->
            yourHpText.text = "$currentHp / $maxHp"
            yourHpBar.max = maxHp
            yourHpBar.progress = currentHp
        }

        csBattleViewModels.opponentHpStatus.observe(this) { (currentHp, maxHp) ->
            opponentHpText.text = "$currentHp / $maxHp"
            opponentHpBar.max = maxHp
            opponentHpBar.progress = currentHp
        }

        // 문제 개수
        csBattleViewModels.problemCount.observe(this) { count ->
            currentCount = count
            updateProblemList(roomId)
        }

        csBattleViewModels.hideProblemUi.observe(this) { shouldHide ->
            if (shouldHide) {
                findViewById<View>(R.id.problem_container).visibility = View.GONE
            }
        }

        // 해결한 문제
        csBattleViewModels.solvedProblems.observe(this) { solved ->
            currentSolvedSet = solved
            updateProblemList(roomId)
        }

        // 전투 결과
        csBattleViewModels.battleResult.observe(this) { result ->
            result?.let {
                val intent = when (it) {
                    "WIN" -> Intent(this, ResultActivity::class.java)
                    "LOSE" -> Intent(this, ResultDefeatActivity::class.java)
                    else -> return@let
                }.apply {
                    putExtra("result", it)
                }
                startActivity(intent)
                finish()
            }
        }

        // 문제 UI 갱신
        csBattleViewModels.currentProblem.observe(this) { problem ->
            findViewById<TextView>(R.id.problem_title).text = "문제 ${problem?.problemIndex}"
            findViewById<TextView>(R.id.problem_description).text = problem?.question

            val options = listOf(problem?.choice1, problem?.choice2, problem?.choice3, problem?.choice4)
            val radioButtons = listOf(
                findViewById<RadioButton>(R.id.radio_button_option1),
                findViewById<RadioButton>(R.id.radio_button_option2),
                findViewById<RadioButton>(R.id.radio_button_option3),
                findViewById<RadioButton>(R.id.radio_button_option4)
            )

            radioButtons.forEachIndexed { index, btn ->
                btn.text = options[index]
                btn.setOnClickListener {
                    csBattleViewModels.selectAnswer(index + 1)
                }
            }
        }

        // 홈 버튼
        findViewById<Button>(R.id.home_btn).setOnClickListener {
            lifecycleScope.launch {
                csBattleViewModels.giveUp(roomId)
                finish()
            }
        }

        // 공격 버튼
        findViewById<Button>(R.id.attack_btn).setOnClickListener {
            lifecycleScope.launch {
                csBattleViewModels.attackOpponent(roomId)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed() // 기본 동작 (finish() 호출)
    }

    private fun updateProblemList(roomId: String) {
        val itemList = List(currentCount) { it + 1 }
            .filter { it !in currentSolvedSet }
            .map { SelectableItem("id_$it", "문제 $it") }

        radioAdapter = RadioSelectionAdapter(itemList) { selectedItem ->
            val selectedIndex = selectedItem.id.removePrefix("id_").toInt()
            findViewById<View>(R.id.problem_container).visibility = View.VISIBLE
            csBattleViewModels.loadProblem(roomId, selectedIndex)
        }

        recyclerView.adapter = radioAdapter
    }
    private fun resetUi() {
        findViewById<TextView>(R.id.problem_title).text = ""
        findViewById<TextView>(R.id.problem_description).text = ""

        val radioButtons = listOf(
            findViewById<RadioButton>(R.id.radio_button_option1),
            findViewById<RadioButton>(R.id.radio_button_option2),
            findViewById<RadioButton>(R.id.radio_button_option3),
            findViewById<RadioButton>(R.id.radio_button_option4)
        )

        radioButtons.forEach { btn ->
            btn.text = ""
            btn.isChecked = false
//            btn.isEnabled = false
        }

        findViewById<ProgressBar>(R.id.xp_progress_bar).progress = 0
        findViewById<ProgressBar>(R.id.opponent_xp_progress_bar).progress = 0
    }
}