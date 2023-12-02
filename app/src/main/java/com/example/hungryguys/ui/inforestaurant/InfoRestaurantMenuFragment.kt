package com.example.hungryguys.ui.inforestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hungryguys.databinding.FragmentInfoRestaurantMenuBinding


enum class InfoMenuItem {
    /** 음식 이름 */
    food_name,
    /** 음식 설명 */
    food_description,
    /** 음식가격 */
    food_price
}
class InfoRestaurantMenuFragment : Fragment() {

    lateinit var recyclerAdapter: InfoRestaurantMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInfoRestaurantMenuBinding.inflate(inflater, container, false)
        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val selectid =  (activity as InfoRestaurantActivity).restaurantid   //현재 선택된 아이템

        // 청년다방인 경우
        if (selectid == 0) {
            val data1 = mutableMapOf(
                InfoMenuItem.food_name.name to "세트1",
                InfoMenuItem.food_description.name to "고구마, 떡볶이",
                InfoMenuItem.food_price.name to "12000",
            )
            val data2 = mutableMapOf(
                InfoMenuItem.food_name.name to "세트2",
                InfoMenuItem.food_description.name to "치즈, 떡볶이",
                InfoMenuItem.food_price.name to "14000",
            )
            dbdata.add(data1)
            dbdata.add(data2)
        }

        // 푸라닭 인경우
        if (selectid == 1) {
            val data1 = mutableMapOf(
                InfoMenuItem.food_name.name to "후라이드",
                InfoMenuItem.food_description.name to "고소한 후라이드",
                InfoMenuItem.food_price.name to "20000",
            )
            val data2 = mutableMapOf(
                InfoMenuItem.food_name.name to "양념",
                InfoMenuItem.food_description.name to "매콤한 치킨",
                InfoMenuItem.food_price.name to "21000",
            )
            dbdata.add(data1)
            dbdata.add(data2)
        }

        recyclerAdapter = InfoRestaurantMenuAdapter(dbdata)
        binding.infomenuRecycler.adapter = recyclerAdapter

        return binding.root
    }
}