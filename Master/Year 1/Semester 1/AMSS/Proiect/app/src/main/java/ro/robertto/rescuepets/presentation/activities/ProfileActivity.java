package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.databinding.ActivityProfileBinding;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;
import ro.robertto.rescuepets.presentation.utils.Utils;
import timber.log.Timber;

public class ProfileActivity extends AppCompatActivity {

    // ViewBinding
    private ActivityProfileBinding binding;

    // Database
    private DatabaseReference database;

    // ActionBar
    private ActionBar actionBar;

    // FirebaseAuth
    private FirebaseAuth firebaseAuth;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = ActivityProfileBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        actionBar = getSupportActionBar();
        if ( actionBar != null ) {
            actionBar.setTitle( "Profile" );
        }
        DrawerLayout drawerLayout = findViewById( R.id.drawer_layout );
        NavigationView navView = findViewById( R.id.nav_view );
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        checkUser();

        toggle = new ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close );

        DatabaseReference db = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                .getReference( "Users" );
        db.child( firebaseUser.getUid() ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                Object maybeName = snapshot.child( "name" ).getValue();
                if ( maybeName != null ) {
                    String name = String.valueOf( maybeName );
                    TextView nameTextView = findViewById( R.id.user_name );
                    if ( nameTextView != null ) {
                        nameTextView.setText( name );
                    }
                }
                Object maybeEmail = snapshot.child( "email" ).getValue();
                if ( maybeEmail != null ) {
                    String email = String.valueOf( maybeEmail );
                    TextView emailTextView = findViewById( R.id.user_email );
                    if ( emailTextView != null ) {
                        emailTextView.setText( email );
                    }
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {
            }
        } );

        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();
        actionBar.setDisplayHomeAsUpEnabled( true );

        navView.setNavigationItemSelectedListener( item -> {
            int itemId = item.getItemId();
            if ( itemId == R.id.nav_assistants ) {
                startActivity( new Intent( ProfileActivity.this, AssistantsListForUsersActivity.class ) );
            } else if ( itemId == R.id.nav_map ) {
                startActivity( new Intent( ProfileActivity.this, PermissionsActivity.class ) );
            } else if ( itemId == R.id.nav_pets ) {
                startActivity( new Intent( ProfileActivity.this, PetAdoptActivity.class ) );
            } else if ( itemId == R.id.nav_shelter_visits ) {
                startActivity( new Intent( ProfileActivity.this, AdoptionFormRecyclerViewActivity.class ) );
            } else if ( itemId == R.id.nav_shelter_contact ) {
                startActivity( new Intent( ProfileActivity.this, CenterInfoActivity.class ) );
            } else if ( itemId == R.id.nav_profile ) {
                startActivity( new Intent( ProfileActivity.this, ProfileActivity.class ) );
            } else if ( itemId == R.id.nav_logout ) {
                firebaseAuth.signOut();
                startActivity( new Intent( ProfileActivity.this, LogInActivity.class ) );
            } else if ( itemId == R.id.nav_share_app ) {
                new Utils().shareButtonFunctionality( ProfileActivity.this );
            } else if ( itemId == R.id.nav_show_guide ) {
                startActivity( new Intent( ProfileActivity.this, GuidePage1.class ) );
            } else if ( itemId == R.id.nav_show_tutorial_video ) {
                startActivity( new Intent( ProfileActivity.this, TutorialVideoActivity.class ) );
            }
            return true;
        } );

        // Handle click, logOut
        binding.logoutBtn.setOnClickListener( view -> {
            firebaseAuth.signOut();
            checkUser();
        } );

        // handle click, open edit profile
        binding.profileEditBtn.setOnClickListener( view -> {
            startActivity( new Intent( ProfileActivity.this, ProfileEditActivity.class ) );
        } );

        database = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                .getReference( "Users" );

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot dataSnapshot ) {
                User userProfile = dataSnapshot.getValue( User.class );
                if ( userProfile != null ) {
                    String name = userProfile.getName();
                    String email = userProfile.getEmail();
                    String phoneNumber = userProfile.getPhoneNumber();
                    String yearOfBirth = userProfile.getBirthDate();
                    String profileImage = userProfile.getProfileImage();

                    binding.nameUpEt.setText( name );
                    binding.nameEt.setText( name );
                    binding.emailEt.setText( email );
                    binding.phoneNumberEt.setText( String.valueOf( phoneNumber ) );
                    binding.birthDateEt.setText( yearOfBirth );
                    // set image
                    try {
                        Glide.with( ProfileActivity.this ).load( profileImage )
                                .placeholder( R.drawable.profile_pic )
                                .into( ( ImageView ) binding.profilePic );
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {
                // Failed to read value
                Timber.w( error.toException(), "Failed." );
            }
        };

        database.child( firebaseUser.getUid() ).addListenerForSingleValueEvent( postListener );
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back to the previous activity, when the back button of actionBar is clicked
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        if ( toggle.onOptionsItemSelected( item ) ) {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }
}
