package com.example.hungryguys.ui.inforestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.FragmentInfoRestaurantReviewBinding

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInfoRestaurantReviewBinding.inflate(inflater, container, false)
        val selectid =  (activity as InfoRestaurantActivity).restaurantid   //현재 선택된 아이템
        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()

        // 청년다방인 경우
        if (selectid == 0) {
            val data1 = mutableMapOf(
                InfoReviewItem.review_text.name to "청년다방은 역시 맛있네요",
                InfoReviewItem.restaurant_star.name to "5.0",
                InfoReviewItem.user_name.name to "유저1"
            )
            val data2 = mutableMapOf(
                InfoReviewItem.review_text.name to "떡이 좀 길어요",
                InfoReviewItem.restaurant_star.name to "4.0",
                InfoReviewItem.user_name.name to "유저2"
            )
            dbdata.add(data1)
            dbdata.add(data2)
        }

        // 푸라닭 인경우
        if (selectid == 1) {
            val data1 = mutableMapOf(
                InfoReviewItem.review_text.name to "맛있어용~",
                InfoReviewItem.restaurant_star.name to "5.0",
                InfoReviewItem.user_name.name to "유저1"
            )
            val data2 = mutableMapOf(
                InfoReviewItem.review_text.name to "나름 가성비 있네요",
                InfoReviewItem.restaurant_star.name to "5.0",
                InfoReviewItem.user_name.name to "유저2"
            )
            dbdata.add(data1)
            dbdata.add(data2)
        }

        binding.fab.setOnClickListener {
            InfoRestaurantReviewDialog().show(requireActivity().supportFragmentManager, "리뷰작성")
        }

        recyclerView = binding.inforeviewRecycler
        recyclerAdapter = InfoRestaurantTabAdapter(dbdata, "review")
        recyclerView.adapter = recyclerAdapter

        return binding.root
    }
}