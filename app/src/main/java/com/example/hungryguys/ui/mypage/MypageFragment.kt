package com.example.hungryguys.ui.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.hungryguys.databinding.FragmentMypageBinding

class MypageFragment : Fragment() {
    lateinit var recyclerAdapter: MypageAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMypageBinding.inflate(inflater, container, false)

        binding.logoutButtonText.setOnClickListener {
            Toast.makeText(requireContext(), "로그아웃", Toast.LENGTH_SHORT).show()
        }

        binding.cancelButton.setOnClickListener {
            Toast.makeText(requireContext(), "탈퇴", Toast.LENGTH_SHORT).show()
        }

        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val data1 = mutableMapOf<String, String>()
        data1["room_title"] = "방1"
        data1["last_chat"] = "채팅"
        data1["connect_people"] = "10"
        data1["last_chat_time"] = "시간"
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