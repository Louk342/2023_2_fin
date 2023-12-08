package com.example.hungryguys.ui.mypage

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.hungryguys.databinding.DialogCencalBinding
import com.example.hungryguys.databinding.DialogLogoutBinding
import com.example.hungryguys.ui.auth.AuthActivity
import com.example.hungryguys.utills.GoogleLoginData
import com.example.hungryguys.utills.Request
import org.json.JSONArray

// 로그아웃, 회원탈퇴 다이얼로그
class MypageDialog : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = true
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 로그아웃 다이얼로그이면
        if (tag == "로그아웃") {
            val binding = DialogLogoutBinding.inflate(inflater, container, false)

            binding.buttonOk.setOnClickListener {
                GoogleLoginData.auth.signOut()
                GoogleLoginData.email = null
                GoogleLoginData.name = null
                Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                startActivity(Intent(context, AuthActivity::class.java))
                activity?.finish()
                dismiss()
            }

            binding.buttonCencel.setOnClickListener {
                dismiss()
            }

            return binding.root
        }

        //  회원탈퇴 다이얼로그이면
        else if (tag == "회원탈퇴") {
            val binding = DialogCencalBinding.inflate(inflater, container, false)

            binding.buttonOk.setOnClickListener {
                // TODO: db에서 회원 탈퇴 처리하는 작업필요
                var userId = ""
                val userdataThread = Thread {
                    val userdataJson =
                        Request.reqget("${Request.REQUSET_URL}/email/${GoogleLoginData.email}")
                            ?: JSONArray()
                    userId = userdataJson.getJSONObject(0).getString("user_id")
                }
                userdataThread.start()
                userdataThread.join()

                val deleteuserThread = Thread {
                    Request.reqget("${Request.REQUSET_URL}/deleteUser/${userId}")
                }
                deleteuserThread.start()
                deleteuserThread.join()

                GoogleLoginData.auth.signOut()
                GoogleLoginData.email = null
                GoogleLoginData.name = null
                Toast.makeText(requireContext(), "탈퇴처리 되었습니다", Toast.LENGTH_SHORT).show()
                startActivity(Intent(context, AuthActivity::class.java))
                activity?.finish()
                dismiss()
            }

            binding.buttonCencel.setOnClickListener {
                dismiss()
            }

            return binding.root
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}