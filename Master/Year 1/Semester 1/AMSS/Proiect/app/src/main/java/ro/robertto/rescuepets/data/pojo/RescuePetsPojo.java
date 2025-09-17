package ro.robertto.rescuepets.data.pojo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RescuePetsPojo implements Serializable {
    @ColumnInfo( name = "uid" )
    @SerializedName( "uid" )
    @Expose
    @NonNull
    protected String uid;

    RescuePetsPojo() {
        this.uid = "";
    }

    RescuePetsPojo( @NonNull String uid ) {
        this.uid = uid;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid( @NonNull String uid ) {
        this.uid = uid;
    }
}
