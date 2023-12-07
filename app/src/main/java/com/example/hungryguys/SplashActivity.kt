package com.example.hungryguys

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.example.hungryguys.ui.auth.AuthActivity
import com.example.hungryguys.ui.settings.SettingsList
import com.example.hungryguys.ui.tutorial.TutorialActivity
import com.example.hungryguys.utills.ActivityUtills
import com.example.hungryguys.utills.GoogleLoginData

// 앱 시작시 가장 처음에 실행되는 클래스
// 최초 실행 여부로 튜토리얼 -> 로그인, 메인으로 이동 담당, 권한설정도 여기서
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val PERMISSIONS_REQUEST = 1 // 권한 요청 레벨
    private lateinit var localprf: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val activityUtills = ActivityUtills(this)
        localprf = PreferenceManager.getDefaultSharedPreferences(this)

        // 설정을 안건드린 상태에서 사용자 테마가 다크모드인 경우 다크모드 옵션이 체크 안되는 문제 해결
        val isNight =
            applicationContext.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
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

        // 권한체크
        val permissionlist = mutableListOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (!checkpermission(this, permissionlist)) {
            requestPermissions(permissionlist.toTypedArray(), PERMISSIONS_REQUEST)
        } else {
            chageActivity()
        }
    }

    // 권한 체크 이후 엑티비티 이동
    private fun chageActivity() {
        val isFirst = localprf.getBoolean("isfirst", true)

        if (isFirst) {
            startActivity(Intent(applicationContext, TutorialActivity::class.java))
            finish()
        } else {
            if (GoogleLoginData.checkAuth()) {
                // 로그인 상태일시
                startActivity(Intent(applicationContext, MainActivity::class.java))
                Log.d("로그", "${GoogleLoginData.email}")
                Log.d("로그", "${GoogleLoginData.name}")
                finish()
            } else {
                // 비 로그인 상태일시
                startActivity(Intent(applicationContext, AuthActivity::class.java))
                finish()
            }
        }
    }

    // 권한확인을 안했을 경우 요청을 처리하는 함수
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            chageActivity()
        } else {
            // 하나라도 수락안하면 다시 할수 있게
            requestPermissions(permissions, PERMISSIONS_REQUEST)
            Toast.makeText(this, "권한을 수락해야 합니다!", Toast.LENGTH_SHORT).show()
        }
    }

    // 권한 확인 하는 함수
    private fun checkpermission(context: Context, list: MutableList<String>): Boolean {
        list.forEach {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }
}