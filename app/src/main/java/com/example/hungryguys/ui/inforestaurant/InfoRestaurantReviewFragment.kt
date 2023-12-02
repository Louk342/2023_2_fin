package com.example.hungryguys.ui.inforestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.FragmentInfoRestaurantReviewBinding

enum class InfoReviewItem {
    /** 식당 식별값 (DB 쪽에서 )*/
    inforestaurant_id,
    /** 유저 이름 */
    user_name,
    /** 리뷰내용 */
    review_text,
    /** 평점 */
    restaurant_star
}
class InfoRestaurantReviewFragment : Fragment() {
    companion object {
        var dbdata: MutableList<MutableMap<String, String>> = mutableListOf()
    }

    lateinit var recyclerAdapter: InfoRestaurantReviewAdapter
    lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentInfoRestaurantReviewBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener {
            InfoRestaurantReviewDialog().show(requireActivity().supportFragmentManager, "리뷰작성")
        }

        recyclerView = binding.inforeviewRecycler
        recyclerAdapter = InfoRestaurantReviewAdapter(dbdata, requireActivity())
        recyclerView.adapter = recyclerAdapter

        return binding.root
    }
}