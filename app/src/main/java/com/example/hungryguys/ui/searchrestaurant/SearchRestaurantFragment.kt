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
import com.example.hungryguys.utills.ActivityUtills
import com.example.hungryguys.utills.GoogleLoginData
import com.example.hungryguys.utills.Request
import com.google.android.gms.maps.model.LatLng
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
    /** 식당과 그룹사이에 거리*/
    restaurant_distance
}

class SearchRestaurantFragment : Fragment() {
    lateinit var binding: FragmentSearchRestaurantBinding
    private lateinit var recyclerAdapter: SearchRestaurantAdapter
    private lateinit var activityUtills: ActivityUtills
    private lateinit var dbdata: MutableList<MutableMap<String, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchRestaurantBinding.inflate(inflater, container, false)
        val searchtext = (activity as MainActivity).actionbarView.searchText

        activityUtills = ActivityUtills(requireActivity())

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
            val userdataJson = Request.reqget("${Request.REQUSET_URL}/email/${GoogleLoginData.email}") ?: JSONArray()
            val groupId = userdataJson.getJSONObject(0).getString("group_id")

            val groupJson = Request.reqget("${Request.REQUSET_URL}/getGroup/${groupId}") ?: JSONArray()
            val getGroupX = groupJson.getJSONObject(0).getString("x").toDouble()
            val getGroupY = groupJson.getJSONObject(0).getString("y").toDouble()

            //그룹위치
            val grouploc = LatLng(getGroupX, getGroupY)
            val restaurantJson = Request.reqget("${Request.REQUSET_URL}/store/${groupId}") ?: JSONArray()

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

                // 식당 위치
                val Restaurantloc = LatLng(json.getString("x").toDouble(), json.getString("y").toDouble())
                val distance =activityUtills.getDistance(grouploc, Restaurantloc)

                val data= mutableMapOf(
                    RestaurantItemId.inforestaurant_id.name to json.getString("store_id"),
                    RestaurantItemId.restaurant_name.name to json.getString("store_name"),
                    RestaurantItemId.restaurant_img.name to json.getString("img"),
                    RestaurantItemId.restaurant_star.name to storeStar,
                    RestaurantItemId.restaurant_star_count.name to storeStarCount,
                    RestaurantItemId.restaurant_description.name to json.getString("store_kind"),
                    RestaurantItemId.restaurant_we.name to json.getString("x"),
                    RestaurantItemId.restaurant_ky.name to json.getString("y"),
                    RestaurantItemId.restaurant_distance.name to distance
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