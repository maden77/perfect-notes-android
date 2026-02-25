package com.example.perfectnotes.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.perfectnotes.databinding.ActivitySplashBinding
import com.example.perfectnotes.presentation.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2000) // 2 detik
    }
}