package com.example.hungryguys.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.hungryguys.R
import com.example.hungryguys.databinding.ActivityAuthBinding
import com.example.hungryguys.ui.register.RegisterGroupActivity
import com.example.hungryguys.utills.ActivityUtills
import com.example.hungryguys.utills.GoogleLoginData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 상테바, 하단 내비게이션 투명화 및 보정
        val activityUtills = ActivityUtills(this)
        activityUtills.setStatusBarTransparent()
        activityUtills.setStatusBarAllPadding(binding.root)

        //구글 로그인 결과 처리
        val requestLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                try {
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                    val credential: AuthCredential = GoogleAuthProvider.getCredential(account.idToken, null) //
                    GoogleLoginData.auth.signInWithCredential(credential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // 사용자 정보 얻어오기
                                GoogleLoginData.email = account.email
                                GoogleLoginData.name = account.displayName

                                Log.d("로그", "${account.email}")
                                Log.d("로그", "${account.displayName}")

                                // 구글 로그인 성공시
                                // TODO: DB에 해당 회원 조회해서 실제 회원인지 판단해서 회원가입 시킬지 말지결정해야함
                                val intent = Intent(applicationContext, RegisterGroupActivity::class.java)
                                intent.putExtra("type", "register")
                                startActivity(intent)
                                //startActivity(Intent(applicationContext, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "구글 계정과 연동에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                } catch (e: ApiException) {
                    Log.e("로그인 실패", "API Exception: ${e.statusCode}", e)
                    Toast.makeText(this, "구글에 인증하는 과정에서 문제가 생겼습니다.", Toast.LENGTH_SHORT).show()
                }
            }

        //구글 인증 버튼
        binding.googleLoginButton.setOnClickListener {
            //구글 로그인
            val gso: GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id)) //구글에 요청할 정보 (토큰, 이메일)
                    .requestEmail()
                    .build()
            val signInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = signInClient.signInIntent
            requestLauncher.launch(signInIntent)
        }
    }
}
