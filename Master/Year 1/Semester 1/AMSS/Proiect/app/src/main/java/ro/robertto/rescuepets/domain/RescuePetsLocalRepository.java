package ro.robertto.rescuepets.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

import ro.robertto.rescuepets.data.pojo.Address;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.BankInfo;
import ro.robertto.rescuepets.data.pojo.Center;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.User;

public abstract class RescuePetsLocalRepository {
    protected Context mContext;

    protected RescuePetsLocalRepository( Context context ) {
        super();
        this.mContext = context;
    }

    protected abstract void insertAddress( Address address );

    protected abstract void insertCenter( Center center );

    protected abstract void insertBankInfo( BankInfo bankInfo );

    protected abstract void insertEmployee( Employee employee );

    protected abstract void insertPet( Pet pet );

    protected abstract void insertUser( User user );

    protected abstract LiveData< Pet > getPet( @NonNull String petUid );

    protected abstract LiveData< Center > getCenter( @NonNull String centerUid );

    protected abstract LiveData< Address > getAddress( @NonNull String addressUid );

    protected abstract LiveData< List< BankInfo > > getBankInfoByCenterUid( @NonNull String centerUid );

    protected abstract void insertAdoptionForm( AdoptionForm adoptionForm );

    protected abstract LiveData< List< Pet > > getAllPets();

    protected abstract LiveData< List< AdoptionForm > > getAllAdoptionForms();

    protected abstract LiveData< List< AdoptionForm > > getAdoptionFormsByUserUid( @NonNull String UserUid );

    protected abstract LiveData< Employee > getEmployee( @NonNull String employeeUid );

    protected abstract LiveData< User > getUser( @NonNull String userUid );

    protected abstract LiveData< AdoptionForm > getAdoptionForm( @NonNull String adoptionFormUid );
}
