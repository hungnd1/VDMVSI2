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

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import hao.bk.com.adapter.ContactItemAdapter;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.models.MemberVsiObj;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/23/2016.
 */
public class FragmentContactPage extends Fragment {

    public RecyclerView recyclerView;
    // adapterLv cho recycleView
    private ContactItemAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager llm;
    public MainActivity main;
    public String curTabName = "";
    ToastUtil toastUtil;
    DataStoreApp dataStoreApp;
    MyProgressDialog mpdl;
    ArrayList<MemberVsiObj> listMemberChat =  new ArrayList<>();
    public FragmentContactPage(){

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
        adapter = new ContactItemAdapter(this, listMemberChat);
        recyclerView.setAdapter(adapter);
        getAllUser();
        setupFilter();
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
                Collections.sort(listMemberChat, new Comparator<MemberVsiObj>() {
                    @Override
                    public int compare(MemberVsiObj lhs, MemberVsiObj rhs) {
                        return lhs.getUserName().compareToIgnoreCase(rhs.getUserName());
                    }
                });
                adapter.updateFilter();
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
