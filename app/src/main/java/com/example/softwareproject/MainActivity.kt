package com.example.softwareproject // 자신의 패키지 이름으로 변경하세요.

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
// import androidx.navigation.ui.AppBarConfiguration // ActionBar를 사용하지 않으므로 주석 처리 또는 삭제
// import androidx.navigation.ui.setupActionBarWithNavController // ActionBar를 사용하지 않으므로 주석 처리 또는 삭제
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

// import com.example.softwareproject.R

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // res/layout/activity_main.xml을 사용

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        navController = navHostFragment.navController

        // BottomNavigationView 설정
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)

        // ActionBar 관련 코드 주석 처리 또는 삭제
        /*
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_a, R.id.navigation_b, R.id.navigation_c // 최상위 목적지 ID들
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        */
    }

    // ActionBar를 사용하지 않으므로, 이 메소드는 기본 동작만 하도록 하거나,
    // navController.navigateUp() 부분을 제거합니다.
    override fun onSupportNavigateUp(): Boolean {
        // return navController.navigateUp() || super.onSupportNavigateUp() // ActionBar와 연동 시
        // 만약 NavController의 Up 버튼 동작이 여전히 필요하다면 (예: 다른 Toolbar 사용 시)
        // 이 부분을 유지할 수도 있지만, 현재 ActionBar가 없으므로 super.onSupportNavigateUp()만 호출해도 무방합니다.
        // 또는, NavController가 Up navigation을 처리하도록 하려면 다음과 같이 할 수 있습니다.
        // return navController.navigateUp()
        return super.onSupportNavigateUp() // 가장 기본적인 Up 동작
    }
}
