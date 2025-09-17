package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;
import ro.robertto.rescuepets.presentation.utils.Utils;

public class TutorialVideoActivity extends AppCompatActivity {
    private ActionBar actionBar;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_tutorial_video );

        actionBar = getSupportActionBar();
        if ( actionBar != null ) {
            actionBar.setTitle( "Tutorial - Rescue Pets App" );
        }

        DrawerLayout drawerLayout = findViewById( R.id.drawer_layout );
        NavigationView navView = findViewById( R.id.nav_view );
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        toggle = new ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close );

        DatabaseReference database = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                .getReference( "Users" );
        database.child( firebaseUser.getUid() ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                String email = snapshot.child( "email" ).getValue().toString();
                String name = snapshot.child( "name" ).getValue().toString();

                TextView nameTextView = findViewById( R.id.user_name );
                if ( nameTextView != null )
                    nameTextView.setText( name );
                TextView emailTextView = findViewById( R.id.user_email );
                if ( emailTextView != null )
                    emailTextView.setText( email );
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {
            }
        } );

        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();
        if ( actionBar != null ) {
            actionBar.setDisplayHomeAsUpEnabled( true );
        }

        navView.setNavigationItemSelectedListener( item -> {
            int itemId = item.getItemId();
            if ( itemId == R.id.nav_assistants ) {
                startActivity( new Intent( this, AssistantsListForUsersActivity.class ) );
            } else if ( itemId == R.id.nav_map ) {
                startActivity( new Intent( this, PermissionsActivity.class ) );
            } else if ( itemId == R.id.nav_pets ) {
                startActivity( new Intent( this, PetAdoptActivity.class ) );
            } else if ( itemId == R.id.nav_shelter_visits ) {
                startActivity( new Intent( this, AdoptionFormRecyclerViewActivity.class ) );
            } else if ( itemId == R.id.nav_shelter_contact ) {
                startActivity( new Intent( this, CenterInfoActivity.class ) );
            } else if ( itemId == R.id.nav_profile ) {
                startActivity( new Intent( this, ProfileActivity.class ) );
            } else if ( itemId == R.id.nav_logout ) {
                firebaseAuth.signOut();
                startActivity( new Intent( this, LogInActivity.class ) );
            } else if ( itemId == R.id.nav_share_app ) {
                Utils.shareButtonFunctionality( this );
            } else if ( itemId == R.id.nav_show_guide ) {
                startActivity( new Intent( this, GuidePage1.class ) );
            } else if ( itemId == R.id.nav_show_tutorial_video ) {
                startActivity( new Intent( this, TutorialVideoActivity.class ) );
            }
            return true;
        } );

        VideoView videoView = findViewById( R.id.video_view );
        MediaController mediaController = new MediaController( this );
        mediaController.setAnchorView( videoView );

        Uri videoUri = Uri.parse( "android.resource://" + getPackageName() + "/" + R.raw.tutorial );

        videoView.setMediaController( mediaController );
        videoView.setVideoURI( videoUri );
        videoView.requestFocus();
        videoView.start();
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        if ( toggle.onOptionsItemSelected( item ) ) {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }
}
