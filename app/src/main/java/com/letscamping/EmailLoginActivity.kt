package com.letscamping

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.letscamping.databinding.ActivityEmailLoginBinding

class EmailLoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmailLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val username = binding.username
        val password = binding.password
        val login = binding.loginbtn
        val loading = binding.loading

    }
}