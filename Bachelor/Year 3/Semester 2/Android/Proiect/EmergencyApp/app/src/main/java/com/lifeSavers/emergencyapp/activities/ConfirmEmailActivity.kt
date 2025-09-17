package com.lifeSavers.emergencyapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.lifeSavers.emergencyapp.R

class ConfirmEmailActivity : AppCompatActivity() {

    private val gmailPackageName = "com.google.android.gm"
    private val playStorePackageName = "com.android.vending"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_email)

        val gmailButton = findViewById<Button>(R.id.gmail_button)
        gmailButton.setOnClickListener {
            val gmailIntent = packageManager.getLaunchIntentForPackage(gmailPackageName)
            if (gmailIntent != null) {
                startActivity(gmailIntent)
            } else {
                val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("market://details?id=$gmailPackageName")
                    setPackage(playStorePackageName)
                }
                startActivity(playStoreIntent)
            }
        }

        val outlookButton = findViewById<Button>(R.id.outlook_button)
        outlookButton.setOnClickListener {
            val outlookPackageName = "com.microsoft.office.outlook"
            val outlookIntent = packageManager.getLaunchIntentForPackage(outlookPackageName)
            if (outlookIntent != null) {
                startActivity(outlookIntent)
            } else {
                val playStoreIntent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("market://details?id=$outlookPackageName")
                    setPackage(playStorePackageName)
                }
                startActivity(playStoreIntent)
            }
        }

        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }
}
