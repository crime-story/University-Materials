package ro.robertto.rescuepets.presentation.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

import ro.robertto.rescuepets.data.pojo.User;
import ro.robertto.rescuepets.databinding.ActivitySignUpBinding;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;

public class SignUpActivity extends AppCompatActivity {
    // ViewBinding
    private ActivitySignUpBinding binding;

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
    private String phoneNumber = "";
    private String birthDate = "";
    private String password = "";
    private String confirmedPassword = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actionBar = getSupportActionBar();
        actionBar.setTitle("Sign Up");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // Configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Creating account...");
        progressDialog.setCanceledOnTouchOutside(false);

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        // handle click, begin signUp
        binding.signUpBtn.setOnClickListener(view -> {
            // validate data
            validateData();
        });
    }

    private void validateData() {
        // get data
        name = binding.nameEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim();
        phoneNumber = binding.phoneNumberEt.getText().toString().trim();
        birthDate = binding.birthDateEt.getText().toString().trim();
        password = binding.passwordEt.getText().toString().trim();
        confirmedPassword = binding.confirmedPasswordEt.getText().toString().trim();

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // validate data
        if (name.isEmpty()) {
            binding.nameEt.setError("Please enter name");
        } else if (name.length() < 3) {
            binding.nameEt.setError("Name must contain at least 3 letters");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEt.setError("Invalid email format");
        } else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            binding.phoneNumberEt.setError("Invalid phone format");
        } else if (birthDate.length() != 4 || Integer.parseInt(birthDate) >= currentYear) {
            binding.birthDateEt.setError("Invalid year");
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordEt.setError("Please enter password");
        } else if (password.length() < 6) {
            binding.passwordEt.setError("Password must contain at least 6 characters");
        } else if (!password.equals(confirmedPassword)) {
            binding.passwordEt.setError("Password and Confirmed Password must match");
            binding.confirmedPasswordEt.setError("Password and Confirmed Password must match");
        } else {
            // data is valid, continue signUp
            firebaseSignUp();
        }
    }

    private void firebaseSignUp() {
        // show progress
        progressDialog.show();

        // Create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    // signUp success
                    progressDialog.dismiss();

                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    firebaseUser.sendEmailVerification()
                            .addOnSuccessListener(aVoid -> {
                                String name1 = name;
                                String email1 = email;
                                String phoneNumber1 = phoneNumber;
                                String birthDate1 = birthDate;

                                database = FirebaseDatabase.getInstance( RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl )
                                        .getReference("Users");

                                String deviceToken = getSharedPreferences("ro.robertto.rescuepets", MODE_PRIVATE)
                                        .getString("device_token", null);

                                User user = new User(firebaseUser.getUid(), name1, email1, phoneNumber1, birthDate1, null, "", deviceToken);
                                database.child(firebaseUser.getUid())
                                        .setValue(user)
                                        .addOnSuccessListener(aVoid1 -> {
                                            binding.nameEt.getText().clear();
                                            binding.emailEt.getText().clear();
                                            binding.phoneNumberEt.getText().clear();
                                            binding.birthDateEt.getText().clear();

                                            Toast.makeText(
                                                    this,
                                                    "Account created. Please check your email for verification.",
                                                    Toast.LENGTH_SHORT
                                            ).show();

                                            firebaseAuth.signOut();

                                            startActivity(new Intent(this, ConfirmEmailActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show());
                            })
                            .addOnFailureListener(e -> Toast.makeText(this, "Account not created due to " + e.getMessage() + ".", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> {
                    // signUp failed
                    progressDialog.dismiss();
                    Toast.makeText(this, "SignUp Failed due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // go back to the previous activity, when back button of actionBar clicked
        return super.onSupportNavigateUp();
    }
}
