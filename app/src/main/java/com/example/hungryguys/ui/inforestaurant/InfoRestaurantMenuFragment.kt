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

    lateinit var recyclerAdapter: InfoRestaurantTabAdapter

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
                InfoMenuItem.food_name.name to "[덤벼라맵부심들]맵도전세트",
                InfoMenuItem.food_description.name to "쿨다방, 에이드와 함께 즐기는 청년다방 알짜세트메뉴입니다.",
                InfoMenuItem.food_price.name to "20,500원",
            )
            val data2 = mutableMapOf(
                InfoMenuItem.food_name.name to "[청다1등떡볶이]불향차돌세트",
                InfoMenuItem.food_description.name to "떡볶이 + 버터갈릭감자튀김 or 핫버터갈릭 옥수수튀김+쿨다방 or 에이드(택1)",
                InfoMenuItem.food_price.name to "23,500원",
            )
            dbdata.add(data1)
            dbdata.add(data2)
        }

        // 맘스터치 인경우
        if (selectid == 1) {
            val data1 = mutableMapOf(
                InfoMenuItem.food_name.name to "싸이플렉스버거",
                InfoMenuItem.food_description.name to "통다리살 싸이패티가 2장! 압도적 사이즈의 FLEX, 리얼 입찢버거 싸이플렉스버거",
                InfoMenuItem.food_price.name to "7,700원",
            )
            val data2 = mutableMapOf(
                InfoMenuItem.food_name.name to "아라비아따치즈버거",
                InfoMenuItem.food_description.name to "통모짜렐라 치즈패티, 부드러운 통다리살, 매콤한 아라비아따 소스의 환상적인 조화",
                InfoMenuItem.food_price.name to "7,100원",
            )
            dbdata.add(data1)
            dbdata.add(data2)
        }
        recyclerAdapter = InfoRestaurantTabAdapter(dbdata, "menu")
        binding.infomenuRecycler.adapter = recyclerAdapter

        return binding.root
    }
}