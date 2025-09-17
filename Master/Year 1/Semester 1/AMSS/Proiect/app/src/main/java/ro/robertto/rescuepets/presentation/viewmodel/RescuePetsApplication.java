package ro.robertto.rescuepets.presentation.viewmodel;

import android.app.Application;

import androidx.annotation.Nullable;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import ro.robertto.rescuepets.domain.DaggerRescuePetsDependencyProviderComponent;
import ro.robertto.rescuepets.domain.RescuePetsDependencyProviderComponent;
import ro.robertto.rescuepets.domain.RescuePetsDependencyProviderModule;

public class RescuePetsApplication extends Application {
    private RescuePetsDependencyProviderComponent rescuePetsDependencyProviderComponent = null;

    static RescuePetsApplication m_rescue_pets_context=null;

    public RescuePetsApplication() {
        super();
        m_rescue_pets_context = this;
    }

    public static RescuePetsApplication get_rescue_pets_context(){
        return m_rescue_pets_context;

    }
    @Override
    public void onCreate() {
        super.onCreate();

        rescuePetsDependencyProviderComponent = DaggerRescuePetsDependencyProviderComponent
                .builder()
                .application( this )
                .rescuePetsDependencyProviderModule( new RescuePetsDependencyProviderModule( this ) )
                .build();


/*        try {
            FileInputStream serviceAccount = new FileInputStream( "firebase-adminsdk.json" );
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials( GoogleCredentials.fromStream( serviceAccount ) )
                    .setDatabaseUrl( "https://rescuepets-b1ab1-default-rtdb.europe-west1.firebasedatabase.app" )
                    .build();
            FirebaseApp.initializeApp( options );

        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }*/


    }

    @Nullable
    RescuePetsDependencyProviderComponent getRescuePetsDependencyProviderComponent() {
        return rescuePetsDependencyProviderComponent;
    }
}
