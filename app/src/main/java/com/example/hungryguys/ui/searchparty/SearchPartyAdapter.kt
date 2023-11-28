package com.example.hungryguys.ui.searchparty


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.SearchPartyItemBinding

class SearchPartyAdapter(val data: MutableList<MutableMap<String, String>>) :
    RecyclerView.Adapter<SearchPartyAdapter.SearchPartyHolder>() {

    class SearchPartyHolder(val binding: SearchPartyItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
        // 리사이클러뷰 이벤트 처리
        fun recyclerevent(position: Int) {
            val partyclickEV = View.OnClickListener {
                // 채팅창으로 이동되게
                Toast.makeText(context, "$position 선택", Toast.LENGTH_SHORT).show()
            }

            binding.root.setOnClickListener(partyclickEV)
            binding.partyJoinBt.setOnClickListener(partyclickEV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPartyHolder {
        val binding = SearchPartyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchPartyHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SearchPartyHolder, position: Int) {
        val partyname = data[position][SearchPartyItemId.party_name.name]!!
        val partylocation = data[position][SearchPartyItemId.party_location.name]!!
        val partyperson = "${data[position][SearchPartyItemId.party_person.name]!!}명 참여중"

        holder.binding.apply {
            partyName.text = partyname
            partyLocation.text = partylocation
            partyPerson.text = partyperson
        }

        holder.recyclerevent(position)
    }
}