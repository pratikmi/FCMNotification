package com.mindinventory.fcmnotification.messages;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.mindinventory.fcmnotification.MainActivity;
import com.mindinventory.fcmnotification.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mindinventory.fcmnotification.model.Data;
import com.mindinventory.fcmnotification.util.KeyUtils;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private NotificationManager mManager;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            Log.d(TAG, "!@#Message data payload: " + remoteMessage.getData());

            // Check if data needs to be processed by long running job
            // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            /*scheduleJob();*/

            try {
                Map<String, String> hashMap = remoteMessage.getData();
                Data data = new Data();
                data.setBody(hashMap.get(KeyUtils.BODY));
                data.setTitle(hashMap.get(KeyUtils.TITLE));
                data.setSound(hashMap.get(KeyUtils.SOUND));
                data.setIcon(hashMap.get(KeyUtils.ICON));
                data.setChannelId(hashMap.get(KeyUtils.CHANNEL_ID));
                data.setChannelName(hashMap.get(KeyUtils.CHANNEL_NAME));
                if (data.getBody() != null && !data.getBody().isEmpty()) {
                    handleNotification(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // [END receive_message]


    /**
     * For long-running tasks, schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }


    /**
     * this method will
     */
    private void handleNotification(Data mData) {
        String mNotificationChannelId = "";
        String mNotificationChannelName = "";
        String mNotificationTitle = "";
        String mNotificationMessage = "";
        try {
            // channel id
            if (mData.getChannelId() != null && !mData.getChannelId().isEmpty()) {
                mNotificationChannelId = mData.getChannelId();
            } else {
                mNotificationChannelId = "0";
            }


            // channel name
            if (mData.getChannelName() != null && !mData.getChannelName().isEmpty()) {
                mNotificationChannelName = mData.getChannelName();
            } else {
                mNotificationChannelName = "general";
            }


            // Also if you intend on generating your own notifications as a result of a received FCM
            // message, here is where that should be initiated. See sendNotification method below.
            // here we are handling notification in two different ways according to current device's Android OS.
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                handleNotificationFromOreo(mNotificationChannelId, mNotificationChannelName, mNotificationTitle, mNotificationMessage);
            } else {
                handleNotificationBelowOreo(mNotificationChannelId, mNotificationTitle, mNotificationMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     * this method will handle notification below Oreo Android 8.0
     *
     * @param mChannelId   channel id as a notification type
     * @param mTitle       title for the notification
     * @param mMessageBody message body of the notification
     */
    private void handleNotificationBelowOreo(String mChannelId, String mTitle, String mMessageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(KeyUtils.CHANNEL_ID, mChannelId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(mTitle)
                        .setContentText(mMessageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        getManager().notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     * this method will handle notification from Oreo Android 8.0 to upwards
     *
     * @param mNotificationChannelId   channel id as a notification type
     * @param mNotificationChannelName channel name which user will be able to see in application setting
     * @param mTitle                   title for the notification
     * @param mMessageBody             message body of the notification
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void handleNotificationFromOreo(String mNotificationChannelId, String mNotificationChannelName, String mTitle, String mMessageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder mNotificationBuilder = new Notification.Builder(this, mNotificationChannelId)
                .setContentTitle(mTitle)
                .setContentText(mMessageBody)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        createChannels(mNotificationChannelId, mNotificationChannelName);

        getManager().notify((int) System.currentTimeMillis(), mNotificationBuilder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels(String mNotificationChannelId, String mNotificationChannelName) {

        // create android channel
        NotificationChannel androidChannel = new NotificationChannel(mNotificationChannelId,
                mNotificationChannelName, NotificationManager.IMPORTANCE_DEFAULT);

        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);


        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);


        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);


        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);

    }


    /**
     * this method will return instance of notification manager
     *
     * @return
     */
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }
}