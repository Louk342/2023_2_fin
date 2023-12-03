package com.example.hungryguys.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hungryguys.databinding.FragmentHomeBinding
import com.example.hungryguys.ui.inforestaurant.InfoRestaurantActivity
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlin.math.roundToInt

class HomeFragment : Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)

        // 식당 클릭 시 식당 정보 페이지로 이동
        binding.randomMenuLayout.setOnClickListener {
            val intent = Intent(context, InfoRestaurantActivity::class.java)
            intent.putExtra("item_position", 0)
            context?.startActivity(intent)
        }

        // 그룹 위치 정보
        val groupLatLng = LatLng(37.500049, 126.868003)
        // 식당 위치 정보
        val restaurantLatLng = LatLng(37.502045, 126.864655)
        // 그룹과 식당 사이의 거리
        val distance = SphericalUtil.computeDistanceBetween(groupLatLng, restaurantLatLng).roundToInt()
        // 거리가 1000m가 넘는다면 km로 변경
        if (distance >= 1000) {
            binding.randomMenuLocation.text = String.format("%.2f", (distance.toDouble() / 1000)) + "km"
        } else
            binding.randomMenuLocation.text = distance.toString() + "m"

        return binding.root
    }
}