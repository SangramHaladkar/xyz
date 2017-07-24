package com.app.monitormymortgage.GCM;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.app.monitormymortgage.Activity.SplashScreenActivity;
import com.app.monitormymortgage.R;

import java.util.List;
import java.util.Random;


/**
 * Created by Sangram on 23/05/16.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
//        try {
//            boolean foreground=new ForegroundCheckTask().execute(this).get();
//            Log.v("TAG", String.valueOf(foreground));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }


//        if (from.startsWith("/topics/")) {
//            // message received from some topic.
//        } else {
//            // normal downstream message.
//        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(data, isAppRunning(getApplicationContext()));
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param bundleData GCM bundleData received.
     */
    private void sendNotification(Bundle bundleData, boolean isAppRunning) {

        String message = bundleData.getString("message");
        String title = bundleData.getString("title");

        Log.i("GCM", "sendNotification");

        if (isAppRunning) {
            Log.i("GCM", "isAppRunning notification");
            Intent intent = new Intent("pushNotification");
            intent.putExtra("message", message);
            intent.putExtra("title", title);
            sendPushNotificationBroadcast(intent);
        } else {
            Log.i("GCM", "Building Intent and Notification");
            Intent intent = new Intent(this, SplashScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setAction(Long.toString(System.currentTimeMillis()));
            intent.putExtra("viaNotification", true);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.notificationicon)
                    .setColor(getResources().getColor(R.color.notification_background))
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(m, notificationBuilder.build());
            Log.i("GCM", "Notification Manager sent");
        }
    }

    public static boolean isAppRunning(Context context) {
//        return !BaseActivity.isInBackground;
//        Log.i("GCM", "Background: " + String.valueOf(BaseActivity.isInBackground));
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfos = activityManager.getRunningAppProcesses();

        Log.i("GCM", runningAppProcessInfos.toString());
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfos) {
            Log.i("GCM", processInfo.toString());
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    Log.i("GCM", activeProcess + ":" + processInfo.importance);

                    if (activeProcess.equals(context.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void sendPushNotificationBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
