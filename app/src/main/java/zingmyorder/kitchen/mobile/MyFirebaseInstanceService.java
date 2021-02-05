package zingmyorder.kitchen.mobile;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import zingmyorder.kitchen.mobile.view.orders.OrdersListActivity;

import static zingmyorder.kitchen.mobile.Utils.INFO_UPDATE_FILTER;


public class MyFirebaseInstanceService extends FirebaseMessagingService {
    private static final String ACTION_DISMISS = "action_dismis";
    private static final String ACTION_VIEW_ORDERS = "action_orders";
    Bitmap bitmap;
    private NotificationManager notifManager;
    SharedPreferences prefs;
    private String selected_ringtone="1";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        prefs=getSharedPreferences(Utils.MY_PREFS_NAME, MODE_PRIVATE);
        selected_ringtone=prefs.getString("selected_ringtone", "1");
        if(remoteMessage.getData()!=null) {
            String title=remoteMessage.getData().get("message");
            String orders_count=remoteMessage.getData().get("count");
            String refresh_tag=remoteMessage.getData().get("refresh");
            String body=remoteMessage.getData().get("body");
            Intent i = new Intent ( INFO_UPDATE_FILTER );
            i.putExtra("orders_count",orders_count);
            i.putExtra("refresh_tag",refresh_tag);
            i.putExtra ( INFO_UPDATE_FILTER , "refresh");
            LocalBroadcastManager. getInstance (this) .sendBroadcast (i);
//            bitmap = getImageFromUrl(remoteMessage.getData().get("image"));
//            if (bitmap==null)
//                bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            if (isAppIsInBackground(this))
            sendNotification(title, body);
//            createNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
    }

    private Bitmap getImageFromUrl(String image) {
        URL url = null;
        try {
            url = new URL(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url!=null) {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream is = connection.getInputStream();
                Bitmap bmap = BitmapFactory.decodeStream(is);
                return bmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else return null;

    }

    private void sendNotification(String title, String body) {
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE);
        Uri alarmSound = ringChooser(selected_ringtone);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "101";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build();
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.setDescription("Game Notifications");
            notificationChannel.enableLights(true);
            notificationChannel.setSound(alarmSound, audioAttributes);
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000});
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent buttonIntent = new Intent(this, NotificationActionsReceiver.class);
        buttonIntent.putExtra("notificationId",1);

//Create the PendingIntent
        PendingIntent btPendingIntent = PendingIntent.getBroadcast(this, 0, buttonIntent,0);

        Intent dismissIntent = new Intent(this, OrdersListActivity.class);
        dismissIntent.setAction(ACTION_DISMISS);
        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

        //Intent snoozeIntent = new Intent(this, OrderListActivity.class);
        Intent snoozeIntent = new Intent(this, OrdersListActivity.class);
        snoozeIntent.putExtra("zingmyorder.kitchen.mobile.notifyId", 1);
        snoozeIntent.setAction(ACTION_VIEW_ORDERS);
        PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);

        Intent intent = new Intent(this, OrdersListActivity.class);
        intent.putExtra("zingmyorder.kitchen.mobile.notifyId", 1);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent emptyIntent=new Intent();
        PendingIntent emptyPendingIntent = PendingIntent.getActivity(this, 0, emptyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, snoozeIntent, 0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setCategory(Notification.CATEGORY_CALL)
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(body)
//                .setLargeIcon(image)
                .setStyle(style)
                .setContentInfo("Info")
                .setSound(alarmSound)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setFullScreenIntent(emptyPendingIntent,true)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
//                .addAction(android.R.drawable.star_on,
//                        "DISMISS", btPendingIntent)
                .addAction(android.R.drawable.star_on,
                        "SEE ORDER", pIntent);


        if (Build.VERSION.SDK_INT >= 21) notificationBuilder.setVibrate(new long[0]);
        Notification notification = notificationBuilder.build();
//        notification.flags = Notification.FLAG_INSISTENT;
        notificationManager.notify(1, notification);
    }

    private Uri ringChooser(String ringID) {
        if (ringID.equalsIgnoreCase("1")) {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.alert43);
        } else if (ringID.equalsIgnoreCase("2")) {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.ring);
        } else if (ringID.equalsIgnoreCase("3")) {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.short_ring);
        } else if (ringID.equalsIgnoreCase("4")) {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.bell);
        } else {
            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.alert43);
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(!task.isSuccessful()){

                }
                String token = task.getResult().getToken();
                FirebaseMessaging.getInstance().subscribeToTopic("all");
                Log.d("Token ", token);
            }
        });
    }

    public void createNotification(String aMessage,String body) {
        final int NOTIFY_ID = 1002;

        // There are hardcoding only for show it's just strings
        String name = "my_package_channel";
        String id = "my_package_channel_1"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager =
                    (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.GREEN);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle(aMessage)
                    .setContentText(body)// required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(this);

            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            builder.setContentTitle(aMessage)
                    .setContentText(body)// required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder) // required
                    .setContentText(this.getString(R.string.app_name))  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        } // else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }

    /**
     * Method checks if the app is in background or not
     */
    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        }
        else
        {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}
