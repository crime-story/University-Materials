package com.lifeSavers.emergencyappsignup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.second_page_guide.*

class GuidePage2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_page_guide)

        ThirdPageButton.setOnClickListener {
            val intent = Intent(this, GuidePage3::class.java)
            startActivity(intent)
        }

        ThirdPageButtonSkip.setOnClickListener {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
    }
}