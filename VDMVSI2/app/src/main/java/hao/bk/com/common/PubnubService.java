package hao.bk.com.common;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import hao.bk.com.config.Config;
import hao.bk.com.utils.TextUtils;
import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.MainActivity;
import hao.bk.com.vdmvsi.R;

/**
 * Created by T430 on 4/26/2016.
 */

public class PubnubService extends Service {
    private final String tag = "PubnubService";
    final String channel = "vsi_group_chanel_notificaton";
    Pubnub pubnub = new Pubnub(Config.publish_key_pub, Config.publish_key_pub, Config.secret_key_pub, "", false);
    PowerManager.WakeLock wl = null;
    // Sets an ID for the notification, so it can be updated
    public static final int notifyID = 9002;
    NotificationCompat.Builder builder;

    private void notifyUser(Object message) {
        String msg = message.toString();
        JsonParser parser = new JsonParser();
        JsonObject obj = (JsonObject) (parser.parse(msg).getAsJsonObject()).get("mess");
        sendNotification(message.toString());
    }

    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SubscribeAtBoot");
        if (wl != null) {
            wl.acquire();
            Log.i("PUBNUB", "Partial Wake Lock : " + wl.isHeld());
        }

        try {
            pubnub.subscribe(new String[] { channel }, new Callback() {
                public void connectCallback(String channel) {
                }

                public void disconnectCallback(String channel) {
                }

                public void reconnectCallback(String channel) {
                }

                @Override
                public void successCallback(String channel, Object message) {
                    notifyUser(message.toString());
                }

                @Override
                public void errorCallback(String channel, PubnubError error) {
                    super.errorCallback(channel, error);
                }
            });
        } catch (PubnubException e) {

        }
    }
    private void sendNotification(String msg) {
        Util.LOGD(tag,"---sendNotify:"+msg);

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("msg", msg);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                resultIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_avatar)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_avatar))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(TextUtils.isEmpty(msg)?"You've received new message.":msg));
        // Set pending intent
        mNotifyBuilder.setContentIntent(resultPendingIntent);

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set the content for Notification
        mNotifyBuilder.setContentText(TextUtils.isEmpty(msg)?"New message":msg);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        // Post a notification
        mNotificationManager.notify(notifyID, mNotifyBuilder.build());
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wl != null) {
            wl.release();
            Log.i("PUBNUB", "Partial Wake Lock : " + wl.isHeld());
            Toast.makeText(this, "Partial Wake Lock : " + wl.isHeld(), Toast.LENGTH_LONG).show();
            wl = null;
        }
        Toast.makeText(this, "PubnubService destroyed...", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
