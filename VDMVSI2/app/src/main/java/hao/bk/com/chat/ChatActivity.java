package hao.bk.com.chat;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import hao.bk.com.adapter.ChatMessageAdapter;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.customview.ViewToolBar;
import hao.bk.com.models.ChatObj;
import hao.bk.com.models.ChatPubNubObj;
import hao.bk.com.models.MemberVsiObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.utils.TextUtils;
import hao.bk.com.utils.Util;
import hao.bk.com.vdmvsi.MainActivity;
import hao.bk.com.vdmvsi.R;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/22/2016.
 */
public class ChatActivity extends AppCompatActivity{
    private final String tag = "ChatActivity";
    MemberVsiObj friend;
    ToastUtil toastUtil;
    ViewToolBar vToolBar;
    DataStoreApp dataStoreApp;
    View viewRoot;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    EditText edtMsg;
    ImageButton imgSend;
    Bitmap myAvatar = null, yourAvatar = null;
    ArrayList<ChatObj> listChat;
    ChatMessageAdapter chatMessageAdapter;
    MyProgressDialog mpDl;
    String yourUserName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        viewRoot = (View) findViewById(R.id.container);
        vToolBar = new ViewToolBar(this, viewRoot);
        mpDl = new MyProgressDialog(this);
        dataStoreApp = new DataStoreApp(this);
        dataStoreApp.setChatActivityShowing(true);
        Bundle extras = getIntent().getExtras();
        friend = new MemberVsiObj();
        listChat = new ArrayList<>();
        if (extras != null) {
            if (extras.containsKey(Config.CHAT_PUBNUB)) {
                ChatPubNubObj chatMsg = JsonCommon.getMessageChatFromPubNub(extras.getString(Config.CHAT_PUBNUB));
                friend.setUserName(chatMsg.getFrom());
            } else {
                friend.setUserName(extras.getString(Config.USER_NAME_PUT, ""));
            }
        }
        friend.setUrlThumnails(dataStoreApp.getFriendAvatar(friend.getUserName()));
        getAvatarYourFriend();
        yourUserName = friend.getUserName();
        vToolBar.showTextTitle(yourUserName);
        initViews();
    }

    @Override
    protected void onStop() {
        dataStoreApp.setChatActivityShowing(false);
        super.onStop();
    }

    public void initViews() {
        vToolBar.showButtonBack(true);
        toastUtil = new ToastUtil(this);
        friend = new MemberVsiObj();
        recyclerView = (RecyclerView) findViewById(R.id.rv_chat);
        manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        chatMessageAdapter = new ChatMessageAdapter(this, listChat, myAvatar, yourAvatar);
        recyclerView.setAdapter(chatMessageAdapter);
        imgSend = (ImageButton) findViewById(R.id.imb_send);
        edtMsg = (EditText) findViewById(R.id.edt_chat);
        edtMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.smoothScrollToPosition(0);
                    }
                }, 100);
            }
        });
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HViewUtils.isFastDoubleClick())
                    return;
                if (TextUtils.isEmpty(edtMsg.getText().toString()))
                    return;
                ChatObj obj = new ChatObj();
                obj.setContent(edtMsg.getText().toString());
                obj.setItsMe(true);
                obj.setCdate(TextUtils.dateToString(new Date()));
                if (publish(obj.getContent())) {
                    if (listChat.size() > 0 && listChat.get(0).isItsMe() && TextUtils.equalTime(obj.getCdate(), listChat.get(0).getCdate())) {
                        obj.setContent(listChat.get(0).getContent() + "\n" + obj.getContent());
                        listChat.set(0, obj);
                    } else listChat.add(0, obj);
                    chatMessageAdapter.notifyDataSetChanged();
                } else {
                    ToastUtil toastUtil = new ToastUtil(getApplicationContext());
                    toastUtil.showToast("Gửi không thành công!");
                }
                edtMsg.setText("");
                recyclerView.smoothScrollToPosition(0);
            }
        });
        imgSend = (ImageButton) findViewById(R.id.imb_send);
        edtMsg = (EditText) findViewById(R.id.edt_chat);
        // register Broad Cast receiver from MLMap
        IntentFilter intentFilter = new IntentFilter(Config.NAME_BROAD_CAST_FROM_MAIN_TO_CHAT_ACTIVITY);
        registerReceiver(broadCastReceiver, intentFilter);
        runGetChatMessage(dataStoreApp.getUserName(), yourUserName);
    }

    // BroadcastReceiver nhan message from FragmentHome de handle
    private BroadcastReceiver broadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String incomingSms = intent.getStringExtra(Config.BROAD_CASS_MSG_MAIN_TO_CHAT_FILTER);
            ChatPubNubObj chatMsg = JsonCommon.getMessageChatFromPubNub(incomingSms);
            ChatObj chatObj = new ChatObj();
            chatObj.setItsMe(false);
            chatObj.setContent(chatMsg.getContent());
            chatObj.setCdate(TextUtils.dateToString(new Date()));
            if (listChat.size() > 0 && !listChat.get(0).isItsMe()) {
                chatObj.setContent(listChat.get(0).getContent() + "\n" + chatObj.getContent());
                listChat.set(0, chatObj);
            } else listChat.add(0, chatObj);
            chatMessageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
        }
    };

    public void getAvatarYourFriend() {
        myAvatar = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_user);
        yourAvatar = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_avatar_default);
        try {
            Picasso.with(this)
                    .load(friend.getUrlThumnails())
                    .into(new Target() {
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            yourAvatar = bitmap;
                        }
                    });
        } catch (Exception e) {
        }

        try {
            Picasso.with(this)
                    .load(dataStoreApp.getAvatar())
                    .into(new Target() {
                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                        }

                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                            myAvatar = bitmap;
                        }
                    });
        } catch (Exception e) {
        }

    }

    public void runGetChatMessage(String fromUser, String toUser) {
        if (!UtilNetwork.checkInternet(this, getString(R.string.check_internet))) {
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_REGISTER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetWorkServerApi serverNetWorkAPI = retrofit.create(NetWorkServerApi.class);
        mpDl.showLoading("");
        Map users = new HashMap();
        users.put("from_user", fromUser);
        users.put("to_user", toUser);
        Call<JsonObject> call = serverNetWorkAPI.getChatMessageTwoUser(users);
        call.enqueue(new retrofit2.Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mpDl.hideLoading();
                try {
                    boolean status = response.body().get(Config.status_response).getAsBoolean();
                    if (!status) {
                        return;
                    }
                } catch (Exception e) {
                    return;
                }
                try {
                    listChat.clear();
                    listChat.addAll(JsonCommon.getChatTwoUser(dataStoreApp.getUserName(), response.body().getAsJsonArray("data")));
                    chatMessageAdapter.notifyDataSetChanged();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.scrollToPosition(0);
                        }
                    });
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mpDl.hideLoading();
                Util.LOGD(tag, " Throwable is " + t);
            }
        });
    }

    public boolean publish(String message) {
        Callback callback = new Callback() {
            public void successCallback(String channel, Object response) {
                Util.LOGD("26_4 publish successCallback", response.toString());
            }

            public void errorCallback(String channel, PubnubError error) {
                Util.LOGD("26_4 errorCallback publish", error.toString());
            }
        };
        try {
            MainActivity.pubnub.publish(Config.startChannelName + yourUserName, createMessageSend(message).toString(), callback);
            runSendMessTwoUserToServer(dataStoreApp.getUserName(), yourUserName, message);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean runSendMessTwoUserToServer(String fromUser, String toUser, String content) {
        if (!UtilNetwork.checkInternet(this, getString(R.string.check_internet))) {
            throw new RuntimeException();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_REGISTER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        NetWorkServerApi serverNetWorkAPI = retrofit.create(NetWorkServerApi.class);
        Map users = new HashMap();
        users.put("from_user", fromUser);
        users.put("to_user", toUser);
        users.put("content", content);
        Call<JsonObject> call = serverNetWorkAPI.setMess(users);
        call.enqueue(new retrofit2.Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
        return true;
    }

    public JsonObject createMessageSend(String message) {
//        mess:{
//            author:nxtuyen
//            mess:content
//            time:321313
//        }
        JsonObject jsonObject = new JsonObject();
        JsonObject objC = new JsonObject();
        objC.addProperty("admin", dataStoreApp.getUserName());
        objC.addProperty("mess", message);
        objC.addProperty("time", System.currentTimeMillis() + "");
        jsonObject.add("mess", objC);
        return jsonObject;
    }

}
