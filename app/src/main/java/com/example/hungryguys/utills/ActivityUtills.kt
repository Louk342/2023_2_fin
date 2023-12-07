package com.example.hungryguys.utills

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt

// Activity와 종속적인 것들
class ActivityUtills(private val activity: Activity) {

    // 상태바 투명하게 하는 함수
    fun setStatusBarTransparent() {
        activity.apply {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            // API 30 에 적용
            if (Build.VERSION.SDK_INT >= 30) {
                WindowCompat.setDecorFitsSystemWindows(window, false)
            }
        }
    }

    // 상태바 투명화 Padding 보정 함수
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun setStatusBarAllPadding(viewGroup: ViewGroup) {
        activity.apply {
            val statusbarId = resources.getIdentifier("status_bar_height", "dimen", "android")
            val statusBarHeight = resources.getDimensionPixelSize(statusbarId)

            val navigationbarId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            val navigationHeight = resources.getDimensionPixelSize(navigationbarId)

            viewGroup.setPadding(0, statusBarHeight, 0, navigationHeight)
        }
    }

    // 상태바 투명화 Padding 하단 보정 함수
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun setStatusBarBottomPadding(viewGroup: ViewGroup) {
        activity.apply {
            val navigationbarId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            val navigationHeight = resources.getDimensionPixelSize(navigationbarId)

            viewGroup.setPadding(
                viewGroup.paddingLeft,
                viewGroup.paddingTop,
                viewGroup.right,
                navigationHeight + viewGroup.paddingBottom
            )
        }
    }

    // 상태바 투명화 Padding 상단 보정 함수
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    fun setStatusBarTopPadding(viewGroup: ViewGroup) {
        activity.apply {
            val statusbarId = resources.getIdentifier("status_bar_height", "dimen", "android")
            val statusBarHeight = resources.getDimensionPixelSize(statusbarId)

            viewGroup.setPadding(
                viewGroup.paddingLeft,
                statusBarHeight + viewGroup.paddingTop,
                viewGroup.right,
                viewGroup.paddingBottom
            )
        }
    }

    // 알림 설정 함수
    fun setNotification(value: Boolean) {
        if (value) {
            Log.d("설정", "알림 활성화")
        } else {
            Log.d("설정", "알림 비활성화")
        }
    }

    // 두 지점간 거리 구하는 함수
    fun getDistance(LatLng1: LatLng, LatLng2: LatLng): String {
        val distance = SphericalUtil.computeDistanceBetween(LatLng1, LatLng2).roundToInt()

        // 거리가 1000m가 넘는다면 km로 변경
        return if (distance >= 1000) {
            String.format("%.2f", (distance.toDouble() / 1000)) + "km"
        } else {
            distance.toString() + "m"
        }
    }

    // 다크모드 설정 함수
    fun setDarkmode(value: Boolean) {
        activity.apply {
            if (value) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    // 웹 이미지 설정 함수 (비동기)
    fun setWebImg(imageView: ImageView, url: String): Thread {
        return  Thread {
            val connection = URL(url).openConnection() as HttpURLConnection
            val bitmap = BitmapFactory.decodeStream(connection.inputStream)
            activity.runOnUiThread {
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    // 키보드 닫는 함수
    fun closeKeyboard() {
        val imm = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }
}