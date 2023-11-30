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
        val data1 = mutableMapOf(
            InfoMenuItem.food_name.name to "음식1",
            InfoMenuItem.food_description.name to "음식설명",
            InfoMenuItem.food_price.name to "1000"
        )
        val data2 = mutableMapOf(
            InfoMenuItem.food_name.name to "음식2",
            InfoMenuItem.food_description.name to "음식설명",
            InfoMenuItem.food_price.name to "2000"
        )
        dbdata.add(data1)
        dbdata.add(data2)

        recyclerAdapter = InfoRestaurantMenuAdapter(dbdata)
        binding.infomenuRecycler.adapter = recyclerAdapter

        return binding.root
    }
}