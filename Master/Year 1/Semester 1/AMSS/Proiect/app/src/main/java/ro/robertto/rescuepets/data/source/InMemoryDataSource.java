package ro.robertto.rescuepets.data.source;

import ro.robertto.rescuepets.data.pojo.Address;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.BankInfo;
import ro.robertto.rescuepets.data.pojo.Center;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.RescuePetsPojo;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.domain.RescuePetsInMemoryRepository;

public final class InMemoryDataSource extends RescuePetsInMemoryRepository {
    private final InMemoryTemporaryStorage addressInMemoryTemporaryStorage;
    private final InMemoryTemporaryStorage centerInMemoryTemporaryStorage;
    private final InMemoryTemporaryStorage bankInfoInMemoryTemporaryStorage;
    private final InMemoryTemporaryStorage employeeInMemoryTemporaryStorage;
    private final InMemoryTemporaryStorage petInMemoryTemporaryStorage;
    private final InMemoryTemporaryStorage adoptionFormInMemoryTemporaryStorage;
    private final InMemoryTemporaryStorage userInMemoryTemporaryStorage;


    public InMemoryDataSource() {
        super();
        addressInMemoryTemporaryStorage = InMemoryTemporaryStorage.getInstance( Address.class );
        centerInMemoryTemporaryStorage = InMemoryTemporaryStorage.getInstance( Center.class );
        bankInfoInMemoryTemporaryStorage = InMemoryTemporaryStorage.getInstance( BankInfo.class );
        employeeInMemoryTemporaryStorage = InMemoryTemporaryStorage.getInstance( Employee.class );
        petInMemoryTemporaryStorage = InMemoryTemporaryStorage.getInstance( Pet.class );
        adoptionFormInMemoryTemporaryStorage = InMemoryTemporaryStorage.getInstance( AdoptionForm.class );
        userInMemoryTemporaryStorage = InMemoryTemporaryStorage.getInstance( User.class );
    }

    @Override
    protected void addInMemory( RescuePetsPojo pojo ) {
        if ( pojo instanceof Address )
            addressInMemoryTemporaryStorage.addInMemory( pojo );
        else if ( pojo instanceof Center )
            centerInMemoryTemporaryStorage.addInMemory( pojo );
        else if ( pojo instanceof BankInfo )
            bankInfoInMemoryTemporaryStorage.addInMemory( pojo );
        else if ( pojo instanceof Employee )
            employeeInMemoryTemporaryStorage.addInMemory( pojo );
        else if ( pojo instanceof Pet )
            petInMemoryTemporaryStorage.addInMemory( pojo );
        else if ( pojo instanceof AdoptionForm )
            adoptionFormInMemoryTemporaryStorage.addInMemory( pojo );
        else if ( pojo instanceof User )
            userInMemoryTemporaryStorage.addInMemory( pojo );
        else
            throw new RuntimeException( "obj instance not known" );
    }

    protected RescuePetsPojo removeInMemory( Class< ? extends RescuePetsPojo > pojoClass ) {
        if ( pojoClass.equals( Address.class ) )
            return addressInMemoryTemporaryStorage.removeInMemory();
        else if ( pojoClass.equals( Center.class ) )
            return centerInMemoryTemporaryStorage.removeInMemory();
        else if ( pojoClass.equals( BankInfo.class ) )
            return bankInfoInMemoryTemporaryStorage.removeInMemory();
        else if ( pojoClass.equals( Employee.class ) )
            return employeeInMemoryTemporaryStorage.removeInMemory();
        else if ( pojoClass.equals( Pet.class ) )
            return petInMemoryTemporaryStorage.removeInMemory();
        else if ( pojoClass.equals( AdoptionForm.class ) )
            return adoptionFormInMemoryTemporaryStorage.removeInMemory();
        else if ( pojoClass.equals( User.class ) )
            return userInMemoryTemporaryStorage.removeInMemory();
        else
            throw new RuntimeException( "obj instance not known" );
    }

    @Override
    protected int getNrOfElements( Class< ? extends RescuePetsPojo > pojoClass ) {
        if ( pojoClass.equals( Address.class ) )
            return addressInMemoryTemporaryStorage.getNrOfElements();
        else if ( pojoClass.equals( Center.class ) )
            return centerInMemoryTemporaryStorage.getNrOfElements();
        else if ( pojoClass.equals( BankInfo.class ) )
            return bankInfoInMemoryTemporaryStorage.getNrOfElements();
        else if ( pojoClass.equals( Employee.class ) )
            return employeeInMemoryTemporaryStorage.getNrOfElements();
        else if ( pojoClass.equals( Pet.class ) )
            return petInMemoryTemporaryStorage.getNrOfElements();
        else if ( pojoClass.equals( AdoptionForm.class ) )
            return adoptionFormInMemoryTemporaryStorage.getNrOfElements();
        else if ( pojoClass.equals( User.class ) )
            return userInMemoryTemporaryStorage.getNrOfElements();
        else
            throw new RuntimeException( "obj instance not known" );
    }
}
