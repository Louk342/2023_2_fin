package com.example.hungryguys.ui.searchparty


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.SearchPartyItemBinding

class SearchPartyAdapter(val data: MutableList<MutableMap<String, String>>) :
    RecyclerView.Adapter<SearchPartyAdapter.SerchPartyHolder>() {

    class SerchPartyHolder(val binding: SearchPartyItemBinding, private val context: Context) : RecyclerView.ViewHolder(binding.root) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerchPartyHolder {
        val binding = SearchPartyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SerchPartyHolder(binding, parent.context)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: SerchPartyHolder, position: Int) {
        val partyname = data[position]["party_name"]!!  //파티이름
        val partylocation = data[position]["party_location"]!!  //주소
        val partyperson = "${data[position]["party_person"]!!}명 참여중"    //접속인원

        holder.binding.partyName.text = partyname
        holder.binding.partyLocation.text = partylocation
        holder.binding.partyPerson.text = partyperson

        holder.recyclerevent(position)
    }
}