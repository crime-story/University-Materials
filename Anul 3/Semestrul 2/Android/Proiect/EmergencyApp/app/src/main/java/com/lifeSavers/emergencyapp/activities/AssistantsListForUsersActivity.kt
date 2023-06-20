package com.lifeSavers.emergencyapp.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lifeSavers.emergencyapp.R
import com.lifeSavers.emergencyapp.adapter.UserAdapter
import com.lifeSavers.emergencyapp.databinding.ActivityAssistantsListForUsersBinding
import com.lifeSavers.emergencyapp.model.User
import com.lifeSavers.emergencyapp.utils.Utils
import java.util.*

class AssistantsListForUsersActivity : AppCompatActivity() {

    var binding: ActivityAssistantsListForUsersBinding? = null
    private var database: FirebaseDatabase? = null
    private var searchView: SearchView? = null
    var users: ArrayList<User>? = null
    var usersAdapter: UserAdapter? = null
    private var dialog: ProgressDialog? = null
    private lateinit var toggle: ActionBarDrawerToggle

    // ActionBar
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssistantsListForUsersBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        actionBar = supportActionBar!!
        actionBar.title = "Assistants"

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val firebaseUser: FirebaseUser? = firebaseAuth.currentUser

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)

        val db =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
        val dbCollection = db.getReference("Users")
        dbCollection.child(firebaseUser!!.uid)
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

        dialog = ProgressDialog(this@AssistantsListForUsersActivity)
        dialog!!.setMessage("Uploading image...")
        dialog!!.setCancelable(false)
        database =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
        users = ArrayList()
        usersAdapter = UserAdapter(this@AssistantsListForUsersActivity, users!!)

        val layoutManagerPortrait = GridLayoutManager(this@AssistantsListForUsersActivity, 2)
        val layoutManagerLandscape = GridLayoutManager(this@AssistantsListForUsersActivity, 3)
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding!!.mRec.layoutManager = layoutManagerLandscape
        } else {
            binding!!.mRec.layoutManager = layoutManagerPortrait
        }
        binding!!.mRec.adapter = usersAdapter

        searchView = findViewById(R.id.searchView)

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText?.lowercase())
                return true
            }

        })

        database!!.reference.child("Users").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                users!!.clear()
                for (snapshot1 in snapshot.children) {
                    val user: User? = snapshot1.getValue(User::class.java)
                    if ((!user!!.email.equals(FirebaseAuth.getInstance().currentUser?.email)) and (user.userType == 1L)) {
                        users!!.add(user)
                    }
                    usersAdapter!!.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun filterList(query: String?) {
        if (query != null) {
            var filteredList = ArrayList<User>()
            for (i in users!!) {
                if (i.name?.lowercase(Locale.ROOT)!!.contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                filteredList = ArrayList()
                Toast.makeText(this, "No assistants found", Toast.LENGTH_SHORT).show()
            }
            usersAdapter!!.setFilteredList(filteredList)
        }
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!).setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        if (currentId != null) {
            database!!.reference.child("Presence")
                .child(currentId).setValue("Offline")
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