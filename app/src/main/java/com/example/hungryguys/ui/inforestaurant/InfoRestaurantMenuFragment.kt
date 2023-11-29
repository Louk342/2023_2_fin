package com.example.hungryguys.ui.inforestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hungryguys.databinding.FragmentInfoRestaurantMenuBinding

class InfoRestaurantMenuFragment : Fragment() {

    lateinit var recyclerAdapter: InfoRestaurantMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInfoRestaurantMenuBinding.inflate(inflater, container, false)
        val data1 = mutableListOf(mutableMapOf<String, String>())

        recyclerAdapter = InfoRestaurantMenuAdapter(data1)
        binding.infomenuRecycler.adapter

        return binding.root
    }
}