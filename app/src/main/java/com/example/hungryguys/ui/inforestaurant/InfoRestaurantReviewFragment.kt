package com.example.hungryguys.ui.inforestaurant

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.FragmentInfoRestaurantReviewBinding
import com.example.hungryguys.utills.Request
import org.json.JSONArray

enum class InfoReviewItem {
    /** 유저 이름 */
    user_name,
    /** 리뷰내용 */
    review_text,
    /** 평점 */
    restaurant_star
}

class InfoRestaurantReviewFragment : Fragment() {
    lateinit var recyclerAdapter: InfoRestaurantTabAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var dbdata: MutableList<MutableMap<String, String>>

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInfoRestaurantReviewBinding.inflate(inflater, container, false)
        val selectid =  (activity as InfoRestaurantActivity).restaurantid   //현재 선택된 아이템
        dbdata = mutableListOf()

        recyclerView = binding.inforeviewRecycler
        recyclerAdapter = InfoRestaurantTabAdapter(dbdata, "review", requireActivity())
        recyclerView.adapter = recyclerAdapter

        binding.fab.setOnClickListener {
            InfoRestaurantReviewDialog().show(requireActivity().supportFragmentManager, "리뷰작성")
        }

        // 리뷰 데이터 불러오기
        Thread {
            val reivewJson = Request.reqget("${Request.REQUSET_URL}/review/${selectid}") ?: JSONArray()

            for (i in 0..<reivewJson.length()) {
                val json = reivewJson.getJSONObject(i)
                val usernameJson = Request.reqget("${Request.REQUSET_URL}/user/${json.getString("user_id")}") ?: JSONArray()
                val data = mutableMapOf(
                    InfoReviewItem.review_text.name to json.getString("reivew_context"),
                    InfoReviewItem.restaurant_star.name to json.getString("star"),
                    InfoReviewItem.user_name.name to usernameJson.getJSONObject(0).getString("user_name")
                )
                dbdata.add(data)
            }

            activity?.runOnUiThread {
                recyclerAdapter.notifyDataSetChanged()
            }
        }.start()

        return binding.root
    }
}