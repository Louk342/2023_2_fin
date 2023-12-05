package com.example.hungryguys.ui.inforestaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.R
import com.example.hungryguys.databinding.InfoRestaurantMenuItemBinding
import com.example.hungryguys.databinding.InfoRestaurantReviewItemBinding

// 메뉴, 리뷰 리사이클러 처리
class InfoRestaurantTabAdapter (
    val data: MutableList<MutableMap<String, String>>,
    val recyclertype: String
) :
    RecyclerView.Adapter<InfoRestaurantTabAdapter.InfoRestaurantHolder>() {
    class InfoRestaurantHolder(
        val menuBinding: InfoRestaurantMenuItemBinding? = null,
        val reviewBinding: InfoRestaurantReviewItemBinding? = null
    ) : RecyclerView.ViewHolder(menuBinding?.root ?: reviewBinding?.root!!)

    /* menu: viewType = 1 review: viewType = 2 */
    override fun getItemViewType(position: Int): Int {
        return if (recyclertype == "menu") 1
        else 2
    }

    // 뷰타입에 따라 메뉴 아이템, 리뷰 아이템 처리

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoRestaurantHolder {
        return if (viewType == 1) {
            val binding = InfoRestaurantMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            InfoRestaurantHolder(menuBinding = binding)
        } else {
            val binding = InfoRestaurantReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            InfoRestaurantHolder(reviewBinding = binding)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: InfoRestaurantHolder, position: Int) {
        if (holder.menuBinding != null) {
            /* 메뉴 아이템 */
            val foodname = data[position][InfoMenuItem.food_name.name]!!
            val fooddescription = data[position][InfoMenuItem.food_description.name]!!
            val foodprice = data[position][InfoMenuItem.food_price.name]!!

            holder.menuBinding.apply {
                foodImg.visibility = View.VISIBLE
                if (position == 0) {
                    foodImg.setImageResource(R.drawable.coungyear_menu_1)
                } else {
                    foodImg.setImageResource(R.drawable.coungyear_menu_2)
                }

                foodName.text = foodname
                foodDescription.text = fooddescription
                foodPrice.text = foodprice
            }
        } else {
            /* 리뷰 아이템 */
            val username = data[position][InfoReviewItem.user_name.name]
            val restaurantstar = data[position][InfoReviewItem.restaurant_star.name]
            val reviewtext = data[position][InfoReviewItem.review_text.name]

            holder.reviewBinding!!.apply {
                userName.text = username
                restaurantStar.text = restaurantstar
                reviewText.text = reviewtext
            }
        }
    }
}