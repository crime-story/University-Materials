package ro.robertto.rescuepets.domain;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

import ro.robertto.rescuepets.data.pojo.Address;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.BankInfo;
import ro.robertto.rescuepets.data.pojo.Center;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.RescuePetsPojo;
import ro.robertto.rescuepets.data.pojo.User;

// Mediator may implement more than one UseCase in mvvm with clean architecture
// so we cannot make the use-cases abstract classes, only interfaces.
// Even though these are public methods, they can be accessed only through
// the use-cases, as the mediator is not a public class and cannot be imported
// outside this package
public interface PetsUseCase {
    LiveData< List< Pet > > getAllPets();

    LiveData< Pet > getPet( @NonNull String petUid );

    LiveData< Center > getCenter( @NonNull String centerUid );

    LiveData< Address > getAddress( @NonNull String addressUid );

    LiveData< Employee > getEmployee( @NonNull String employeeUid );

    LiveData< User > getUser( @NonNull String userUid );

    LiveData< AdoptionForm > getAdoptionForm( @NonNull String adoptionFormUid );

    LiveData< List< AdoptionForm > > getAllAdoptionForms();

    LiveData< List< AdoptionForm > > getAdoptionFormsByUserUid( @NonNull String userUid );

    LiveData< List< BankInfo > > getBankInfoByCenterUid( @NonNull String centerUid );

    void insertPojo( @NonNull RescuePetsPojo pojo );

    void updatePojo( @NonNull RescuePetsPojo pojo );

    void syncGet();
}
