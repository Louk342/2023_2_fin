package com.example.hungryguys.ui.inforestaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.hungryguys.databinding.FragmentInfoRestaurantMenuBinding
import com.example.hungryguys.utills.Request
import org.json.JSONArray


enum class InfoMenuItem {
    /** 음식 이름 */
    food_name,
    /** 음식 설명 */
    food_description,
    /** 음식가격 */
    food_price,
    /** 음식사진 경로 */
    food_img
}
class InfoRestaurantMenuFragment : Fragment() {

    lateinit var recyclerAdapter: InfoRestaurantTabAdapter
    lateinit var binding: FragmentInfoRestaurantMenuBinding
    lateinit var dbdata: MutableList<MutableMap<String, String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoRestaurantMenuBinding.inflate(inflater, container, false)
        dbdata = mutableListOf()

        //DB 데이터 가져오기
        addData().start()

        return binding.root
    }

    // DB에서 데이터 가져오기
    private fun addData(): Thread {
        val selectid =  (activity as InfoRestaurantActivity).restaurantid   //현재 선택된 아이템

        return Thread {
            val restaurantJson = Request.reqget("${Request.REQUSET_URL}/menu/${selectid}") ?: JSONArray()
            //추후 그룹 아이디를 db에서

            for (i in 0..< restaurantJson.length()) {
                val json = restaurantJson.getJSONObject(i)

                val data= mutableMapOf(
                    InfoMenuItem.food_name.name to json.getString("name"),
                    InfoMenuItem.food_description.name to json.getString("des"),
                    InfoMenuItem.food_price.name to json.getString("price"),
                    InfoMenuItem.food_img.name to json.getString("img"),
                )
                dbdata.add(data)
            }

            activity?.runOnUiThread {
                recyclerAdapter = InfoRestaurantTabAdapter(dbdata, "menu", requireActivity())
                binding.infomenuRecycler.adapter = recyclerAdapter
            }
        }
    }
}