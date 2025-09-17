package ro.robertto.rescuepets.data.pojo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity( tableName = "Center", primaryKeys = { "uid" },
        foreignKeys = @ForeignKey( entity = Address.class, parentColumns = "uid", childColumns = "addressUid" ),
        indices = { @Index( "addressUid" ) } )
public class Center extends RescuePetsPojo {
    @ColumnInfo( name = "addressUid" )
    @SerializedName( "addressUid" )
    @Expose
    @NonNull
    private final String addressUid;
    @ColumnInfo( name = "phoneNumber" )
    @SerializedName( "phoneNumber" )
    @Expose
    @NonNull
    private final String phoneNumber;

    @ColumnInfo( name = "schedule" )
    @SerializedName( "schedule" )
    @Expose
    @NonNull
    private final String schedule;

    public Center( @NonNull String uid, @NonNull String addressUid, @NonNull String phoneNumber, @NonNull String schedule ) {
        super( uid );
        this.addressUid = addressUid;
        this.phoneNumber = phoneNumber;
        this.schedule = schedule;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Center center = ( Center ) o;
        return uid.equals( center.uid ) && addressUid.equals( center.addressUid ) && phoneNumber.equals( center.phoneNumber ) && schedule.equals( center.schedule );
    }

    @Override
    public int hashCode() {
        return Objects.hash( uid, addressUid, phoneNumber, schedule );
    }

    @NonNull
    public String getAddressUid() {
        return addressUid;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @NonNull
    public String getSchedule() {
        return schedule;
    }
}
