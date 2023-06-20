package com.lifeSavers.emergencyapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lifeSavers.emergencyapp.databinding.ThirdPageGuideBinding

class GuidePage3 : AppCompatActivity() {

    private lateinit var binding: ThirdPageGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ThirdPageGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LoginPageButton.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}
