package com.example.hungryguys.ui.searchparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hungryguys.databinding.FragmentSearchPartyBinding

// 리사이클러 뷰에 전달되야 되는 키 값이 더있으면 여기다 추가
enum class SearchPartyItemId {
    /** 파티이름 */
    party_name, //파티이름
    /** 파티장소 */
    party_location,
    /** 접속인원 */
    party_person,
}

class SearchPartyFragment : Fragment() {
    lateinit var recyclerAdapter: SearchPartyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchPartyBinding.inflate(inflater, container, false)
        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()

        val data1 = mutableMapOf(
            SearchPartyItemId.party_name.name to "밥먹을사람 구해요~",
            SearchPartyItemId.party_location.name to "청년다방 동양미래대점",
            SearchPartyItemId.party_person.name to "3"
        )
        val data2 = mutableMapOf(
            SearchPartyItemId.party_name.name to "간단하게 점심해결할 4명 모집",
            SearchPartyItemId.party_location.name to "맘스터치 동양미래대점",
            SearchPartyItemId.party_person.name to "2"
        )
        dbdata.add(data1)
        dbdata.add(data2)

        recyclerAdapter = SearchPartyAdapter(dbdata)
        binding.partyrecycler.adapter = recyclerAdapter

        return binding.root
    }
}