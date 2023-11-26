package com.example.hungryguys.ui.tutorial

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.TypedValue
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.hungryguys.R
import com.example.hungryguys.databinding.ActivityTutorialBinding
import com.example.hungryguys.utills.ActivityUtills

class TutorialActivity : AppCompatActivity() {

    private lateinit var layouts: Array<Int>
    private lateinit var dots: Array<TextView?>
    private lateinit var binding: ActivityTutorialBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)

        // 상단 상테바, 하단 내비게이션 투명화
        val activityUtills = ActivityUtills(this)
        activityUtills.setStatusBarTransparent(binding.layoutDotsView)

        binding.layoutDots

        layouts = arrayOf(
            R.layout.tutorial_page1,
            R.layout.tutorial_page2,
            R.layout.tutorial_page3
        )
        addDots()

        val viewpager = binding.viewPager
        val viewpagerAdapter = TutorialAdapter(this, layouts)
        viewpager.adapter = viewpagerAdapter
        viewpager.registerOnPageChangeCallback(viewPagerEvent)
        setContentView(binding.root)
    }

    private val viewPagerEvent = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            changeDots(position)
        }
    }

    // 하단 점 주가
    private fun addDots() {
        val dotLayout = binding.layoutDots
        dots = arrayOfNulls(layouts.size)

        for (i in dots.indices) {
            dots[i] = TextView(this)
            dots[i]?.text = Html.fromHtml("&#8226;", 1)
            dots[i]?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50F)
            dots[i]?.setTextColor(Color.parseColor("#FFFFFF"))
            dotLayout.addView(dots[i])
        }

        dots[0]?.setTextColor(Color.parseColor("#4200FF"))
    }

    // 하단 점 바꾸기
    private fun changeDots(position: Int) {
        dots.forEach {
            it?.setTextColor(Color.parseColor("#FFFFFF"))
        }

        dots[position]?.setTextColor(Color.parseColor("#4200FF"))
    }
}