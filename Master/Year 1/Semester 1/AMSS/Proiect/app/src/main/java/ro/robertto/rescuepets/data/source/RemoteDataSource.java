package ro.robertto.rescuepets.data.source;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ro.robertto.rescuepets.data.pojo.Address;
import ro.robertto.rescuepets.data.pojo.AdoptionForm;
import ro.robertto.rescuepets.data.pojo.BankInfo;
import ro.robertto.rescuepets.data.pojo.Center;
import ro.robertto.rescuepets.data.pojo.Employee;
import ro.robertto.rescuepets.data.pojo.Pet;
import ro.robertto.rescuepets.data.pojo.RescuePetsPojo;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.domain.RescuePetsInMemoryRepository;
import ro.robertto.rescuepets.domain.RescuePetsLocalRepository;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;
import timber.log.Timber;

public final class RemoteDataSource extends RescuePetsRemoteRepository {
    private final Map< Class< ? extends RescuePetsPojo >, ApiHandlerGet > apiHandlersGet;
    private final Map< Class< ? extends RescuePetsPojo >, ApiHandlerGetByUid > apiHandlersGetByUid;

    private final RetrofitApi api;

    private final LocalDataSource localDataSource;
    private final InMemoryDataSource inMemoryDataSource;

    private interface ApiHandlerGet {
        Call< JsonObject > call();
    }

    private interface ApiHandlerGetByUid {
        Call< JsonObject > call( @NotNull String uid );
    }


    public RemoteDataSource( RescuePetsLocalRepository localDataSource, RescuePetsInMemoryRepository rescuePetsInMemoryRepository ) {
        super();

        this.localDataSource = ( LocalDataSource ) localDataSource;
        this.inMemoryDataSource = ( InMemoryDataSource ) rescuePetsInMemoryRepository;
        api = RetrofitApi.createApi();

        apiHandlersGet = new HashMap<>();
        apiHandlersGet.put( Address.class, api::getAddresses );
        apiHandlersGet.put( AdoptionForm.class, api::getAdoptionForms );
        apiHandlersGet.put( BankInfo.class, api::getBankInfos );
        apiHandlersGet.put( Center.class, api::getCenters );
        apiHandlersGet.put( Employee.class, api::getEmployees );
        apiHandlersGet.put( Pet.class, api::getPets );

        apiHandlersGetByUid = new HashMap<>();
        apiHandlersGetByUid.put( User.class, api::getUserByUid );
    }

    @Override
    protected @Nullable List< RescuePetsPojo > getObjects( Class< ? extends RescuePetsPojo > clazz ) {
        ApiHandlerGet apiHandler = apiHandlersGet.get( clazz );

        if ( apiHandler == null ) {
            Timber.wtf( "no api handler found" );
            return null;
        }

        List< RescuePetsPojo > entities = null;
        Gson gson = new Gson();
        try {
            JsonObject response = apiHandler.call().execute().body();
            if ( response != null ) {
                entities = new ArrayList<>();
                for ( String jsonObjectKeys : response.keySet() ) {
                    JsonObject jsonObject = response.getAsJsonObject( jsonObjectKeys );
                    RescuePetsPojo pojoEntity = gson.fromJson( jsonObject, clazz );
                    if ( pojoEntity != null ) {
                        pojoEntity.setUid( jsonObjectKeys );
                        entities.add( pojoEntity );
                    }
                }
            }
        } catch ( Exception e ) {
            Timber.d( e, "Something happened" );
        }
        return entities;
    }

    @Override
    protected @Nullable RescuePetsPojo getObjectByUid( Class< ? extends RescuePetsPojo > clazz, @NonNull String uid ) {
        ApiHandlerGetByUid apiHandler = apiHandlersGetByUid.get( clazz );

        if ( apiHandler == null ) {
            Timber.wtf( "no api handler found" );
            return null;
        }

        Gson gson = new Gson();
        try {
            JsonObject response = apiHandler.call( uid ).execute().body();
            if ( response != null ) {
                RescuePetsPojo pojoEntity = gson.fromJson( response, clazz );
                if ( pojoEntity != null ) {
                    pojoEntity.setUid( uid );
                    return pojoEntity;
                }

            }
        } catch ( Exception e ) {
            Timber.d( e, "Something happened" );
        }
        return null;
    }

    @Override
    protected void insertObject( RescuePetsPojo pojo ) {
        Callback< JsonObject > callback = new Callback< JsonObject >() {
            @Override
            public void onResponse( @NonNull Call< JsonObject > call, @NonNull Response< JsonObject > response ) {
                if ( response.isSuccessful() ) {
                    Timber.d( "Success inserting pojo in firebase db" );
                    JsonObject jsonObject = response.body();
                    if ( jsonObject != null ) {
                        JsonElement jsonElement = jsonObject.get( "name" );
                        if ( jsonElement != null ) {
                            try {
                                String uid = jsonElement.getAsString();
                                if ( uid != null ) {
                                    pojo.setUid( uid );
                                    if ( pojo instanceof Pet ) localDataSource.insertPet( ( Pet ) pojo );
                                    else if ( pojo instanceof AdoptionForm ) localDataSource.insertAdoptionForm( ( AdoptionForm ) pojo );
                                    else if ( pojo instanceof Center ) localDataSource.insertCenter( ( Center ) pojo );
                                    else if ( pojo instanceof Address ) localDataSource.insertAddress( ( Address ) pojo );
                                    else if ( pojo instanceof BankInfo ) localDataSource.insertBankInfo( ( BankInfo ) pojo );
                                    else if ( pojo instanceof Employee ) localDataSource.insertEmployee( ( Employee ) pojo );
                                    else if ( pojo instanceof User ) localDataSource.insertUser( ( User ) pojo );
                                    else Timber.tag( "remoteDataSource" ).wtf( "unexpected class" );
                                }
                            } catch ( Exception e ) {
                                Timber.e( e );
                            }
                        }
                    }

                } else {
                    Timber.d( "Recieved firebase response is failed" );
                    inMemoryDataSource.addInMemory( pojo );
                }
            }

            @Override
            public void onFailure( @NonNull Call< JsonObject > call, @NonNull Throwable t ) {
                Timber.d( "fail inserting pojo in firebase db" );
                Timber.tag( "remoteDataSource" ).e( t );
                inMemoryDataSource.addInMemory( pojo );
            }
        };
        if ( pojo instanceof Address )
            api.insertAddress( ( Address ) pojo ).enqueue( callback );
        else if ( pojo instanceof Center )
            api.insertCenter( ( Center ) pojo ).enqueue( callback );
        else if ( pojo instanceof BankInfo )
            api.insertBankInfo( ( BankInfo ) pojo ).enqueue( callback );
        else if ( pojo instanceof Employee )
            api.insertEmployee( ( Employee ) pojo ).enqueue( callback );
        else if ( pojo instanceof Pet )
            api.insertPet( ( Pet ) pojo ).enqueue( callback );
        else if ( pojo instanceof AdoptionForm )
            api.insertAdoptionForm( ( AdoptionForm ) pojo ).enqueue( callback );
    }

    @Override
    protected void updateObject( RescuePetsPojo pojo ) {
        Callback< JsonObject > callback = new Callback< JsonObject >() {
            @Override
            public void onResponse( @NonNull Call< JsonObject > call, @NonNull Response< JsonObject > response ) {
                if ( response.isSuccessful() ) {
                    Timber.d( "Success updating pojo in firebase db" );
                    if ( pojo instanceof Pet ) localDataSource.insertPet( ( Pet ) pojo );
                    else if ( pojo instanceof AdoptionForm ) localDataSource.insertAdoptionForm( ( AdoptionForm ) pojo );
                    else if ( pojo instanceof Center ) localDataSource.insertCenter( ( Center ) pojo );
                    else if ( pojo instanceof Address ) localDataSource.insertAddress( ( Address ) pojo );
                    else if ( pojo instanceof BankInfo ) localDataSource.insertBankInfo( ( BankInfo ) pojo );
                    else if ( pojo instanceof Employee ) localDataSource.insertEmployee( ( Employee ) pojo );
                    else if ( pojo instanceof User ) localDataSource.insertUser( ( User ) pojo );
                    else Timber.tag( "remoteDataSource" ).wtf( "unexpected class" );
                } else {
                    Timber.d( "Recieved firebase response is failed" );
                    inMemoryDataSource.addInMemory( pojo );
                }
            }

            @Override
            public void onFailure( @NonNull Call< JsonObject > call, @NonNull Throwable t ) {
                Timber.d( "fail inserting pojo in firebase db" );
                Timber.tag( "remoteDataSource" ).e( t );
                inMemoryDataSource.addInMemory( pojo );
            }
        };
        if ( pojo instanceof Pet )
            api.updatePet( pojo.getUid(), ( Pet ) pojo ).enqueue( callback );
        else if ( pojo instanceof AdoptionForm )
            api.updateAdoptionForm( pojo.getUid(), ( AdoptionForm ) pojo ).enqueue( callback );
        else
            Timber.wtf( "unexpected class" );
    }

    private interface RetrofitApi {
        @GET( "Center/hashCenter1/Address.json" )
        Call< JsonObject > getAddresses();

        @GET( "Center/hashCenter1/BankInfo.json" )
        Call< JsonObject > getBankInfos();

        @GET( "Center.json" )
        Call< JsonObject > getCenters();

        @GET( "Center/hashCenter1/Employee.json" )
        Call< JsonObject > getEmployees();

        @GET( "Center/hashCenter1/Pet.json" )
        Call< JsonObject > getPets();

        @GET( "Center/hashCenter1/AdoptionForm.json" )
        Call< JsonObject > getAdoptionForms();

        @GET( "Users/{uid}.json" )
        Call< JsonObject > getUserByUid( @Path( "uid" ) String uid );

        @POST( "Center/hashCenter1/Address.json" )
        Call< JsonObject > insertAddress( @Body Address address );

        @POST( "Center/hashCenter1/BankInfo.json" )
        Call< JsonObject > insertBankInfo( @Body BankInfo bankInfo );

        @POST( "Center.json" )
        Call< JsonObject > insertCenter( @Body Center center );

        @POST( "Center/hashCenter1/Employee.json" )
        Call< JsonObject > insertEmployee( @Body Employee employee );

        @POST( "Center/hashCenter1/Pet.json" )
        Call< JsonObject > insertPet( @Body Pet pet );

        @POST( "Center/hashCenter1/AdoptionForm.json" )
        Call< JsonObject > insertAdoptionForm( @Body AdoptionForm adoptionForm );

        @PUT( "Center/hashCenter1/Pet/{petId}.json" )
        Call< JsonObject > updatePet( @Path( "petId" ) String petId, @Body Pet pet );

        @PUT( "Center/hashCenter1/AdoptionForm/{adoptionFormUid}.json" )
        Call< JsonObject > updateAdoptionForm( @Path( "adoptionFormUid" ) String adoptionFormUid, @Body AdoptionForm adoptionForm );

        static RetrofitApi createApi() {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addNetworkInterceptor( new StethoInterceptor() ).build();

            return new Retrofit.Builder().baseUrl( firebaseRealtimeDatabaseUrl ).client( okHttpClient ).addConverterFactory( GsonConverterFactory.create() ).build().create( RetrofitApi.class );
        }
    }
}
