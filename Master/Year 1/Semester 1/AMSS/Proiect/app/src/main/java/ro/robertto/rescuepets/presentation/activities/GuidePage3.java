package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ro.robertto.rescuepets.databinding.ThirdPageGuideBinding;

public class GuidePage3 extends AppCompatActivity {

    private ThirdPageGuideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ThirdPageGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.LoginPageButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        });
    }
}
