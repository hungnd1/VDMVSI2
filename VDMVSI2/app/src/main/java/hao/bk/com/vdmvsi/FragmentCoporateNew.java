package hao.bk.com.vdmvsi;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.pubnub.api.Pubnub;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;

import hao.bk.com.adapter.InterestingNewsAdapter;
import hao.bk.com.adapter.MyProjectNewsAdapter;
import hao.bk.com.adapter.NewChanceNewsAdapter;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.models.CoporateNewsObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.utils.HViewUtils;
import hao.bk.com.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/21/2016.
 */
public class FragmentCoporateNew extends Fragment{
    public RecyclerView recyclerView;
    // adapterLv cho recycleView
    public RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager llm;
    // lay ve activity
    public MainActivity main;
    public String curTabName = "";
    public ToastUtil toastUtil;
    LinearLayout lnlError;
    TextView tvErrorMsg;
    Button btnRetry;
    DataStoreApp dataStoreApp;
    private String curgetAction;
    MyProgressDialog mpdl;
    ArrayList<NewsObj> listNews =  new ArrayList<>();
    Button btnAddNewProject;

    public FragmentCoporateNew(){

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main = (MainActivity)context;
        dataStoreApp = main.dataStoreApp;
        toastUtil = main.toastUtil;
        mpdl = new MyProgressDialog(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        curTabName = bundle.getString(Config.NAME_BUNDLE, Config.NEW_CHANCE_TAB);
        curgetAction = Config.getProject;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_coporate_new, container, false);
        initViews(content);
        return content;
    }
    public void initViews(View v){
        if(dataStoreApp == null){
            dataStoreApp = new DataStoreApp(getContext());
        }
        btnAddNewProject = (Button)v.findViewById(R.id.btn_add_new_project);
        btnAddNewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentCreateMyProject fragmentCreateMyProject =  FragmentCreateMyProject.newInstance();
                fragmentCreateMyProject.show(main.getFragmentManager(), "");
            }
        });
        lnlError = (LinearLayout)v.findViewById(R.id.lnl_error);
        tvErrorMsg = (TextView)v.findViewById(R.id.tv_error);
        btnRetry = (Button)v.findViewById(R.id.btn_retry);
        lnlError.setVisibility(View.GONE);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                runGetNews(curgetAction);
            }
        });
        lnlError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                runGetNews(curgetAction);
            }
        });
        recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        llm = new LinearLayoutManager(main);
        recyclerView.setLayoutManager(llm);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.PrimaryDarkColor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runGetNews(curgetAction);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if(Config.NEW_CHANCE_TAB.equals(curTabName)){
            curgetAction = Config.getProject;
            runGetNews(curgetAction);
            adapter = new NewChanceNewsAdapter(this, listNews);
            recyclerView.setAdapter(adapter);
        } else if(Config.INTERESTING_TAB.equals(curTabName)){
            curgetAction = Config.getProjectCare;
            runGetNews(curgetAction);
            adapter = new InterestingNewsAdapter(this, listNews);
            recyclerView.setAdapter(adapter);
        } else if(Config.MY_PROJECT_tAB.equals(curTabName)){
            btnAddNewProject.setVisibility(View.VISIBLE);
            curgetAction = Config.getProjectItme;
            runGetNews(curgetAction);
            adapter = new MyProjectNewsAdapter(this, listNews);
            recyclerView.setAdapter(adapter);
        }
    }
    public void runGetNews(String action){
        if(!UtilNetwork.checkInternet(main,getString(R.string.txt_check_internet))){
            showBtnRetry(getString(R.string.txt_check_internet));
            return;
        }
        lnlError.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        mpdl.showLoading(getString(R.string.txt_loading));
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey",Config.PUBLIC_KEY);
        hashMap.put("action", action);
        hashMap.put("username", dataStoreApp.getUserName());

        Call<JsonObject> call = stackOverflowAPI.getNews(hashMap);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mpdl.hideLoading();
                try{
                    if(response.body().has(Config.status_response)){
                        boolean status =response.body().get(Config.status_response).getAsBoolean();
                        if (!status) {
                            notifyDataSetChanged();
                            return;
                        }
                    }
                }catch (Exception e){
                    toastUtil.showToast(getString(R.string.txt_error_common));
                    notifyDataSetChanged();
                    return;
                }
                try {
                    listNews.clear();
                    listNews.addAll(JsonCommon.getCoporateNews(response.body().getAsJsonArray("data")));
                }catch (Exception e){
                }
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("CallBack", " Throwable is " +t);
                mpdl.hideLoading();
                notifyDataSetChanged();
            }
        });
    }
    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
        if(listNews.size() == 0){
            showBtnRetry(getString(R.string.txt_server_not_data));
        }
    }

    public void showBtnRetry(String message){
        if(listNews.size() == 0) {
            lnlError.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
            tvErrorMsg.setText(message);
        }
    }
}
