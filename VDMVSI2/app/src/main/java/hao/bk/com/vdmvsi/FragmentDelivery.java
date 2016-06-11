package hao.bk.com.vdmvsi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.HashMap;

import hao.bk.com.adapter.DeliveryItemAdapter;
import hao.bk.com.adapter.MyProjectNewsAdapter;
import hao.bk.com.adapter.ProductItemAdapter;
import hao.bk.com.adapter.RequestSupportNewsAdapter;
import hao.bk.com.chat.CreateSupportActivity;
import hao.bk.com.common.DataStoreApp;
import hao.bk.com.common.JsonCommon;
import hao.bk.com.common.NetWorkServerApi;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.common.UtilNetwork;
import hao.bk.com.config.Config;
import hao.bk.com.customview.MyProgressDialog;
import hao.bk.com.models.NewsObj;
import hao.bk.com.models.ProductObj;
import hao.bk.com.models.SupportObj;
import hao.bk.com.utils.HViewUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by T430 on 4/23/2016.
 */
public class FragmentDelivery extends Fragment {
    public RecyclerView recyclerView;
    // adapterLv cho recycleView
    public RecyclerView.Adapter adapter;
    public ProductItemAdapter adapter2;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager llm;
    // lay ve activity
    public MainActivity main;
    public String curTabName = "";
    LinearLayout lnlError;
    private String curgetAction;
    TextView tvErrorMsg;
    Button btnRetry;
    DataStoreApp dataStoreApp;
    MyProgressDialog mPdl;
    Button btnSupport;
    public ToastUtil toastUtil;
    ArrayList<ProductObj> lisProducts =  new ArrayList<>();
    ArrayList<NewsObj> listSupports = new ArrayList<>();
    Button btn_addSupport;
    public FragmentDelivery(){

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main = (MainActivity)context;
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
        View content = inflater.inflate(R.layout.fragment_delivery_new, container, false);
        initViews(content);
        return content;
    }
    public void initViews(View v){
        if (dataStoreApp == null) {
            dataStoreApp = new DataStoreApp(getContext());
        }
        lnlError = (LinearLayout) v.findViewById(R.id.lnl_error);
        tvErrorMsg = (TextView) v.findViewById(R.id.tv_error);
        lnlError.setVisibility(View.GONE);
        btnRetry = (Button) v.findViewById(R.id.btn_retry);
        lnlError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HViewUtils.isFastDoubleClick())
                    return;
                runGetNews(curgetAction);
            }
        });
        btn_addSupport = (Button) v.findViewById(R.id.btn_support);
        btn_addSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Log.v("users2", "aaa");
                    Intent intent = new Intent(main, CreateSupportActivity.class);
                    main.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        llm = new LinearLayoutManager(main);
        recyclerView.setLayoutManager(llm);

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
        swipeRefreshLayout.setColorSchemeResources(R.color.PrimaryDarkColor);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runGetNews(curgetAction);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btnSupport = (Button)v.findViewById(R.id.btn_support);
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastUtil.showToast(getString(R.string.txt_comming_soon));
            }
        });
        if(Config.SUPPORT_NEED_TAB.equals(curTabName)){
//         curgetAction = Config.getRequestSupport;
            curgetAction = Config.getNewsVsi;
            runGetNews(curgetAction);
//            supportRequest();
//            Log.v("listSupprot",listSupports.get(0).getContent());
            adapter = new RequestSupportNewsAdapter(this, listSupports);
            recyclerView.setAdapter(adapter);

        } else if(Config.FAIR_GOOD_TAB.equals(curTabName)) {
            btnSupport.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new GridLayoutManager(main,2));
            dulieugia1();
            adapter2 = new ProductItemAdapter(this,lisProducts);
            recyclerView.setAdapter(adapter2);
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
                listSupports.clear();
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
                    listSupports.addAll(JsonCommon.getCoporateNews(response.body().getAsJsonArray("data")));
                    Log.v("log1",listSupports+"");
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
        if (listSupports.size() == 0) {
            showBtnRetry(getString(R.string.txt_server_not_data));
        }
    }
    public void dulieugia1(){
        lisProducts.clear();
        for(int i = 1; i <= 10; i++){
            ProductObj obj = new ProductObj();
            obj.setTitle("title nay:" + i);
           // obj.setDescription("Description nay: " + i);
            obj.setUrlThumnails("http://ithethao.com/sites/default/files/304442c800000578-3403987-image-a-32_1453064019909.jpg");
            obj.setCompany("VSI");
            lisProducts.add(obj);
        }
    }
    public void showBtnRetry(String message) {
        if (listSupports.size() == 0) {
            lnlError.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
            tvErrorMsg.setText(message);
        }
    }
}
