package com.example.hungryguys.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.hungryguys.R
import com.example.hungryguys.databinding.ActivityAuthBinding
import com.example.hungryguys.utills.MyApplication
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

        if (MyApplication.checkAuth()) changeVisibility("login") //로그인 된 상태
        else changeVisibility("logout") //로그아웃 상태

        binding.goSignInBtn.setOnClickListener { changeVisibility("signin") } //로그인 창 상태

        //회원가입 버튼
        binding.signBtn.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()

            //파이어베이스 응답 리스너
            val task = MyApplication.auth.createUserWithEmailAndPassword(email, password)
            task.addOnCompleteListener(this@AuthActivity) { p0 ->
                binding.authEmailEditView.text.clear()
                binding.authPasswordEditView.text.clear()
                if (p0.isSuccessful) { //파이어베이스 연결 성공시
                    val task1 = MyApplication.auth.currentUser?.sendEmailVerification() //인증 이메일 전송
                    task1?.addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(baseContext, "인증메일이 발송되었습니다", Toast.LENGTH_LONG).show()
                            changeVisibility("logout")
                        } else {
                            Toast.makeText(baseContext, "인증메일 발송 실패", Toast.LENGTH_LONG).show()
                            changeVisibility("logout")
                        }
                    }//

                } else { //실패시
                    Toast.makeText(baseContext, "회원 가입 실패", Toast.LENGTH_LONG).show()
                    changeVisibility("logout")
                }
            }
        }

        //로그인 버튼
        binding.loginBtn.setOnClickListener {
            //이메일, 비밀번호 로그인
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()

            //
            MyApplication.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    binding.authEmailEditView.text.clear()
                    binding.authPasswordEditView.text.clear()
                    if (task.isSuccessful) { //계정 연결 확인
                        if (MyApplication.checkAuth()) { //이메일 인증 확인
                            MyApplication.email = email
                            changeVisibility("login")
                        } else Toast.makeText(
                            baseContext,
                            "전송된 메일로 이메일 인증이 되지 않았습니다.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else Toast.makeText(baseContext, "로그인 실패", Toast.LENGTH_LONG).show()
                }
        }

        //로그아웃 버튼
        binding.logoutBtn.setOnClickListener {
            MyApplication.auth.signOut()
            MyApplication.email = null
            changeVisibility("logout")
        }

        //구글 로그인 결과 처리
        val requestLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val account: GoogleSignInAccount = task.getResult(ApiException::class.java)!!
                    val credential: AuthCredential =
                        GoogleAuthProvider.getCredential(account.idToken, null) //
                    MyApplication.auth.signInWithCredential(credential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) { //구글 연동 성공
                                MyApplication.email = account.email
                                changeVisibility("login")
                            } else changeVisibility("logout") //실패
                        }
                } catch (e: ApiException) {
                    Log.e("GoogleSignIn", "API Exception: ${e.statusCode}", e)
                    changeVisibility("logout")
                }
            }

        //구글 인증 버튼
        binding.googleLoginBtn.setOnClickListener {
            //구글 로그인....................
            val gso: GoogleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id)) //구글에 요청할 정보 (토큰, 이메일)
                    .requestEmail()
                    .build()
            val signInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso)
            val signInIntent = signInClient.signInIntent
            requestLauncher.launch(signInIntent)
        }
    } //onCreate


    //상태에 따른 변화 관리
    fun changeVisibility(mode: String) {
        if (mode === "login") {
            binding.run {
                authMainTextView.text = "${MyApplication.name} 님 반갑습니다."
                logoutBtn.visibility = View.VISIBLE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.GONE
                authPasswordEditView.visibility = View.GONE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.GONE

            }

        } else if (mode === "logout") {
            binding.run {
                authMainTextView.text = "로그인 하거나 회원가입 해주세요."
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.VISIBLE
                googleLoginBtn.visibility = View.VISIBLE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.GONE
                loginBtn.visibility = View.VISIBLE
            }

        } else if (mode === "signin") {
            binding.run {
                logoutBtn.visibility = View.GONE
                goSignInBtn.visibility = View.GONE
                googleLoginBtn.visibility = View.GONE
                authEmailEditView.visibility = View.VISIBLE
                authPasswordEditView.visibility = View.VISIBLE
                signBtn.visibility = View.VISIBLE
                loginBtn.visibility = View.GONE
            }
        }
    }
}
