package com.example.hungryguys.ui.searchparty

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hungryguys.databinding.FragmentSearchPartyBinding
import com.example.hungryguys.utills.GoogleLoginData
import com.example.hungryguys.utills.Request
import org.json.JSONArray

// 리사이클러 뷰에 전달되야 되는 키 값이 더있으면 여기다 추가
enum class SearchPartyItemId {
    /** 파티 아이디 */
    party_id,
    /** 파티이름 */
    party_name,
    /** 파티장소 */
    party_location,
    /** 파티 장소 아이디*/
    party_location_id,
    /** 접속인원 */
    party_person,
}

class SearchPartyFragment : Fragment() {
    lateinit var recyclerAdapter: SearchPartyAdapter
    lateinit var dbdata: MutableList<MutableMap<String, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchPartyBinding.inflate(inflater, container, false)
        dbdata = mutableListOf()

        recyclerAdapter = SearchPartyAdapter(dbdata)
        binding.partyrecycler.adapter = recyclerAdapter

        // DB 에 데이터 갖고 와서 추가
        addData().start()

        return binding.root
    }

    // DB 데이터 불러오기
    @SuppressLint("NotifyDataSetChanged")
    fun addData(): Thread {
        return Thread {
            val userdataJson =
                Request.reqget("${Request.REQUSET_URL}/email/${GoogleLoginData.email}")
                    ?: JSONArray()
            val groupId = userdataJson.getJSONObject(0).getString("group_id")

            // 해당 그룹에 활성 파티
            val partyJson = Request.reqget("${Request.REQUSET_URL}/party/${groupId}")
            for (i in 0..<partyJson?.length()!!) {
                val thisparty = partyJson.getJSONObject(i)

                val partylistjson = Request.reqget("${Request.REQUSET_URL}/partyUser/${thisparty.getString("party_id")}")
                val restaurantinfo = Request.reqget("${Request.REQUSET_URL}/getStore/${thisparty.getString("store_id")}")?.getJSONObject(0)!!

                val partyid = thisparty.getString("party_id")
                val partyname = thisparty.getString("party_name")
                val partylocation = restaurantinfo.getString("store_name")
                val partylocationid = restaurantinfo.getString("store_id")
                val partyperson = partylistjson?.length()?.toString()!!

                val data = mutableMapOf(
                    SearchPartyItemId.party_id.name to partyid,
                    SearchPartyItemId.party_name.name to partyname,
                    SearchPartyItemId.party_location.name to partylocation,
                    SearchPartyItemId.party_location_id.name to partylocationid,
                    SearchPartyItemId.party_person.name to partyperson
                )

                dbdata.add(data)
            }

            activity?.runOnUiThread {
                recyclerAdapter.notifyDataSetChanged()
            }
        }
    }
}