package com.letscamping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.letscamping.databinding.ActivityMain2Binding

//import kotlinx.android.synthetic.main.activity_main2.*

class MainTapActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        //setContentView(R.layout.activity_main2)
        val view = binding.root
        setContentView(view)

        binding.tapViewpager.adapter = ScreenSlidePagerAdapter(this)
        binding.tapViewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.tapViewpager.setUserInputEnabled(false)
        val tapTextList = arrayListOf("홈","내주변","예약정보","내정보")
        //val tabIconList = arrayListOf(R.drawable.아이콘이름, R.drawable.아이콘이름) // 이미지를 리스트로 선언하고

        // 뷰페이저와 탭레이아웃을 붙임
        TabLayoutMediator(binding.mainTablayout, binding.tapViewpager) { tab, position ->
            tab.text = tapTextList[position]
            //tab.setIcon(tabIconList[position]) // 아이콘 추가 시
        }.attach()

        /*val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(HomeFragment(), "Home" )
        adapter.addFragment(MyAddrFragment(), "Around Me")
        adapter.addFragment(MyInfoFragment(), "MyList")
        adapter.addFragment(MoreStgFragment(), "Setting")
        tap_viewpager.adapter = adapter
        main_tablayout.setupWithViewPager(tap_viewpager)*/

    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 4 // 페이지 수 리턴

        override fun createFragment(position: Int): Fragment {
            return when(position){ // 페이지 포지션에 따라 그에 맞는 프래그먼트를 보여줌
                0 -> HomeFragment() //홈
                1 -> MyAddrFragment()   //내주변
                2 -> MyReservationFragment()  //예약/배송현황/
                else -> MyInfoFragment()    //내정보/장바구니(위시리스트)
            }
        }
    }
}
