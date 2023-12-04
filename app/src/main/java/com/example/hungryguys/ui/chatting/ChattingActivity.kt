package com.example.hungryguys.ui.chatting

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.example.hungryguys.databinding.ActivityChattingBinding
import com.example.hungryguys.databinding.ChatInfoNavBinding
import com.example.hungryguys.ui.inforestaurant.ChatRoomData
import com.example.hungryguys.utills.ActivityUtills
enum class ChatItem {
    /** 채팅타입 (내 채팅 = me 상대방 = user 로 표기) */
    Chat_Type,
    /** 유저이름 */
    User_Name,
    /** 채팅 */
    Chat,
    /** 보낸시간 */
    Chat_Time
}

class ChattingActivity : AppCompatActivity() {
    lateinit var binding: ActivityChattingBinding
    lateinit var chatrecyclerAdapter: ChattingAdapter
    lateinit var navrecyclerAdapter: ChattingNavAdapter
    private lateinit var activityUtills: ActivityUtills

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 툴바 설정
        val toolbar = binding.toolbar
        toolbar.setContentInsetsRelative(0, 0)
        setSupportActionBar(binding.toolbar)

        // 툴바에 백 버튼 만들기
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        activityUtills = ActivityUtills(this)

        // 드로어 레이아웃 설정
        drawerSetting(binding.navViewLayout)

        //채팅방 제목으로 타이틀 변경
        binding.actionBarTitle.text = intent.getStringExtra(ChatRoomData.room_name.name)

        // 유저 수 설정
        binding.userCount.text = navrecyclerAdapter.data.size.toString()

        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val data1 = mutableMapOf(
            ChatItem.Chat_Type.name to "user",
            ChatItem.User_Name.name to "유저1",
            ChatItem.Chat.name to "안녕하세요",
            ChatItem.Chat_Time.name to "11:11"
        )
        val data2 = mutableMapOf(
            ChatItem.Chat_Type.name to "user",
            ChatItem.User_Name.name to "유저2",
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

        // 채팅창 어탭터 설정
        chatrecyclerAdapter = ChattingAdapter(dbdata)
        binding.chatrecycler.adapter = chatrecyclerAdapter

        // 햄버거 버튼으로 드로어 펼치기
        binding.hamburgerButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.END)
        }

        // 키보드 완료 이벤트 등록
        binding.pushMessge.setOnEditorActionListener(setKeyOkEvent(binding.pushBt))

        // 보내기 버튼 이벤트
        binding.pushBt.setOnClickListener {
            // 키보드 확인버튼, 보내기버튼 둘다 여기서 처리
            val message = binding.pushMessge.text.toString()
            if (message.isEmpty()) return@setOnClickListener

            val item =  mutableMapOf(
                ChatItem.Chat_Type.name to "me",
                ChatItem.Chat.name to message,
                ChatItem.Chat_Time.name to "11:11",
            )
            addNewChat(item)

            binding.pushMessge.text = null
        }
    }

    // 네비게이션 뷰 셋팅
    fun drawerSetting (navView: ChatInfoNavBinding) {
        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val data1 = mutableMapOf(
            ChatItem.User_Name.name to "유저1"
        )
        val data2 = mutableMapOf(
            ChatItem.User_Name.name to "유저2"
        )
        val data3 = mutableMapOf(
            ChatItem.User_Name.name to "유저3"
        )
        dbdata.add(data1)
        dbdata.add(data2)
        dbdata.add(data3)

        // 네비게이션 유저목록 어뎁터 설정
        navrecyclerAdapter = ChattingNavAdapter(dbdata)
        navView.chatNavRecycler.adapter = navrecyclerAdapter

        // 인텐트로 넘겨온 식당 이름 적용
        val restaurantname = intent.getStringExtra(ChatRoomData.restaurant_name.name)
        navView.restaurantName.text = restaurantname

        // 채팅방 나가기 이벤트
        navView.exitButton.setOnClickListener {
            finish()
        }
    }

    // 키도드 완료 이벤트 설정
    fun setKeyOkEvent(view: View): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.callOnClick()
            }
            true
        }
    }

    // 새로운 채팅 갱신
    fun addNewChat(item: MutableMap<String, String>) {
        chatrecyclerAdapter.data.add(item)
        chatrecyclerAdapter.notifyItemInserted(chatrecyclerAdapter.data.lastIndex)
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

    // 키보드 이외에 다른요소 선택시 키보드 닫치게
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        activityUtills.closeKeyboard()
        return super.dispatchTouchEvent(ev)
    }
}