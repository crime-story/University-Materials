package ro.robertto.rescuepets.presentation.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import javax.net.ssl.HttpsURLConnection;

import ro.robertto.rescuepets.R;
import ro.robertto.rescuepets.domain.RescuePetsRemoteRepository;
import ro.robertto.rescuepets.presentation.activities.ChatActivity;
import ro.robertto.rescuepets.presentation.viewmodel.RescuePetsApplication;
import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onCreate() {
        super.onCreate();
        // Save the application context in a static field.
    }

    @Override
    public void onNewToken(String token) {
        Timber.tag("device_token").d(token);
        getSharedPreferences("ro.robertto.rescuepets", MODE_PRIVATE)
                .edit()
                .putString("device_token", token)
                .apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Create a notification channel if needed.
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "notificationChannel",
                    "Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Extract the data payload and display a notification.
        showNotification(
                remoteMessage.getData().get("title"),
                remoteMessage.getData().get("content"),
                remoteMessage.getData().get("uid"),
                remoteMessage.getData().get("name"),
                remoteMessage.getData().get("image")
        );
    }

    private void showNotification(String title, String message,
                                  String senderUid, String senderName, String senderImage) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("name", senderName);
        intent.putExtra("image", senderImage);
        intent.putExtra("uid", senderUid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String channelId = "notification_channel";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "Rescue Pets App", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, builder.build());
    }

    /**
     * Sends a push notification using the FCM HTTP v1 API.
     * This method is static and uses the static appContext to load assets.
     *
     * **WARNING:** Embedding your service account credentials in your app is very insecure.
     */
    public static void sendNotification(String senderUid,
                                        String receiverUid,
                                        String senderName,
                                        String senderImage) {

        FirebaseDatabase.getInstance(RescuePetsRemoteRepository.firebaseRealtimeDatabaseUrl)
                .getReference()
                .child("Users")
                .child(receiverUid)
                .child("deviceToken")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String deviceToken = snapshot.getValue(String.class);
                        if (deviceToken == null || deviceToken.isEmpty()) {
                            Timber.tag("notification").d("Device token is null or empty");
                            return;
                        }
                        new Thread(() -> {
                            // Replace with your Firebase project ID.
                            String projectId = "rescuepets-b1ab1";
                            String endpoint = "https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send";
                            try {
                                URL url = new URL(endpoint);
                                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                                connection.setReadTimeout(10000);
                                connection.setConnectTimeout(15000);
                                connection.setRequestMethod("POST");
                                connection.setDoInput(true);
                                connection.setDoOutput(true);

                                // Obtain the OAuth2 access token using the service account.
                                String accessToken = getAccessToken();
                                if (accessToken == null) {
                                    Timber.tag("notification").d("Failed to obtain access token");
                                    return;
                                }
                                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
                                connection.setRequestProperty("Content-Type", "application/json; UTF-8");

                                // Build the notification payload.
                                JSONObject data = new JSONObject();
                                data.put("title", "New message");
                                data.put("content", "You got a new message from " + senderName);
                                data.put("name", senderName);
                                data.put("image", senderImage);
                                data.put("uid", senderUid);

                                JSONObject message = new JSONObject();
                                message.put("token", deviceToken);
                                message.put("data", data);

                                JSONObject body = new JSONObject();
                                body.put("message", message);

                                OutputStream os = new BufferedOutputStream(connection.getOutputStream());
                                BufferedWriter writer = new BufferedWriter(
                                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                                writer.write(body.toString());
                                writer.flush();
                                writer.close();
                                os.close();

                                int responseCode = connection.getResponseCode();
                                if (responseCode == 200) {
                                    Timber.tag("notification").d("Notification sent successfully");
                                } else {
                                    Timber.tag("notification").d("Error sending notification, response code: " + responseCode);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Timber.tag("notification").d("Database error: " + error.getMessage());
                    }
                });
    }

    /**
     * Obtains an OAuth2 access token using the embedded service account JSON.
     * The "service-account.json" file should be placed in the assets folder.
     */
    private static String getAccessToken() {
        try {
            // Use the static application context to open the service account file.
            InputStream serviceAccountStream = RescuePetsApplication.get_rescue_pets_context().getAssets().open("service-account.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream)
                    .createScoped(Collections.singleton("https://www.googleapis.com/auth/firebase.messaging"));
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
