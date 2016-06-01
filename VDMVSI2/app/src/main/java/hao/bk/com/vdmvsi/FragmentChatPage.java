package hao.bk.com.vdmvsi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import hao.bk.com.adapter.ChatItemAdapter;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.models.ChatObj;
import hao.bk.com.models.MemberVsiObj;
import hao.bk.com.models.MessageOfFriendObj;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/23/2016.
 */
public class FragmentChatPage extends Fragment {

    public RecyclerView recyclerView;
    // adapterLv cho recycleView
    private ChatItemAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager llm;
    // lay ve activity
    public MainActivity main;
    public String curTabName = "";
    ToastUtil toastUtil;
    DataStoreApp dataStoreApp;
    MyProgressDialog mpdl;
    ArrayList<MemberVsiObj> listMemberChat =  new ArrayList<>();
    public FragmentChatPage(){

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main = (MainActivity)context;
        toastUtil = new ToastUtil(context);
        dataStoreApp = new DataStoreApp(context);
        mpdl = new MyProgressDialog(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        curTabName = bundle.getString(Config.NAME_BUNDLE, Config.LAST_MSG_TAB);
    }
    public void setupFilter(){
        main.edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_coporate_new, container, false);
        initViews(content);
        return content;
    }
    public void initViews(View v){
        recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        llm = new LinearLayoutManager(main);
        recyclerView.setLayoutManager(llm);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        // refresh recyclerView, tai tin tuc moi (pull to refresh)
        swipeRefreshLayout.setColorSchemeResources(R.color.PrimaryDarkColor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListMemeberChat();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        adapter = new ChatItemAdapter(this, listMemberChat);
        recyclerView.setAdapter(adapter);
        getListMemeberChat();
        setupFilter();
    }
    public void getListMemeberChat(){
        if(Config.LAST_MSG_TAB.equals(curTabName)){
            getAllUser();
        } else if(Config.CONTACTS_TAB.equals(curTabName)){
            getAllUser();
        }
    }
    public void getListLastMessage(){
        if(!UtilNetwork.checkInternet(main)){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey",Config.PUBLIC_KEY);
        hashMap.put("action", Config.getListLateMess);
        hashMap.put("username", dataStoreApp.getUserName());
        Call<JsonObject> call = stackOverflowAPI.getUserInfo(hashMap);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                ArrayList<ChatObj> listMsg = JsonCommon.getLastMessage(response.body().getAsJsonArray("data"));
//                listFriendMsg.clear();;
//                for(ChatObj obj : listMsg){
//                    if(listFriendMsg.)
//                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("CallBack", " Throwable is " +t);
            }
        });
    }

    public void getAllUser(){
        if(!UtilNetwork.checkInternet(main)){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        mpdl.showLoading("");
        NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey",Config.PUBLIC_KEY);
        hashMap.put("action", Config.getUser);
        Call<JsonObject> call = stackOverflowAPI.getAllUser(hashMap);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mpdl.hideLoading();
                try{
                    if(response.body().has(Config.status_response)){
                        boolean status =response.body().get(Config.status_response).getAsBoolean();
                        if (!status) {
                            toastUtil.showToast(getString(R.string.txt_error_common));
                            return;
                        }
                    }
                }catch (Exception e){
                    toastUtil.showToast(getString(R.string.txt_error_common));
                    return;
                }
                listMemberChat.clear();
                listMemberChat.addAll(JsonCommon.getAllUser(dataStoreApp.getUserName(), response.body().getAsJsonArray("data")));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("CallBack", " Throwable is " +t);
                mpdl.hideLoading();
                if(listMemberChat.size() == 0)
                    toastUtil.showToast(getString(R.string.txt_error_common));
            }
        });
    }

}
