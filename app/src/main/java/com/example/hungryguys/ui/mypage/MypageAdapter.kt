package com.example.hungryguys.ui.mypage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.FragmentMypageBinding
import com.example.hungryguys.databinding.MypageChatItemBinding

class MypageAdapter(
    val data: MutableList<MutableMap<String, String>>,
    val mainbinding: FragmentMypageBinding
) :
    RecyclerView.Adapter<MypageAdapter.MypageHolder>() {

    class MypageHolder(val binding: MypageChatItemBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        // 리사이클러뷰 이벤트 처리
        fun recyclerevent(position: Int) {
            binding.root.setOnClickListener {
                Toast.makeText(context, "채팅방 $position 클릭", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MypageHolder {
        val binding =
            MypageChatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MypageHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MypageHolder, position: Int) {
        val roomtitle = data[position][MypageChatItemId.room_title.name]!!
        val lastchat = data[position][MypageChatItemId.last_chat.name]!!
        val connectpeople = data[position][MypageChatItemId.connect_people.name]!!
        val lastchattime = data[position][MypageChatItemId.last_chat_time.name]!!

        holder.binding.apply {
            roomTitle.text = roomtitle
            lastChat.text = lastchat
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