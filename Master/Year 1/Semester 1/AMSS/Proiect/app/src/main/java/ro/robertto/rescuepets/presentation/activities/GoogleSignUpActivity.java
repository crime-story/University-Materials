package ro.robertto.rescuepets.presentation.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.databinding.ActivityGoogleSignUpBinding;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;

public class GoogleSignUpActivity extends AppCompatActivity {

    // ViewBinding
    private ActivityGoogleSignUpBinding binding;

    // Database
    private DatabaseReference database;

    // ActionBar
    private ActionBar actionBar;

    // ProgressDialog
    private ProgressDialog progressDialog;

    // FirebaseAuth
    private FirebaseAuth firebaseAuth;

    private String name = "";
    private String email = "";
    private String photo = "";
    private String phoneNumber = "";
    private String birthDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGoogleSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Sign Up Google");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating account...");
        progressDialog.setCanceledOnTouchOutside(false);

        // Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        EditText nameEditText = findViewById(R.id.nameEt);

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        photo = getIntent().getStringExtra("pic");

        nameEditText.setText(name);

        // Handle click, begin signUp
        binding.signUpBtn.setOnClickListener(view -> validateData());
    }

    private void validateData() {
        // Get data
        name = binding.nameEt.getText().toString().trim();
        phoneNumber = binding.phoneNumberEt.getText().toString().trim();
        birthDate = binding.birthDateEt.getText().toString().trim();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // Validate data
        if (name.isEmpty()) {
            binding.nameEt.setError("Please enter name");
        } else if (name.length() < 3) {
            binding.nameEt.setError("Name must contain at least 3 letters");
        } else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            binding.phoneNumberEt.setError("Invalid phone format");
        } else if (birthDate.length() != 4 || birthDate.compareTo(String.valueOf(currentYear)) >= 0) {
            binding.birthDateEt.setError("Invalid year");
        } else {
            // Data is valid, continue signUp
            firebaseSignUp();
        }
    }

    private void firebaseSignUp() {
        // Show progress
        progressDialog.show();

        // Get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String name1 = name;
        String email1 = email;
        String phoneNumber1 = phoneNumber;
        String birthDate1 = birthDate;
        String profilePic = photo;

        database = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                .getReference("Users");

        String deviceToken = getSharedPreferences("ro.robertto.rescuepets", MODE_PRIVATE)
                .getString("device_token", null);

        User user = new User(
                firebaseUser.getUid(), name1, email1, phoneNumber1, birthDate1, null, profilePic,
                deviceToken
        );
        database.child(firebaseUser.getUid())
                .setValue(user) // Adds a new registered user to the Database
                .addOnSuccessListener(aVoid -> {
                    binding.nameEt.getText().clear();
                    binding.phoneNumberEt.getText().clear();
                    binding.birthDateEt.getText().clear();

                    Toast.makeText(this, "Account created.", Toast.LENGTH_SHORT).show();

                    firebaseAuth.signOut();

                    // Go to login page
                    startActivity(new Intent(this, LogInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Go back to the previous activity when the back button of actionBar is clicked
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
