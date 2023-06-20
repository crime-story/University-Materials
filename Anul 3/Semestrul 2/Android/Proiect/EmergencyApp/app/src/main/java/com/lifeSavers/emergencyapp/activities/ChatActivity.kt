package com.lifeSavers.emergencyapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.lifeSavers.emergencyapp.R
import com.lifeSavers.emergencyapp.adapter.MessagesAdapter
import com.lifeSavers.emergencyapp.databinding.ActivityChatBinding
import com.lifeSavers.emergencyapp.model.Message
import com.lifeSavers.emergencyapp.service.MyFirebaseMessagingService
import java.io.File
import java.io.IOException
import java.util.*


class ChatActivity : AppCompatActivity() {
    private val requestTakePhoto = 1

    var binding: ActivityChatBinding? = null
    var adapter: MessagesAdapter? = null
    var messages: ArrayList<Message>? = null
    private var senderRoom: String? = null
    private var receiverRoom: String? = null
    var database: FirebaseDatabase? = null
    private var storage: FirebaseStorage? = null
    private var dialog: ProgressDialog? = null
    var senderUid: String? = null
    private var receiverUid: String? = null
    private lateinit var photoPath: String

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
                                    val message = Message(messageTxt, senderUid)
                                    message.message = "photo"
                                    message.imageUrl = filePath
                                    binding!!.messageBox.setText("")
                                    val randomKey = database!!.reference.push().key
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
                                        .child(randomKey!!)
                                        .setValue(message).addOnSuccessListener {
                                            database!!.reference.child("Chats")
                                                .child(receiverRoom!!)
                                                .child("Messages")
                                                .child(randomKey)
                                                .setValue(message)
                                                .addOnSuccessListener { }
                                        }
                                }
                            }
                        }
                }
            }
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            val calendar = Calendar.getInstance()
            val reference = storage!!.reference.child("Chats")
                .child(calendar.timeInMillis.toString() + "")
            dialog!!.show()
            reference.putFile(Uri.fromFile(File(photoPath)))
                .addOnCompleteListener { task ->
                    dialog!!.dismiss()
                    if (task.isSuccessful) {
                        reference.downloadUrl.addOnSuccessListener { uri ->
                            val filePath = uri.toString()
                            val messageTxt: String = binding!!.messageBox.text.toString()
                            val date = Date()
                            val message = Message(messageTxt, senderUid)
                            message.message = "photo"
                            message.imageUrl = filePath
                            binding!!.messageBox.setText("")
                            val randomKey = database!!.reference.push().key
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
                                .child(randomKey!!)
                                .setValue(message).addOnSuccessListener {
                                    database!!.reference.child("Chats")
                                        .child(receiverRoom!!)
                                        .child("Messages")
                                        .child(randomKey)
                                        .setValue(message)
                                        .addOnSuccessListener { }
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
        binding!!.imageView.setOnClickListener { finish() }
        receiverUid = intent.getStringExtra("uid")
        senderUid = FirebaseAuth.getInstance().uid
        database!!.reference.child("Presence").child(receiverUid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val status = snapshot.getValue(String::class.java)
                        if (status!!.isNotEmpty()) {
                            if (status == "Offline") {
                                binding!!.status.visibility = View.GONE
                            } else {
                                binding!!.status.text = status
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

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        linearLayoutManager.scrollToPositionWithOffset(0, 100)
        binding!!.recyclerView.layoutManager = linearLayoutManager
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
                    linearLayoutManager.smoothScrollToPosition(
                        binding!!.recyclerView,
                        null,
                        adapter!!.itemCount
                    )
                    adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        binding!!.send.setOnClickListener {
            val messageTxt: String = binding!!.messageBox.text.toString()
            if (messageTxt.isEmpty()) {
                return@setOnClickListener
            }
            val date = Date()
            val message = Message(messageTxt, senderUid)

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
                    val senderNameRef = database!!.reference.child("Users").child(senderUid!!)
                        .child("name")
                    senderNameRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val senderName = dataSnapshot.value.toString()
                            val profilePicRef =
                                database!!.reference.child("Users").child(senderUid!!)
                                    .child("profileImage")
                            profilePicRef.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val senderProfilePic = snapshot.value.toString()
                                    MyFirebaseMessagingService().sendNotification(
                                        senderUid!!, receiverUid!!, senderName,
                                        senderProfilePic
                                    )
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                        }

                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
        }

        binding!!.attachment.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 25)
        }

        binding!!.camera.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    requestTakePhoto
                )
            } else {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (e: IOException) {
                }
                if (photoFile != null) {
                    val photoUri = FileProvider.getUriForFile(
                        this,
                        "com.example.android.provider",
                        photoFile
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(intent, requestTakePhoto)
                }
            }
        }


        val handler = Handler()
        binding!!.messageBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding!!.send.isEnabled = p0.toString().trim().isNotEmpty()
            }

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
    }

    private fun createImageFile(): File? {
        val fileName = "pic"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            fileName,
            ".jpg",
            storageDir
        )
        photoPath = image.absolutePath
        return image
    }
}
