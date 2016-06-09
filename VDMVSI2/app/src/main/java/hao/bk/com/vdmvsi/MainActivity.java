package hao.bk.com.vdmvsi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import hao.bk.com.chat.ChatActivity;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.PubnubService;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.config.Config;
import hao.bk.com.customview.ViewToolBar;
import hao.bk.com.models.ChatObj;
import hao.bk.com.models.ChatPubNubObj;
import hao.bk.com.utils.Util;

public class MainActivity extends AppCompatActivity {
    public BottomBar mBottomBar;
    ToastUtil toastUtil;
    DataStoreApp dataStoreApp;
    EditText edtSearch;
    ViewToolBar vToolBar;
    public static Pubnub pubnub;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toastUtil = new ToastUtil(this);
        dataStoreApp = new DataStoreApp(this);
        vToolBar = new ViewToolBar(this, (View)findViewById(R.id.container_main));
        edtSearch = vToolBar.getEdtSearch();
        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bt_bar_coporate) {
                    // The user selected item number one.
                    loadNewsFragment(new FragmentCoporateTab());
                    return;
                }
                if (menuItemId == R.id.bt_bar_chat) {
                    // The user selected item number one.
                    loadNewsFragment(new FragmentChatTab());
                    return;
                }
                if (menuItemId == R.id.bt_bar_news) {
                    // The user selected item number one.
                    loadNewsFragment(new FragmentNewsVsiTab());
                    return;
                }
                if (menuItemId == R.id.bt_bar_delivery) {
                    // The user selected item number one.
                    loadNewsFragment(new FragmentDeliveryTab());
                    return;
                }
                if (menuItemId == R.id.bt_bar_more) {
                    // The user selected item number one.
                    loadNewsFragment(new FragmentMoreTab());
                    return;
                }

            }
            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                if (menuItemId == R.id.bt_bar_coporate) {
                    // The user reselected item number one, scroll your content to top.\
                }
            }
        });
        animationBottomTabs();
        Intent serviceIntent = new Intent(this, PubnubService.class);
        startService(serviceIntent);
        pubnub = new Pubnub(Config.publish_key_pub, Config.subscribe_key_pub,  Config.secret_key_pub, "",false);
//        pubnub,
        subcribeMyChannel();
    }

    private void animationBottomTabs(){
        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.PrimaryDarkColor));
        mBottomBar.mapColorForTab(1, ContextCompat.getColor(this, R.color.PrimaryColor));
        mBottomBar.mapColorForTab(2, ContextCompat.getColor(this, R.color.PrimaryDarkColor));
        mBottomBar.mapColorForTab(3, ContextCompat.getColor(this, R.color.PrimaryColor));
        mBottomBar.mapColorForTab(4, ContextCompat.getColor(this, R.color.PrimaryDarkColor));
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
        mBottomBar.onSaveInstanceState(outState);
    }
    // Load fragment tin tuc tu cac giai dau qucc gia
    public void loadNewsFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frm_main, fragment)
                .commit();
    }
    // Sets an ID for the notification, so it can be updated
    public static final int notifyID = 9001;
    NotificationCompat.Builder builder;

    // public void sendMessage to Chat Activity
//    public void sendMessgeToChatActivity(String message){
//        try{
//            Log.v("sendMessageTo",message);
//            Intent intent = new Intent(Config.NAME_BROAD_CAST_FROM_MAIN_TO_CHAT_ACTIVITY).putExtra(Config.BROAD_CASS_MSG_MAIN_TO_CHAT_FILTER, message);
//            sendBroadcast(intent);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
    private void sendNotification(String msg) {
        Log.v("message_punub",msg);
//        if(dataStoreApp.getChatActivityShowing()){
//            sendMessgeToChatActivity(msg);
//            return;
//        }
        ChatPubNubObj chatObj = JsonCommon.getMessageChatFromPubNub(msg);
        Log.v("type",String.valueOf(chatObj.getType()));
        if(chatObj.getType() == 4){
            Intent  resultIntent = new Intent(this, ChatActivity.class);
            resultIntent.putExtra(Config.CHAT_PUBNUB, msg);
            resultIntent.setAction(Intent.ACTION_MAIN);
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(chatObj.getTitle())
                    .setSmallIcon(R.drawable.ic_avatar)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_avatar))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(TextUtils.isEmpty(chatObj.getContent())?"You've received new message.":chatObj.getContent()));
            // Set pending intent
            mNotifyBuilder.setContentIntent(resultPendingIntent);
            // Set Vibrate, Sound and Light
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
            mNotifyBuilder.setDefaults(defaults);
            // Set the content for Notification
            mNotifyBuilder.setContentText(TextUtils.isEmpty(chatObj.getContent())?"New message":chatObj.getContent());
            // Set autocancel
            mNotifyBuilder.setAutoCancel(true);
            // Post a notification
            mNotificationManager.notify(notifyID, mNotifyBuilder.build());
        }else{
            Intent  resultIntent = new Intent(this, MainActivity.class);
            resultIntent.putExtra(Config.CHAT_PUBNUB, msg);
            resultIntent.setAction(Intent.ACTION_MAIN);
            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                    resultIntent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder mNotifyBuilder;
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(chatObj.getTitle())
                    .setSmallIcon(R.drawable.ic_avatar)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_avatar))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(TextUtils.isEmpty(chatObj.getContent())?"You've received new message.":chatObj.getContent()));
            // Set pending intent
            mNotifyBuilder.setContentIntent(resultPendingIntent);
            // Set Vibrate, Sound and Light
            int defaults = 0;
            defaults = defaults | Notification.DEFAULT_LIGHTS;
            defaults = defaults | Notification.DEFAULT_VIBRATE;
            defaults = defaults | Notification.DEFAULT_SOUND;
            mNotifyBuilder.setDefaults(defaults);
            // Set the content for Notification
            mNotifyBuilder.setContentText(TextUtils.isEmpty(chatObj.getContent())?"New message":chatObj.getContent());
            // Set autocancel
            mNotifyBuilder.setAutoCancel(true);
            // Post a notification
            mNotificationManager.notify(notifyID, mNotifyBuilder.build());
        }


    }

    @Override
    protected void onDestroy() {
        unSubcribeMychannel(Config.startChannelName + dataStoreApp.getUserName());
        super.onDestroy();
    }
    public static void unSubcribeMychannel(String myChannel){
        try{
            pubnub.unsubscribe(myChannel);
        }catch (Exception e){
        }
    }
    // subcribe my channer for message from my friend chat
    // flase: la unsubcribe
    public void subcribeMyChannel(){

        String my_channel = Config.CHANNEL_NOTIFICATION ;
        Log.v("test1",my_channel);
        //unSubcribe
        try {
            pubnub.subscribe(my_channel, new Callback() {
                        @Override
                        public void connectCallback(String channel, Object message) {
                            Util.LOGD("26_4", "connectCallback");
                        }
                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            Util.LOGD("26_4", "SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        public void reconnectCallback(String channel, Object message) {
                            Util.LOGD("26_4","SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            Util.LOGD("27_4","SUBSCRIBE : " + channel + " : "
                                    + message.toString());
                            sendNotification(message.toString());
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            Util.LOGD("26_4","SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                        }
                    }
            );
        } catch (PubnubException e) {
            Util.LOGD("26_4", "haodv " + e.toString());
        }
    }

    @Override
    public void onBackPressed() {
        confirmFinishApp();
    }
    private void confirmFinishApp() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.txt_finish_app_confirm));
        builder.setPositiveButton(getString(R.string.btn_ok_vn), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.txt_continue), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener(){
            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.PrimaryColor));
                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.dark_ness_text));
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
