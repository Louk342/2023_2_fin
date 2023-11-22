package com.example.hungryguys.ui.serchparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hungryguys.databinding.FragmentSerchPartyBinding

class SerchPartyFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSerchPartyBinding.inflate(inflater, container, false)
        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val data1 = mutableMapOf<String, String>()
        data1["text"] = "테스트1"
        dbdata.add(data1)

        val data2 = mutableMapOf<String, String>()
        data2["text"] = "테스트2"
        dbdata.add(data2)

        binding.partyrecycler.adapter = SerchPartyAdapter(dbdata)

        return binding.root
    }

}