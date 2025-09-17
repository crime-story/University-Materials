package ro.robertto.rescuepets.presentation.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import ro.robertto.rescuepets.R;
import timber.log.Timber;

public class PermissionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_permissions );

        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
            startActivity( new Intent( PermissionsActivity.this, MapsActivity.class ) );
            finish();
            return;
        }

        Button btnGrant = findViewById( R.id.btn_grant );
        if ( btnGrant == null ) {
            Timber.e( "btnGrant not found" );
            return;
        }
        btnGrant.setOnClickListener( view -> {
            Dexter.withContext( PermissionsActivity.this )
                    .withPermission( Manifest.permission.ACCESS_FINE_LOCATION )
                    .withListener( new PermissionListener() {
                        @Override
                        public void onPermissionGranted( PermissionGrantedResponse response ) {
                            startActivity( new Intent( PermissionsActivity.this, MapsActivity.class ) );
                            finish();
                        }

                        @Override
                        public void onPermissionDenied( PermissionDeniedResponse response ) {
                            if ( response.isPermanentlyDenied() ) {
                                AlertDialog.Builder builder = new AlertDialog.Builder( PermissionsActivity.this );
                                builder.setTitle( "Permission Denied" )
                                        .setMessage( "Permission to access device location is permanently denied. You need to go to settings to allow the permission." )
                                        .setNegativeButton( "Cancel", null )
                                        .setPositiveButton( "OK", ( dialog, which ) -> {
                                            Intent intent = new Intent();
                                            intent.setAction( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
                                            intent.setData( Uri.fromParts( "package", getPackageName(), null ) );
                                            startActivity( intent );
                                        } )
                                        .show();
                            } else {
                                Toast.makeText( PermissionsActivity.this, "Permission Denied", Toast.LENGTH_SHORT ).show();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown( PermissionRequest permission, PermissionToken token ) {
                            token.continuePermissionRequest();
                        }
                    } )
                    .check();
        } );
    }
}
