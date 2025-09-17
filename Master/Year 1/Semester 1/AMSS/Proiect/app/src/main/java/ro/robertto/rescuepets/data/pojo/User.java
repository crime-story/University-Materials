package ro.robertto.rescuepets.data.pojo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity( tableName = "User", primaryKeys = { "uid" } )
public class User extends RescuePetsPojo {
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

    @ColumnInfo( name = "phoneNumber" )
    @SerializedName( "phoneNumber" )
    @Expose
    @NonNull
    private String phoneNumber;

    @ColumnInfo( name = "birthDate" )
    @SerializedName( "birthDate" )
    @Expose
    @NonNull
    private String birthDate;

    @ColumnInfo( name = "centerUid" )
    @SerializedName( "centerUid" )
    @Expose
    @Nullable
    private String centerUid;

    @ColumnInfo( name = "profileImage" )
    @SerializedName( "profileImage" )
    @Expose
    @Nullable
    private String profileImage;

    @ColumnInfo( name = "deviceToken" )
    @SerializedName( "deviceToken" )
    @Expose
    @Nullable
    private String deviceToken;

    @Ignore
    public User() {
        super();
        name = "";
        email = "";
        phoneNumber = "";
        birthDate = "";
    }

    public User( @NonNull String uid, @NonNull String name, @NonNull String email, @NonNull String phoneNumber, @NonNull String birthDate, @Nullable String centerUid, @Nullable String profileImage, @Nullable String deviceToken ) {
        super( uid );
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.centerUid = centerUid;
        this.profileImage = profileImage;
        this.deviceToken = deviceToken;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        User user = ( User ) o;
        return uid.equals( user.uid );
    }

    @Override
    public int hashCode() {
        return Objects.hash( uid );
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName( @NonNull String name ) {
        this.name = name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail( @NonNull String email ) {
        this.email = email;
    }

    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber( @NonNull String phoneNumber ) {
        this.phoneNumber = phoneNumber;
    }

    @NonNull
    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate( @NonNull String birthDate ) {
        this.birthDate = birthDate;
    }

    @Nullable
    public String getCenterUid() {
        return centerUid;
    }

    public void setCenterUid( @Nullable String centerUid ) {
        this.centerUid = centerUid;
    }

    @Nullable
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage( @Nullable String profileImage ) {
        this.profileImage = profileImage;
    }

    @Nullable
    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken( @Nullable String deviceToken ) {
        this.deviceToken = deviceToken;
    }
}
