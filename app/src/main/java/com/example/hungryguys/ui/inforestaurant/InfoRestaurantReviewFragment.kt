package com.example.hungryguys.ui.inforestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    lateinit var recyclerAdapter: InfoRestaurantReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInfoRestaurantReviewBinding.inflate(inflater, container, false)
        val dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
        val data1 = mutableMapOf(
            InfoReviewItem.user_name.name  to "유저닉네임1",
            InfoReviewItem.restaurant_star.name  to "평점",
            InfoReviewItem.review_text.name  to "리뷰텍스트"
        )
        val data2 = mutableMapOf(
            InfoReviewItem.user_name.name  to "유저닉네임2",
            InfoReviewItem.restaurant_star.name  to "평점",
            InfoReviewItem.review_text.name  to "리뷰텍스트"
        )

        binding.fab.setOnClickListener {
            InfoRestaurantReviewDialog().show(requireActivity().supportFragmentManager, "리뷰작성")
        }

        dbdata.add(data1)
        dbdata.add(data2)
        recyclerAdapter = InfoRestaurantReviewAdapter(dbdata)
        binding.inforeviewRecycler.adapter = recyclerAdapter

        return binding.root
    }
}