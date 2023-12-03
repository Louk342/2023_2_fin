package com.example.hungryguys.ui.searchrestaurant

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
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

    lateinit var recyclerAdapter: SearchRestaurantAdapter
    lateinit var searchtext: TextView
    lateinit var searchIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 카테고리 별 아이콘 만들어 지면 여기다 등록
        val categoryImageMap = mutableMapOf(
            "떡볶이" to R.drawable.tteokbokki_icon
        )

        val binding = FragmentSearchRestaurantBinding.inflate(inflater, container, false)

        searchtext =  (activity as MainActivity).actionbarView.searchText
        searchIcon = (activity as MainActivity).actionbarView.searchIcon

        // 완료버튼 이벤트 설정
        searchtext.setOnEditorActionListener(addKeyOkEvent(searchIcon))

        searchIcon.setOnClickListener {
            //키보드 완료랑 검색아이콘 클릭모두 이 함수를 거침
            searchList()
        }

        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
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
    fun searchList() {
        Log.d("로그", "거침")
    }
    fun addKeyOkEvent(view: View): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.callOnClick()
            }
            false
        }
    }

    override fun onDestroy() {
        searchtext.text = null
        searchIcon.setOnClickListener(null)
        super.onDestroy()
    }
}