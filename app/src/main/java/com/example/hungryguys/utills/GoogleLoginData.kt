package com.example.hungryguys.utills

import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// 구글로그인 데이터 보관 클래스
class GoogleLoginData: MultiDexApplication() {

    companion object {
        lateinit var auth: FirebaseAuth
        var email: String? = null   //이메일
        var name: String? = null    //이름

        fun checkAuth(): Boolean { //회원체크
            val currentUser = auth.currentUser
            email = currentUser?.email
            name = currentUser?.displayName
            return currentUser?.isEmailVerified ?: false
        }
    }

    override fun onCreate() {
        super.onCreate()
        auth = Firebase.auth //로그인 상태 관리
    }
}

