package com.letscamping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.letscamping.databinding.ActivityMain2Binding
import com.letscamping.databinding.ActivitySearchtapBinding

//import kotlinx.android.synthetic.main.activity_search_tap.*

class SearchTapActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySearchtapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchtapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_search_tap)

        binding.searchViewpager.adapter = ScreenSlidePagerAdapter1(this)
        binding.searchViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.searchViewpager.isUserInputEnabled = false
        val tapTextList = arrayListOf("검색1","검색2")

        // 뷰페이저와 탭레이아웃을 붙임
        TabLayoutMediator(binding.mainSearchTablayout, binding.searchViewpager) { tab, position ->
            tab.text = tapTextList[position]
        }.attach()

        binding.exitSearch.setOnClickListener { finish() }
    }
    private inner class ScreenSlidePagerAdapter1(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2 // 페이지 수 리턴

        override fun createFragment(position: Int): Fragment {
            return when(position){ // 페이지 포지션에 따라 그에 맞는 프래그먼트를 보여줌
                0 -> SearchFragment1() //홈
                else -> SearchFragment2()    //내정보
            }
        }
    }
}
