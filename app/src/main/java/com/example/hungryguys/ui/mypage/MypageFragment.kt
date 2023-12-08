package com.example.hungryguys.ui.mypage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.hungryguys.MainActivity
import com.example.hungryguys.R
import com.example.hungryguys.databinding.FragmentMypageBinding
import com.example.hungryguys.ui.register.RegisterGroupActivity
import com.example.hungryguys.utills.GoogleLoginData
import com.example.hungryguys.utills.Request
import org.json.JSONArray

// 리사이클러 뷰에 전달되야 되는 키 값이 더있으면 여기다 추가
enum class MypageChatItemId {
    /** 채팅방 식별번호 */
    room_id,
    /** 채팅방 이름 */
    room_title,
    /** 식당이름 */
    restaurant_name,
    /** 식당아이디  */
    restaurant_id,
    /** 접속자 수 */
    connect_people,
    /** 마지막 채팅 시간 */
    last_chat_time,
}

class MypageFragment : Fragment() {
    lateinit var binding:FragmentMypageBinding
    private var launcher: ActivityResultLauncher<Intent>? = null
    lateinit var recyclerAdapter: MypageAdapter
    lateinit var groupId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        val contract = ActivityResultContracts.StartActivityForResult()
        val callback = ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                loadgroup().start()
                loadgroup().join()
            }
        }
        launcher = registerForActivityResult(contract, callback)

        // 로그아웃 클릭
        binding.logoutButtonText.setOnClickListener {
            MypageDialog().show(requireActivity().supportFragmentManager, "로그아웃")
        }

        // 회원탈퇴 클릭
        binding.cancelButton.setOnClickListener {
            MypageDialog().show(requireActivity().supportFragmentManager, "회원탈퇴")
        }

        binding.userName.text = GoogleLoginData.name

        loadgroup().start()
        loadgroup().join()

        binding.userLocationLayout.setOnClickListener{
            val intent = Intent(context, RegisterGroupActivity::class.java)
            intent.putExtra("type", "change")
            launcher?.launch(intent)
        }

        (activity as MainActivity).apply {
            // 설정 이미지 클릭하면 settingsFragment 로 이동
            actionbarView.settingButton.setOnClickListener {
                navController.navigate(R.id.nav_settings)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        val userdataThread = Thread {
            val userdataJson =
                Request.reqget("${Request.REQUSET_URL}/email/${GoogleLoginData.email}")
                    ?: JSONArray()
            groupId = userdataJson.getJSONObject(0).getString("group_id")
        }
        userdataThread.start()
        userdataThread.join()

        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()

        val partyThread = Thread {
            val partyJson = Request.reqget("${Request.REQUSET_URL}/party/${groupId}") ?: JSONArray()

            for (i in 0..<partyJson.length()) {
                val partyId = partyJson.getJSONObject(i).getString("party_id")
                val storeid = partyJson.getJSONObject(i).getString("store_id")

                val partyName = partyJson.getJSONObject(i).getString("party_name")
                val partyuserJson = Request.reqget("${Request.REQUSET_URL}/partyUser/${partyId}") ?: JSONArray()
                val storename = Request.reqget("${Request.REQUSET_URL}/getStore/${storeid}")?.getJSONObject(0)?.getString("store_name")!!

                var count = 0
                for (i in 0..<partyuserJson.length()) {
                    count++
                }
                for (i in 0..<partyuserJson.length()) {
                    if(partyuserJson.getJSONObject(i).getString("email") == GoogleLoginData.email) {
                        val data1 = mutableMapOf(
                            MypageChatItemId.room_id.name to partyId,
                            MypageChatItemId.room_title.name to partyName,
                            MypageChatItemId.restaurant_name.name to storename,
                            MypageChatItemId.restaurant_id.name to storeid,
                            MypageChatItemId.connect_people.name to count.toString(),
                            MypageChatItemId.last_chat_time.name to "시간"
                        )

                        dbdata.add(data1)
                    }
                }
            }
        }
        partyThread.start()
        partyThread.join()

        recyclerAdapter = MypageAdapter(dbdata, binding)
        binding.myChatRecycler.adapter = recyclerAdapter

        return binding.root
    }

    // DB에서 그룹 이름 가져오기
    private fun loadgroup(): Thread {
        return Thread {
            val userdataJson = Request.reqget("${Request.REQUSET_URL}/email/${GoogleLoginData.email}") ?: JSONArray()
            val group_id = userdataJson.getJSONObject(0).getString("group_id")

            val groupJson = Request.reqget("${Request.REQUSET_URL}/group") ?: JSONArray()

            for (i in 0..< groupJson.length()) {
                val json = groupJson.getJSONObject(i)

                if(json.getString("group_id") == group_id) {
                    binding.userLocation.text = json.getString("group_name")
                }
            }
        }
    }
}