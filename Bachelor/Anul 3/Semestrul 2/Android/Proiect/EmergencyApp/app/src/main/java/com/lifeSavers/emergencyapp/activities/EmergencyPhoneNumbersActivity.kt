package com.lifeSavers.emergencyapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.lifeSavers.emergencyapp.R
import com.lifeSavers.emergencyapp.utils.Utils

class EmergencyPhoneNumbersActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var phoneNumber = "123"

    private val requestPhoneCall = 1

    // ActionBar
    private lateinit var actionBar: ActionBar

    private lateinit var databaseReference: DatabaseReference
    private lateinit var phoneNumbersMap: MutableMap<String, String>
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_phone_numbers)

        actionBar = supportActionBar!!
        actionBar.title = "Emergency in Romania"

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

        listView = findViewById(R.id.listView)

        databaseReference = FirebaseDatabase.getInstance().reference.child("PhoneNumbers")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val names = ArrayList<String>()
                phoneNumbersMap = mutableMapOf()

                for (ds in snapshot.children) {
                    val name = ds.key.toString().removeSurrounding("\"")
                    val phoneNumber = ds.value.toString()

                    names.add(name)
                    phoneNumbersMap[name] = phoneNumber
                }

                val arrayAdapter: ArrayAdapter<String> =
                    ArrayAdapter(
                        this@EmergencyPhoneNumbersActivity,
                        android.R.layout.simple_list_item_1,
                        names
                    )

                listView.adapter = arrayAdapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        listView.setOnItemClickListener { _, _, position, _ ->
            val name = listView.getItemAtPosition(position).toString()
            val phoneNumber = phoneNumbersMap[name]

            if (phoneNumber != null) {
                this.phoneNumber = phoneNumber
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CALL_PHONE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CALL_PHONE),
                        requestPhoneCall
                    )
                } else {
                    startCall()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startCall() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel: $phoneNumber")
        startActivity(callIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestPhoneCall)
            startCall()
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
