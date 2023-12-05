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
                InfoReviewItem.user_name.name to "클린홈케어"
            )
            val data2 = mutableMapOf(
                InfoReviewItem.review_text.name to "맛있게 잘 먹었습니다~",
                InfoReviewItem.restaurant_star.name to "5.0",
                InfoReviewItem.user_name.name to "번데기"
            )
            val data3 = mutableMapOf(
                InfoReviewItem.review_text.name to "좋습니다",
                InfoReviewItem.restaurant_star.name to "5.0",
                InfoReviewItem.user_name.name to "곰꾹이"
            )
            val data4 = mutableMapOf(
                InfoReviewItem.review_text.name to "평소에도 많이 시켜먹습니다",
                InfoReviewItem.restaurant_star.name to "4.0",
                InfoReviewItem.user_name.name to "내가내다"
            )
            val data5 = mutableMapOf(
                InfoReviewItem.review_text.name to "떡이 좀 길어요",
                InfoReviewItem.restaurant_star.name to "3.0",
                InfoReviewItem.user_name.name to "띠또이"
            )
            dbdata.add(data1)
            dbdata.add(data2)
            dbdata.add(data3)
            dbdata.add(data4)
            dbdata.add(data5)
        }

        // 맘스터치 인경우
        if (selectid == 1) {
            val data1 = mutableMapOf(
                InfoReviewItem.review_text.name to "다른 버거집 보다 가성비가 좋아요",
                InfoReviewItem.restaurant_star.name to "5.0",
                InfoReviewItem.user_name.name to "쏘옹"
            )
            val data2 = mutableMapOf(
                InfoReviewItem.review_text.name to "빠르고 간편하게 먹기 좋네요~",
                InfoReviewItem.restaurant_star.name to "5.0",
                InfoReviewItem.user_name.name to "맛있게먹을게요오"
            )
            val data3 = mutableMapOf(
                InfoReviewItem.review_text.name to "좋습니다",
                InfoReviewItem.restaurant_star.name to "4.0",
                InfoReviewItem.user_name.name to "외향적인연어포케"
            )
            val data4 = mutableMapOf(
                InfoReviewItem.review_text.name to "가격이좀 많이 올랐네요",
                InfoReviewItem.restaurant_star.name to "4.0",
                InfoReviewItem.user_name.name to "앤디"
            )
            val data5 = mutableMapOf(
                InfoReviewItem.review_text.name to "점심시간에는 주문이 너무 안나오네요",
                InfoReviewItem.restaurant_star.name to "4.0",
                InfoReviewItem.user_name.name to "헝그리"
            )
            dbdata.add(data1)
            dbdata.add(data2)
            dbdata.add(data3)
            dbdata.add(data4)
            dbdata.add(data5)
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