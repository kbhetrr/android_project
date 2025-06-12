package com.example.softwareproject.presentation.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.softwareproject.R
import com.example.softwareproject.com.example.softwareproject.presentation.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // LoginFragment 삽입
        supportFragmentManager.beginTransaction()
            .replace(R.id.login_fragment_container, LoginFragment())
            .commit()
    }
}
//수정된 로직
