package com.lifeSavers.emergencyapp.activities

import android.animation.ObjectAnimator
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.lifeSavers.emergencyapp.databinding.ActivityMainBinding
import com.lifeSavers.emergencyapp.utils.Utils

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var actionBar: ActionBar
    private lateinit var sharedPreferences: SharedPreferences
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Home"

        handler.postDelayed({
            rotater()
        }, 1000)

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

        binding.shareBtn.setOnClickListener {
            Utils().shareButtonFunctionality(this)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            sharedPreferences = getSharedPreferences("com.lifeSavers.emergencyapp", MODE_PRIVATE)
            if (!sharedPreferences.contains(uid) || sharedPreferences.getBoolean(uid, true)) {
                with(sharedPreferences.edit()) {
                    putBoolean(uid, false)
                    apply()
                }
                startActivity(Intent(this, GuidePage1::class.java))
            }
        }
    }

    private fun rotater() {
        val mapBtnAnim = ObjectAnimator.ofFloat(binding.mapBtn, View.ROTATION, -360f, 0f)
        mapBtnAnim.duration = 500
        mapBtnAnim.start()

        val profileBtnAnim = ObjectAnimator.ofFloat(binding.profileBtn, View.ROTATION, -360f, 0f)
        profileBtnAnim.duration = 500
        profileBtnAnim.start()

        val phoneNumbersBtnAnim =
            ObjectAnimator.ofFloat(binding.phoneNumbersBtn, View.ROTATION, -360f, 0f)
        phoneNumbersBtnAnim.duration = 500
        phoneNumbersBtnAnim.start()

        val assistantsListBtnAnim =
            ObjectAnimator.ofFloat(binding.assistantsListBtn, View.ROTATION, -360f, 0f)
        assistantsListBtnAnim.duration = 500
        assistantsListBtnAnim.start()

        val shareBtnAnim = ObjectAnimator.ofFloat(binding.shareBtn, View.ROTATION, -360f, 0f)
        shareBtnAnim.duration = 500
        shareBtnAnim.start()
    }
}
