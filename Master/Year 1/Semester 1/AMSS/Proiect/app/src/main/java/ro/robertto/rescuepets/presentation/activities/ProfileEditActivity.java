package ro.robertto.rescuepets.presentation.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.Menu;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.databinding.ActivityProfileEditBinding;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;

public class ProfileEditActivity extends AppCompatActivity {
    private static final int REQUEST_TAKE_PHOTO = 1;

    // view binding
    private ActivityProfileEditBinding binding;

    // ActionBar
    private ActionBar actionBar;

    // firebase auth
    private FirebaseAuth firebaseAuth;

    // image url (which we will pick)
    private Uri imageUri = null;

    // progress dialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = ActivityProfileEditBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        // setup progress dialog
        progressDialog = new ProgressDialog( this );
        progressDialog.setTitle( "Please wait..." );
        progressDialog.setCanceledOnTouchOutside( false );

        actionBar = getSupportActionBar();
        Objects.requireNonNull( actionBar ).setTitle( "Edit Profile" );
        actionBar.setDisplayHomeAsUpEnabled( true );
        actionBar.setDisplayShowHomeEnabled( true );

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        loadUserInfo();

        // handle click, pick image from camera/gallery
        binding.profilePic.setOnClickListener( view -> showImageAttachMenu() );
        // handle click, begin update profile
        binding.updateBtn.setOnClickListener( view -> validateData() );
    }

    private String name = "";
    private String email = "";
    private String phoneNumber = "";
    private String birthDate = "";
    private String centerUid = "";
    private String employeeProfilePictureUrl = "";

    private void validateData() {
        // get data
        name = binding.nameEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        phoneNumber = binding.phoneNumberEt.getText().toString().trim();
        birthDate = binding.birthDateEt.getText().toString().trim();
        centerUid = binding.EmployeeCenterId.getText().toString().trim();
        employeeProfilePictureUrl = binding.EmployeeProfilePictureUrl.getText().toString().trim();

        // validate data
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get( Calendar.YEAR );

        // validate data
        if ( !Patterns.EMAIL_ADDRESS.matcher( email ).matches() ) {
            // invalid email format
            binding.emailEt.setError( "Invalid email format" );
        } else if ( name.isEmpty() ) {
            binding.nameEt.setError( "Please enter name" );
        } else if ( name.length() < 3 ) {
            binding.nameEt.setError( "Name must contain at least 3 letters" );
        } else if ( !Patterns.PHONE.matcher( phoneNumber ).matches() ) {
            binding.phoneNumberEt.setError( "Invalid phone format" );
        } else if ( birthDate.length() != 4 || Integer.parseInt( birthDate ) >= currentYear ) {
            binding.birthDateEt.setError( "Invalid year" );
        } else {
            if ( imageUri == null ) {
                // need to update without image
                updateProfile( "" );
            } else {
                // update with image
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        progressDialog.setMessage( "Uploading profile image" );
        progressDialog.show();

        // image path and name, use uid to replace previous
        String filePathAndName = "ProfileImages/" + firebaseAuth.getUid();

        // storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        storageReference.child( filePathAndName ).putFile( imageUri )
                .addOnSuccessListener( taskSnapshot -> {
                    // image uploaded, get url of uploaded image
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener( uri -> {
                        String uploadedImageUrl = uri.toString();
                        updateProfile( uploadedImageUrl );
                    } );
                } )
                .addOnFailureListener( e -> {
                    // failed to upload image
                    progressDialog.dismiss();
                    Toast.makeText( this, "Failed to upload image due to " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                } );
    }

    private void updateProfile( String uploadedImageUrl ) {
        progressDialog.setMessage( "Updating profile..." );

        {
            // setup info to update to database
            HashMap< String, Object > hashmap = new HashMap<>();
            hashmap.put( "name", name );
            hashmap.put( "email", email );
            hashmap.put( "phoneNumber", phoneNumber );
            hashmap.put( "birthDate", birthDate );
            if ( imageUri != null ) {
                hashmap.put( "profileImage", uploadedImageUrl );
            }

            // update to database
            DatabaseReference reference = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                    .getReference( "Users" );
            reference.child( Objects.requireNonNull( firebaseAuth.getUid() ) )
                    .updateChildren( hashmap )
                    .addOnSuccessListener( aVoid -> {
                        // profile updated
                        progressDialog.dismiss();
                        Toast.makeText( this, "Profile updated", Toast.LENGTH_SHORT ).show();
                        startActivity( new Intent( ProfileEditActivity.this, ProfileActivity.class ) );
                    } )
                    .addOnFailureListener( e -> {
                        // failed to upload image
                        progressDialog.dismiss();
                        Toast.makeText( this, "Failed to upload image due to " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                    } );
        }
        if ( !centerUid.isEmpty() ) { // they are also an employee, so we update that table to
            HashMap< String, Object > hashmap = new HashMap<>();
            hashmap.put( "name", name );
            hashmap.put( "email", email );
            hashmap.put( "uid", firebaseAuth.getUid() );
            hashmap.put( "centerUid", centerUid );
            if ( imageUri != null && !uploadedImageUrl.isEmpty() )
                hashmap.put( "profileImage", uploadedImageUrl );
            else if ( !employeeProfilePictureUrl.isEmpty() )
                hashmap.put( "profileImage", employeeProfilePictureUrl );

            DatabaseReference reference = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                    .getReference( "Center" ).child( centerUid ).child( "Employee" );
            reference.child( Objects.requireNonNull( firebaseAuth.getUid() ) )
                    .updateChildren( hashmap )
                    .addOnSuccessListener( aVoid -> {
                        // profile updated
                        progressDialog.dismiss();
                        Toast.makeText( this, "Profile updated", Toast.LENGTH_SHORT ).show();
                        startActivity( new Intent( ProfileEditActivity.this, ProfileActivity.class ) );
                    } )
                    .addOnFailureListener( e -> {
                        // failed to upload image
                        progressDialog.dismiss();
                        Toast.makeText( this, "Failed to upload image due to " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                    } );
        }
    }

    private void loadUserInfo() {
        DatabaseReference database = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                .getReference( "Users" );
        database.child( Objects.requireNonNull( firebaseAuth.getUid() ) )
                .addValueEventListener( new ValueEventListener() {
                    @Override
                    public void onDataChange( @NonNull DataSnapshot snapshot ) {
                        // get user info
                        String birthDate = Objects.requireNonNull( snapshot.child( "birthDate" ).getValue() ).toString().trim();
                        String email = Objects.requireNonNull( snapshot.child( "email" ).getValue() ).toString().trim();
                        String name = Objects.requireNonNull( snapshot.child( "name" ).getValue() ).toString().trim();
                        String phoneNumber = Objects.requireNonNull( snapshot.child( "phoneNumber" ).getValue() ).toString().trim();
                        String profileImageUrl = "";
                        Object maybeProfileImage = snapshot.child( "profileImage" ).getValue();
                        if ( maybeProfileImage != null ) {
                            profileImageUrl = maybeProfileImage.toString().trim();
                        }
                        String centerId = "";
                        Object maybeCenter = snapshot.child( "centerUid" ).getValue();
                        if ( maybeCenter != null ) {
                            centerId = maybeCenter.toString().trim();
                        }

                        // set data
                        binding.nameEt.setText( name );
                        binding.emailEt.setText( email );
                        binding.phoneNumberEt.setText( phoneNumber );
                        binding.birthDateEt.setText( birthDate );
                        binding.EmployeeCenterId.setText( centerId );
                        binding.EmployeeProfilePictureUrl.setText( profileImageUrl );

                        // set image
                        try {
                            Glide.with( ProfileEditActivity.this )
                                    .load( profileImageUrl )
                                    .placeholder( R.drawable.profile_pic )
                                    .into( binding.profilePic );
                        } catch ( Exception e ) {
                            // Handle the exception if necessary
                        }
                    }

                    @Override
                    public void onCancelled( @NonNull DatabaseError error ) {
                        // Handle the cancellation if necessary
                    }
                } );
    }

    private void showImageAttachMenu() {
        /* Show popup menu with options Camera, Gallery to pick image */
        // setup popup menu
        PopupMenu popupMenu = new PopupMenu( this, binding.profilePic );
        popupMenu.getMenu().add( Menu.NONE, 0, 0, "Camera" );
        popupMenu.getMenu().add( Menu.NONE, 1, 1, "Gallery" );
        popupMenu.show();

        // handle popup menu item click
        popupMenu.setOnMenuItemClickListener( item -> {
            // get id of clicked item
            int id = item.getItemId();
            if ( id == 0 ) {
                // Camera clicked
                pickImageCamera();
            } else if ( id == 1 ) {
                // Gallery clicked
                pickImageGallery();
            }
            return true;
        } );
    }

    private void pickImageGallery() {
        // intent to pick image from gallery
        Intent intent = new Intent( Intent.ACTION_PICK );
        intent.setType( "image/*" );
        galleryActivityResultLauncher.launch( intent );
    }

    private void pickImageCamera() {
        if ( ActivityCompat.checkSelfPermission( this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] { Manifest.permission.CAMERA }, REQUEST_TAKE_PHOTO );
        }

        if ( ActivityCompat.checkSelfPermission( this, Manifest.permission.CAMERA ) == PackageManager.PERMISSION_GRANTED ) {
            // intent to pick image from camera
            ContentValues values = new ContentValues();
            values.put( MediaStore.Images.Media.TITLE, "Temp_Title" );
            values.put( MediaStore.Images.Media.DESCRIPTION, "Temp_Description" );

            imageUri = getContentResolver().insert( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values );

            Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
            intent.putExtra( MediaStore.EXTRA_OUTPUT, imageUri );
            cameraActivityResultLauncher.launch( intent );
        }
    }

    // used to handle result of camera intent (new way in replacement of startActivityForResult)
    private final ActivityResultLauncher< Intent > cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // get uri of image
                if ( result.getResultCode() == Activity.RESULT_OK ) {
                    // set to imageview
                    binding.profilePic.setImageURI( imageUri );
                } else {
                    // cancelled
                    Toast.makeText( this, "Cancelled", Toast.LENGTH_SHORT ).show();
                }
            }
    );

    // used to handle result of gallery intent (new way in replacement of startActivityForResult)
    private final ActivityResultLauncher< Intent > galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                // get uri of image
                if ( result.getResultCode() == Activity.RESULT_OK ) {
                    Intent data = result.getData();
                    imageUri = Objects.requireNonNull( data ).getData();

                    // set to imageview
                    binding.profilePic.setImageURI( imageUri );
                } else {
                    // cancelled
                    Toast.makeText( this, "Cancelled", Toast.LENGTH_SHORT ).show();
                }
            }
    );

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back to the previous activity, when back button of actionBar clicked
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }

    private void checkUser() {
        // Check user is logged in or not
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser == null ) {
            // user is null, user is not logged in, go to login activity
            startActivity( new Intent( this, LogInActivity.class ) );
            finish();
        }
    }
}
