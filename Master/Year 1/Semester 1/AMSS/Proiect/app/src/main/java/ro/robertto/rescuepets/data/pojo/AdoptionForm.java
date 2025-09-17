package ro.robertto.rescuepets.data.pojo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity( tableName = "AdoptionForm", primaryKeys = { "uid" },
        foreignKeys = { @ForeignKey( entity = Pet.class, parentColumns = "uid", childColumns = "petUid" ) },
        indices = { @Index( "petUid" ), @Index( "employeeUid" ), @Index( "userUid" ) } )
public class AdoptionForm extends RescuePetsPojo {
    @ColumnInfo( name = "petUid" )
    @SerializedName( "petUid" )
    @Expose
    @NonNull
    private final String petUid;

    @ColumnInfo( name = "userUid" )
    @SerializedName( "userUid" )
    @Expose
    @NonNull
    private final String userUid;

    @ColumnInfo( name = "employeeUid" )
    @SerializedName( "employeeUid" )
    @Expose
    @Nullable
    private String employeeUid;

    @ColumnInfo( name = "nickName" )
    @SerializedName( "nickName" )
    @Expose
    @NonNull
    private final String nickName;

    @ColumnInfo( name = "contactEmail" )
    @SerializedName( "contactEmail" )
    @Expose
    @NonNull
    private final String contactEmail;

    @ColumnInfo( name = "contactPhone" )
    @SerializedName( "contactPhone" )
    @Expose
    @NonNull
    private final String contactPhone;

    @ColumnInfo( name = "desiredVisitDate" )
    @SerializedName( "desiredVisitDate" )
    @Expose
    @NonNull
    private final String date;

    @ColumnInfo( name = "comment" )
    @SerializedName( "comment" )
    @Expose
    @NonNull
    private final String comment;

    @ColumnInfo( name = "status" )
    @SerializedName( "status" )
    @Expose
    @Nullable
    private Boolean status;

    public AdoptionForm( @NonNull String uid, @NonNull String petUid, @NonNull String userUid, @Nullable String employeeUid, @NonNull String nickName, @NonNull String contactEmail, @NonNull String contactPhone, @NonNull String date, @NonNull String comment, @Nullable Boolean status ) {
        super( uid );
        this.petUid = petUid;
        this.userUid = userUid;
        this.employeeUid = employeeUid;
        this.nickName = nickName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.date = date;
        this.comment = comment;
        this.status = status;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        AdoptionForm that = ( AdoptionForm ) o;
        return petUid.equals( that.petUid ) && userUid.equals( that.userUid ) && Objects.equals( employeeUid, that.employeeUid ) && nickName.equals( that.nickName ) && contactEmail.equals( that.contactEmail ) && contactPhone.equals( that.contactPhone ) && date.equals( that.date ) && comment.equals( that.comment ) && Objects.equals( status, that.status );
    }

    @Override
    public int hashCode() {
        return Objects.hash( petUid, userUid, employeeUid, nickName, contactEmail, contactPhone, date, comment, status );
    }

    @NonNull
    public String getPetUid() {
        return petUid;
    }

    @NonNull
    public String getUserUid() {
        return userUid;
    }

    @Nullable
    public String getEmployeeUid() {
        return employeeUid;
    }

    public void setEmployeeUid( @Nullable String employeeUid ) {
        this.employeeUid = employeeUid;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getComment() {
        return comment;
    }

    @Nullable
    public Boolean getStatus() {
        return status;
    }

    public void setStatus( @Nullable Boolean status ) {
        this.status = status;
    }

    @NonNull
    public String getNickName() {
        return nickName;
    }

    @NonNull
    public String getContactEmail() {
        return contactEmail;
    }

    @NonNull
    public String getContactPhone() {
        return contactPhone;
    }
}
