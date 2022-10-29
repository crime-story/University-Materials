package com.lifeSavers.emergencyappsignup

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.lifeSavers.emergencyappsignup.databinding.ActivityProfileEditBinding
import java.util.*

class ProfileEditActivity : AppCompatActivity() {

    // view binding
    private lateinit var binding: ActivityProfileEditBinding

    // ActionBar
    private lateinit var actionBar: ActionBar

    // firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    // image url (which we will pick)
    private var imageUri: Uri? = null

    // progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        // Configure ActionBar, enable back button
        actionBar = supportActionBar!!
        actionBar.title = "Edit Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()

        // handle click, pick image from camera/gallery
        binding.profilePic.setOnClickListener {
            showImageAttachMenu()
        }
        // handle click, begin update profile
        binding.updateBtn.setOnClickListener {
            validateData()
        }

    }

    private var name = ""
    private var email = ""
    private var phoneNumber = ""
    private var birthDate = ""


    private fun validateData() {
        // get data
        name = binding.nameEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        phoneNumber = binding.phoneNumberEt.text.toString().trim()
        birthDate = binding.birthDateEt.text.toString().trim()

        // validate data
        val calendar = Calendar.getInstance()
        val currentYear = calendar[Calendar.YEAR]

        // validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            // invalid email format
            binding.emailEt.error = "Invalid email format"
        } else if (name.equals("")) {
            binding.nameEt.error = "Please enter name"
        } else if (name.length < 3) {
            binding.nameEt.error = "Name must contain at least 3 letters"
        } else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            binding.phoneNumberEt.error = "Invalid phone format"
        } else if (birthDate.length != 4 || birthDate >= currentYear.toString()) {
            binding.birthDateEt.error = "Invalid year"
        } else {
            if (imageUri == null) {
                // meed to update without image
                updateProfile("")
            } else {
                // update with image
                uploadImage()
            }


        }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Uploading profile image")
        progressDialog.show()

        // image path and name, use uid to replace previous
        val filePathAndName = "ProfileImages/" + firebaseAuth.uid

        // storage reference
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                // image uploaded, get url of uploaded image

                val uriTask: Task<Uri> = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedImageUrl = "${uriTask.result}"

                updateProfile(uploadedImageUrl)

            }
            .addOnFailureListener { e ->
                // failed to upload image
                progressDialog.dismiss()
                makeText(this, "Failed to upload image due to ${e.message}", LENGTH_SHORT).show()
            }
    }

    private fun updateProfile(uploadedImageUrl: String) {
        progressDialog.setMessage("Updating profile...")

        // setup info to update to database
        val hashmap: HashMap<String, Any> = HashMap()
        hashmap["name"] = "$name"
        hashmap["email"] = "$email"
        hashmap["phoneNumber"] = "$phoneNumber"
        hashmap["birthDate"] = "$birthDate"
        if (imageUri != null) {
            hashmap["profileImage"] = uploadedImageUrl
        }

        // update to database
        val reference =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
        reference.child(firebaseAuth.uid!!)
            .updateChildren(hashmap)
            .addOnSuccessListener {
                // profile updated
                progressDialog.dismiss()
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@ProfileEditActivity, ProfileActivity::class.java))
            }
            .addOnFailureListener { e ->
                // failed to upload image
                progressDialog.dismiss()
                makeText(this, "Failed to upload image due to ${e.message}", LENGTH_SHORT).show()
            }

    }

    private fun loadUserInfo() {
        val database =
            FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Users")
        database.child(firebaseAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // get user info
                    val birthDate = "${snapshot.child("birthDate").value}"
                    val email = "${snapshot.child("email").value}"
                    val name = "${snapshot.child("name").value}"
                    val phoneNumber = "${snapshot.child("phoneNumber").value}"
                    val profileImage = "${snapshot.child("profileImage").value}"

                    // set data
                    binding.nameEt.setText(name)
                    binding.emailEt.setText(email)
                    binding.phoneNumberEt.setText(phoneNumber)
                    binding.birthDateEt.setText(birthDate)

                    // set image
                    try {
                        Glide.with(this@ProfileEditActivity).load(profileImage)
                            .placeholder(R.drawable.profile_pic)
                            .into(binding.profilePic)
                    } catch (e: Exception) {

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun showImageAttachMenu() {
        /* Show popup menu with options Camera, Gallery to pick image */
        // setup popup menu
        val popupMenu = PopupMenu(this, binding.profilePic)
        popupMenu.menu.add(Menu.NONE, 0, 0, "Camera")
        popupMenu.menu.add(Menu.NONE, 1, 1, "Gallery")
        popupMenu.show()

        // handle popup menu item click
        popupMenu.setOnMenuItemClickListener { item ->
            // get id of clicked item
            val id = item.itemId
            if (id == 0) {
                // Camera clicked
                pickImageCamera()

            } else if (id == 1) {
                // Gallery clicked
                pickImageGallery()
            }
            true

        }
    }

    private fun pickImageGallery() {
        // intent to pick image from gallery
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)

    }

    private fun pickImageCamera() {
        // intent to pick image from camera
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    // used to handle result of camera intent (new way in replacement of startActivityForResult)
    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            // get uri of image
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                //imageUri = data!!.data

                // set to imageview
                binding.profilePic.setImageURI(imageUri)
            } else {
                // cancelled
                makeText(this, "Cancelled", LENGTH_SHORT).show()
            }

        }
    )

    // used to handle result of gallery intent (new way in replacement of startActivityForResult)
    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            // get uri of image
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data!!.data

                // set to imageview
                binding.profilePic.setImageURI(imageUri)
            } else {
                // cancelled
                makeText(this, "Cancelled", LENGTH_SHORT).show()
            }
        }
    )


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // go back to previous activity, when back button of actionBar clicked
        return super.onSupportNavigateUp()
    }
}