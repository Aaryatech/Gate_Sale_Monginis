package com.ats.gate_sale_monginis.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.activity.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0 || !remoteMessage.getNotification().equals(null)) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                sendPushNotification(json);
            } catch (Exception e) {
                //Log.e(TAG, "-----------------------------Exception: " + e.getMessage());
                e.printStackTrace();
            }

            super.onMessageReceived(remoteMessage);
            ////Log.e("msg", "-------------------------------------onMessageReceived: " + remoteMessage.getData().get("message"));
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                    .setSmallIcon(R.mipmap.ic_launcher).setDefaults(Notification.DEFAULT_ALL)
//                    .setContentTitle(remoteMessage.getData().get("title"))
//                    .setContentText(remoteMessage.getData().get("body"));
//            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            manager.notify(0, builder.build());
//            builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);

        }
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);
    }

    private void sendPushNotification(JSONObject json) {

        try {
            String title = json.getString("title");
            String message = json.getString("body");
            int tag = json.getInt("tag");

            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("FcmTag", tag);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            if (tag == 1) {
                message = "Order bill is pending";
            } else if (tag == 2) {
                message = "Order bill is approved";
            } else if (tag == 3) {
                message = "Order bill is rejected";
            }

            mNotificationManager.showSmallNotification("Gate Sale", message, intent);

        } catch (JSONException e) {
            //Log.e(TAG, "Json Exception: -----------" + e.getMessage());
        } catch (Exception e) {
            //Log.e(TAG, "Exception: ------------" + e.getMessage());
            e.printStackTrace();
        }

    }
}
