package com.example.hungryguys.ui.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.hungryguys.databinding.ActivityChattingBinding
import com.example.hungryguys.ui.inforestaurant.ChatRoomData
import com.example.hungryguys.utills.ActivityUtills

enum class ChatItem {
    /** 채팅타입 (내 채팅 = me 상대방 = user 로 표기) */
    Chat_Type,
    /** 채팅 */
    Chat,
    /** 보낸시간 */
    Chat_Time
}

class ChattingActivity : AppCompatActivity() {
    lateinit var binding: ActivityChattingBinding
    lateinit var recyclerAdapter: ChattingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 상테바, 하단 내비게이션 투명화 및 보정
        val activityUtills = ActivityUtills(this)
        activityUtills.setStatusBarTransparent()
        activityUtills.setStatusBarPadding(binding.root)

        // 툴바 설정
        val toolbar = binding.toolbar
        toolbar.setContentInsetsRelative(0, 0)
        setSupportActionBar(binding.toolbar)

        // 툴바에 백 버튼 만들기
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //채팅방 제목으로 타이틀 변경
        binding.actionBarTitle.text = intent.getStringExtra(ChatRoomData.room_name.name)

        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val data1 = mutableMapOf(
            ChatItem.Chat_Type.name to "user",
            ChatItem.Chat.name to "안녕하세요",
            ChatItem.Chat_Time.name to "11:11"
        )
        val data2 = mutableMapOf(
            ChatItem.Chat_Type.name to "user",
            ChatItem.Chat.name to "잘되나요?",
            ChatItem.Chat_Time.name to "11:11"
        )
        val data3 = mutableMapOf(
            ChatItem.Chat_Type.name to "me",
            ChatItem.Chat.name to "아이고난1",
            ChatItem.Chat_Time.name to "11:11"
        )
        dbdata.add(data1)
        dbdata.add(data2)
        dbdata.add(data3)

        recyclerAdapter = ChattingAdapter(dbdata)
        binding.chatrecycler.adapter = recyclerAdapter

        // 보내기 버튼 이벤트
        binding.pushBt.setOnClickListener {
            val message = binding.pushMessge.text.toString()
            val item =  mutableMapOf(
                ChatItem.Chat_Type.name to "me",
                ChatItem.Chat.name to message,
                ChatItem.Chat_Time.name to "11:11",
            )
            addNewChat(item)
        }
    }

    // 새로운 채팅 갱신
    fun addNewChat(item: MutableMap<String, String>) {
        recyclerAdapter.data.add(item)
        recyclerAdapter.notifyItemInserted(recyclerAdapter.data.lastIndex)
    }

    // 백버튼 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}