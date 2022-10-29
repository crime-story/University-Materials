package com.lifeSavers.emergencyappsignup

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lifeSavers.emergencyappsignup.adapter.UserAdapter
import com.lifeSavers.emergencyappsignup.databinding.ActivityAssistantsListForUsersBinding
import com.lifeSavers.emergencyappsignup.model.User

class AssistantsListForUsersActivity : AppCompatActivity() {

    var binding: ActivityAssistantsListForUsersBinding? = null
    var database: FirebaseDatabase? = null
    var users: ArrayList<User>? = null
    var usersAdapter: UserAdapter? = null
    var dialog: ProgressDialog? = null
    var user: User? = null

    // ActionBar
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssistantsListForUsersBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        // Configure ActionBar, enable back button
        actionBar = supportActionBar!!
        actionBar.title = "Assistants"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        dialog = ProgressDialog(this@AssistantsListForUsersActivity)
        dialog!!.setMessage("Uploading image...")
        dialog!!.setCancelable(false)
        database =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
        users = ArrayList<User>()
        usersAdapter = UserAdapter(this@AssistantsListForUsersActivity, users!!)
        val layoutManager = GridLayoutManager(this@AssistantsListForUsersActivity, 2)
        binding!!.mRec.layoutManager = layoutManager
        database!!.reference.child("Users")
            .child(FirebaseAuth.getInstance().uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)
                }

                override fun onCancelled(error: DatabaseError) {}

            })
        binding!!.mRec.adapter = usersAdapter
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

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!).setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!).setValue("Offline")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // go back to previous activity, when back button of actionBar clicked
        return super.onSupportNavigateUp()
    }
}