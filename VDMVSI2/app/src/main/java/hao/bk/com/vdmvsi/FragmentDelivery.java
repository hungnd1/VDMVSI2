package hao.bk.com.vdmvsi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import hao.bk.com.adapter.DeliveryItemAdapter;
import hao.bk.com.adapter.NewsItemAdapter;
import hao.bk.com.common.ToastUtil;
import hao.bk.com.config.Config;
import hao.bk.com.models.DeliveryObj;
import hao.bk.com.models.NewsObj;
import hao.bk.com.models.NewsVsiObj;

/**
 * Created by T430 on 4/23/2016.
 */
public class FragmentDelivery extends Fragment {
    public RecyclerView recyclerView;
    // adapterLv cho recycleView
    public DeliveryItemAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager llm;
    // lay ve activity
    public MainActivity main;
    public String curTabName = "";
    Button btnSupport;
    public ToastUtil toastUtil;
    ArrayList<NewsObj> lisNews =  new ArrayList<>();
    public FragmentDelivery(){

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main = (MainActivity)context;
        toastUtil = new ToastUtil(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        curTabName = bundle.getString(Config.NAME_BUNDLE, Config.LAST_MSG_TAB);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_delivery_new, container, false);
        initViews(content);
        return content;
    }
    public void initViews(View v){
        recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        llm = new LinearLayoutManager(main);
        recyclerView.setLayoutManager(llm);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        // refresh recyclerView, tai tin tuc moi (pull to refresh)
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //refreshListNews();
                toastUtil.showToast("tải dữ liệu");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.PrimaryDarkColor);
        if(Config.SUPPORT_NEED_TAB.equals(curTabName)){
            dulieugia1();
            adapter = new DeliveryItemAdapter(this, lisNews);
            recyclerView.setAdapter(adapter);
        } else if(Config.FAIR_GOOD_TAB.equals(curTabName)) {
            dulieugia1();
            adapter = new DeliveryItemAdapter(this,lisNews);
            recyclerView.setAdapter(adapter);
        }
        btnSupport = (Button)v.findViewById(R.id.btn_support);
        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastUtil.showToast(getString(R.string.txt_comming_soon));
            }
        });
    }

    public void dulieugia1(){
        lisNews.clear();
        for(int i = 1; i <= 10; i++){
            DeliveryObj obj = new DeliveryObj();
            obj.setNameUser("User name: " + i);
            obj.setTitle("title nay:" + i);
           // obj.setDescription("Description nay: " + i);
            obj.setTime(i + " Phút trước");
            obj.setUrlThumnails("http://ithethao.com/sites/default/files/304442c800000578-3403987-image-a-32_1453064019909.jpg");
            lisNews.add(obj);
        }
    }
}
