package ro.robertto.rescuepets.presentation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import javax.inject.Inject;

import ro.robertto.rescuepets.data.pojo.Address;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.BankInfo;
import ro.robertto.rescuepets.data.pojo.Center;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.RescuePetsPojo;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.domain.PetsUseCase;
import ro.robertto.rescuepets.domain.RescuePetsDependencyProviderModule;
import timber.log.Timber;

public final class RescuePetsViewModel extends AndroidViewModel implements PetsUseCase {

    @Inject
    PetsUseCase petsUseCase = null;

    RescuePetsViewModel( Application application ) {
        super( application );
        Timber.tag( "RescuePetsViewModel" ).d( "RescuePetsViewModel constructor called" );
    }

    @Override
    public LiveData< List< Pet > > getAllPets() {
        return safePetsUseCase().getAllPets();
    }

    @Override
    public LiveData< List< BankInfo > > getBankInfoByCenterUid( @NonNull String centerUid ) {
        return safePetsUseCase().getBankInfoByCenterUid( centerUid );
    }

    @Override
    public LiveData< Pet > getPet( @NonNull String petUid ) {
        return safePetsUseCase().getPet( petUid );
    }

    @Override
    public LiveData< Center > getCenter( @NonNull String centerUid ) {
        return safePetsUseCase().getCenter( centerUid );
    }

    @Override
    public LiveData< Address > getAddress( @NonNull String addressUid ) {
        return safePetsUseCase().getAddress( addressUid );
    }

    @Override
    public void insertPojo( @NonNull RescuePetsPojo rescuePetsPojo ) {
        safePetsUseCase().insertPojo( rescuePetsPojo );
    }

    @Override
    public void updatePojo( @NonNull RescuePetsPojo rescuePetsPojo ) {
        safePetsUseCase().updatePojo( rescuePetsPojo );
    }

    @Override
    public @Nullable LiveData< Employee > getEmployee( @NonNull String employeeUid ) {
        return safePetsUseCase().getEmployee( employeeUid );
    }

    @Override
    public @Nullable LiveData< User > getUser( @NonNull String userUid ) {
        return safePetsUseCase().getUser( userUid );
    }

    @Override
    public @Nullable LiveData< AdoptionForm > getAdoptionForm( @NonNull String adoptionFormUid ) {
        return safePetsUseCase().getAdoptionForm( adoptionFormUid );
    }

    @Override
    public LiveData< List< AdoptionForm > > getAllAdoptionForms() {
        return safePetsUseCase().getAllAdoptionForms();
    }

    @Override
    public LiveData< List< AdoptionForm > > getAdoptionFormsByUserUid( @NonNull String userUid ) {
        return safePetsUseCase().getAdoptionFormsByUserUid( userUid );
    }

    @Override
    public void syncGet() {
        safePetsUseCase().syncGet();
    }

    private PetsUseCase safePetsUseCase() {
        if ( petsUseCase == null ) {
            Timber.e( "dagger injection failed, fallback to trivial dependency provider" );
            RescuePetsDependencyProviderModule rescuePetsDependencyProviderModule = new RescuePetsDependencyProviderModule( RescuePetsViewModel.this.getApplication() );
            petsUseCase = rescuePetsDependencyProviderModule.providePetsUseCase();
        } else {
            Timber.d( "dagger injection succeeded" );
        }
        return petsUseCase;
    }
}
