package com.example.hungryguys.ui.searchrestaurant

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.SearchRestaurantItemBinding
import com.example.hungryguys.ui.inforestaurant.InfoRestaurantActivity

class SearchRestaurantAdapter(
    val data: MutableList<MutableMap<String, String>>,
    val categoryImageMap: MutableMap<String, Int>   // 카테고리별 이미지 id저장한 map 객체
) :
    RecyclerView.Adapter<SearchRestaurantAdapter.SearchRestaurantHolder>() {

    class SearchRestaurantHolder(
        val binding: SearchRestaurantItemBinding,
        private val context: Context,
        private val data: MutableList<MutableMap<String, String>>
    ) : RecyclerView.ViewHolder(binding.root) {

        // 리사이클러뷰 이벤트 처리
        fun recyclerevent(position: Int) {
            binding.root.setOnClickListener {
                val restaurantname = data[position][RestaurantItemId.restaurant_name.name]
                val restaurantstar = data[position][RestaurantItemId.restaurant_star.name]
                val restaurantstarcount = data[position][RestaurantItemId.restaurant_star_count.name]
                val restaurantwe = data[position][RestaurantItemId.restaurant_we.name]
                val restaurantky = data[position][RestaurantItemId.restaurant_ky.name]

                val intent = Intent(context, InfoRestaurantActivity::class.java)
                intent.putExtra(RestaurantItemId.inforestaurant_id.name, position)  //추후 DB 식별값으로 변경
                intent.putExtra(RestaurantItemId.restaurant_name.name, restaurantname)
                intent.putExtra(RestaurantItemId.restaurant_star.name, restaurantstar)
                intent.putExtra(RestaurantItemId.restaurant_star_count.name, restaurantstarcount)
                intent.putExtra(RestaurantItemId.restaurant_we.name, restaurantwe)
                intent.putExtra(RestaurantItemId.restaurant_ky.name, restaurantky)

                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRestaurantHolder {
        val binding =
            SearchRestaurantItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchRestaurantHolder(binding, parent.context, data)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchRestaurantHolder, position: Int) {
        val restaurantname = data[position][RestaurantItemId.restaurant_name.name]!!
        val restaurantcategory = data[position][RestaurantItemId.restaurant_category.name]!!
        val restaurantdescription = data[position][RestaurantItemId.restaurant_description.name]!!
        val restaurantstar = data[position][RestaurantItemId.restaurant_star.name]!!
        val restaurantky = data[position][RestaurantItemId.restaurant_ky.name]!!
        val restaurantwe = data[position][RestaurantItemId.restaurant_we.name]!!

        holder.binding.apply {
            restaurantName.text = restaurantname
            restaurantDescription.text = restaurantdescription
            restaurantStar.text = restaurantstar
            restaurantDistance.text = setdescription(restaurantky.toInt(), restaurantwe.toInt())
            setCategoryImage(this, restaurantcategory)
        }

        holder.recyclerevent(position)
    }

    // 식당과 유저사이 거리 나타네는 함수
    private fun setdescription(resKy: Int, resWe: Int): String {
        return (resWe - resKy).toString()
    }

    //미리 보기 화면 설정
    private fun setCategoryImage(binding: SearchRestaurantItemBinding, key: String) {
        val imageid = categoryImageMap[key]
        if (imageid == null) {
            binding.categoryText.text = key
            binding.categoryText.visibility = View.VISIBLE
        } else {
            binding.categoryImage.setImageResource(imageid)
            binding.categoryImageLayout.setBackgroundResource(0)
            binding.categoryImage.visibility = View.VISIBLE
        }
    }
}