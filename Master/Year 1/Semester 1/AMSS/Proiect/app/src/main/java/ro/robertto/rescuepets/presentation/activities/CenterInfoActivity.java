package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.databinding.ActivityCenterInfoBinding;
import ro.robertto.rescuepets.presentation.utils.Utils;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsBindingAdapter;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModel;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModelFactory;
import timber.log.Timber;

public class CenterInfoActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    RescuePetsViewModel rescuePetsViewModel;
    static private final String centerUid = "hashCenter1";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        // ViewBinding
        ActivityCenterInfoBinding binding = ActivityCenterInfoBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        // ActionBar
        ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null ) {
            actionBar.setTitle( "Shelter Contact" );
            actionBar.setDisplayHomeAsUpEnabled( true );
        }

        DrawerLayout drawerLayout = findViewById( R.id.drawer_layout_center );
        NavigationView navView = findViewById( R.id.nav_view_center );

        toggle = new ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close );

        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();

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

        rescuePetsViewModel = new ViewModelProvider( this,
                new RescuePetsViewModelFactory( getApplication() ) ).get( RescuePetsViewModel.class );
        RescuePetsBindingAdapter.centerProfileBind( binding, binding.getRoot(), this, rescuePetsViewModel, centerUid );
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser == null ) {
            startActivity( new Intent( CenterInfoActivity.this, LogInActivity.class ) );
            finish();
        } else if ( rescuePetsViewModel != null ) {
            LiveData< User > userLiveData = rescuePetsViewModel.getUser( firebaseUser.getUid() );

            if ( userLiveData == null )
                return;
            userLiveData.observe( this, new Observer< User >() {
                @Override
                public void onChanged( User user ) {
                    if ( user == null )
                        return;

                    TextView nameTextView = findViewById( R.id.user_name );
                    if ( nameTextView != null )
                        nameTextView.setText( user.getName() );
                    TextView emailTextView = findViewById( R.id.user_email );
                    if ( emailTextView != null )
                        emailTextView.setText( user.getEmail() );
                }
            } );
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back to the previous activity, when the back button of actionBar is clicked
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if ( toggle.onOptionsItemSelected( item ) ) {
            return true;
        }
        return super.onOptionsItemSelected( item );
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
