package com.lifeSavers.emergencyapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lifeSavers.emergencyapp.databinding.FirstPageGuideBinding

class GuidePage1 : AppCompatActivity() {
    private lateinit var binding: FirstPageGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FirstPageGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SecondPageButton.setOnClickListener {
            val intent = Intent(this, GuidePage2::class.java)
            startActivity(intent)
        }

        binding.SecondPageButtonSkip.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}
