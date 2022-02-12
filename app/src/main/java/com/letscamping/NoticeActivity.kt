package com.letscamping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.letscamping.databinding.ActivityNoticePageBinding

//import kotlinx.android.synthetic.main.activity_notice_page.*

class NoticeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoticePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticePageBinding.inflate(layoutInflater)
        val view = binding.root
        //setContentView(R.layout.activity_notice_page)
        setContentView(view)

        binding.exitNotice.setOnClickListener{
            finish()
        }
    }
}
