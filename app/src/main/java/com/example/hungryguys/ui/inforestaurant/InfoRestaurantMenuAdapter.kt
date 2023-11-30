package com.example.hungryguys.ui.inforestaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.InfoRestaurantMenuItemBinding

class InfoRestaurantMenuAdapter(val data: MutableList<MutableMap<String, String>>) :
    RecyclerView.Adapter<InfoRestaurantMenuAdapter.InfoRestaurantMenuHolder>() {
    class InfoRestaurantMenuHolder(
        val binding: InfoRestaurantMenuItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        // 리사이클러뷰 이벤트 처리
        fun recyclerevent(position: Int) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRestaurantMenuHolder {
        val binding = InfoRestaurantMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InfoRestaurantMenuHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: InfoRestaurantMenuHolder, position: Int) {
        val foodname = data[position][InfoMenuItem.food_name.name]!!
        val fooddescription = data[position][InfoMenuItem.food_description.name]!!
        val foodprice = data[position][InfoMenuItem.food_price.name]!!

        holder.binding.apply {
            foodName.text = foodname
            foodDescription.text = fooddescription
            foodPrice.text = foodprice
        }

        holder.recyclerevent(position)
    }
}