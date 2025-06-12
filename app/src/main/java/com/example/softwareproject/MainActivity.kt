package com.example.softwareproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

import com.example.softwareproject.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
// import androidx.navigation.ui.AppBarConfiguration // ActionBar를 사용하지 않으므로 주석 처리 또는 삭제
// import androidx.navigation.ui.setupActionBarWithNavController // ActionBar를 사용하지 않으므로 주석 처리 또는 삭제
import androidx.navigation.ui.setupWithNavController
import com.example.softwareproject.util.UserPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView

// import com.example.softwareproject.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main) // res/layout/activity_main.xml을 사용

        // id 설정
        UserPreferences.saveGithubId(this, "kbhetrr")
        UserPreferences.saveSolvedAcHandle(this, "kimkh7534")

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        // BottomNavigationView 설정
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)
//        Handler(Looper.getMainLooper()).postDelayed({
//            // 로그인 액티비티로 화면 전환
//            startActivity(Intent(this, LoginActivity::class.java))
//            finish() // 현재 스플래시 액티비티 종료
//        }, 1500)
    }
    override fun onSupportNavigateUp(): Boolean {
        return super.onSupportNavigateUp() // 가장 기본적인 Up 동작
    }
}

