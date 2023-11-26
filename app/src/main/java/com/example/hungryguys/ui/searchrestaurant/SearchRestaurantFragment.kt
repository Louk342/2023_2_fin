package com.example.hungryguys.ui.searchrestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hungryguys.databinding.FragmentSearchRestaurantBinding

class SearchRestaurantFragment : Fragment() {
    lateinit var recyclerAdapter: SearchRestaurantAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchRestaurantBinding.inflate(inflater, container, false)

        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val data1 = mutableMapOf<String, String>()
        data1["restaurant_title"] = "식당1"
        dbdata.add(data1)

        recyclerAdapter = SearchRestaurantAdapter(dbdata)
        binding.restaurantrecycler.adapter = recyclerAdapter

        return binding.root
    }
}