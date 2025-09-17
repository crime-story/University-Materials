package com.lifeSavers.emergencyappsignup

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lifeSavers.emergencyappsignup.databinding.ActivityLoginBinding

class LogInActivity : AppCompatActivity() {
    // ViewBinding
    private lateinit var binding: ActivityLoginBinding

    // ActionBar
    private lateinit var actionBar: ActionBar

    // ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    // FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configure actionBar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        // Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        // init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        // handle click, open SignUpActivity
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        // handle click, open ForgotPasswordActivity
        binding.forgotPasswordTv.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Reset Password")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
            val email = view.findViewById<EditText>(R.id.emailEt)
            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
                forgotPassword(email)
            })
            builder.setNegativeButton("close", DialogInterface.OnClickListener { _, _ -> })
            builder.show()
        }

        // handle click, begin login
        binding.loginBtn.setOnClickListener {
            // before logging in, validate data
            validateData()
        }
    }

    private fun forgotPassword(email: EditText) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            // invalid email format
            return
        }

        firebaseAuth.sendPasswordResetEmail(email.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Email sent.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateData() {
        // get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // invalid email format
            binding.emailEt.error = "Invalid email format"

        } else if (TextUtils.isEmpty(password)) {
            // no password entered
            binding.passwordEt.error = "Please enter password"
        } else {
            // data is validated, begin login
            firebaseLogIn()
        }
    }

    private fun firebaseLogIn() {
        // show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // login success
                progressDialog.dismiss()
                // get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email

                if (firebaseUser.isEmailVerified) {
                    Toast.makeText(this, "LoggedIn as $email", Toast.LENGTH_SHORT).show()

                    var database =
                        FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                            .getReference("Users")

                    val postListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val userType = dataSnapshot.getValue(Long::class.java)

                            if (userType.toString() == "0") {
                                startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                                finish()
                            } else {
                                startActivity(Intent(this@LogInActivity, MainActivity::class.java))
                                finish()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Failed to read value
                            Log.w(TAG, "Failed.", error.toException())
                        }
                    }

                    database.child(firebaseUser.uid).child("userType")
                        .addListenerForSingleValueEvent(postListener)
                    // open profile

//                    startActivity(Intent(this, PermissionsActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Please verify your email address.", Toast.LENGTH_SHORT)
                        .show()
                }

            }
            .addOnFailureListener { e ->
                // login failed
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        // if user is already logged in go to profile activity
        // get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            // user is already logged in
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}