package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsBindingAdapter;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModel;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModelFactory;
import timber.log.Timber;

public class PetRecyclerViewActivity extends AppCompatActivity {
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    RescuePetsViewModel rescuePetsViewModel;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_pet_list );

        //butonul de sus pentru a inchide activitatea
        ActionBar actionBar = getSupportActionBar();
        if ( actionBar != null )
            actionBar.setDisplayHomeAsUpEnabled( true );

        // obtinem recycleView-ul si searchView-ul
        RecyclerView mRecyclerView = findViewById( R.id.recycler_view_pet );
        SearchView searchView = findViewById( R.id.search_bar_pet );

        //daca suntem pe jocurile unui singur utilizator, va fi setat fals la verificare
        //pentru a nu permite sa se creeze activitati la infinit pt a afisa jocurile unui utilizator
        boolean setOnClickListenerOnViewCards = true;

        RescuePetsBindingAdapter.recycleViewPetInit( mRecyclerView, setOnClickListenerOnViewCards );

        //obtinem ViewModel
        rescuePetsViewModel = new ViewModelProvider( this,
                new RescuePetsViewModelFactory( getApplication() ) ).get( RescuePetsViewModel.class );

        //binding pentru a prelua datele din repository
        RescuePetsBindingAdapter.recycleViewPetsBind( mRecyclerView, this, rescuePetsViewModel );

        //activare searchView
        RescuePetsBindingAdapter.setPetSearchViewFilter( mRecyclerView, searchView );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ( rescuePetsViewModel == null ) {
            Timber.e( "rescuePetsViewModel was null onStart" );
            rescuePetsViewModel = new ViewModelProvider( this,
                    new RescuePetsViewModelFactory( getApplication() ) ).get( RescuePetsViewModel.class );
        }
        checkEmployee();
        rescuePetsViewModel.syncGet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if ( item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected( item );
    }

    private void checkEmployee() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser == null ) {
            startActivity( new Intent( this, LogInActivity.class ) );
            finish();
        } else {
            LiveData< Employee > employeeLiveData = rescuePetsViewModel.getEmployee( firebaseUser.getUid() );
            if ( employeeLiveData != null ) {
                employeeLiveData.observe( this, new Observer< Employee >() {
                    @Override
                    public void onChanged( Employee employee ) {
                        if ( employee == null ) {
                            Timber.d( "not employee" );
                            return;
                        }

                        View view = findViewById( R.id.addNewPet );
                        if ( view == null ) {
                            Timber.e( "add pet button view not found" );
                            return;
                        }
                        view.setVisibility( View.VISIBLE );
                        view.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick( View v ) {
                                startActivity( new Intent( PetRecyclerViewActivity.this, PetProfileEditActivity.class ) );
                            }
                        } );
                    }
                } );
            }
        }
    }
}

