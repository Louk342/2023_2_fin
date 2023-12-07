package com.example.hungryguys.ui.home

import android.annotation.SuppressLint
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import com.example.hungryguys.MainActivity
import com.example.hungryguys.R
import com.example.hungryguys.databinding.FragmentHomeBinding
import com.example.hungryguys.ui.inforestaurant.InfoRestaurantActivity
import com.example.hungryguys.ui.searchrestaurant.RestaurantItemId
import com.example.hungryguys.utills.ActivityUtills
import com.example.hungryguys.utills.GoogleLoginData
import com.example.hungryguys.utills.Request
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class HomeFragment : Fragment() {

    lateinit var homebinding: FragmentHomeBinding
    lateinit var groupId: String
    lateinit var groupLatLng: LatLng
    lateinit var activityUtills: ActivityUtills
    lateinit var storeId: String
    lateinit var storeName: String
    lateinit var storeLat: String
    lateinit var storeLng: String
    lateinit var storeStar: String
    lateinit var storeStarCount: String

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homebinding = FragmentHomeBinding.inflate(inflater, container, false)

        activityUtills = ActivityUtills(requireActivity())

        loadData()

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

    // DB에서 데이터 가져오기
    @SuppressLint("NotifyDataSetChanged")
    private fun loadData() {
        val userdataThread = Thread {
            val userdataJson =
                Request.reqget("${Request.REQUSET_URL}/email/${GoogleLoginData.email}")
                    ?: JSONArray()
            groupId = userdataJson.getJSONObject(0).getString("group_id")
        }
        userdataThread.start()
        userdataThread.join()

        val groupThread = Thread {
            val groupJson = Request.reqget("${Request.REQUSET_URL}/group") ?: JSONArray()

            for (i in 0..<groupJson.length()) {
                val json = groupJson.getJSONObject(i)

                if (json.getString("group_id") == groupId) {
                    groupLatLng = LatLng(json.getDouble("x"), json.getDouble("y"))
                }
            }
        }
        groupThread.start()
        groupThread.join()

        val randomThread = Thread {
            val randomJson = Request.reqget("${Request.REQUSET_URL}/rand/${groupId}") ?: JSONArray()
            storeId = randomJson.getJSONObject(0).getString("store_id")
            storeName = randomJson.getJSONObject(0).getString("store_name")
            storeLat = randomJson.getJSONObject(0).getString("x")
            storeLng = randomJson.getJSONObject(0).getString("y")
        }
        randomThread.start()
        randomThread.join()

        val menuThread = Thread {
            val menuJson = Request.reqget("${Request.REQUSET_URL}/menu/${storeId}") ?: JSONArray()
            val menuName = menuJson.getJSONObject(0).getString("name")
            val menuImg = menuJson.getJSONObject(0).getString("img")

            // 랜덤 메뉴 정보 변경
            homebinding.randomMenuTitle.text = menuName
            homebinding.randomMenuStore.text = storeName
            homebinding.randomMenuImg.visibility = View.VISIBLE
            setRestaurantImg(homebinding.randomMenuImg, menuImg).start()
            val restaurantLatLng = LatLng(storeLat.toDouble(), storeLng.toDouble())
            val distance = activityUtills.getDistance(groupLatLng, restaurantLatLng)
            homebinding.randomMenuLocation.text = distance
        }
        menuThread.start()
        menuThread.join()

        val reviewThread = Thread {
            val reviewJson =
                Request.reqget("${Request.REQUSET_URL}/review/${storeId}") ?: JSONArray()
            var count = 0
            var sum = 0.0
            for (i in 0..<reviewJson.length()) {
                val json = reviewJson.getJSONObject(i)
                count++
                sum += json.getDouble("star")
            }
            storeStar = String.format("%.1f", (sum / count))
            storeStarCount = count.toString()

        }
        reviewThread.start()
        reviewThread.join()

        // 식당 클릭 시 식당 정보 페이지로 이동
        homebinding.randomMenuLayout.setOnClickListener {
            val intent = Intent(context, InfoRestaurantActivity::class.java)

            //DB 에서 랜덤으로 식당 하나 받아오기
            intent.putExtra(RestaurantItemId.inforestaurant_id.name, storeId.toInt())
            intent.putExtra(RestaurantItemId.restaurant_name.name, storeName)
            intent.putExtra(RestaurantItemId.restaurant_star.name, storeStar)
            intent.putExtra(RestaurantItemId.restaurant_star_count.name, storeStarCount)
            intent.putExtra(RestaurantItemId.restaurant_we.name, storeLat)
            intent.putExtra(RestaurantItemId.restaurant_ky.name, storeLng)
            context?.startActivity(intent)
        }
    }

    // 웹 이미지 설정 함수 (비동기)
    private fun setRestaurantImg(imageView: ImageView, url: String): Thread {
        return  Thread {
            val connection = URL(url).openConnection() as HttpURLConnection
            val bitmap = BitmapFactory.decodeStream(connection.inputStream)
            activity?.runOnUiThread {
                imageView.setImageBitmap(bitmap)
            }
        }
    }
}