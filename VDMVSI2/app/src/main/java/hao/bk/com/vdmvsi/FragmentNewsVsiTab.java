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

import hao.bk.com.adapter.ChatPagerAdapter;
import hao.bk.com.adapter.NewsPagerAdapter;
import hao.bk.com.config.Config;
import hao.bk.com.customview.SlidingTabLayout;

/**
 * Created by T430 on 4/23/2016.
 */
public class FragmentNewsVsiTab extends Fragment {

    ViewPager coporatePager;
    SlidingTabLayout coporateTabs;
    // so tab, viewpager
    private int numTab = 4;
    NewsPagerAdapter adapter;
    public CharSequence titlesTab[] = new String[4];
    MainActivity main;
    FragmentNews frmDarshBoard, frmVsiNews, frmMajorNews, frmVipNews;
    ArrayList<FragmentNews> listFrmTab;
    public FragmentNewsVsiTab(){

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
        CharSequence darshboard = getString(R.string.txt_darshboard);
        CharSequence vsiNews = getString(R.string.txt_vsi_news);
        CharSequence majorNews = getString(R.string.txt_major_news);
        CharSequence vipNews = getString(R.string.txt_vip_news);
        titlesTab[0] = darshboard;
        titlesTab[1] = vsiNews;
        titlesTab[2] = majorNews;
        titlesTab[3] = vipNews;
        frmDarshBoard = new FragmentNews();
        Bundle bundle1 = new Bundle();
        bundle1.putString(Config.NAME_BUNDLE, Config.DARSH_BOARD_TAB);
        frmDarshBoard.setArguments(bundle1);
        listFrmTab.add(frmDarshBoard);
        frmVsiNews = new FragmentNews();
        Bundle bundle2 = new Bundle();
        bundle2.putString(Config.NAME_BUNDLE, Config.VSI_NEWS_TAB);
        frmVsiNews.setArguments(bundle2);
        listFrmTab.add(frmVsiNews);
        frmMajorNews = new FragmentNews();
        Bundle bundle3 = new Bundle();
        bundle3.putString(Config.NAME_BUNDLE, Config.MAJOR_NEWS_TAB);
        frmMajorNews.setArguments(bundle3);
        listFrmTab.add(frmMajorNews);
        frmVipNews = new FragmentNews();
        Bundle bundle4 = new Bundle();
        bundle4.putString(Config.NAME_BUNDLE, Config.VIP_NEWS_TAB);
        frmVipNews.setArguments(bundle4);
        listFrmTab.add(frmVipNews);
    }
    public void initViews(View v){
        initTitleTopTabs();
        adapter = new NewsPagerAdapter(main.getSupportFragmentManager(), listFrmTab, titlesTab, numTab);
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
