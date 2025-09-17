package ro.robertto.rescuepets.domain;

import ro.robertto.rescuepets.data.pojo.RescuePetsPojo;

public abstract class RescuePetsInMemoryRepository {
    protected RescuePetsInMemoryRepository() {
        super();
    }

    protected abstract void addInMemory( RescuePetsPojo obj );

    protected abstract RescuePetsPojo removeInMemory( Class< ? extends RescuePetsPojo > obj );

    protected abstract int getNrOfElements( Class< ? extends RescuePetsPojo > obj );
}
