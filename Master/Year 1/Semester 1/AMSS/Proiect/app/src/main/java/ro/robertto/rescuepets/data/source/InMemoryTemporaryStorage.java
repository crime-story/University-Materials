package ro.robertto.rescuepets.data.source;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import ro.robertto.rescuepets.data.pojo.RescuePetsPojo;

final class InMemoryTemporaryStorage {
    private static final ConcurrentHashMap< Class< ? extends RescuePetsPojo >, InMemoryTemporaryStorage > instances = new ConcurrentHashMap<>();

    private final ConcurrentLinkedQueue< RescuePetsPojo > q = new ConcurrentLinkedQueue<>();
    private int nrOfElements = 0;

    public static InMemoryTemporaryStorage getInstance( Class< ? extends RescuePetsPojo > type ) {
        synchronized ( instances ) {
            InMemoryTemporaryStorage instance = instances.get( type );
            if ( instance == null ) {
                instance = new InMemoryTemporaryStorage();
                instances.put( type, instance );
            }
            return instance;
        }
    }

    private InMemoryTemporaryStorage() {
        super();
    }

    void addInMemory( RescuePetsPojo obj ) {
        synchronized ( q ) {
            q.add( obj );
            nrOfElements++;
        }
    }

    RescuePetsPojo removeInMemory() {
        synchronized ( q ) {
            if ( nrOfElements > 0 ) {
                RescuePetsPojo obj = q.remove();
                nrOfElements--;          //daca mu s-a aruncat exceptie, inseamna ca putem scadea nr de elemente
                return obj;
            } else {
                return null;
            }
        }
    }

    int getNrOfElements() {
        synchronized ( q ) {
            return nrOfElements;
        }
    }
}