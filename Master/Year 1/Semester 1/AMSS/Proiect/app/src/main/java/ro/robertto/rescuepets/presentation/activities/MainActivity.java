package ro.robertto.rescuepets.presentation.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ro.robertto.rescuepets.databinding.ActivityMainBinding;
import ro.robertto.rescuepets.presentation.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ActionBar actionBar;
    private SharedPreferences sharedPreferences;
    private final Handler handler = new Handler( Looper.getMainLooper() );

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        actionBar = getSupportActionBar();
        if ( actionBar != null ) {
            actionBar.setTitle( "Home" );
        }

        handler.postDelayed( this::rotater, 1000 );

        binding.profileBtn.setOnClickListener( view -> {
            startActivity( new Intent( MainActivity.this, ProfileActivity.class ) );
        } );

        binding.mapBtn.setOnClickListener( view -> {
            startActivity( new Intent( MainActivity.this, PermissionsActivity.class ) );
        } );

        binding.PetsBtn.setOnClickListener( view -> {
            startActivity( new Intent( MainActivity.this, PetRecyclerViewActivity.class ) );
        } );

        binding.assistantsListBtn.setOnClickListener( view -> {
            startActivity( new Intent( MainActivity.this, AssistantsListForUsersActivity.class ) );
        } );

        binding.formsBtn.setOnClickListener( view -> {
            startActivity( new Intent( MainActivity.this, AdoptionFormRecyclerViewActivity.class ) );
        } );

        binding.shelterInfoBtn.setOnClickListener( view -> {
            startActivity( new Intent( MainActivity.this, CenterInfoActivity.class ) );
        } );

        binding.shareBtn.setOnClickListener( view -> {
            Utils.shareButtonFunctionality( MainActivity.this );
        } );

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if ( firebaseAuth.getCurrentUser() != null ) {
            String uid = firebaseAuth.getCurrentUser().getUid();
            sharedPreferences = getSharedPreferences( "ro.robertto.rescuepets", MODE_PRIVATE );
            if ( !sharedPreferences.contains( uid ) || sharedPreferences.getBoolean( uid, true ) ) {
                sharedPreferences.edit().putBoolean( uid, false ).apply();
                startActivity( new Intent( MainActivity.this, GuidePage1.class ) );
            }
        }
    }

    private void rotater() {
        animateButton( binding.mapBtn );
        animateButton( binding.profileBtn );
        animateButton( binding.PetsBtn );
        animateButton( binding.assistantsListBtn );
        animateButton( binding.formsBtn );
        animateButton( binding.shareBtn );
        animateButton( binding.shelterInfoBtn );
    }

    private void animateButton( View button ) {
        ObjectAnimator anim = ObjectAnimator.ofFloat( button, View.ROTATION, -360f, 0f );
        anim.setDuration( 500 );
        anim.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUser();
    }

    private void checkUser() {
        // Check user is logged in or not
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser == null ) {
            // user is null, user is not logged in, go to login activity
            startActivity( new Intent( this, LogInActivity.class ) );
            finish();
        }
    }

}
