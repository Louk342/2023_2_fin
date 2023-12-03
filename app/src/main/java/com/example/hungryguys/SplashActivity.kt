package com.example.hungryguys

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.hungryguys.ui.settings.SettingsList
import com.example.hungryguys.ui.tutorial.TutorialActivity
import com.example.hungryguys.utills.ActivityUtills

// 앱 시작시 가장 처음에 실행되는 클래스
// 최초 실행 여부로 튜토리얼 -> 로그인, 메인으로 이동 담당, 권한설정도 여기서
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    @SuppressLint("ServiceCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val activityUtills = ActivityUtills(this)
        val localprf = PreferenceManager.getDefaultSharedPreferences(this)

        // 설정을 안건드린 상태에서 사용자 테마가 다크모드인 경우 다크모드 옵션이 체크 안되는 문제 해결
        val isNight = applicationContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        if (isNight && !localprf.contains(SettingsList.darkmode.name)) {
            val editprf = localprf.edit()
            editprf.putBoolean(SettingsList.darkmode.name, true)
            editprf.apply()
        }

        // 설정 값 불러와서 처리하기
        val isdark = localprf.getBoolean(SettingsList.darkmode.name, false)
        val notification = localprf.getBoolean(SettingsList.notification.name, false)
        if (isdark || isNight) activityUtills.setDarkmode(isdark)
        if (notification) activityUtills.setNotification(notification)

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