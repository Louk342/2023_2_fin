package com.example.hungryguys.ui.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.ChatMeItemBinding
import com.example.hungryguys.databinding.ChatUserItemBinding

class ChattingAdapter(val data: MutableList<MutableMap<String, String>>) :
    RecyclerView.Adapter<ChattingAdapter.ChattingHolder>() {

    class ChattingHolder(
        val meChatBinding: ChatMeItemBinding? = null,
        val userChatBinding: ChatUserItemBinding? = null
    ) : RecyclerView.ViewHolder(meChatBinding?.root ?: userChatBinding?.root!!)

    // 내 채팅 이냐 상대방 채팅이냐에 따라 뷰 타입 처리
    override fun getItemViewType(position: Int): Int {
        return if (data[position][ChatItem.Chat_Type.name] == "me") {
            1
        } else {
            2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingHolder {
        // 내 채팅이냐 아니냐에 따라 다른 바인딩 적용
        return if (viewType == 1) {
            val binding = ChatMeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChattingHolder(meChatBinding = binding)
        } else {
            val binding = ChatUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ChattingHolder(userChatBinding = binding)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ChattingHolder, position: Int) {
        val chat = data[position][ChatItem.Chat.name]
        val chattime = data[position][ChatItem.Chat_Time.name]

        if (holder.meChatBinding != null) {
            holder.meChatBinding.chatText.text = chat
            holder.meChatBinding.chatTime.text = chattime
        } else {
            holder.userChatBinding!!.chatText.text = chat
            holder.userChatBinding.chatTime.text = chattime
        }
    }
}