package com.example.hungryguys.ui.home

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import com.example.hungryguys.MainActivity
import com.example.hungryguys.R
import com.example.hungryguys.databinding.FragmentHomeBinding
import com.example.hungryguys.ui.inforestaurant.InfoRestaurantActivity
import com.example.hungryguys.ui.searchrestaurant.RestaurantItemId
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import kotlin.math.roundToInt

class HomeFragment : Fragment() {

    lateinit var homebinding: FragmentHomeBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homebinding = FragmentHomeBinding.inflate(inflater, container, false)

        // 식당 클릭 시 식당 정보 페이지로 이동
        homebinding.randomMenuLayout.setOnClickListener {
            val intent = Intent(context, InfoRestaurantActivity::class.java)

            //DB 에서 랜덤으로 식당 하나 받아오기
            intent.putExtra(RestaurantItemId.inforestaurant_id.name, 0)
            intent.putExtra(RestaurantItemId.restaurant_name.name, "청년다방")
            intent.putExtra(RestaurantItemId.restaurant_star.name, "4.1")
            intent.putExtra(RestaurantItemId.restaurant_star_count.name, "120")
            intent.putExtra(RestaurantItemId.restaurant_we.name, "200")
            intent.putExtra(RestaurantItemId.restaurant_ky.name, "100")
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
            homebinding.randomMenuLocation.text = String.format("%.2f", (distance.toDouble() / 1000)) + "km"
        } else
            homebinding.randomMenuLocation.text = distance.toString() + "m"

        return homebinding.root
    }

    // 여기에서 MainActivity 요소 건들여야 오류 안남
    override fun onStart() {
        super.onStart()
        (activity as MainActivity).apply {
            // 검색버튼 클릭이벤트
            actionbarView.searchIconButton.setOnClickListener {
                navController.navigate(
                    R.id.nav_searchrestaurant,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.nav_home, true)
                        .build()
                )

                // 검색창 포커싱 및 키보드 열기
                actionbarView.searchText.requestFocus()
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(actionbarView.searchText, 0)
            }
        }
    }
}