package ro.robertto.rescuepets.presentation.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Objects;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.databinding.ActivityPetProfileEditBinding;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModel;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModelFactory;
import timber.log.Timber;

public class PetProfileEditActivity extends AppCompatActivity {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private ActivityPetProfileEditBinding binding;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private Uri imageUri = null;
    private Pet pet = null;
    private RescuePetsViewModel rescuePetsViewModel;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        // ViewBinding
        binding = ActivityPetProfileEditBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        // ActionBar
        ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null ) {
            actionBar.setTitle( "Edit Pet Profile" );
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setDisplayShowHomeEnabled( true );
        }

        Bundle b = getIntent().getExtras();
        if ( b != null ) {
            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ) {
                pet = b.getSerializable( "pet", Pet.class );
            } else {
                pet = ( Pet ) b.getSerializable( "pet" );
            }
        }

        if ( pet != null ) {
            binding.PetEditBirthYear.setText( pet.getBirthYear() );
            binding.PetEditBreed.setText( pet.getBreed() );
            binding.PetEditSpecies.setText( pet.getSpecies() );
            binding.PetEditName.setText( pet.getName() );
            binding.PetEditDescription.setText( pet.getDescription() );
            String profileImageUrl = pet.getProfileImage();
            try {
                if ( profileImageUrl != null && !profileImageUrl.isEmpty() )
                    Glide.with( this ).load( profileImageUrl )
                            .placeholder( R.drawable.profile_pic )
                            .into( binding.profilePicPetEdit );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } else {
            pet = new Pet( "", "hashCenter1", "", "", "", "", "", "" );
        }

        binding.profilePicPetEdit.setOnClickListener( view -> showImageAttachMenu() );
        binding.updateBtnPet.setOnClickListener( view -> validateData() );

        rescuePetsViewModel = new ViewModelProvider( this,
                new RescuePetsViewModelFactory( getApplication() ) ).get( RescuePetsViewModel.class );
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser == null ) {
            startActivity( new Intent( PetProfileEditActivity.this, LogInActivity.class ) );
            finish();
        }
        //TODO finish activity if not employee of this center
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back to the previous activity, when the back button of actionBar is clicked
        return super.onSupportNavigateUp();
    }


    private void showImageAttachMenu() {
        /* Show popup menu with options Camera, Gallery to pick image */
        // setup popup menu
        PopupMenu popupMenu = new PopupMenu( this, binding.profilePicPetEdit );
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
                    binding.profilePicPetEdit.setImageURI( imageUri );
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
                    if ( data != null ) {
                        imageUri = Objects.requireNonNull( data ).getData();

                        // set to imageview
                        binding.profilePicPetEdit.setImageURI( imageUri );
                    } else {
                        Timber.e( "uri is null" );
                    }
                } else {
                    // cancelled
                    Toast.makeText( this, "Cancelled", Toast.LENGTH_SHORT ).show();
                }
            }
    );

    private void validateData() {
        // get data
        String name = binding.PetEditName.getText().toString().trim();
        String species = binding.PetEditSpecies.getText().toString().trim();
        String breed = binding.PetEditBreed.getText().toString().trim();
        String birthDate = binding.PetEditBirthYear.getText().toString().trim();
        String description = binding.PetEditDescription.getText().toString().trim();

        // validate data
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get( Calendar.YEAR );

        // validate data
        if ( name.isEmpty() ) {
            binding.PetEditName.setError( "Please enter name" );
        } else if ( name.length() < 3 ) {
            binding.PetEditName.setError( "Name must contain at least 3 letters" );
        } else if ( species.isEmpty() ) {
            binding.PetEditName.setError( "Please enter species" );
        } else if ( species.length() < 3 ) {
            binding.PetEditName.setError( "Species must contain at least 3 letters" );
        } else if ( breed.isEmpty() ) {
            binding.PetEditName.setError( "Please enter breed" );
        } else if ( breed.length() < 3 ) {
            binding.PetEditName.setError( "Breed must contain at least 3 letters" );
        } else if ( birthDate.length() != 4 || Integer.parseInt( birthDate ) >= currentYear ) {
            binding.PetEditBirthYear.setError( "Invalid year" );
        } else {
            pet.setBreed( breed );
            pet.setBirthYear( birthDate );
            pet.setSpecies( species );
            pet.setDescription( description );
            pet.setName( name );
            if ( imageUri == null ) {
                // need to update without image
                updateProfile();
            } else {
                // update with image
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        // image path and name, use uid to replace previous
        String filePathAndName = "PetImages/" + firebaseAuth.getUid();

        // storage reference
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        storageReference.child( filePathAndName ).putFile( imageUri )
                .addOnSuccessListener( taskSnapshot -> {
                    // image uploaded, get url of uploaded image
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener( uri -> {
                        String uploadedImageUrl = uri.toString();
                        pet.setProfileImage( uploadedImageUrl );
                        updateProfile();
                    } );
                } )
                .addOnFailureListener( e -> {
                    // failed to upload image
                    Toast.makeText( this, "Failed to upload image due to " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                } );
    }

    void updateProfile() {
        rescuePetsViewModel.updatePojo( pet );
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ( rescuePetsViewModel == null ) {
            Timber.e( "rescuePetsViewModel was null onStart" );
            rescuePetsViewModel = new ViewModelProvider( this,
                    new RescuePetsViewModelFactory( getApplication() ) ).get( RescuePetsViewModel.class );
        }
        checkUser();
        rescuePetsViewModel.syncGet();
    }
}
