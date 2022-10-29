package com.lifeSavers.emergencyappsignup

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lifeSavers.emergencyappsignup.databinding.ActivitySignUpBinding
import com.lifeSavers.emergencyappsignup.model.User
import java.util.*

class SignUpActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivitySignUpBinding

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
    private var phoneNumber = ""
    private var birthDate = ""
    private var password = ""
    private var confirmedPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure ActionBar, enable back button
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // handle click, begin signUp
        binding.signUpBtn.setOnClickListener {
            // validate data
            validateData()
        }

    }

    private fun validateData() {
        // get data
        name = binding.nameEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        phoneNumber = binding.phoneNumberEt.text.toString().trim()
        birthDate = binding.birthDateEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        confirmedPassword = binding.confirmedPasswordEt.text.toString().trim()


        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // invalid email format
            binding.emailEt.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            // password isn't entered
            binding.passwordEt.error = "Please enter password"
        } else if (password.length < 6) {
            // password length is less than 6
            binding.passwordEt.error = "Password must contain at least 6 characters"
        } else if (!password.equals(confirmedPassword)) {
            binding.passwordEt.error = "Password and Confirmed Password must match"
            binding.confirmedPasswordEt.error = "Password and Confirmed Password must match"
        } else if (name.equals("")) {
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
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // signUp success
                progressDialog.dismiss()
                // get current user
                val firebaseUser = firebaseAuth.currentUser
                val sendEmailVerification = firebaseUser!!.sendEmailVerification()
                    .addOnSuccessListener {
                        val name1 = name
                        val email1 = email
                        val phoneNumber1 = phoneNumber
                        val birthDate1 = birthDate

                        val email = firebaseUser.email

                        database =
                            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                                .getReference("Users")

                        val user =
                            User(firebaseUser.uid, name1, email1, phoneNumber1, birthDate1, 0, "")
                        database.child(firebaseUser.uid)
                            .setValue(user) // adds on Database a new registred user
                            .addOnSuccessListener {
                                binding.nameEt.text.clear()
                                binding.emailEt.text.clear()
                                binding.phoneNumberEt.text.clear()
                                binding.birthDateEt.text.clear()

                                Toast.makeText(
                                    this,
                                    "Account created. Please check your email for verification.",
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
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Account not created due to ${e.message}.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
            .addOnFailureListener { e ->
                // signUp failed
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // go back to previous activity, when back button of actionBar clicked
        return super.onSupportNavigateUp()
    }
}