package ro.robertto.rescuepets.presentation.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ro.robertto.rescuepets.R;

public class ConfirmEmailActivity extends AppCompatActivity {

    private final String gmailPackageName = "com.google.android.gm";
    private final String playStorePackageName = "com.android.vending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_email);

        Button gmailButton = findViewById(R.id.gmail_button);
        gmailButton.setOnClickListener(view -> {
            Intent gmailIntent = getPackageManager().getLaunchIntentForPackage(gmailPackageName);
            if (gmailIntent != null) {
                startActivity(gmailIntent);
            } else {
                Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
                playStoreIntent.setData(Uri.parse("market://details?id=" + gmailPackageName));
                playStoreIntent.setPackage(playStorePackageName);
                startActivity(playStoreIntent);
            }
        });

        Button outlookButton = findViewById(R.id.outlook_button);
        outlookButton.setOnClickListener(view -> {
            String outlookPackageName = "com.microsoft.office.outlook";
            Intent outlookIntent = getPackageManager().getLaunchIntentForPackage(outlookPackageName);
            if (outlookIntent != null) {
                startActivity(outlookIntent);
            } else {
                Intent playStoreIntent = new Intent(Intent.ACTION_VIEW);
                playStoreIntent.setData(Uri.parse("market://details?id=" + outlookPackageName));
                playStoreIntent.setPackage(playStorePackageName);
                startActivity(playStoreIntent);
            }
        });

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            startActivity(new Intent(this, LogInActivity.class));
        });
    }
}
