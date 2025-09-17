package ro.robertto.rescuepets.domain;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;

import ro.robertto.rescuepets.data.pojo.Address;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.BankInfo;
import ro.robertto.rescuepets.data.pojo.Center;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.RescuePetsPojo;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.data.source.InMemoryDataSource;
import ro.robertto.rescuepets.data.source.LocalDataSource;
import ro.robertto.rescuepets.data.source.RemoteDataSource;
import timber.log.Timber;


public class RescuePetsWorker extends Worker {
    private final Context context;

    public RescuePetsWorker( @NonNull Context context, @NonNull WorkerParameters workerParams ) {
        super( context, workerParams );
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Data data = getInputData();
        String value = data.getString( "mode" );
        if ( value == null )
            return Result.failure();

        RescuePetsLocalRepository localRepository = new LocalDataSource( context );
        RescuePetsInMemoryRepository inMemoryRepository = new InMemoryDataSource();
        RescuePetsRemoteRepository remoteRepository = new RemoteDataSource( localRepository, inMemoryRepository );

        if ( "get".equals( value ) ) {
            Timber.d( "GET Operation" );

            boolean ok = false;

            do {
                List< RescuePetsPojo > addresses = remoteRepository.getObjects( Address.class );
                if ( addresses == null )
                    break;
                for ( RescuePetsPojo address : addresses ) {
                    localRepository.insertAddress( ( Address ) address );
                }

                List< RescuePetsPojo > centers = remoteRepository.getObjects( Center.class );
                if ( centers == null )
                    break;
                for ( RescuePetsPojo center : centers ) {
                    localRepository.insertCenter( ( Center ) center );
                }

                List< RescuePetsPojo > bankInfos = remoteRepository.getObjects( BankInfo.class );
                if ( bankInfos == null )
                    break;
                for ( RescuePetsPojo bankInfo : bankInfos ) {
                    localRepository.insertBankInfo( ( BankInfo ) bankInfo );
                }

                List< RescuePetsPojo > employees = remoteRepository.getObjects( Employee.class );
                if ( employees == null )
                    break;
                for ( RescuePetsPojo employee : employees ) {
                    localRepository.insertEmployee( ( Employee ) employee );
                }

                List< RescuePetsPojo > pets = remoteRepository.getObjects( Pet.class );
                if ( pets == null )
                    break;
                for ( RescuePetsPojo pet : pets ) {
                    localRepository.insertPet( ( Pet ) pet );
                }

                List< RescuePetsPojo > adoptionForms = remoteRepository.getObjects( AdoptionForm.class );
                if ( adoptionForms != null ) {
                    for ( RescuePetsPojo adoptionForm : adoptionForms ) {
                        localRepository.insertAdoptionForm( ( AdoptionForm ) adoptionForm );
                    }
                }

                ok = true;
            } while ( false );


            if ( !ok ) {
                Timber.d( "null response received from remote datasource" );
                return Result.retry();
            }

        } else if ( "post".equals( value ) || "put".equals( value ) ) {
            Timber.d( "POST Operation" );

            List< Class< ? extends RescuePetsPojo > > classTypes = new ArrayList<>();
            classTypes.add( Address.class );
            classTypes.add( Center.class );
            classTypes.add( BankInfo.class );
            classTypes.add( Employee.class );
            classTypes.add( Pet.class );
            classTypes.add( AdoptionForm.class );

            for ( Class< ? extends RescuePetsPojo > classType : classTypes ) {
                int nrOfElements = inMemoryRepository.getNrOfElements( classType );
                for ( int i = 0; i < nrOfElements; i++ ) {
                    RescuePetsPojo obj = inMemoryRepository.removeInMemory( classType );
                    if ( obj != null ) {
                        if ( obj.getUid().isEmpty() )
                            remoteRepository.insertObject( obj );
                        else
                            remoteRepository.updateObject( obj );
                    } else {
                        break;
                    }
                }
            }
        } else if ( "getByUid".equals( value ) ) {
            String uid = data.getString( "uid" );
            if ( uid == null )
                return Result.failure();
            User user = ( User ) remoteRepository.getObjectByUid( User.class, uid );
            if ( user != null ) {
                localRepository.insertUser( user );
            } else {
                Timber.e( "current user not found" );
                return Result.retry();
            }
        }

        return Result.success();
    }
}
