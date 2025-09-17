package ro.robertto.rescuepets.presentation.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.databinding.ActivityMapsBinding;
import ro.robertto.rescuepets.presentation.utils.Utils;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModel;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModelFactory;
import timber.log.Timber;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private AutocompleteSupportFragment autocompleteSupportFragment;
    private ActionBarDrawerToggle toggle;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    RescuePetsViewModel rescuePetsViewModel;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        binding = ActivityMapsBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null ) {
            actionBar.setTitle( "Rescue Pets Map" );
            actionBar.setDisplayHomeAsUpEnabled( true );
            DrawerLayout drawerLayout = findViewById( R.id.drawer_layout_map );
            NavigationView navView = findViewById( R.id.nav_view_map );

            if ( drawerLayout != null && navView != null ) {
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
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = binding.map.getFragment();
        if ( mapFragment != null )
            mapFragment.getMapAsync( this );
        else
            Timber.wtf( "mapFragment is null" );

        Places.initialize( getApplicationContext(), "AIzaSyBh-uT9rGUA1QhVtbpdKSTUiILh_VPg6fQ" );

        autocompleteSupportFragment = binding.autocompleteFragment.getFragment();
        if ( autocompleteSupportFragment != null )
            autocompleteSupportFragment.setPlaceFields( new ArrayList<>( Arrays.asList( Place.Field.ID, Place.Field.ADDRESS, Place.Field.LAT_LNG ) ) );
        else
            Timber.wtf( "autocompleteSupportFragment is null" );

        rescuePetsViewModel = new ViewModelProvider( this,
                new RescuePetsViewModelFactory( getApplication() ) ).get( RescuePetsViewModel.class );
    }

    @SuppressLint( "ResourceType" )
    @Override
    public void onMapReady( @NonNull GoogleMap googleMap ) {
        mMap = googleMap;

        mMap.setIndoorEnabled( true );
        mMap.setBuildingsEnabled( true );
        mMap.getUiSettings().setCompassEnabled( true );
        mMap.getUiSettings().setZoomControlsEnabled( true );
        mMap.getUiSettings().setIndoorLevelPickerEnabled( true );
        mMap.getUiSettings().setAllGesturesEnabled( true );

        if ( ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            startActivity( new Intent( MapsActivity.this, PermissionsActivity.class ) );
            finish();
            return;
        }
        mMap.setMyLocationEnabled( true );
        mMap.getUiSettings().setMyLocationButtonEnabled( true );

        FloatingActionButton floatingButton = binding.floatingButton;
        PopupMenu popupMenu = new PopupMenu( this, floatingButton );
        popupMenu.getMenuInflater().inflate( R.menu.map_options, popupMenu.getMenu() );
        popupMenu.setOnMenuItemClickListener( new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick( MenuItem item ) {
                if ( item == null )
                    return false;
                int itemId = item.getItemId();
                if ( itemId == R.id.normal_map )
                    mMap.setMapType( GoogleMap.MAP_TYPE_NORMAL );
                else if ( itemId == R.id.hybrid_map )
                    mMap.setMapType( GoogleMap.MAP_TYPE_HYBRID );
                else if ( itemId == R.id.satellite_map )
                    mMap.setMapType( GoogleMap.MAP_TYPE_SATELLITE );
                else if ( itemId == R.id.terrain_map ) {
                    mMap.setMapType( GoogleMap.MAP_TYPE_TERRAIN );
                } else
                    return false;
                return true;
            }
        } );

        floatingButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                popupMenu.show();
            }
        } );

        autocompleteSupportFragment.setOnPlaceSelectedListener( new PlaceSelectionListener() {
            @Override
            public void onError( @NonNull Status status ) {
                Timber.e( status.toString() );
                String message = status.getStatusMessage();
                if ( message != null )
                    Toast.makeText( MapsActivity.this, message, Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onPlaceSelected( @NonNull Place place ) {
                LatLng latLng = place.getLatLng();
                if ( latLng == null ) {
                    Timber.e( "latLng is null" );
                    return;
                }
                String name = place.getName();
                String address = place.getAddress();

                if ( address != null && !address.isEmpty() )
                    zoomOnMap( latLng, address );
                else if ( name != null && !name.isEmpty() )
                    zoomOnMap( latLng, name );
                else
                    zoomOnMap( latLng, latLng.toString() );

                if ( name != null ) {
                    Timber.d( name );
                    Toast.makeText( MapsActivity.this, name, Toast.LENGTH_LONG ).show();
                }
            }
        } );


        // Add a marker in Sydney and move the camera
        LatLng fmiUnibuc = new LatLng( 44.43563506131102, 26.099661800477286 );
        zoomOnMap( fmiUnibuc, "RescuePets Unibuc" );
    }

    void zoomOnMap( @Nullable LatLng latLng, @Nullable String markName ) {
        if ( latLng == null ) {
            Timber.e( "latLng is null" );
            return;
        }
        mMap.animateCamera( CameraUpdateFactory.newLatLngZoom( latLng, 16f ) );
        addMarkOnMap( latLng, markName );
    }

    void addMarkOnMap( @Nullable LatLng latLng, @Nullable String markName ) {
        if ( latLng == null ) {
            Timber.e( "latLng is null" );
            return;
        }
        if ( markName == null ) {
            Timber.e( "markName is null" );
            return;
        }

        mMap.addMarker( new MarkerOptions().position( latLng ).title( markName ) );
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back to the previous activity, when the back button of actionBar is clicked
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if ( toggle != null && toggle.onOptionsItemSelected( item ) ) {
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

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser == null ) {
            startActivity( new Intent( MapsActivity.this, LogInActivity.class ) );
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

}