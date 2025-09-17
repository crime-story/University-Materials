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

@Entity( tableName = "Pet", primaryKeys = { "uid" },
        foreignKeys = @ForeignKey( entity = Center.class, parentColumns = "uid", childColumns = "centerUid" ),
        indices = { @Index( "centerUid" ) } )
public class Pet extends RescuePetsPojo {
    @ColumnInfo( name = "centerUid" )
    @SerializedName( "centerUid" )
    @Expose
    @NonNull
    private final String centerUid;

    @ColumnInfo( name = "species" )
    @SerializedName( "species" )
    @Expose
    @NonNull
    private String species;

    @ColumnInfo( name = "name" )
    @SerializedName( "name" )
    @Expose
    @NonNull
    private String name;

    @ColumnInfo( name = "breed" )
    @SerializedName( "breed" )
    @Expose
    @NonNull
    private String breed;

    @ColumnInfo( name = "birthYear" )
    @SerializedName( "birthYear" )
    @Expose
    @NonNull
    private String birthYear;

    @ColumnInfo( name = "profileImage" )
    @SerializedName( "profileImage" )
    @Expose
    @Nullable
    private String profileImage;

    @ColumnInfo( name = "description" )
    @SerializedName( "description" )
    @Expose
    @NonNull
    private String description;

    public Pet( @NonNull String uid, @NonNull String centerUid, @NonNull String species, @NonNull String name, @NonNull String breed, @NonNull String birthYear, @Nullable String profileImage, @NonNull String description ) {
        this.uid = uid;
        this.centerUid = centerUid;
        this.species = species;
        this.name = name;
        this.breed = breed;
        this.birthYear = birthYear;
        this.profileImage = profileImage;
        this.description = description;
    }

    @Override
    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;
        Pet pet = ( Pet ) o;
        return centerUid.equals( pet.centerUid ) && birthYear.equals( pet.birthYear ) && uid.equals( pet.uid ) && species.equals( pet.species ) && name.equals( pet.name ) && breed.equals( pet.breed );
    }

    @Override
    public int hashCode() {
        return Objects.hash( uid, centerUid, species, name, breed, birthYear );
    }

    @NonNull
    public String getCenterUid() {
        return centerUid;
    }

    @NonNull
    public String getSpecies() {
        return species;
    }

    @NonNull
    public String getBreed() {
        return breed;
    }

    @Nullable
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage( @Nullable String profileImage ) {
        this.profileImage = profileImage;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription( @NonNull String description ) {
        this.description = description;
    }

    @NonNull
    public String getBirthYear() {
        return birthYear;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setSpecies( @NonNull String species ) {
        this.species = species;
    }

    public void setBreed( @NonNull String breed ) {
        this.breed = breed;
    }

    public void setBirthYear( @NonNull String birthYear ) {
        this.birthYear = birthYear;
    }

    public void setName( @NonNull String name ) {
        this.name = name;
    }
}
