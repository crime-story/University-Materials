package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ro.robertto.rescuepets.databinding.SecondPageGuideBinding;

public class GuidePage2 extends AppCompatActivity {

    private SecondPageGuideBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SecondPageGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ThirdPageButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, GuidePage3.class);
            startActivity(intent);
        });

        binding.ThirdPageButtonSkip.setOnClickListener(view -> {
            Intent intent = new Intent(this, LogInActivity.class);
            startActivity(intent);
        });
    }
}
