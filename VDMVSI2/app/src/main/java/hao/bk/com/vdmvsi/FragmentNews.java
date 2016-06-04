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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import hao.bk.com.adapter.ChatItemAdapter;
import hao.bk.com.adapter.NewsItemAdapter;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.models.ChatObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.models.NewsVsiObj;
import hao.bk.com.utils.HViewUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/23/2016.
 */
public class FragmentNews extends Fragment {

    public RecyclerView recyclerView;
    // adapterLv cho recycleView
    private NewsItemAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager llm;
    // lay ve activity
    public MainActivity main;
    public String curTabName = "";
    ToastUtil toastUtil;
    LinearLayout lnlError;
    TextView tvErrorMsg;
    Button btnRetry;
    private String curgetAction;
    DataStoreApp dataStoreApp;
    ArrayList<NewsObj> listNews = new ArrayList<>();
    MyProgressDialog mPdl;

    public FragmentNews() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main = (MainActivity) context;
        toastUtil = new ToastUtil(context);
        dataStoreApp = main.dataStoreApp;
        mPdl = new MyProgressDialog(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        curTabName = bundle.getString(Config.NAME_BUNDLE, Config.LAST_MSG_TAB);
        curgetAction = Config.getNewsVsi;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_coporate_new, container, false);
        initViews(content);
        return content;
    }

    public void initViews(View v) {
        if (dataStoreApp == null) {
            dataStoreApp = new DataStoreApp(getContext());
        }
        lnlError = (LinearLayout) v.findViewById(R.id.lnl_error);
        tvErrorMsg = (TextView) v.findViewById(R.id.tv_error);
        btnRetry = (Button) v.findViewById(R.id.btn_retry);
        lnlError.setVisibility(View.GONE);
        lnlError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HViewUtils.isFastDoubleClick())
                    return;
                runGetNews(curgetAction);
            }
        });
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HViewUtils.isFastDoubleClick())
                    return;
                runGetNews(curgetAction);
            }
        });
        recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        llm = new LinearLayoutManager(main);
        recyclerView.setLayoutManager(llm);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        // refresh recyclerView, tai tin tuc moi (pull to refresh)
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runGetNews(curgetAction);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.PrimaryDarkColor);
        if (Config.DARSH_BOARD_TAB.equals(curTabName)) {
            curgetAction = Config.getNewsChuyenNganh;
            runGetNews(curgetAction);
            adapter = new NewsItemAdapter(this, listNews);
            recyclerView.setAdapter(adapter);
        } else if (Config.VSI_NEWS_TAB.equals(curTabName)) {
            curgetAction = Config.getNewsVsi;
            runGetNews(curgetAction);
            adapter = new NewsItemAdapter(this, listNews);
            recyclerView.setAdapter(adapter);
        } else if (Config.MAJOR_NEWS_TAB.equals(curTabName)) {
            curgetAction = Config.getNewsChuyenNganh;
            runGetNews(curgetAction);
            adapter = new NewsItemAdapter(this, listNews);
            recyclerView.setAdapter(adapter);
        } else if (Config.VIP_NEWS_TAB.equals(curTabName)) {
            if (dataStoreApp.isVip()) {
                curgetAction = Config.getNewsVip;
                runGetNews(curgetAction);
                adapter = new NewsItemAdapter(this, listNews);
                recyclerView.setAdapter(adapter);
            } else {
                lnlError.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                btnRetry.setVisibility(View.INVISIBLE);
                tvErrorMsg.setText("Bạn phải đăng kí VIP để có thể xem được nội dung này");
            }
        }

    }

    public void runGetNews(String action) {
        if (!UtilNetwork.checkInternet(main, getString(R.string.txt_check_internet))) {
            showBtnRetry(getString(R.string.txt_check_internet));
            return;
        }
        lnlError.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.BASE_URL_GET)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Khởi tạo các cuộc gọi cho Retrofit 2.0
        NetWorkServerApi stackOverflowAPI = retrofit.create(NetWorkServerApi.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("publicKey", Config.PUBLIC_KEY);
        hashMap.put("action", action);
        hashMap.put("username", dataStoreApp.getUserName());
        mPdl.showLoading(getString(R.string.txt_loading));
        Call<JsonObject> call = stackOverflowAPI.getNewsVis(hashMap);
        // Cuộc gọi bất đồng bọ (chạy dưới background)
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                listNews.clear();
                mPdl.hideLoading();
                try {
                    if (response.body().has(Config.status_response)) {
                        boolean status = response.body().get(Config.status_response).getAsBoolean();
                        if (!status) {
                            notifyDataSetChanged();
                            return;
                        }
                    }
                } catch (Exception e) {
                    toastUtil.showToast(getString(R.string.txt_error_common));
                    notifyDataSetChanged();
                    return;
                }
                try {
                    listNews.addAll(JsonCommon.getNewsVsi(response.body().getAsJsonArray("data")));
                } catch (Exception e) {

                }
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("CallBack", " Throwable is " + t);
                mPdl.hideLoading();
                notifyDataSetChanged();
            }
        });
    }

    public void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
        if (listNews.size() == 0) {
            showBtnRetry(getString(R.string.txt_server_not_data));
        }
    }

    public void showBtnRetry(String message) {
        if (listNews.size() == 0) {
            lnlError.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
            tvErrorMsg.setText(message);
        }
    }

}
