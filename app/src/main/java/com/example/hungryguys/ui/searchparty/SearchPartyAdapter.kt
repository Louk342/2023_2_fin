package com.example.hungryguys.ui.searchparty


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hungryguys.databinding.SearchPartyItemBinding
import com.example.hungryguys.ui.chatting.ChattingActivity

class SearchPartyAdapter(val data: MutableList<MutableMap<String, String>>) :
    RecyclerView.Adapter<SearchPartyAdapter.SearchPartyHolder>() {

    class SearchPartyHolder(
        val binding: SearchPartyItemBinding,
        private val context: Context,
        private val data: MutableList<MutableMap<String, String>>
    ) : RecyclerView.ViewHolder(binding.root) {

        // 리사이클러뷰 이벤트 처리
        fun recyclerevent(position: Int) {
            val partyname = data[position][SearchPartyItemId.party_name.name]!!
            val partylocation = data[position][SearchPartyItemId.party_location.name]!!
            val partylocationid = data[position][SearchPartyItemId.party_location_id.name]!!
            val partyid = data[position][SearchPartyItemId.party_id.name]!!

            val partyclickEV = View.OnClickListener {
                val intent = Intent(context, ChattingActivity::class.java)
                intent.putExtra(SearchPartyItemId.party_name.name, partyname)
                intent.putExtra(SearchPartyItemId.party_location.name, partylocation)
                intent.putExtra(SearchPartyItemId.party_location_id.name, partylocationid)
                intent.putExtra(SearchPartyItemId.party_id.name, partyid)
                context.startActivity(intent)
            }

            binding.root.setOnClickListener(partyclickEV)
            binding.partyJoinBt.setOnClickListener(partyclickEV)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPartyHolder {
        val binding =
            SearchPartyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchPartyHolder(binding, parent.context, data)
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