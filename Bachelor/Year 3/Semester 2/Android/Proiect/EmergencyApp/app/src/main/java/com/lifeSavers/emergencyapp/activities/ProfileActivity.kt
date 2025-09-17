package com.lifeSavers.emergencyapp.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.lifeSavers.emergencyapp.R
import com.lifeSavers.emergencyapp.databinding.ActivityProfileBinding
import com.lifeSavers.emergencyapp.model.User
import com.lifeSavers.emergencyapp.utils.Utils

class ProfileActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivityProfileBinding

    // Database
    private lateinit var database: DatabaseReference

    // ActionBar
    private lateinit var actionBar: ActionBar

    // FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Profile"
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        checkUser()

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        val db =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
        db.child(firebaseUser!!.uid)
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

        // Handle click, logOut
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        // handle click, open edit profile
        binding.profileEditBtn.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }

        database =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userProfile = dataSnapshot.getValue(User::class.java)
                if (userProfile != null) {
                    val name = userProfile.name
                    val email = userProfile.email
                    val phoneNumber = userProfile.phoneNumber
                    val yearOfBirth = userProfile.birthDate
                    val profileImage = userProfile.profileImage

                    binding.nameUpEt.text = name
                    binding.nameEt.text = name
                    binding.emailEt.text = email
                    binding.phoneNumberEt.text = phoneNumber.toString()
                    binding.birthDateEt.text = yearOfBirth
                    // set image
                    try {
                        Glide.with(this@ProfileActivity).load(profileImage)
                            .placeholder(R.drawable.profile_pic)
                            .into(binding.profilePic)
                    } catch (e: Exception) {

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(ContentValues.TAG, "Failed.", error.toException())
            }
        }

        database.child(firebaseUser.uid).addListenerForSingleValueEvent(postListener)
    }

    private fun checkUser() {
        // Check user is logged in or not
        firebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            // user is null, user is not logged in, go to login activity
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // go back to previous activity, when back button of actionBar clicked
        return super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}