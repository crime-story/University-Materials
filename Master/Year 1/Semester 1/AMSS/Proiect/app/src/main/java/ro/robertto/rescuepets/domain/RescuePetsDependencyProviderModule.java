package ro.robertto.rescuepets.domain;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.work.WorkManager;

import dagger.Module;
import dagger.Provides;
import ro.robertto.rescuepets.data.source.InMemoryDataSource;
import ro.robertto.rescuepets.data.source.LocalDataSource;

@Module
public final class RescuePetsDependencyProviderModule {
    private final @NonNull PetsUseCase petsUseCase;

    public RescuePetsDependencyProviderModule( Application application ) {
        RescuePetsLocalRepository rescuePetsLocalRepository = new LocalDataSource( application );
        RescuePetsInMemoryRepository rescuePetsInMemoryRepository = new InMemoryDataSource();

        WorkManager workManager1;           //necesar pt jUnitTests
        try {
            workManager1 = WorkManager.getInstance( application );
        } catch ( Exception e ) {
            workManager1 = null;
        }
        WorkManager workManager = workManager1;

        petsUseCase = new RescuePetsMediator( rescuePetsLocalRepository, rescuePetsInMemoryRepository, workManager );
    }

    @Provides
    public @NonNull PetsUseCase providePetsUseCase() {
        return petsUseCase;
    }
}
