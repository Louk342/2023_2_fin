package com.example.hungryguys.utills

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MyApplication: MultiDexApplication() { // 먼저 실행

    companion object {
        lateinit var auth: FirebaseAuth
        var email: String? = null
        var name: String? = null


        fun checkAuth(): Boolean { //회원체크
            var currentUser = auth.currentUser
            email = currentUser?.email
            return currentUser?.isEmailVerified ?: false
        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth //로그인 상태 관리
    }
}

