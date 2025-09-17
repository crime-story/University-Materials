package ro.robertto.rescuepets.data.source;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ro.robertto.rescuepets.data.pojo.Address;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.BankInfo;
import ro.robertto.rescuepets.data.pojo.Center;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.User;

@Database( entities = { User.class, Center.class, Address.class, Employee.class, BankInfo.class, Pet.class, AdoptionForm.class }, version = 1 )
abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    protected static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool( NUMBER_OF_THREADS );

    AppDatabase() {
        super();
    }

    protected abstract LocalDataSource.RescuePetsDao rescuePetsDao();

    protected static AppDatabase getAppDatabase( final Context context ) {
        synchronized ( AppDatabase.class ) {
            if ( INSTANCE == null ) {
                INSTANCE = Room.databaseBuilder( context.getApplicationContext(),
                                AppDatabase.class,
                                "rescuepets-db" )
                        .build();
            }
            return INSTANCE;
        }
    }
}
