package com.mindinventory.fcmnotification.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // add action name on your Firebase Messaging Service.
        Intent i = new Intent("com.mindinventory.fcmnotification.messages.MyFirebaseMessagingService");
        i.setClass(context, MyFirebaseMessagingService.class);
        context.startService(i);
    }
}