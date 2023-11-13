package com.example.hungryguys

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hungryguys.ui.tutorial.TutorialActivity
import com.example.hungryguys.utills.ConstantList.Companion.DEFAULT_PRF

// 앱 시작시 가장 처음에 실행되는 클래스
// 최초 실행 여부로 튜토리얼 -> 로그인, 메인으로 이동 담당, 권한설정도 여기서
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val localprf = getSharedPreferences(DEFAULT_PRF, Activity.MODE_PRIVATE)
        val isFirst = localprf.getBoolean("isfirst", true)

        if (isFirst) {
            startActivity(Intent(applicationContext, TutorialActivity::class.java))
            val editprf = localprf.edit()
            editprf.putBoolean("isfirst", false)
            editprf.apply()
            finish()
        } else {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }
}