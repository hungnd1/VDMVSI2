package hao.bk.com.vdmvsi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hao.bk.com.adapter.DeliveryPagerAdapter;
import hao.bk.com.adapter.NewsPagerAdapter;
import hao.bk.com.config.Config;
import hao.bk.com.customview.SlidingTabLayout;

/**
 * Created by T430 on 4/23/2016.
 */
public class FragmentDeliveryTab extends Fragment {

    ViewPager coporatePager;
    SlidingTabLayout coporateTabs;
    // so tab, viewpager
    private int numTab = 2;
    DeliveryPagerAdapter adapter;
    public CharSequence titlesTab[] = new String[2];
    MainActivity main;
    FragmentDelivery frmSupportNeed, frmGoodsFair;
    ArrayList<FragmentDelivery> listFrmTab;
    public FragmentDeliveryTab(){

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main = (MainActivity)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listFrmTab = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_coporate_tab, container, false);
        initViews(view);
        return view;
    }
    public void initTitleTopTabs(){
        CharSequence supportNeed = getString(R.string.txt_need_support);
        CharSequence fairGoods = getString(R.string.txt_fair_goods);
        titlesTab[0] = supportNeed;
        titlesTab[1] = fairGoods;
        frmSupportNeed = new FragmentDelivery();
        Bundle bundle1 = new Bundle();
        bundle1.putString(Config.NAME_BUNDLE, Config.SUPPORT_NEED_TAB);
        frmSupportNeed.setArguments(bundle1);
        listFrmTab.add(frmSupportNeed);
        frmGoodsFair = new FragmentDelivery();
        Bundle bundle2 = new Bundle();
        bundle2.putString(Config.NAME_BUNDLE, Config.FAIR_GOOD_TAB);
        frmGoodsFair.setArguments(bundle2);
        listFrmTab.add(frmGoodsFair);
    }
    public void initViews(View v){
        initTitleTopTabs();
        adapter = new DeliveryPagerAdapter(main.getSupportFragmentManager(), listFrmTab, titlesTab, numTab);
        coporatePager = (ViewPager)v.findViewById(R.id.coporate_pager);
        coporatePager.setAdapter(adapter);
        coporateTabs = (SlidingTabLayout)v.findViewById(R.id.coporate_top_tabs);
        // khong dan deu theo chieu ngang
        coporateTabs.setDistributeEvenly(false);
        coporateTabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        coporateTabs.setViewPager(coporatePager);
        coporateTabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });

    }
}