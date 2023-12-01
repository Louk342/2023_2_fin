package com.example.hungryguys.ui.inforestaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.hungryguys.databinding.DialogAddPartyBinding

class InfoRestaurantPartyDialog: DialogFragment() {
    lateinit var binding: DialogAddPartyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddPartyBinding.inflate(inflater, container, false)
        return binding.root
    }
}