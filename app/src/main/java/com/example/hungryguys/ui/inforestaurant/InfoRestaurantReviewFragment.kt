package com.example.hungryguys.ui.inforestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hungryguys.databinding.FragmentInfoRestaurantReviewBinding

class InfoRestaurantReviewFragment : Fragment() {

    lateinit var recyclerAdapter: InfoRestaurantReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInfoRestaurantReviewBinding.inflate(inflater, container, false)
        val data1 = mutableListOf(mutableMapOf<String, String>())

        recyclerAdapter = InfoRestaurantReviewAdapter(data1)
        binding.inforeviewRecycler.adapter = recyclerAdapter

        return binding.root
    }
}