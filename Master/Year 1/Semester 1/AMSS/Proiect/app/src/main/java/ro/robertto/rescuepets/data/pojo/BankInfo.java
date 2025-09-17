package ro.robertto.rescuepets.data.pojo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity( tableName = "BankInfo", primaryKeys = { "uid" },
        foreignKeys = @ForeignKey( entity = Center.class, parentColumns = "uid", childColumns = "centerUid" ),
        indices = { @Index( "centerUid" ) } )
public class BankInfo extends RescuePetsPojo {
    @ColumnInfo( name = "centerUid" )
    @SerializedName( "centerUid" )
    @Expose
    @NonNull
    private final String centerUid;

    @ColumnInfo( name = "iban" )
    @SerializedName( "iban" )
    @Expose
    @NonNull
    private final String iban;

    @ColumnInfo( name = "details" )
    @SerializedName( "details" )
    @Expose
    @NonNull
    private final String details;

    public BankInfo( @NonNull String uid, @NonNull String centerUid, @NonNull String iban, @NonNull String details ) {
        super( uid );
        this.centerUid = centerUid;
        this.iban = iban;
        this.details = details;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        BankInfo bankInfo = ( BankInfo ) o;
        return uid.equals( bankInfo.uid ) && centerUid.equals( bankInfo.centerUid ) && iban.equals( bankInfo.iban ) && details.equals( bankInfo.details );
    }

    @Override
    public int hashCode() {
        return Objects.hash( uid, centerUid, iban, details );
    }

    @NonNull
    public String getCenterUid() {
        return centerUid;
    }

    @NonNull
    public String getIban() {
        return iban;
    }

    @NonNull
    public String getDetails() {
        return details;
    }
}
