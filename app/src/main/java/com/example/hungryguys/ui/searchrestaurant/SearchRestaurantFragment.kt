package com.example.hungryguys.ui.searchrestaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
    // 음식점 별 아이콘 만들어 지면 여기다 등록
    private val restaurantImageMap = mutableMapOf(
        "청년다방 동양미래대점" to R.drawable.coungyear,
        "맘스터치 동양미래대점" to R.drawable.momstouch,
        "고척돈까스" to R.drawable.gocuckdon,
        "홍콩반점0410 고척동양대점" to R.drawable.hongkongban,
    )

    lateinit var binding: FragmentSearchRestaurantBinding
    lateinit var recyclerAdapter: SearchRestaurantAdapter
    private lateinit var dbdata: MutableList<MutableMap<String, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchRestaurantBinding.inflate(inflater, container, false)

        val searchtext = (activity as MainActivity).actionbarView.searchText

        // 키보드 입력이벤트
        searchtext.addTextChangedListener {
            searchList(it.toString())
        }

        dbdata = mutableListOf()
        val data1 = mutableMapOf(
            RestaurantItemId.restaurant_name.name to "청년다방 동양미래대점",
            RestaurantItemId.restaurant_category.name to "분식",
            RestaurantItemId.restaurant_star.name to "4.6",
            RestaurantItemId.restaurant_star_count.name to "5",
            RestaurantItemId.restaurant_description.name to "떡볶이와 커피를 결합한 분식 카페",
            RestaurantItemId.restaurant_we.name to "37.50023492723507",
            RestaurantItemId.restaurant_ky.name to "126.86688571370166"
        )
        val data2 = mutableMapOf(
            RestaurantItemId.restaurant_name.name to "맘스터치 동양미래대점",
            RestaurantItemId.restaurant_category.name to "햄버거",
            RestaurantItemId.restaurant_star.name to "4.4",
            RestaurantItemId.restaurant_star_count.name to "5",
            RestaurantItemId.restaurant_description.name to "맛있는 햄버거 치킨 브랜드",
            RestaurantItemId.restaurant_we.name to "37.50079897353079",
            RestaurantItemId.restaurant_ky.name to "126.86635713389877"
        )

        val data3 = mutableMapOf(
            RestaurantItemId.restaurant_name.name to "고척돈까스",
            RestaurantItemId.restaurant_category.name to "돈까스",
            RestaurantItemId.restaurant_star.name to "4.0",
            RestaurantItemId.restaurant_star_count.name to "5",
            RestaurantItemId.restaurant_description.name to "맛있고 가성비 좋은 돈까스집",
            RestaurantItemId.restaurant_we.name to "37.50205556383366",
            RestaurantItemId.restaurant_ky.name to "126.86490388817289"
        )
        val data4 = mutableMapOf(
            RestaurantItemId.restaurant_name.name to "홍콩반점0410 고척동양대점",
            RestaurantItemId.restaurant_category.name to "중식",
            RestaurantItemId.restaurant_star.name to "4.0",
            RestaurantItemId.restaurant_star_count.name to "5",
            RestaurantItemId.restaurant_description.name to "백종원의 중식 브랜드",
            RestaurantItemId.restaurant_we.name to "37.500386294196936",
            RestaurantItemId.restaurant_ky.name to "126.86621315398844"
        )
        dbdata.add(data1)
        dbdata.add(data2)
        dbdata.add(data3)
        dbdata.add(data4)

        recyclerAdapter = SearchRestaurantAdapter(dbdata, restaurantImageMap)
        binding.restaurantrecycler.apply {
            adapter = recyclerAdapter

            // 하단 구분선 추가
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        }

        return binding.root
    }

    // 검색 구현
    private fun searchList(text: String) {
        val data = if (text.isEmpty()) {
            dbdata
        } else {
            dbdata.filter {
                val name = it[RestaurantItemId.restaurant_name.name]!!
                name.contains(text, true)
            }.toMutableList()
        }

        binding.restaurantrecycler.adapter = SearchRestaurantAdapter(data, restaurantImageMap)
    }
}