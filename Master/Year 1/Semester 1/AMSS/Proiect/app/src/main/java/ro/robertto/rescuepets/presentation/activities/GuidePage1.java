package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ro.robertto.rescuepets.databinding.FirstPageGuideBinding;

public class GuidePage1 extends AppCompatActivity {
    private FirstPageGuideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FirstPageGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.SecondPageButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, GuidePage2.class);
            startActivity(intent);
        });

        binding.SecondPageButtonSkip.setOnClickListener(view -> {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        });
    }
}
