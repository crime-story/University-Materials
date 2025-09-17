package ro.robertto.rescuepets.data.pojo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity( tableName = "Employee", primaryKeys = { "uid" },
        foreignKeys = @ForeignKey( entity = Center.class, parentColumns = "uid", childColumns = "centerUid" ),
        indices = { @Index( "centerUid" ) } )
public class Employee extends RescuePetsPojo {
    @ColumnInfo( name = "centerUid" )
    @SerializedName( "centerUid" )
    @Expose
    @NonNull
    private String centerUid;

    @ColumnInfo( name = "name" )
    @SerializedName( "name" )
    @Expose
    @NonNull
    private String name;

    @ColumnInfo( name = "email" )
    @SerializedName( "email" )
    @Expose
    @NonNull
    private String email;

    @ColumnInfo( name = "profileImage" )
    @SerializedName( "profileImage" )
    @Expose
    @NonNull
    private String profileImage;

    @Ignore
    public Employee() {
        super();
        centerUid = "";
        name = "";
        email = "";
        profileImage = "";
    }

    public Employee( @NonNull String uid, @NonNull String centerUid, @NonNull String name, @NonNull String email, @NonNull String profileImage ) {
        super( uid );
        this.centerUid = centerUid;
        this.name = name;
        this.email = email;
        this.profileImage = profileImage;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Employee employee = ( Employee ) o;
        return uid.equals( employee.uid ) && centerUid.equals( employee.centerUid ) && name.equals( employee.name ) && email.equals( employee.email ) && profileImage.equals( employee.profileImage );
    }

    @Override
    public int hashCode() {
        return Objects.hash( uid, centerUid, name, email, profileImage );
    }

    @NonNull
    public String getCenterUid() {
        return centerUid;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setUid( @NonNull String uid ) {
        this.uid = uid;
    }

    public void setCenterUid( @NonNull String centerUid ) {
        this.centerUid = centerUid;
    }

    public void setProfileImage( @NonNull String profileImage ) {
        this.profileImage = profileImage;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getProfileImage() {
        return profileImage;
    }

    public void setName( @NonNull String name ) {
        this.name = name;
    }

    public void setEmail( @NonNull String email ) {
        this.email = email;
    }
}
