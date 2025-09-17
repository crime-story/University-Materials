package ro.robertto.rescuepets.domain;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ro.robertto.rescuepets.data.pojo.Address;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.BankInfo;
import ro.robertto.rescuepets.data.pojo.Center;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.RescuePetsPojo;
import ro.robertto.rescuepets.data.pojo.User;
import timber.log.Timber;

final class RescuePetsMediator implements PetsUseCase {
    private final RescuePetsLocalRepository localRepository;
    private final RescuePetsInMemoryRepository inMemoryRepository;
    private final WorkManager workManager;
    private final Data getDataForBuilder = ( new Data.Builder().putString( "mode", "get" ) ).build();
    private final Data postDataForBuilder = ( new Data.Builder().putString( "mode", "post" ) ).build();
    private final Data putDataForBuilder = ( new Data.Builder().putString( "mode", "put" ) ).build();

    RescuePetsMediator( RescuePetsLocalRepository rescuePetsLocalRepository, RescuePetsInMemoryRepository rescuePetsInMemoryRepository, WorkManager workManager ) {
        this.localRepository = rescuePetsLocalRepository;
        this.inMemoryRepository = rescuePetsInMemoryRepository;
        this.workManager = workManager;

        //The first get should be called instantly on constructor
        syncGet();

        //Periodic post request
        setPeriodicRequests();
    }

    @Override
    public void syncGet() {
        try {
            OneTimeWorkRequest downloadWorkRequest = new OneTimeWorkRequest.Builder( RescuePetsWorker.class ).setInputData( getDataForBuilder ).build();
            workManager.enqueue( downloadWorkRequest );
        } catch ( Exception e ) {
            Timber.e( e );
        }
    }

    @Override
    public LiveData< List< Pet > > getAllPets() {
        return localRepository.getAllPets();
    }

    @Override
    public LiveData< List< AdoptionForm > > getAllAdoptionForms() {
        return localRepository.getAllAdoptionForms();
    }

    @Override
    public LiveData< List< AdoptionForm > > getAdoptionFormsByUserUid( @NonNull String userUid ) {
        return localRepository.getAdoptionFormsByUserUid( userUid );
    }

    @Override
    public LiveData< List< BankInfo > > getBankInfoByCenterUid( @NonNull String centerUid ) {
        return localRepository.getBankInfoByCenterUid( centerUid );
    }

    @Override
    public void insertPojo( @NonNull RescuePetsPojo pojo ) {
        uploadToRemoteRepository( pojo, postDataForBuilder );//we can't directly insert in room, as we do not have an uid
    }

    @Override
    public void updatePojo( @NonNull RescuePetsPojo pojo ) {
        uploadToRemoteRepository( pojo, putDataForBuilder );
    }

    @Override
    public LiveData< Pet > getPet( @NonNull String petUid ) {
        return localRepository.getPet( petUid );
    }

    @Override
    public LiveData< Center > getCenter( @NonNull String centerUid ) {
        return localRepository.getCenter( centerUid );
    }

    @Override
    public LiveData< Address > getAddress( @NonNull String addressUid ) {
        return localRepository.getAddress( addressUid );
    }

    @Override
    public LiveData< Employee > getEmployee( @NonNull String employeeUid ) {
        return localRepository.getEmployee( employeeUid );
    }

    @Override
    public LiveData< AdoptionForm > getAdoptionForm( @NonNull String adoptionFormUid ) {
        return localRepository.getAdoptionForm( adoptionFormUid );
    }

    @Override
    public LiveData< User > getUser( @NonNull String userUid ) {
        getFromRemoteRepositoryByUid( userUid );
        return localRepository.getUser( userUid );
    }

    private void getFromRemoteRepositoryByUid( @NonNull String uid ) {
        try {
            Data getDataByUid = ( new Data.Builder().putString( "mode", "getByUid" ).putString( "uid", uid ) ).build();
            OneTimeWorkRequest downloadWorkRequest = new OneTimeWorkRequest.Builder( RescuePetsWorker.class ).setInputData( getDataByUid ).build();
            workManager.enqueue( downloadWorkRequest );
        } catch ( Exception e ) {
            Timber.e( e );
        }
    }

    private void uploadToRemoteRepository( RescuePetsPojo pojo, Data uploadMode ) {
        //queue for upload
        inMemoryRepository.addInMemory( pojo );

        //create work post request
        try {
            OneTimeWorkRequest postWorkRequest = new OneTimeWorkRequest.Builder( RescuePetsWorker.class ).setInputData( uploadMode ).build();
            workManager.enqueue( postWorkRequest );
        } catch ( Exception e ) {
            Timber.e( e );
        }
    }

    private void setPeriodicRequests() {
        try {
            PeriodicWorkRequest postWorkRequest = new PeriodicWorkRequest.Builder( RescuePetsWorker.class, PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS, TimeUnit.MILLISECONDS ).setInputData( postDataForBuilder ).build();
            workManager.enqueueUniquePeriodicWork( "postInMemory", ExistingPeriodicWorkPolicy.KEEP, postWorkRequest );
        } catch ( Exception e ) {
            Timber.d( e );
        }
    }
}
