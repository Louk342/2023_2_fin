package com.example.hungryguys.ui.inforestaurant

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hungryguys.databinding.ActivityInfoRestaurantBinding
import com.example.hungryguys.utills.ActivityUtills
import com.google.android.material.tabs.TabLayoutMediator

class InfoRestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInfoRestaurantBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 상단 상테바, 하단 내비게이션 투명화 및 보정
        val activityUtills = ActivityUtills(this)
        activityUtills.setStatusBarTransparent()
        activityUtills.setStatusBarPadding(binding.root)

        // 툴바 설정
        val toolbar = binding.toolbar
        toolbar.setContentInsetsRelative(0, 0)
        setSupportActionBar(binding.toolbar)

        // 툴바에 백 버튼 만들기
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 뷰 패이저 어뎁터 설정 및 탭 구성
        val pagerAdapter = RestaurantMenuAdapter(this, binding)
        binding.menuPager.adapter = pagerAdapter
        pagerAdapter.setTabLayout()

        // 파티생성 추가 텍스트 이벤트
        binding.addPartyButton.setOnClickListener {
            InfoRestaurantPartyDialog().show(supportFragmentManager, "파티추가")
        }
    }

    // 백버튼 클릭 이벤트
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}

// 탭 레이아웃 구성 클래스
class RestaurantMenuAdapter(activity: FragmentActivity, val binding: ActivityInfoRestaurantBinding): FragmentStateAdapter(activity)  {
    override fun getItemCount() = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InfoRestaurantMenuFragment()
            1 -> InfoRestaurantReviewFragment()
            else -> Fragment()
        }
    }

    fun setTabLayout() {
        TabLayoutMediator(binding.menuLayout, binding.menuPager) { tab, position ->
            tab.text = when(position) {
                0 -> "메뉴"
                1 -> "리뷰"
                else -> ""
            }
        }.attach()
    }
}