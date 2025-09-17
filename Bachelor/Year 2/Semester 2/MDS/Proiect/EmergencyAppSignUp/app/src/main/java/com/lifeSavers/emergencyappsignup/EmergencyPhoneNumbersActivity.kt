package com.lifeSavers.emergencyappsignup

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class EmergencyPhoneNumbersActivity : AppCompatActivity() {

    var phoneNumber = "123"

    val REQUEST_PHONE_CALL = 1

    // ActionBar
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_phone_numbers)

        // Configure ActionBar, enable back button
        actionBar = supportActionBar!!
        actionBar.title = "Emergency in Romania"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        val listView = findViewById<ListView>(R.id.listView)
        val names = arrayOf(
            "Single Emergency Call Number",
            "Emergency SMS (only for registered people)",
            "Child's phone",
            "Consumers Protection",
            "Border Police",
            "Romanian Automotive Register (RAR) - Appointments and Informations",
            "Ministry of Public Finance - Consumers phone",
            "Green line anti-corruption",
            "National Sanitary Veterinary and Food Safety Authority",
            "National Tourism Authority - Consumers phone",
            "National Energy Regulatory Authority",
            "National Company of Highways and National Roads in Romania - Informations and Vignette"
        )
        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, names)

        val phoneNumbers = arrayOf(
            "112", "113", "119", "+40219551", "+40219590", "+40219672", "+40800800085",
            "+40800806806", "+40800826787", "+40800868282", "+40213278153", "+40212643344"
        )

        listView.adapter = arrayAdapter


        listView.setOnItemClickListener { adapterView, view, i, l ->
            //Toast.makeText(this, "Item selected " + phoneNumbers[i], Toast.LENGTH_LONG).show()

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_PHONE_CALL
                )
            } else {
                phoneNumber = phoneNumbers[i]
                startCall()
            }

        }
    }

    @SuppressLint("MissingPermission")
    private fun startCall() {
        val callIntent = Intent(Intent.ACTION_CALL)

        callIntent.data = Uri.parse("tel: " + phoneNumber)

        startActivity(callIntent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PHONE_CALL)
            startCall()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // go back to previous activity, when back button of actionBar clicked
        return super.onSupportNavigateUp()
    }
}