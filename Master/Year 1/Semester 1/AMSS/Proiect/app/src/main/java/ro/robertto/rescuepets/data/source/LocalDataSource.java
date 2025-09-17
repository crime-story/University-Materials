package ro.robertto.rescuepets.data.source;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import ro.robertto.rescuepets.data.pojo.Address;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.BankInfo;
import ro.robertto.rescuepets.data.pojo.Center;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.domain.RescuePetsLocalRepository;
import timber.log.Timber;

public final class LocalDataSource extends RescuePetsLocalRepository {
    final RescuePetsDao rescuePetsDao;

    public LocalDataSource( Context context ) {
        super( context );
        rescuePetsDao = AppDatabase.getAppDatabase( context ).rescuePetsDao();
    }

    @Override
    protected void insertAddress( Address address ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                rescuePetsDao.insertAddress( address );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } );
    }

    @Override
    protected void insertCenter( Center center ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                rescuePetsDao.insertCenter( center );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } );
    }

    @Override
    protected void insertBankInfo( BankInfo bankInfo ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                rescuePetsDao.insertBankInfo( bankInfo );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } );
    }

    @Override
    protected void insertEmployee( Employee employee ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                rescuePetsDao.insertEmployee( employee );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } );
    }

    @Override
    protected void insertPet( Pet pet ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                rescuePetsDao.insertPet( pet );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } );
    }

    @Override
    protected void insertAdoptionForm( AdoptionForm adoptionForm ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                rescuePetsDao.insertAdoptionForm( adoptionForm );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } );
    }

    @Override
    protected void insertUser( User user ) {
        AppDatabase.databaseWriteExecutor.execute( () -> {
            try {
                rescuePetsDao.insertUser( user );
            } catch ( Exception e ) {
                Timber.e( e );
            }
        } );
    }

    @Override
    protected LiveData< List< Pet > > getAllPets() {
        return rescuePetsDao.getAllPets();
    }

    @Override
    protected LiveData< List< AdoptionForm > > getAllAdoptionForms() {
        return rescuePetsDao.getAllAdoptionForms();
    }

    @Override
    protected LiveData< List< BankInfo > > getBankInfoByCenterUid( @NonNull String centerUid ) {
        return rescuePetsDao.getBankInfoByCenterUid( centerUid );
    }

    @Override
    protected LiveData< List< AdoptionForm > > getAdoptionFormsByUserUid( @NonNull String userUid ) {
        return rescuePetsDao.getAdoptionFormsByUserUid( userUid );
    }

    @Override
    protected LiveData< Pet > getPet( @NonNull String petUid ) {
        return rescuePetsDao.getPet( petUid );
    }

    @Override
    protected LiveData< Center > getCenter( @NonNull String centerUid ) {
        return rescuePetsDao.getCenter( centerUid );
    }

    @Override
    protected LiveData< Address > getAddress( @NonNull String addressUid ) {
        return rescuePetsDao.getAddress( addressUid );
    }

    @Override
    protected LiveData< Employee > getEmployee( @NonNull String employeeUid ) {
        return rescuePetsDao.getEmployee( employeeUid );
    }

    @Override
    protected LiveData< User > getUser( @NonNull String userUid ) {
        return rescuePetsDao.getUser( userUid );
    }

    @Override
    protected LiveData< AdoptionForm > getAdoptionForm( @NonNull String adoptionFormUid ) {
        return rescuePetsDao.getAdoptionForm( adoptionFormUid );
    }

    @Dao
    protected interface RescuePetsDao {
        @Query( "SELECT * FROM PET" )
        LiveData< List< Pet > > getAllPets();

        @Query( "SELECT * FROM ADOPTIONFORM" )
        LiveData< List< AdoptionForm > > getAllAdoptionForms();

        @Query( "SELECT * FROM BANKINFO WHERE BANKINFO.centerUid=:centerUid" )
        LiveData< List< BankInfo > > getBankInfoByCenterUid( String centerUid );

        @Query( "SELECT * FROM ADOPTIONFORM WHERE ADOPTIONFORM.userUid=:userUid" )
        LiveData< List< AdoptionForm > > getAdoptionFormsByUserUid( String userUid );

        @Query( "SELECT * FROM PET WHERE PET.uid=:petUid" )
        LiveData< Pet > getPet( String petUid );

        @Query( "SELECT * FROM CENTER WHERE Center.uid=:centerUid" )
        LiveData< Center > getCenter( String centerUid );

        @Query( "SELECT * FROM ADDRESS WHERE Address.uid=:addressUid" )
        LiveData< Address > getAddress( String addressUid );

        @Query( "SELECT * FROM EMPLOYEE WHERE EMPLOYEE.uid=:employeeUid" )
        LiveData< Employee > getEmployee( String employeeUid );

        @Query( "SELECT * FROM User WHERE User.uid=:userUid" )
        LiveData< User > getUser( String userUid );

        @Query( "SELECT * FROM AdoptionForm WHERE AdoptionForm.uid=:adoptionFormUid" )
        LiveData< AdoptionForm > getAdoptionForm( String adoptionFormUid );

        @Insert( onConflict = OnConflictStrategy.REPLACE )
        void insertAddress( Address address );

        @Insert( onConflict = OnConflictStrategy.REPLACE )
        void insertBankInfo( BankInfo bankInfo );

        @Insert( onConflict = OnConflictStrategy.REPLACE )
        void insertCenter( Center center );

        @Insert( onConflict = OnConflictStrategy.REPLACE )
        void insertEmployee( Employee employee );

        @Insert( onConflict = OnConflictStrategy.REPLACE )
        void insertPet( Pet pet );

        @Insert( onConflict = OnConflictStrategy.REPLACE )
        void insertAdoptionForm( AdoptionForm adoptionForm );

        @Insert( onConflict = OnConflictStrategy.REPLACE )
        void insertUser( User user );
    }
}
