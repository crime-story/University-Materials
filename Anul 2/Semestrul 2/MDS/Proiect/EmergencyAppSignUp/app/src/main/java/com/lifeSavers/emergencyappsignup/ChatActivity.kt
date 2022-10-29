package com.lifeSavers.emergencyappsignup

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.lifeSavers.emergencyappsignup.adapter.MessagesAdapter
import com.lifeSavers.emergencyappsignup.databinding.ActivityChatBinding
import com.lifeSavers.emergencyappsignup.model.Message
import java.util.*

class ChatActivity : AppCompatActivity() {

    var binding: ActivityChatBinding? = null
    var adapter: MessagesAdapter? = null
    var messages: ArrayList<Message>? = null
    var senderRoom: String? = null
    var receiverRoom: String? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var dialog: ProgressDialog? = null
    var senderUid: String? = null
    var receiverUid: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 25) {
            if (data != null) {
                if (data.data != null) {
                    val selectedImage = data.data
                    val calendar = Calendar.getInstance()
                    val reference = storage!!.reference.child("Chats")
                        .child(calendar.timeInMillis.toString() + "")
                    dialog!!.show()
                    reference.putFile(selectedImage!!)
                        .addOnCompleteListener { task ->
                            dialog!!.dismiss()
                            if (task.isSuccessful) {
                                reference.downloadUrl.addOnSuccessListener { uri ->
                                    val filePath = uri.toString()
                                    val messageTxt: String = binding!!.messageBox.text.toString()
                                    val date = Date()
                                    val message = Message(messageTxt, senderUid, date.time)
                                    message.message = "photo"
                                    message.imageUrl = filePath
                                    binding!!.messageBox.setText("")
                                    val randomkey = database!!.reference.push().key
                                    val lastMsgObj = HashMap<String, Any>()
                                    lastMsgObj["lastMsg"] = message.message!!
                                    lastMsgObj["lastMsgTime"] = date.time
                                    database!!.reference.child("Chats")
                                        .child(senderRoom!!)
                                        .updateChildren(lastMsgObj)
                                    database!!.reference.child("Chats")
                                        .child(receiverRoom!!)
                                        .updateChildren(lastMsgObj)
                                    database!!.reference.child("Chats")
                                        .child(senderRoom!!)
                                        .child("Messages")
                                        .child(randomkey!!)
                                        .setValue(message).addOnSuccessListener {
                                            database!!.reference.child("Chats")
                                                .child(receiverRoom!!)
                                                .child("Messages")
                                                .child(randomkey)
                                                .setValue(message)
                                                .addOnSuccessListener { }
                                        }
                                }
                            }
                        }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!)
            .setValue("Offline")
    }

    override fun onPause() {
        super.onPause()
        val currentId = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence")
            .child(currentId!!)
            .setValue("Offline")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        // setSupportActionBar(binding!!.toolbar)
        supportActionBar?.title = "Chat"
        database =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
        storage = FirebaseStorage.getInstance()
        dialog = ProgressDialog(this@ChatActivity)
        dialog!!.setMessage("Uploading image...")
        dialog!!.setCancelable(false)
        messages = ArrayList<Message>()
        val name = intent.getStringExtra("name")
        val profile = intent.getStringExtra("image")
        binding!!.name.text = name
        Glide.with(this@ChatActivity).load(profile)
            .placeholder(R.drawable.profile_pic)
            .into(binding!!.profilePic01)
        binding!!.imageView.setOnClickListener(View.OnClickListener { finish() })
        receiverUid = intent.getStringExtra("uid")
        senderUid = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence").child(receiverUid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val status = snapshot.getValue(String::class.java)
                        if (!status!!.isEmpty()) {
                            if (status == "Offline") {
                                binding!!.status.visibility = View.GONE
                            } else {
                                binding!!.status.setText(status)
                                binding!!.status.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        senderRoom = senderUid + receiverUid
        receiverRoom = receiverUid + senderUid
        adapter = MessagesAdapter(this, messages, senderRoom!!, receiverRoom!!)

        binding!!.recyclerView.layoutManager = LinearLayoutManager(this)
        binding!!.recyclerView.adapter = adapter
        database!!.reference.child("Chats")
            .child(senderRoom!!)
            .child("Messages")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages!!.clear()
                    for (snapshot1 in snapshot.children) {
                        val message: Message? = snapshot1.getValue(Message::class.java)
                        message!!.messageId = snapshot1.key
                        messages!!.add(message)
                    }
                    adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        binding!!.send.setOnClickListener(View.OnClickListener {
            val messageTxt: String = binding!!.messageBox.text.toString()
            val date = Date()
            val message = Message(messageTxt, senderUid, date.time)

            binding!!.messageBox.setText("")
            val randomKey = database!!.reference.push().key
            val lastMsgObj = HashMap<String, Any>()
            lastMsgObj["lastMsg"] = message.message!!
            lastMsgObj["lastMsgTime"] = date.time

            database!!.reference.child("Chats").child(senderRoom!!)
                .updateChildren(lastMsgObj)
            database!!.reference.child("Chats").child(receiverRoom!!)
                .updateChildren(lastMsgObj)
            database!!.reference.child("Chats").child(senderRoom!!)
                .child("Messages")
                .child(randomKey!!)
                .setValue(message)
                .addOnSuccessListener {
                    database!!.reference.child("Chats")
                        .child(receiverRoom!!)
                        .child("Messages")
                        .child(randomKey)
                        .setValue(message).addOnSuccessListener { }
                }
        })

        binding!!.attachment.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 25)
        })

        val handler = Handler()
        binding!!.messageBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                database!!.reference.child("Presence")
                    .child(senderUid!!)
                    .setValue("typing...")
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(userStoppedTyping, 1000)
            }

            var userStoppedTyping = Runnable {
                database!!.reference.child("Presence")
                    .child(senderUid!!)
                    .setValue("Online")
            }
        })

//        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
}