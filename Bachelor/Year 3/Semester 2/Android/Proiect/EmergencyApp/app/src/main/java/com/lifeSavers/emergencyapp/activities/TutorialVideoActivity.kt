package com.lifeSavers.emergencyapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lifeSavers.emergencyapp.R
import com.lifeSavers.emergencyapp.utils.Utils

class TutorialVideoActivity : AppCompatActivity() {
    private lateinit var actionBar: ActionBar
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial_video)

        actionBar = supportActionBar!!
        actionBar.title = "Tutorial - Emergency App"

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        val database =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
        database.child(firebaseUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"

                    val nameTextView: TextView = findViewById(R.id.user_name)
                    nameTextView.text = name
                    val emailTextView: TextView = findViewById(R.id.user_email)
                    emailTextView.text = email
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        actionBar.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_assistants -> {
                    startActivity(Intent(this, AssistantsListForUsersActivity::class.java))
                }
                R.id.nav_map -> {
                    startActivity(Intent(this, PermissionsActivity::class.java))
                }
                R.id.nav_urgent_call -> {
                    startActivity(Intent(this, EmergencyPhoneNumbersActivity::class.java))
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                }
                R.id.nav_logout -> {
                    firebaseAuth.signOut()
                    startActivity(Intent(this, LogInActivity::class.java))
                }
                R.id.nav_share_app -> {
                    Utils().shareButtonFunctionality(this)
                }
                R.id.nav_show_guide -> {
                    startActivity(Intent(this, GuidePage1::class.java))
                }
                R.id.nav_show_tutorial_video -> {
                    startActivity(Intent(this, TutorialVideoActivity::class.java))
                }
            }
            true
        }

        val videoView = findViewById<VideoView>(R.id.video_view)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.tutorial}")

        videoView.setMediaController(mediaController)
        videoView.setVideoURI(videoUri)
        videoView.requestFocus()
        videoView.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}