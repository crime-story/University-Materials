package com.lifeSavers.emergencyappsignup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.first_page_guide.*

class GuidePage1 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_page_guide)

        SecondPageButton.setOnClickListener {
            val intent = Intent(this, GuidePage2::class.java)
            startActivity(intent)
        }

        SecondPageButtonSkip.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}