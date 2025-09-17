package ro.robertto.rescuepets.presentation.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.databinding.ActivityAssistantsListForUsersBinding;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;
import ro.robertto.rescuepets.presentation.adapter.EmployeeAdapter;
import ro.robertto.rescuepets.presentation.utils.Utils;

public class AssistantsListForUsersActivity extends AppCompatActivity {

    private ActivityAssistantsListForUsersBinding binding;
    private FirebaseDatabase database;
    private SearchView searchView;
    private ArrayList< Employee > employees;
    private EmployeeAdapter employeeAdapter;
    private ProgressDialog dialog;
    private ActionBarDrawerToggle toggle;
    private ActionBar actionBar;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = ActivityAssistantsListForUsersBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        actionBar = getSupportActionBar();
        if ( actionBar != null ) {
            actionBar.setTitle( "Assistants" );
        }

        DrawerLayout drawerLayout = findViewById( R.id.drawer_layout );
        NavigationView navView = findViewById( R.id.nav_view );
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        toggle = new ActionBarDrawerToggle( this, drawerLayout, R.string.open, R.string.close );

        FirebaseDatabase db = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl );
        DatabaseReference dbCollection = db.getReference( "Center/hashCenter1/Employee" );
        dbCollection.child( firebaseUser.getUid() ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                String email = snapshot.child( "email" ).getValue( String.class );
                String name = snapshot.child( "name" ).getValue( String.class );

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

        dialog = new ProgressDialog( this );
        dialog.setMessage( "Uploading image..." );
        dialog.setCancelable( false );
        database = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl );
        employees = new ArrayList<>();
        employeeAdapter = new EmployeeAdapter( this, employees );

        GridLayoutManager layoutManagerPortrait = new GridLayoutManager( this, 2 );
        GridLayoutManager layoutManagerLandscape = new GridLayoutManager( this, 3 );
        RecyclerView mRec = binding.mRec;
        if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ) {
            mRec.setLayoutManager( layoutManagerLandscape );
        } else {
            mRec.setLayoutManager( layoutManagerPortrait );
        }
        mRec.setAdapter( employeeAdapter );

        searchView = findViewById( R.id.searchView );

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit( String newText ) {
                return false;
            }

            @Override
            public boolean onQueryTextChange( String newText ) {
                filterList( newText != null ? newText.toLowerCase() : null );
                return true;
            }
        } );

        DatabaseReference usersReference = database.getReference( "Center/hashCenter1/Employee" );
        usersReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange( @NonNull DataSnapshot snapshot ) {
                employees.clear();
                for ( DataSnapshot snapshot1 : snapshot.getChildren() ) {
                    Employee employee = snapshot1.getValue( Employee.class );
                    if ( employee != null && !employee.getEmail().equals( FirebaseAuth.getInstance().getCurrentUser().getEmail() ) && employee.getCenterUid() != null ) {
                        if ( snapshot1.getKey() != null )
                            employee.setUid( snapshot1.getKey() );
                        employees.add( employee );
                    }
                    employeeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled( @NonNull DatabaseError error ) {
            }
        } );
    }

    private void filterList( String query ) {
        if ( query != null ) {
            ArrayList< Employee > filteredList = new ArrayList<>();
            for ( Employee employee : employees ) {
                if ( employee.getName() != null && employee.getName().toLowerCase( Locale.ROOT ).contains( query ) ) {
                    filteredList.add( employee );
                }
            }
            if ( filteredList.isEmpty() ) {
                Toast.makeText( this, "No assistants found", Toast.LENGTH_SHORT ).show();
            }
            employeeAdapter.setFilteredList( filteredList );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        if ( currentId != null ) {
            database.getReference().child( "Presence" ).child( currentId ).setValue( "Online" );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        if ( currentId != null ) {
            database.getReference().child( "Presence" ).child( currentId ).setValue( "Offline" );
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
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
