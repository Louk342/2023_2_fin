package com.example.hungryguys.ui.inforestaurant

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hungryguys.databinding.DialogAddPartyBinding
import com.example.hungryguys.ui.chatting.ChattingActivity
import com.example.hungryguys.ui.searchparty.SearchPartyItemId

class InfoRestaurantPartyDialog: DialogFragment() {
    lateinit var binding: DialogAddPartyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = true
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding = DialogAddPartyBinding.inflate(inflater, container, false)

        // 취소이벤트
        binding.buttonCencel.setOnClickListener {
            dismiss()
        }

        binding.buttonOk.setOnClickListener {
            val roomname = binding.inputAddparty.text.toString()
            val restaurantname = (activity as InfoRestaurantActivity).restaurantname
            val restaurantid = (activity as InfoRestaurantActivity).restaurantid
            if (roomname.isEmpty()) {
                dismiss()
                return@setOnClickListener
            }

            val intent = Intent(context, ChattingActivity::class.java)
            intent.putExtra(SearchPartyItemId.party_name.name, roomname)
            intent.putExtra(SearchPartyItemId.party_location.name, restaurantname)
            intent.putExtra(SearchPartyItemId.party_location_id.name, restaurantid)
            intent.putExtra(SearchPartyItemId.party_id.name, "first")
            startActivity(intent)
            dismiss()
        }

        return binding.root
    }
}