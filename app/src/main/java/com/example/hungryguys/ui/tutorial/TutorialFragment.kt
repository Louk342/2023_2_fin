package com.example.hungryguys.ui.tutorial

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hungryguys.MainActivity
import com.example.hungryguys.R
import com.example.hungryguys.ui.auth.AuthActivity
import com.example.hungryguys.utills.GoogleLoginData

// Tutorial 어뎁터
class TutorialAdapter(act: FragmentActivity, private val layouts: Array<Int>): FragmentStateAdapter(act) {
    override fun getItemCount(): Int {
        return layouts.size
    }

    override fun createFragment(position: Int): Fragment {
        return TutorialFragment().newInstance(layouts[position])
    }
}

/// 각각 페이지 프래그먼트
class TutorialFragment : Fragment() {

    fun newInstance(page: Int): TutorialFragment {
        val args = Bundle()
        args.putInt("tutorial_page", page)
        arguments = args
        return this
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val page = this.arguments?.getInt("tutorial_page")
        val view = inflater.inflate(page!!, container, false)

        //마지막 페이지면 버튼 시작하기 버튼 누르면 시작되게
        if (page == R.layout.tutorial_page3) {
            val button = view.findViewById<Button>(R.id.startBT)
            button.setOnClickListener {

                // 처음시작 여부를 false로
                val localprf = PreferenceManager.getDefaultSharedPreferences(requireContext())
                val editprf = localprf.edit()
                editprf.putBoolean("isfirst", false)
                editprf.apply()

                if (GoogleLoginData.checkAuth()) {
                    // 로그인 상태일시
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                } else {
                    // 비 로그인 상태일시
                    startActivity(Intent(activity, AuthActivity::class.java))
                    activity?.finish()
                }
            }
        }

        return view
    }
}