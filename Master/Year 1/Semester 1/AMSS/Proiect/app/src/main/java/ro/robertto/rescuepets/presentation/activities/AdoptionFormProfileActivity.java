package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

import java.util.Objects;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.databinding.ActivityAdoptionFormProfileBinding;
import ro.robertto.rescuepets.presentation.utils.Utils;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsBindingAdapter;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModel;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModelFactory;
import timber.log.Timber;

public class AdoptionFormProfileActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private RescuePetsViewModel rescuePetsViewModel;
    private ActivityAdoptionFormProfileBinding binding;
    private String adoptionFormUid;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        // ViewBinding
        binding = ActivityAdoptionFormProfileBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        // ActionBar
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull( actionBar ).setTitle( "Shelter Visit Request" );
        DrawerLayout drawerLayout = findViewById( R.id.drawer_layout );
        NavigationView navView = findViewById( R.id.nav_view );

        rescuePetsViewModel = new ViewModelProvider( this,
                new RescuePetsViewModelFactory( getApplication() ) ).get( RescuePetsViewModel.class );

        toggle = new ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close );
        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();
        actionBar.setDisplayHomeAsUpEnabled( true );

        navView.setNavigationItemSelectedListener( item -> {
            int itemId = item.getItemId();
            if ( itemId == R.id.nav_assistants ) {
                startActivity( new Intent( AdoptionFormProfileActivity.this, AssistantsListForUsersActivity.class ) );
            } else if ( itemId == R.id.nav_map ) {
                startActivity( new Intent( AdoptionFormProfileActivity.this, PermissionsActivity.class ) );
            } else if ( itemId == R.id.nav_pets ) {
                startActivity( new Intent( this, AdoptionFormProfileActivity.class ) );
            } else if ( itemId == R.id.nav_profile ) {
                startActivity( new Intent( AdoptionFormProfileActivity.this, ProfileActivity.class ) );
            } else if ( itemId == R.id.nav_logout ) {
                firebaseAuth.signOut();
                startActivity( new Intent( AdoptionFormProfileActivity.this, LogInActivity.class ) );
            } else if ( itemId == R.id.nav_share_app ) {
                Utils.shareButtonFunctionality( AdoptionFormProfileActivity.this );
            } else if ( itemId == R.id.nav_show_guide ) {
                startActivity( new Intent( AdoptionFormProfileActivity.this, GuidePage1.class ) );
            } else if ( itemId == R.id.nav_show_tutorial_video ) {
                startActivity( new Intent( AdoptionFormProfileActivity.this, TutorialVideoActivity.class ) );
            }
            return true;
        } );

        Bundle b = getIntent().getExtras();
        if ( b == null ) return;

        adoptionFormUid = Objects.requireNonNull( b ).getString( "adoptionFormUid" );
        if ( adoptionFormUid == null ) return;


        RescuePetsBindingAdapter.adoptionFormProfileBind( binding, binding.getRoot(), this, rescuePetsViewModel, adoptionFormUid );

    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser == null ) {
            startActivity( new Intent( AdoptionFormProfileActivity.this, LogInActivity.class ) );
            finish();
        } else {
            LiveData< Employee > employeeLiveData = rescuePetsViewModel.getEmployee( firebaseUser.getUid() );
            if ( employeeLiveData == null ) return;
            employeeLiveData.observe( this, new Observer< Employee >() {
                @Override
                public void onChanged( Employee employee ) {
                    if ( employee == null )
                        return;

                    LiveData< AdoptionForm > adoptionFormLiveData = rescuePetsViewModel.getAdoptionForm( adoptionFormUid );
                    if ( adoptionFormLiveData == null )
                        return;
                    adoptionFormLiveData.observe( AdoptionFormProfileActivity.this, new Observer< AdoptionForm >() {
                        @Override
                        public void onChanged( AdoptionForm adoptionForm ) {
                            if ( adoptionForm == null )
                                return;

                            binding.ApproveButton.setVisibility( View.VISIBLE );
                            binding.RejectButton.setVisibility( View.VISIBLE );

                            binding.ApproveButton.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick( View v ) {
                                    adoptionForm.setStatus( true );
                                    adoptionForm.setEmployeeUid( firebaseUser.getUid() );
                                    rescuePetsViewModel.updatePojo( adoptionForm );
                                }
                            } );

                            binding.RejectButton.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick( View v ) {
                                    adoptionForm.setStatus( false );
                                    adoptionForm.setEmployeeUid( firebaseUser.getUid() );
                                    rescuePetsViewModel.updatePojo( adoptionForm );
                                }
                            } );
                        }
                    } );

                }
            } );
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
