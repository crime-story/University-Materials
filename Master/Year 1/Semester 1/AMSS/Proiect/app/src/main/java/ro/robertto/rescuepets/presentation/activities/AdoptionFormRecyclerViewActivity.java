package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.databinding.ActivityAdoptionFormListBinding;
import ro.robertto.rescuepets.presentation.utils.Utils;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsBindingAdapter;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModel;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModelFactory;
import timber.log.Timber;

public class AdoptionFormRecyclerViewActivity extends AppCompatActivity {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    RescuePetsViewModel rescuePetsViewModel;
    private ActionBarDrawerToggle toggle;

    ActivityAdoptionFormListBinding binding;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = ActivityAdoptionFormListBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        //butonul de sus pentru a inchide activitatea
        ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null ) {
            actionBar.setDisplayHomeAsUpEnabled( true );
            actionBar.setTitle( "Shelter Visits" );
        }

        DrawerLayout drawerLayout = findViewById( R.id.drawer_layout_adoption_form_list );
        NavigationView navView = findViewById( R.id.nav_view_adoption_form_list );

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

        // obtinem recycleView-ul si searchView-ul
        RecyclerView recyclerView = findViewById( R.id.recycler_view_adoption_form );
        SearchView searchView = findViewById( R.id.search_bar_adoption_form );

        //daca suntem pe jocurile unui singur utilizator, va fi setat fals la verificare
        //pentru a nu permite sa se creeze activitati la infinit pt a afisa jocurile unui utilizator
        boolean setOnClickListenerOnViewCards = true;

        RescuePetsBindingAdapter.recycleViewFormInit( recyclerView, this, setOnClickListenerOnViewCards );

        //obtinem ViewModel
        rescuePetsViewModel = new ViewModelProvider( this,
                new RescuePetsViewModelFactory( getApplication() ) ).get( RescuePetsViewModel.class );

        RescuePetsBindingAdapter.recycleViewFormsBind( recyclerView, this, rescuePetsViewModel, Objects.requireNonNull( firebaseAuth.getCurrentUser() ).getUid() );

        //activare searchView
        RescuePetsBindingAdapter.setFormsSearchViewFilter( recyclerView, searchView );

        // Is an employee

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if ( toggle.onOptionsItemSelected( item ) ) {
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser == null ) {
            startActivity( new Intent( this, LogInActivity.class ) );
            finish();
        } else {
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

