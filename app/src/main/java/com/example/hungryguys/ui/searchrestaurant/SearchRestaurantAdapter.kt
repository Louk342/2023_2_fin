package com.example.hungryguys.ui.searchrestaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.SearchRestaurantItemBinding

class SearchRestaurantAdapter(val data: MutableList<MutableMap<String, String>>) :
    RecyclerView.Adapter<SearchRestaurantAdapter.SearchRestaurantHolder>() {

    class SearchRestaurantHolder(
        val binding: SearchRestaurantItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        // 리사이클러뷰 이벤트 처리
        fun recyclerevent(position: Int) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRestaurantHolder {
        val binding =
            SearchRestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchRestaurantHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchRestaurantHolder, position: Int) {
        val restauranttitle = data[position]["restaurant_title"]!!  //파티이름
        holder.binding.restaurantTitle.text = restauranttitle

        holder.recyclerevent(position)
    }
}