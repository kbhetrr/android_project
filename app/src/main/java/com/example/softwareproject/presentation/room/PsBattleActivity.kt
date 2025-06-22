package com.example.softwareproject.com.example.softwareproject.presentation.room // 실제 패키지 이름으로 변경

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.softwareproject.com.example.softwareproject.model.DamageCalculator
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

    private var tiers: Float = 0.0f
    private var avgTries: Float = 0.0f
    private var solvers: Float = 0.0f


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battle_ps)

        if (!isUiReset) {
            resetUi()
            isUiReset = true
        }

        recyclerView = findViewById(R.id.problem_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        ProblemLinkButton = findViewById(R.id.problem_link)

        val roomId = intent.getStringExtra("roomId") ?: return

        psBattleViewModel.loadAbility(roomId)
        psBattleViewModel.observeParticipantHp(roomId)
        psBattleViewModel.loadProblemCount(roomId)
        psBattleViewModel.observeRoomState(roomId)

        val yourHpText = findViewById<TextView>(R.id.your_hp_text)
        val yourHpBar = findViewById<ProgressBar>(R.id.xp_progress_bar)
        val opponentHpText = findViewById<TextView>(R.id.opponent_hp_text)
        val opponentHpBar = findViewById<ProgressBar>(R.id.opponent_xp_progress_bar)

        psBattleViewModel.yourHpStatus.observe(this) { (currentHp, maxHp) ->
            yourHpText.text = "$currentHp / $maxHp"
            yourHpBar.max = maxHp
            yourHpBar.progress = currentHp
        }

        psBattleViewModel.opponentHpStatus.observe(this) { (currentHp, maxHp) ->
            opponentHpText.text = "$currentHp / $maxHp"
            opponentHpBar.max = maxHp
            opponentHpBar.progress = currentHp
        }

        var currentProblemCount = 0
        var currentSolvedSet: Set<Int> = emptySet()

        // 1️⃣ 문제 개수 observe
        psBattleViewModel.problemCount.observe(this) { count ->
            currentProblemCount = count
            updateProblemList(currentProblemCount, currentSolvedSet, roomId)
        }

        // 2️⃣ 해결된 문제 observe
        psBattleViewModel.solvedProblems.observe(this) { solvedSet ->
            currentSolvedSet = solvedSet
            updateProblemList(currentProblemCount, currentSolvedSet, roomId)
        }
        psBattleViewModel.hideProblemUi.observe(this) { shouldHide ->
            if (shouldHide) {
                findViewById<View>(R.id.problem_container).visibility = View.GONE
            }
        }

        // 3️⃣ 결과 observe
        psBattleViewModel.battleResult.observe(this) { result ->
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

        // 4️⃣ 현재 문제 observe
        psBattleViewModel.currentProblem.observe(this) { problem ->
            ProblemLinkButton.visibility = Button.VISIBLE
            findViewById<TextView>(R.id.problem_title).text =
                "문제 ${problem?.problemIndex} - ${problem?.title}"
            findViewById<TextView>(R.id.problem_description).text = problem?.title
            findViewById<TextView>(R.id.problem_baekjoon_id).text =
                "백준 ${problem?.problemId}번"
            findViewById<TextView>(R.id.user_count).text =
                "푼 유저 수: ${problem?.acceptedUserCount}"
            findViewById<TextView>(R.id.try_chance).text =
                "평균 시도 횟수: ${problem?.averageTries}"

            solvers = problem?.acceptedUserCount?.toFloat() ?: 0f
            avgTries = problem?.averageTries?.toFloat() ?: 0f

            ProblemLinkButton.setOnClickListener {
                val problemUrl = "https://www.acmicpc.net/problem/${problem?.problemId}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(problemUrl))
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "웹 링크를 열 수 있는 앱이 없습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
        psBattleViewModel.toastData.observe(this) { result ->
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }



        // 홈 버튼
        findViewById<Button>(R.id.home_btn).setOnClickListener {
            lifecycleScope.launch {
                psBattleViewModel.giveUp(roomId)
                finish()
            }
        }

        // 공격 버튼
        findViewById<Button>(R.id.attack_btn).setOnClickListener {
            val damage = DamageCalculator(this).use { calculator ->
                calculator.predictDamage(5f, solvers, avgTries)
            }
            lifecycleScope.launch {
                psBattleViewModel.attackOpponent(roomId, damage.toInt() + 1)
            }
        }
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
    private fun updateProblemList(count: Int, solvedSet: Set<Int>, roomId: String) {
        val itemList = List(count) { it + 1 }
            .filter { it !in solvedSet }
            .map { SelectableItem("id_$it", "문제 $it") }

        radioAdapter = RadioSelectionAdapter(itemList) { selectedItem ->
            val selectedIndex = selectedItem.id.removePrefix("id_").toInt()
            findViewById<View>(R.id.problem_container).visibility = View.VISIBLE
            psBattleViewModel.loadProblem(roomId, selectedIndex)
        }

        recyclerView.adapter = radioAdapter
    }

}