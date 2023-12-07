package com.example.hungryguys.ui.searchparty

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hungryguys.databinding.FragmentSearchPartyBinding
import com.example.hungryguys.utills.Request

// 리사이클러 뷰에 전달되야 되는 키 값이 더있으면 여기다 추가
enum class SearchPartyItemId {
    /** 파티 아이디 */
    party_id,
    /** 파티이름 */
    party_name,
    /** 파티장소 */
    party_location,
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
            // 해당 그룹에 활성 파티
            val partyJson = Request.reqget("${Request.REQUSET_URL}/party/1")
            for (i in 0..<partyJson?.length()!!) {
                val thisparty = partyJson.getJSONObject(i)
                val partylistjson = Request.reqget("${Request.REQUSET_URL}/partyUser/${thisparty.getString("party_id")}")

                val partyid = thisparty.getString("party_id")
                val partyname = thisparty.getString("party_name")
                val partyperson = partylistjson?.length()?.toString()!!

                val data = mutableMapOf(
                    SearchPartyItemId.party_id.name to partyid,
                    SearchPartyItemId.party_name.name to partyname,
                    SearchPartyItemId.party_location.name to "식당이름",
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