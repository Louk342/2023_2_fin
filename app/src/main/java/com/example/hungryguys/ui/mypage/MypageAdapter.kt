package com.example.hungryguys.ui.mypage

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.FragmentMypageBinding
import com.example.hungryguys.databinding.MypageChatItemBinding
import com.example.hungryguys.ui.chatting.ChattingActivity
import com.example.hungryguys.ui.searchparty.SearchPartyItemId

class MypageAdapter(
    val data: MutableList<MutableMap<String, String>>,
    val mainbinding: FragmentMypageBinding
) :
    RecyclerView.Adapter<MypageAdapter.MypageHolder>() {

    class MypageHolder(
        val binding: MypageChatItemBinding,
        private val context: Context,
        val data: MutableList<MutableMap<String, String>>
    ) :
        RecyclerView.ViewHolder(binding.root) {

        // 리사이클러뷰 이벤트 처리
        fun recyclerevent(position: Int) {
            val roomid = data[position][MypageChatItemId.room_id.name]!!
            val roomtitle = data[position][MypageChatItemId.room_title.name]!!
            val restaurantname = data[position][MypageChatItemId.restaurant_name.name]!!
            val restaurantid = data[position][MypageChatItemId.restaurant_id.name]!!

            binding.root.setOnClickListener {
                val intent = Intent(context, ChattingActivity::class.java)
                intent.putExtra(SearchPartyItemId.party_name.name, roomtitle)
                intent.putExtra(SearchPartyItemId.party_location.name, restaurantname)
                intent.putExtra(SearchPartyItemId.party_location_id.name, restaurantid)
                intent.putExtra(SearchPartyItemId.party_id.name, roomid)
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MypageHolder {
        val binding =
            MypageChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MypageHolder(binding, parent.context, data)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MypageHolder, position: Int) {
        val roomtitle = data[position][MypageChatItemId.room_title.name]!!
        val restaurantname = data[position][MypageChatItemId.restaurant_name.name]!!
        val connectpeople = data[position][MypageChatItemId.connect_people.name]!!
        val lastchattime = data[position][MypageChatItemId.last_chat_time.name]!!

        holder.binding.apply {
            roomTitle.text = roomtitle
            restaurantName.text = restaurantname
            connectPeople.text = connectpeople
            lastChatTime.text = lastchattime
        }

        holder.recyclerevent(position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        // 채팅방이 없으면<현재 참여중인 채팅방이 없습니다.> 텍스트 보여주게
        if (data.size == 0) {
            mainbinding.noneChatText.visibility = View.VISIBLE
            mainbinding.myChatRecycler.visibility = View.GONE
        } else {
            mainbinding.noneChatText.visibility = View.GONE
            mainbinding.myChatRecycler.visibility = View.VISIBLE
        }
    }
}