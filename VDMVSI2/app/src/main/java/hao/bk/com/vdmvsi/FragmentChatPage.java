package hao.bk.com.vdmvsi;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import hao.bk.com.utils.TextUtils;
import hao.bk.com.utils.Util;
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
    int dem = 0;
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
                adapter.filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
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
                getAllUser();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        adapter = new ChatItemAdapter(this, listMemberChat);
        recyclerView.setAdapter(adapter);
        getAllUser();
        setupFilter();
    }

    public void getAllUser(){
        if(!UtilNetwork.checkInternet(main)){
            return;
        }
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mpdl.showLoading("");
        final NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey",Config.PUBLIC_KEY);
        hashMap.put("action", Config.getUser);
        Call<JsonObject> call = stackOverflowAPI.getAllUser(hashMap);
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
                final ArrayList<MemberVsiObj> tmp = JsonCommon.getAllUser(dataStoreApp.getUserName(), response.body().getAsJsonArray("data"));
                    Map users = new HashMap();
                    users.put("username", dataStoreApp.getUserName());
                    Call<JsonObject> call2 = stackOverflowAPI.getLastMessage(users);
                    call2.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            try {
                                boolean status = response.body().get(Config.status_response).getAsBoolean();
                                if (!status) {
                                    return;
                                }
                            } catch (Exception e) {
                                return;
                            }
                            try {
                                Map<String, ChatObj> mapChatObj = JsonCommon.getLastMessage(response.body().getAsJsonArray("data"));
                                for (MemberVsiObj obj: tmp) {
                                    if (mapChatObj.containsKey(obj.getUserName())) {
                                        obj.setLastMessage(mapChatObj.get(obj.getUserName()));
                                        listMemberChat.add(obj);
                                        dataStoreApp.createFriendAvatar(obj.getUserName(),obj.getUrlThumnails());
                                    }
                                }
                            } catch (Exception e) {
                            }
                            Collections.sort(listMemberChat, new Comparator<MemberVsiObj>() {
                                @Override
                                public int compare(MemberVsiObj lhs, MemberVsiObj rhs) {
                                    return (int) (TextUtils.stringToDate(rhs.getLastMessage().getCdate()).getTime()-TextUtils.stringToDate(lhs.getLastMessage().getCdate()).getTime());
                                }
                            });
                            adapter.updateFilter();
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                        }
                    });
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
