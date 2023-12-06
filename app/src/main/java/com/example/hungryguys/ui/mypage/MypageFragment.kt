package com.example.hungryguys.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.hungryguys.MainActivity
import com.example.hungryguys.R
import com.example.hungryguys.databinding.FragmentMypageBinding
import com.example.hungryguys.ui.auth.AuthActivity
import com.example.hungryguys.utills.GoogleLoginData

// 리사이클러 뷰에 전달되야 되는 키 값이 더있으면 여기다 추가
enum class MypageChatItemId {
    /** 채팅방 이름 */
    room_title,
    /** 마지막 채팅 */
    last_chat,
    /** 접속자 수 */
    connect_people,
    /** 마지막 채팅 시간 */
    last_chat_time,
}

class MypageFragment : Fragment() {
    lateinit var recyclerAdapter: MypageAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMypageBinding.inflate(inflater, container, false)

        binding.logoutButtonText.setOnClickListener {
            GoogleLoginData.auth.signOut()
            GoogleLoginData.email = null
            GoogleLoginData.name = null
            Toast.makeText(requireContext(), "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, AuthActivity::class.java))
            activity?.finish()
        }

        binding.cancelButton.setOnClickListener {
            // TODO: db에서 회원 탈퇴 처리하는 작업필요
            GoogleLoginData.auth.signOut()
            GoogleLoginData.email = null
            GoogleLoginData.name = null
            Toast.makeText(requireContext(), "탈퇴처리 되었습니다", Toast.LENGTH_SHORT).show()
            startActivity(Intent(context, AuthActivity::class.java))
            activity?.finish()
        }

        binding.userName.text = GoogleLoginData.name

        (activity as MainActivity).apply {
            // 설정 이미지 클릭하면 settingsFragment 로 이동
            actionbarView.settingButton.setOnClickListener {
                navController.navigate(R.id.nav_settings)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val data1 = mutableMapOf(
            MypageChatItemId.room_title.name to "방1",
            MypageChatItemId.last_chat.name to "채팅",
            MypageChatItemId.connect_people.name to "10",
            MypageChatItemId.last_chat_time.name to "시간"
        )

        dbdata.add(data1)
        dbdata.add(data1)
        dbdata.add(data1)
        dbdata.add(data1)
        dbdata.add(data1)
        dbdata.add(data1)
        dbdata.add(data1)

        recyclerAdapter = MypageAdapter(dbdata, binding)
        binding.myChatRecycler.adapter = recyclerAdapter

        return binding.root
    }
}