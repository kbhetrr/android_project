package com.example.softwareproject

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.softwareproject.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
//이걸 붙여야 Fragment에서 @HiltViewModel, ViewModele을 사용할 수 있다.@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // 스플래시 레이아웃

        Handler(Looper.getMainLooper()).postDelayed({
            // 로그인 액티비티로 화면 전환
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // 현재 스플래시 액티비티 종료
        }, 1500)
    }
}
