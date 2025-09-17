package ro.robertto.rescuepets.domain;

import dagger.BindsInstance;
import dagger.Component;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsApplication;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsViewModel;

@Component( modules = RescuePetsDependencyProviderModule.class )
public interface RescuePetsDependencyProviderComponent {
    void inject( RescuePetsViewModel rescuePetsViewModel );

    RescuePetsApplication getApplication();

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application( RescuePetsApplication rescuePetsApplication );

        Builder rescuePetsDependencyProviderModule( RescuePetsDependencyProviderModule module );

        RescuePetsDependencyProviderComponent build();
    }
}
