package com.lifeSavers.emergencyapp.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lifeSavers.emergencyapp.R
import com.lifeSavers.emergencyapp.databinding.ActivityGoogleSignUpBinding
import com.lifeSavers.emergencyapp.model.User
import java.util.*

class GoogleSignUpActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivityGoogleSignUpBinding

    // Database
    private lateinit var database: DatabaseReference

    // ActionBar
    private lateinit var actionBar: ActionBar

    // ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    // FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private var name = ""
    private var email = ""
    private var photo = ""
    private var phoneNumber = ""
    private var birthDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Sign Up Google"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        val nameEditText = findViewById<EditText>(R.id.nameEt)

        name = intent.getStringExtra("name").toString()
        email = intent.getStringExtra("email").toString()

        photo = intent.getStringExtra("pic").toString()

        nameEditText.setText(name)

        // handle click, begin signUp
        binding.signUpBtn.setOnClickListener {
            // validate data
            validateData()
        }

    }

    private fun validateData() {
        // get data
        name = binding.nameEt.text.toString().trim()
        phoneNumber = binding.phoneNumberEt.text.toString().trim()
        birthDate = binding.birthDateEt.text.toString().trim()


        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]

        // validate data
        if (name == "") {
            binding.nameEt.error = "Please enter name"
        } else if (name.length < 3) {
            binding.nameEt.error = "Name must contain at least 3 letters"
        } else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            binding.phoneNumberEt.error = "Invalid phone format"
        } else if (birthDate.length != 4 || birthDate >= currentYear.toString()) {
            binding.birthDateEt.error = "Invalid year"
        } else {
            // data is valid, continue signUp
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp() {
        // show progress
        progressDialog.show()

        // Create account

        // get current user
        val firebaseUser = firebaseAuth.currentUser
        val name1 = name
        val email1 = email
        val phoneNumber1 = phoneNumber
        val birthDate1 = birthDate
        val profilePic = photo

        database =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")

        val deviceToken = getSharedPreferences(
            "com.lifeSavers.emergencyapp",
            MODE_PRIVATE
        ).getString("device_token", null)

        val user =
            User(
                firebaseUser?.uid, name1, email1, phoneNumber1, birthDate1, 0, profilePic,
                deviceToken
            )
        database.child(firebaseUser!!.uid)
            .setValue(user) // adds on Database a new registered user
            .addOnSuccessListener {
                binding.nameEt.text.clear()
                binding.phoneNumberEt.text.clear()
                binding.birthDateEt.text.clear()

                Toast.makeText(
                    this,
                    "Account created.",
                    Toast.LENGTH_SHORT
                ).show()

                firebaseAuth.signOut()

                // go to login page
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // go back to previous activity, when back button of actionBar clicked
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

}