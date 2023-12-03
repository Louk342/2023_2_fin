package com.example.hungryguys.ui.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.ChatInfoNavItemBinding

class ChattingNavAdapter(val data: MutableList<MutableMap<String, String>>): RecyclerView.Adapter<ChattingNavAdapter.ChattingNavHolder>() {
    class ChattingNavHolder(val binding: ChatInfoNavItemBinding) :  RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChattingNavHolder {
        val binding = ChatInfoNavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChattingNavHolder(binding)
    }

    override fun getItemCount(): Int {
       return data.size
    }

    override fun onBindViewHolder(holder: ChattingNavHolder, position: Int) {
        val username = data[position][ChatItem.User_Name.name]

        holder.binding.userName.text = username
    }
}