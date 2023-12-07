package com.example.hungryguys.ui.chatting

import android.annotation.SuppressLint
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
import com.example.hungryguys.ui.searchparty.SearchPartyItemId
import com.example.hungryguys.utills.ActivityUtills
import com.example.hungryguys.utills.GoogleLoginData
import com.example.hungryguys.utills.Request

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
    lateinit var partylocation: String // 파티 장소
    lateinit var partylocationid: String // 파티 장소id
    lateinit var partyid: String // 파티 id
    lateinit var navuserdb: MutableList<MutableMap<String, String>>

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

        //인텐트 처리
        setIntentData()

        // 드로어 레이아웃 설정
        drawerSetting(binding.navViewLayout)

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
            ChatItem.Chat.name to "안녕하세요",
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

    // 인텐트로 넘어온값 처리
    private fun setIntentData() {
        val partyname = intent.getStringExtra(SearchPartyItemId.party_name.name)
        partylocation = intent.getStringExtra(SearchPartyItemId.party_location.name)!!
        partylocationid = intent.getStringExtra(SearchPartyItemId.party_location_id.name)!!
        partyid = intent.getStringExtra(SearchPartyItemId.party_id.name)!!

        if (partyid == "first") {
            partyid = "1"
            val thread = Thread {
                // TODO: 아니 이거 생성하고 파티 id 값을 못 얻는데 어케하라는거
                val userjson = Request.reqget("${Request.REQUSET_URL}/email/${GoogleLoginData.email}")?.getJSONObject(0)!!
                val userid = userjson.getString("user_id")
                val groupid = userjson.getString("group_id")

                Request.reqget("${Request.REQUSET_URL}/addParty/${userid}/${partylocationid}/${groupid}/${partyname}")
            }
            thread.start()
        } else {
            Thread {
                val userjson = Request.reqget("${Request.REQUSET_URL}/email/${GoogleLoginData.email}")?.getJSONObject(0)!!
                val userid = userjson.getString("user_id")

                Request.reqget("${Request.REQUSET_URL}/joinParty/${partyid}/${userid}")
            }.start()
        }

        // 채팅방 이름 변경
        binding.actionBarTitle.text = partyname
    }

    // 네비게이션 뷰 셋팅
    @SuppressLint("NotifyDataSetChanged")
    private fun drawerSetting (navView: ChatInfoNavBinding) {
        navuserdb = mutableListOf()

        // 네비게이션 유저목록 어뎁터 설정
        navrecyclerAdapter = ChattingNavAdapter(navuserdb)
        navView.chatNavRecycler.adapter = navrecyclerAdapter

        navView.restaurantName.text = partylocation

        Thread {
            val userJson = Request.reqget("${Request.REQUSET_URL}/partyUser/${partyid}")!!
            val restaurantdata = Request.reqget("${Request.REQUSET_URL}/getStore/${partylocationid}")!!
            for (i in 0..<userJson.length()) {
                var user = userJson.getJSONObject(i).getString("user_name")

                if (user == GoogleLoginData.name) user = "$user (나)"

                val data = mutableMapOf(
                    ChatItem.User_Name.name to user
                )
                navuserdb.add(data)
            }

            runOnUiThread {
                // 유저 수 설정
                binding.userCount.text = navrecyclerAdapter.data.size.toString()

                // 드로어 식당 이미지 설정
                val imageurl = restaurantdata.getJSONObject(0).getString("img")
                activityUtills.setWebImg(binding.navViewLayout.restaurantImage, imageurl).start()

                navrecyclerAdapter.notifyDataSetChanged()
            }
        }.start()

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