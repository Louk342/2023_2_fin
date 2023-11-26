package com.example.hungryguys.ui.searchparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hungryguys.databinding.FragmentSearchPartyBinding

class SearchPartyFragment : Fragment() {
    lateinit var recyclerAdapter: SearchPartyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchPartyBinding.inflate(inflater, container, false)
        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val data1 = mutableMapOf<String, String>()
        data1["party_name"] = "파티1"
        data1["party_location"] = "고척"
        data1["party_person"] = "10"
        dbdata.add(data1)

        val data2 = mutableMapOf<String, String>()
        data2["party_name"] = "파티2"
        data2["party_location"] = "고척"
        data2["party_person"] = "20"
        dbdata.add(data2)

        recyclerAdapter = SearchPartyAdapter(dbdata)
        binding.partyrecycler.adapter = recyclerAdapter

        return binding.root
    }
}