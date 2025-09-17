package ro.robertto.rescuepets.domain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ro.robertto.rescuepets.data.pojo.RescuePetsPojo;

public abstract class RescuePetsRemoteRepository {
    public static final String firebaseRealtimeDatabaseUrl = "https://rescuepets-b1ab1-default-rtdb.europe-west1.firebasedatabase.app/";

    protected RescuePetsRemoteRepository() {
        super();
    }

    protected abstract @Nullable List< RescuePetsPojo > getObjects( Class< ? extends RescuePetsPojo > clazz );

    protected abstract void insertObject( RescuePetsPojo t );

    protected abstract void updateObject( RescuePetsPojo pojo );

    protected abstract @Nullable RescuePetsPojo getObjectByUid( Class< ? extends RescuePetsPojo > clazz, @NonNull String uid );
}
