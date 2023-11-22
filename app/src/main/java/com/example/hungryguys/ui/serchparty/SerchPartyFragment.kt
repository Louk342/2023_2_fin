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
        binding.partyrecycler.adapter = SerchPartyAdapter()

        return binding.root
    }

}