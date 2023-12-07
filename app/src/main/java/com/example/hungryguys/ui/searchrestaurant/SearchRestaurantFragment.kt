package com.example.hungryguys.ui.searchrestaurant

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.hungryguys.MainActivity
import com.example.hungryguys.databinding.FragmentSearchRestaurantBinding
import com.example.hungryguys.utills.Request
import org.json.JSONArray

// 리사이클러 뷰에 전달되야 되는 키 값이 더있으면 여기다 추가
enum class RestaurantItemId {
    /** 식당 식별값 (DB 쪽에서 )*/
    inforestaurant_id,
    /** 식당이름*/
    restaurant_name,
    /** 식당 이미지 주소*/
    restaurant_img,
    /** 식당 별점 */
    restaurant_star,
    /** 식당 리뷰 개수 */
    restaurant_star_count,
    /** 식당 설명 */
    restaurant_description,
    /** 식당 위도값 */
    restaurant_we,
    /** 식당 경도값 */
    restaurant_ky,
}

class SearchRestaurantFragment : Fragment() {
    lateinit var binding: FragmentSearchRestaurantBinding
    private lateinit var recyclerAdapter: SearchRestaurantAdapter
    private lateinit var dbdata: MutableList<MutableMap<String, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchRestaurantBinding.inflate(inflater, container, false)
        val searchtext = (activity as MainActivity).actionbarView.searchText

        binding.restaurantrecycler.apply {
            // 하단 구분선 추가
            addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        }

        dbdata = mutableListOf()
        recyclerAdapter = SearchRestaurantAdapter(dbdata, requireActivity())
        binding.restaurantrecycler.adapter = recyclerAdapter

        // DB 데이터 불러오기
        addData().start()

        // 키보드 입력이벤트
        searchtext.addTextChangedListener {
            /*searchList(it.toString())*/
        }

        return binding.root
    }

    // DB에서 데이터 가져오기
    @SuppressLint("NotifyDataSetChanged")
    private fun addData(): Thread {
        return Thread {
            val restaurantJson = Request.reqget("${Request.REQUSET_URL}/store/1") ?: JSONArray()
            //추후 그룹 아이디를 db에서

            for (i in 0..< restaurantJson.length()) {
                val json = restaurantJson.getJSONObject(i)

                val reviewJson =
                    Request.reqget("${Request.REQUSET_URL}/review/${json.getString("store_id")}") ?: JSONArray()
                var count = 0
                var sum = 0.0
                for (i in 0..<reviewJson.length()) {
                    val json = reviewJson.getJSONObject(i)
                    count++
                    sum += json.getDouble("star")
                }
                var storeStar = String.format("%.1f", (sum / count))
                if(count == 0) {
                    storeStar = "0"
                }
                val storeStarCount = count.toString()

                val data= mutableMapOf(
                    RestaurantItemId.inforestaurant_id.name to json.getString("store_id"),
                    RestaurantItemId.restaurant_name.name to json.getString("store_name"),
                    RestaurantItemId.restaurant_img.name to json.getString("img"),
                    RestaurantItemId.restaurant_star.name to storeStar,
                    RestaurantItemId.restaurant_star_count.name to storeStarCount,
                    RestaurantItemId.restaurant_description.name to json.getString("store_kind"),
                    RestaurantItemId.restaurant_we.name to json.getString("x"),
                    RestaurantItemId.restaurant_ky.name to json.getString("y")
                )
                dbdata.add(data)
            }
            activity?.runOnUiThread {
                recyclerAdapter.notifyDataSetChanged()
            }
        }
    }

    // 검색 구현
/*    @SuppressLint("NotifyDataSetChanged")
    private fun searchList(text: String) {
        val data = if (text.isEmpty()) {
            dbdata
        } else {
            dbdata.filter {
                val name = it[RestaurantItemId.restaurant_name.name]!!
                name.contains(text, true)
            }.toMutableList()
        }

        recyclerAdapter.notifyDataSetChanged()
    }*/
}