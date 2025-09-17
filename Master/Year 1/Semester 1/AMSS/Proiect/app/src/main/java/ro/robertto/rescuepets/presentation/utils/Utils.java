package ro.robertto.rescuepets.presentation.utils;

import android.app.Activity;
import android.content.Intent;

public class Utils {
    public static void shareButtonFunctionality(Activity activity) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Rescue Pets App");
        String appUrl = "https://play.google.com/store/apps/details?id=ro.robertto.rescuepets";
        shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out the Rescue Pets App at:\n" + appUrl
        );
        activity.startActivity(Intent.createChooser(shareIntent, "Share the app"));
    }
}
