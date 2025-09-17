package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.databinding.ActivityPetAdoptCreateBinding;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModel;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModelFactory;
import timber.log.Timber;

public class PetAdoptActivity extends AppCompatActivity {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private ActivityPetAdoptCreateBinding binding;
    private RescuePetsViewModel rescuePetsViewModel;
    private Pet pet = null;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        // ViewBinding
        binding = ActivityPetAdoptCreateBinding.inflate( getLayoutInflater() );
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
            binding.nameUpPetAdopt.setText( pet.getName() );
            String profileImageUrl = pet.getProfileImage();
            try {
                if ( profileImageUrl != null && !profileImageUrl.isEmpty() )
                    Glide.with( this ).load( profileImageUrl )
                            .placeholder( R.drawable.profile_pic )
                            .into( binding.profilePicPetAdopt );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } else {
            Timber.e( "pet adopt activity with now pet" );
            startActivity( new Intent( this, PetRecyclerViewActivity.class ) );
            finish();
        }

        binding.bookAppointmentBtn.setOnClickListener( view -> validateData() );
        rescuePetsViewModel = new ViewModelProvider( this,
                new RescuePetsViewModelFactory( getApplication() ) ).get( RescuePetsViewModel.class );
    }

    private void checkUser() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser == null ) {
            startActivity( new Intent( this, LogInActivity.class ) );
            finish();
        } else {
            LiveData< User > userLiveData = rescuePetsViewModel.getUser( firebaseUser.getUid() );
            if ( userLiveData != null ) {
                userLiveData.observe( this, new Observer< User >() {
                    @Override
                    public void onChanged( User user ) {
                        if ( user != null ) {
                            binding.nickname.setText( user.getName() );
                            binding.contactPhone.setText( user.getPhoneNumber() );
                            binding.contactEmail.setText( user.getEmail() );
                        }
                    }
                } );
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back to the previous activity, when the back button of actionBar is clicked
        return super.onSupportNavigateUp();
    }

    private void validateData() {
        // get data
        String name = binding.nickname.getText().toString().trim();
        String email = binding.contactEmail.getText().toString().trim();
        String phoneNumber = binding.contactPhone.getText().toString().trim();
        String appointmentDate = binding.appointmentDate.getText().toString().trim();
        String comment = binding.appointmentComment.getText().toString().trim();

        // validate data
        Calendar calendar = Calendar.getInstance();

        // validate data
        if ( !Patterns.EMAIL_ADDRESS.matcher( email ).matches() ) {
            // invalid email format
            binding.contactEmail.setError( "Invalid email format" );
        } else if ( name.isEmpty() ) {
            binding.nickname.setError( "Please enter name" );
        } else if ( name.length() < 3 ) {
            binding.nickname.setError( "Name must contain at least 3 letters" );
        } else if ( !Patterns.PHONE.matcher( phoneNumber ).matches() ) {
            binding.contactPhone.setError( "Invalid phone format" );
        } else if ( appointmentDate.length() < 4 ) {
            binding.appointmentDate.setError( "Invalid date" );
        } else {
            AdoptionForm adoptionForm = new AdoptionForm( "", pet.getUid(), firebaseUser.getUid(), null, name, email, phoneNumber, appointmentDate, comment, null );
            rescuePetsViewModel.insertPojo( adoptionForm );
            finish();
        }
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
