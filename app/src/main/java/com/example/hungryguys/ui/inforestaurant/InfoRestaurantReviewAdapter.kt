package com.example.hungryguys.ui.inforestaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.InfoRestaurantReviewItemBinding

class InfoRestaurantReviewAdapter(val data: MutableList<MutableMap<String, String>>, val activity: FragmentActivity) : RecyclerView.Adapter<InfoRestaurantReviewAdapter.InfoRestaurantReviewHolder>() {

    class InfoRestaurantReviewHolder(
        val binding: InfoRestaurantReviewItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        // 리사이클러뷰 이벤트 처리
        fun recyclerevent(position: Int) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRestaurantReviewHolder {
        val binding = InfoRestaurantReviewItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InfoRestaurantReviewHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: InfoRestaurantReviewHolder, position: Int) {
        val currid = (activity as InfoRestaurantActivity).restaurantid //현재 액티비티에 식당id
        val saveid = data[position][InfoReviewItem.inforestaurant_id.name]?.toInt() //리스트에 저장된 id

        val username = data[position][InfoReviewItem.user_name.name]
        val restaurantstar = data[position][InfoReviewItem.restaurant_star.name]
        val reviewtext = data[position][InfoReviewItem.review_text.name]

        // 지금은 모든 요소가 해당 함수를 거치기에 아무리 처리해도 전부뜸
        holder.binding.apply {
            if (currid == saveid )
            userName.text = username
            restaurantStar.text = restaurantstar
            reviewText.text = reviewtext
        }

        holder.recyclerevent(position)
    }
}