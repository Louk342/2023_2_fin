package com.example.hungryguys.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.hungryguys.MainActivity
import com.example.hungryguys.R
import com.example.hungryguys.databinding.FragmentHomeBinding
import com.example.hungryguys.ui.inforestaurant.InfoRestaurantActivity

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.randomMenuLayout.setOnClickListener {
            val intent = Intent(context, InfoRestaurantActivity::class.java)
            intent.putExtra("item_position", 0)
            context?.startActivity(intent)
        }

        return binding.root
    }
}