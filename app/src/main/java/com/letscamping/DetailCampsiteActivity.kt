package com.letscamping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.letscamping.databinding.ActivityDetailCampsiteBinding

class DetailCampsiteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailCampsiteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCampsiteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_detail_campsite)


    }
}