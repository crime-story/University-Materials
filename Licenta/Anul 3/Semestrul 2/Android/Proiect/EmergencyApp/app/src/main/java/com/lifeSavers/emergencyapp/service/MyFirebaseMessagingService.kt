package com.lifeSavers.emergencyapp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lifeSavers.emergencyapp.R
import com.lifeSavers.emergencyapp.activities.ChatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.*
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(s: String) {
        Log.d("device_token", s)
        val sharedPreferences = getSharedPreferences("com.lifeSavers.emergencyapp", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("device_token", s)
            apply()
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "notificationChannel",
                "Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }

        showNotification(
            remoteMessage.data["title"].toString(),
            remoteMessage.data["content"].toString(),
            remoteMessage.data["uid"].toString(),
            remoteMessage.data["name"].toString(),
            remoteMessage.data["image"].toString()
        )
    }

    private fun showNotification(
        title: String?,
        message: String?,
        senderUid: String,
        senderName: String,
        senderImage: String
    ) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("name", senderName)
        intent.putExtra("image", senderImage)
        intent.putExtra("uid", senderUid)

        val channelId = "notification_channel"
        // FLAG_ACTIVITY_CLEAR_TOP = clear activities present in the activity stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_ls_round)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // check Android >= Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, "Emergency App", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
    }

    fun sendNotification(
        senderUid: String,
        receiverUid: String,
        senderName: String,
        senderImage: String
    ) {
        val serverKey =
            "AAAAISZe1qo:APA91bEEPKBySrq7D_VfTdPX3vAbp3kahRgnYYoO17dfmG1qwFqqZ-1IiQj8TFuvjmO-mqXcKGb6e_Qo4ZdebgHaxR3Wn_NvNbDGWJCBUReFpPVWt-1RcEGoxYWqhc8kPlc2-UnHhSv9"
        FirebaseDatabase.getInstance("https://emergencyapp-3a6bd-default-rtdb.europe-west1.firebasedatabase.app/")
            .reference.child("Users")
            .child(receiverUid)
            .child("deviceToken").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val deviceToken = snapshot.getValue(String::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        val endpoint = "https://fcm.googleapis.com/fcm/send"
                        try {
                            val url = URL(endpoint)
                            val httpsURLConnection: HttpsURLConnection =
                                url.openConnection() as HttpsURLConnection
                            httpsURLConnection.readTimeout = 10000
                            httpsURLConnection.connectTimeout = 15000
                            httpsURLConnection.requestMethod = "POST"
                            httpsURLConnection.doInput = true
                            httpsURLConnection.doOutput = true

                            // Adding the necessary headers
                            httpsURLConnection.setRequestProperty(
                                "Authorization",
                                "key=$serverKey"
                            )
                            httpsURLConnection.setRequestProperty(
                                "Content-Type",
                                "application/json"
                            )

                            val data = JSONObject()
                            data.put("title", "New message")
                            data.put("content", "You got a new message from $senderName")
                            data.put("name", senderName)
                            data.put("image", senderImage)
                            data.put("uid", senderUid)

                            val body = JSONObject()
                            body.put("data", data)
                            body.put("to", deviceToken)

                            val outputStream: OutputStream =
                                BufferedOutputStream(httpsURLConnection.outputStream)
                            val writer = BufferedWriter(OutputStreamWriter(outputStream, "utf-8"))
                            writer.write(body.toString())
                            writer.flush()
                            writer.close()
                            outputStream.close()

                            val responseCode: Int = httpsURLConnection.responseCode
                            val responseMessage: String = httpsURLConnection.responseMessage

                            var result = String()
                            var inputStream: InputStream? = null
                            inputStream = if (responseCode in 400..499) {
                                httpsURLConnection.errorStream
                            } else {
                                httpsURLConnection.inputStream
                            }

                            if (responseCode == 200) {
                                Log.d("notification", "Notification sent")
                            } else {
                                Log.d("notification", "Response Error")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("notification", "DatabaseError: $error")
                }
            })
    }
}
