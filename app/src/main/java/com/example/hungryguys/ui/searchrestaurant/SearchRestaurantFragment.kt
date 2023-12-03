package com.example.hungryguys.ui.searchrestaurant

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.hungryguys.MainActivity
import com.example.hungryguys.R
import com.example.hungryguys.databinding.FragmentSearchRestaurantBinding


// 리사이클러 뷰에 전달되야 되는 키 값이 더있으면 여기다 추가
enum class RestaurantItemId {
    /** 식당 식별값 (DB 쪽에서 )*/
    inforestaurant_id,
    /** 식당이름*/
    restaurant_name,
    /** 식당 카테고리 */
    restaurant_category,
    /** 식당 별점 */
    restaurant_star,
    /** 식당 리뷰 개수 */
    restaurant_star_count,
    /** 식당 설명 */
    restaurant_description,
    /** 식당 위도값 */
    restaurant_we,
    /** 식당 경도값 */
    restaurant_ky
}

class SearchRestaurantFragment : Fragment() {
    // 카테고리 별 아이콘 만들어 지면 여기다 등록
    val categoryImageMap = mutableMapOf(
        "떡볶이" to R.drawable.tteokbokki_icon
    )

    lateinit var binding: FragmentSearchRestaurantBinding
    lateinit var recyclerAdapter: SearchRestaurantAdapter
    private lateinit var searchtext: TextView
    private lateinit var searchIcon: ImageView
    private lateinit var dbdata: MutableList<MutableMap<String, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchRestaurantBinding.inflate(inflater, container, false)

        searchtext =  (activity as MainActivity).actionbarView.searchText
        searchIcon = (activity as MainActivity).actionbarView.searchIcon

        // 키보드 입력이벤트
        searchtext.addTextChangedListener {
            searchList(it.toString())
        }

        dbdata = mutableListOf()
        val data1 = mutableMapOf(
            RestaurantItemId.restaurant_name.name to "청년다방",
            RestaurantItemId.restaurant_category.name to "떡볶이",
            RestaurantItemId.restaurant_star.name to "4.1",
            RestaurantItemId.restaurant_star_count.name to "120",
            RestaurantItemId.restaurant_description.name to "떡복이가 참 긴",
            RestaurantItemId.restaurant_we.name to "200",
            RestaurantItemId.restaurant_ky.name to "100"
        )
        val data2 = mutableMapOf(
            RestaurantItemId.restaurant_name.name to "푸라닭",
            RestaurantItemId.restaurant_category.name to "치킨",
            RestaurantItemId.restaurant_star.name to "4.0",
            RestaurantItemId.restaurant_star_count.name to "200",
            RestaurantItemId.restaurant_description.name to "맛있는 치킨집",
            RestaurantItemId.restaurant_we.name to "200",
            RestaurantItemId.restaurant_ky.name to "100"
        )
        dbdata.add(data1)
        dbdata.add(data2)

        recyclerAdapter = SearchRestaurantAdapter(dbdata, categoryImageMap)
        binding.restaurantrecycler.apply {
            adapter = recyclerAdapter

            // 하단 구분선 추가
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        }

        return binding.root
    }

    // 검색 구현
    fun searchList(text: String) {
        var data: MutableList<MutableMap<String, String>>
        if (text.isEmpty()) {
            data = dbdata
        }

        data = dbdata.filter {
            val name = it[RestaurantItemId.restaurant_name.name]!!
            name.contains(text, true)
        }.toMutableList()

        binding.restaurantrecycler.adapter = SearchRestaurantAdapter(data, categoryImageMap)
    }

    // 프래그 먼트에서 벗어나면 입력 초기화
    override fun onDestroy() {
        searchtext.text = null
        super.onDestroy()
    }
}