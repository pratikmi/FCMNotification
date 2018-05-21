# FCM Notifications

Project demonstrating send and receive notifications on Android device using FireBase cloud messaging service.


# Setup

* Create a new project in FireBase console from following link 
   https://console.firebase.google.com/u/0/

- Add channel_id into your app manifest.xml file like this 
   ```
   <meta-data
      android:name="com.google.firebase.messaging.default_notification_channel_id"
      android:value="@string/default_notification_channel_id"/>
   ```
            
* Add MyFirebaseMessagingService to your app manifest.xml file
   ```
   <service
       android:name="com.mindinventory.fcmnotification.messages.MyFirebaseMessagingService">
       <intent-filter>
           <action android:name="com.google.firebase.MESSAGING_EVENT"/>
       </intent-filter>
   </service>
   ```
         
 * Add MyFirebaseInstanceIDService into manifest.xml file to register token to your server.
   ```
   <service
       android:name="com.mindinventory.fcmnotification.messages.MyFirebaseInstanceIDService">
       <intent-filter>
           <action android:name="com.google.firebase.INSTANCE_ID_EVENT"/>
       </intent-filter>
   </service>
   ```

* After that try sending a push notification from following link.  
https://console.firebase.google.com/u/0/project/[YOUR PROJECT ID]/notification


* You can receive message of notification in Main Activity using following code
```
  if (getIntent().getExtras() != null) {
       for (String key : getIntent().getExtras().keySet()) {
              Object value = getIntent().getExtras().get(key);
          Log.d(TAG, "Key: " + key + " Value: " + value);
      }
  }
```
 
![Screenshots](/notification_sample.png?raw=true "Notification Panel")

# LICENSE!

FCMNotification is [MIT-licensed](https://github.com/mindinventory1/FCMNotification/blob/master/LICENSE).

## Let us know!
We’d be really happy if you sent us links to your projects where you use our component. Just send an email to sales@mindinventory.com And do let us know if you have any questions or suggestion regarding our work.
