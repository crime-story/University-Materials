package ro.robertto.rescuepets.presentation.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import ro.robertto.rescuepets.domain.RescuePetsDependencyProviderComponent;
import timber.log.Timber;

public final class RescuePetsViewModelFactory implements ViewModelProvider.Factory {
    Application mApplication;

    public RescuePetsViewModelFactory( Application application ) {
        super();
        this.mApplication = application;
        Timber.tag( "RescuePetsViewModelFactory" ).d( "RescuePetsViewModelFactory constructor called" );
    }

    @NonNull
    @Override
    public < T extends ViewModel > T create( @NonNull Class< T > modelClass ) {
        RescuePetsViewModel rescuePetsViewModelInstance = new RescuePetsViewModel( this.mApplication );
        try {
            RescuePetsApplication rescuePetsApplication = ( RescuePetsApplication ) this.mApplication; // may throw class cast exception
            RescuePetsDependencyProviderComponent dependencyProviderComponent = rescuePetsApplication.getRescuePetsDependencyProviderComponent();
            if ( dependencyProviderComponent != null ) {
                dependencyProviderComponent.inject( rescuePetsViewModelInstance );
            } else {
                Timber.tag( "RescuePetsViewModelFactory" ).wtf( "dependencyProviderComponent is null" );
            }
        } catch ( Exception e ) {
            Timber.wtf( e );
        }
        return Objects.requireNonNull( modelClass.cast( rescuePetsViewModelInstance ) );
    }
}
