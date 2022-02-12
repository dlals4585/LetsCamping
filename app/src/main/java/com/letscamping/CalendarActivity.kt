package com.letscamping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.letscamping.databinding.ActivityCalendarBinding
import com.letscamping.databinding.FragmentMyinfoBinding

//import kotlinx.android.synthetic.main.activity_calendar.*

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_calendar)
        setContentView(view)

        binding.calendarExit.setOnClickListener{
            finish()
        }
    }
}
