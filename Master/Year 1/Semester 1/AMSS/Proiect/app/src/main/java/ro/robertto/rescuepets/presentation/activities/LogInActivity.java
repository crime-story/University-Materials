package ro.robertto.rescuepets.presentation.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.databinding.ActivityLoginBinding;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;
import timber.log.Timber;

public class LogInActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private ActionBar actionBar;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private String email = "";
    private String password = "";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = ActivityLoginBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );

        actionBar = getSupportActionBar();
        actionBar.setTitle( "Login" );

        progressDialog = new ProgressDialog( this );
        progressDialog.setTitle( "Please wait" );
        progressDialog.setMessage( "Logging In..." );
        progressDialog.setCanceledOnTouchOutside( false );

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
                .requestIdToken( getString( R.string.default_web_client_id ) )
                .requestScopes( new Scope( Scopes.PROFILE ), new Scope( Scopes.EMAIL ) )
                .build();

        googleSignInClient = GoogleSignIn.getClient( this, gso );

        binding.signInGoogleBtn.setOnClickListener( v -> signInGoogle() );

        binding.noAccountTv.setOnClickListener( v -> startActivity( new Intent( this, SignUpActivity.class ) ) );

        binding.forgotPasswordTv.setOnClickListener( v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder( this );
            builder.setTitle( "Reset Password" );
            EditText emailEt = new EditText( this );
            builder.setView( emailEt );
            builder.setPositiveButton( "Reset", ( dialog, which ) -> forgotPassword( emailEt ) );
            builder.setNegativeButton( "Close", null );
            builder.show();
        } );

        binding.loginBtn.setOnClickListener( v -> validateData() );
    }

    private void signInGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        launcher.launch( signInIntent );
    }

    private final ActivityResultLauncher< Intent > launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if ( result.getResultCode() == Activity.RESULT_OK ) {
                    Task< GoogleSignInAccount > task = GoogleSignIn.getSignedInAccountFromIntent( result.getData() );
                    handleResults( task );
                }
            }
    );

    private void handleResults( Task< GoogleSignInAccount > task ) {
        try {
            GoogleSignInAccount account = task.getResult( ApiException.class );
            if ( account != null ) {
                googleAuthentication( account );
            } else {
                Toast.makeText( this, "Google sign in failed", Toast.LENGTH_SHORT ).show();
            }
        } catch ( ApiException e ) {
            Toast.makeText( this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT ).show();
        }
    }

    private void googleAuthentication( GoogleSignInAccount account ) {
        AuthCredential googleCredential = GoogleAuthProvider.getCredential( account.getIdToken(), null );
        String googleEmail = account.getEmail();
        String googleName = account.getDisplayName();
        String googlePic = account.getPhotoUrl().toString();
        DatabaseReference database = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl).getReference( "Users" );

        database.orderByChild( "email" ).equalTo( googleEmail ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot ) {
                if ( snapshot.exists() ) {
                    User user = null;
                    for ( DataSnapshot childSnapshot : snapshot.getChildren() ) {
                        String uid = childSnapshot.getKey();
                        User userData = childSnapshot.getValue( User.class );
                        if ( userData != null && userData.getEmail().equals( googleEmail ) && uid != null ) {
                            userData.setUid( uid );
                            user = userData;
                            break;
                        }
                    }

                    if ( user != null ) {
                        startActivity( new Intent( LogInActivity.this, MainActivity.class ) );
                        finish();
                    } else {
                        Intent intent = new Intent( LogInActivity.this, GoogleSignUpActivity.class );
                        intent.putExtra( "email", googleEmail );
                        intent.putExtra( "name", googleName );
                        intent.putExtra( "pic", googlePic );
                        startActivity( intent );
                    }
                } else {
                    Intent intent = new Intent( LogInActivity.this, GoogleSignUpActivity.class );
                    intent.putExtra( "email", googleEmail );
                    intent.putExtra( "name", googleName );
                    intent.putExtra( "pic", googlePic );
                    startActivity( intent );
                }
            }

            @Override
            public void onCancelled( DatabaseError error ) {
                Timber.e( error.toException(), "onCancelled" );
                Toast.makeText( LogInActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT ).show();
            }
        } );

        FirebaseAuth.getInstance().signInWithCredential( googleCredential )
                .addOnCompleteListener( task -> {
                    if ( !task.isSuccessful() ) {
                        Toast.makeText( this, task.getException().toString(), Toast.LENGTH_SHORT ).show();
                    }
                } );
    }

    private void forgotPassword( EditText email ) {
        if ( !Patterns.EMAIL_ADDRESS.matcher( email.getText().toString() ).matches() ) {
            return;
        }

        firebaseAuth.sendPasswordResetEmail( email.getText().toString() )
                .addOnCompleteListener( task -> {
                    if ( task.isSuccessful() ) {
                        Toast.makeText( this, "Email sent.", Toast.LENGTH_SHORT ).show();
                    }
                } );
    }

    private void validateData() {
        email = binding.emailEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();

        if ( !Patterns.EMAIL_ADDRESS.matcher( email ).matches() ) {
            binding.emailEt.setError( "Invalid email format" );
        } else if ( TextUtils.isEmpty( password ) ) {
            binding.passwordEt.setError( "Please enter password" );
        } else {
            firebaseLogIn();
        }
    }

    private void firebaseLogIn() {
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword( email, password )
                .addOnSuccessListener( authResult -> {
                    progressDialog.dismiss();
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String userEmail = firebaseUser.getEmail();

                    if ( firebaseUser.isEmailVerified() ) {
                        Toast.makeText( this, "Logged in as " + userEmail, Toast.LENGTH_SHORT ).show();

                        DatabaseReference database = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl ).getReference( "Users" );
                        String deviceToken = getSharedPreferences( "ro.robertto.rescuepets", MODE_PRIVATE ).getString( "device_token", null );

                        database.child( firebaseUser.getUid() ).child( "deviceToken" ).setValue( deviceToken )
                                .addOnSuccessListener( aVoid -> Timber.tag( "device_token" ).d( "Device token updated successfully" ) )
                                .addOnFailureListener( e -> Timber.tag( "device_token" ).d( "Device token couldn't be updated" ) );

                        ValueEventListener postListener = new ValueEventListener() {
                            @Override
                            public void onDataChange( DataSnapshot dataSnapshot ) {
                                startActivity( new Intent( LogInActivity.this, MainActivity.class ) );
                                finish();
                            }

                            @Override
                            public void onCancelled( DatabaseError databaseError ) {
                                Timber.tag( "TAG" ).w( databaseError.toException(), "Failed." );
                            }
                        };

                        database.child( firebaseUser.getUid() ).child( "userType" ).addListenerForSingleValueEvent( postListener );
                        finish();
                    } else {
                        Toast.makeText( this, "Please verify your email address.", Toast.LENGTH_SHORT ).show();
                    }
                } )
                .addOnFailureListener( e -> {
                    progressDialog.dismiss();
                    Toast.makeText( this, "Login failed due to " + e.getMessage(), Toast.LENGTH_SHORT ).show();
                } );
    }

    private void checkUser() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if ( firebaseUser != null ) {
            startActivity( new Intent( this, MainActivity.class ) );
            finish();
        }
    }
}
