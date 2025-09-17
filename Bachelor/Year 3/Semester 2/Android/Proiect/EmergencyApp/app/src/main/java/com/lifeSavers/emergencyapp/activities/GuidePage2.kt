package com.lifeSavers.emergencyapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lifeSavers.emergencyapp.databinding.SecondPageGuideBinding

class GuidePage2 : AppCompatActivity() {

    private lateinit var binding: SecondPageGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SecondPageGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ThirdPageButton.setOnClickListener {
            val intent = Intent(this, GuidePage3::class.java)
            startActivity(intent)
        }

        binding.ThirdPageButtonSkip.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}
