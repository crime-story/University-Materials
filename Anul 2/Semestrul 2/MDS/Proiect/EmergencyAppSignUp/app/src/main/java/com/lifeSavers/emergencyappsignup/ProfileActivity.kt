package com.lifeSavers.emergencyappsignup

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.lifeSavers.emergencyappsignup.databinding.ActivityProfileBinding
import com.lifeSavers.emergencyappsignup.model.User

class ProfileActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivityProfileBinding

    // Database
    private lateinit var database: DatabaseReference

    // ActionBar
    private lateinit var actionBar: ActionBar

    // FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure ActionBar, enable back button
        actionBar = supportActionBar!!
        actionBar.title = "Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)


        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // Handle click, logOut
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        // handle click, open edit profile
        binding.profileEditBtn.setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }


        val firebaseUser = firebaseAuth.currentUser
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

                    binding.nameUpEt.setText(name)
                    binding.nameEt.setText(name)
                    binding.emailEt.setText(email)
                    binding.phoneNumberEt.setText(phoneNumber.toString())
                    binding.birthDateEt.setText(yearOfBirth)
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

        if (firebaseUser != null) {
            database.child(firebaseUser.uid).addListenerForSingleValueEvent(postListener)
        }


    }

    private fun checkUser() {
        // Check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            // user not null, user is logged in, get user info
            val email = firebaseUser.email
            // set to text view
            //binding.emailTv.text = email
        } else {
            // user is null, user is not logged in, goto login activity
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // go back to previous activity, when back button of actionBar clicked
        return super.onSupportNavigateUp()
    }

    private fun loadUserInfo() {
        val database =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
        database.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // get user info
                    val name = "${snapshot.child("name").value}"
                    val email = "${snapshot.child("email").value}"
                    val phoneNumber = "${snapshot.child("phoneNumber").value}"
                    val birthDate = "${snapshot.child("birthDate").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"

                    // set data
                    binding.nameEt.text = name
                    binding.emailEt.text = email
                    binding.phoneNumberEt.text = phoneNumber
                    binding.birthDateEt.text = birthDate

                    // set image
                    try {
                        Glide.with(this@ProfileActivity).load(profileImage)
                            .placeholder(R.drawable.profile_pic)
                            .into(binding.profilePic)
                    } catch (e: Exception) {

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}