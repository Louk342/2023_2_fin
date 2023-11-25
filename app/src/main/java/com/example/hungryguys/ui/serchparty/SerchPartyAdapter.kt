package com.example.hungryguys.ui.serchparty

import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.SerchPartyItemBinding

class SerchPartyAdapter(private val data: MutableList<MutableMap<String, String>>) :
    RecyclerView.Adapter<SerchPartyAdapter.SerchPartyHolder>() {

    class SerchPartyHolder(val binding: SerchPartyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // 여기에는 이벤트 처리 들어갈꺼
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerchPartyHolder {
        val binding = SerchPartyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SerchPartyHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SerchPartyHolder, position: Int) {
        val text = data[position]["text"]!!

        holder.binding.partyname.text = text
    }
}