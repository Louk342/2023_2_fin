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
        val roomtitle = data[position]["room_title"]!!  //채팅방 이름
        val lastchat = data[position]["last_chat"]!!    //마지막 채팅
        val connectpeople = data[position]["connect_people"]!!  //접속자 수
        val lastchattime = data[position]["last_chat_time"]!!   //마지막 채팅 시간

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