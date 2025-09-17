package ro.robertto.rescuepets.data.pojo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity( tableName = "Address", primaryKeys = { "uid" } )
public class Address extends RescuePetsPojo {
    @ColumnInfo( name = "county" )
    @SerializedName( "county" )
    @Expose
    @NonNull
    private final String county;

    @ColumnInfo( name = "city" )
    @SerializedName( "city" )
    @Expose
    @NonNull
    private final String city;

    @ColumnInfo( name = "street" )
    @SerializedName( "street" )
    @Expose
    @NonNull
    private final String street;

    @ColumnInfo( name = "postalCode" )
    @SerializedName( "postalCode" )
    @Expose
    @NonNull
    private final String postalCode;

    public Address( @NonNull String uid, @NonNull String county, @NonNull String city, @NonNull String street, @NonNull String postalCode ) {
        super( uid );
        this.county = county;
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
    }

    @NonNull
    public String getCounty() {
        return county;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    @NonNull
    public String getStreet() {
        return street;
    }

    @NonNull
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Address address = ( Address ) o;
        return uid.equals( address.uid ) && county.equals( address.county ) && city.equals( address.city ) && street.equals( address.street ) && postalCode.equals( address.postalCode );
    }

    @Override
    public int hashCode() {
        return Objects.hash( uid, county, city, street, postalCode );
    }
}
