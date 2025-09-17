package com.lifeSavers.emergencyappsignup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.lifeSavers.emergencyappsignup.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // ActionBar
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Home"

        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.mapBtn.setOnClickListener {
            startActivity(Intent(this, PermissionsActivity::class.java))
        }

        binding.phoneNumbersBtn.setOnClickListener {
            startActivity(Intent(this, EmergencyPhoneNumbersActivity::class.java))
        }

        binding.assistantsListBtn.setOnClickListener {
            startActivity(Intent(this, AssistantsListForUsersActivity::class.java))
        }
    }
}