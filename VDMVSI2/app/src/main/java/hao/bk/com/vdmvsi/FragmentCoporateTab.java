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

import hao.bk.com.adapter.CoporatePagerAdapter;
import hao.bk.com.config.Config;
import hao.bk.com.customview.SlidingTabLayout;

/**
 * Created by T430 on 4/21/2016.
 */
public class FragmentCoporateTab extends Fragment {

    ViewPager coporatePager;
    SlidingTabLayout coporateTabs;
    // so tab, viewpager
    private int numTab = 3 ;
    CoporatePagerAdapter adapter;
    public CharSequence titlesTab[] = new String[3];
    MainActivity main;
    FragmentCoporateNew frmNewChance, frmInteresting, frmMyProject;
    ArrayList<FragmentCoporateNew> listFrmTab;
    public FragmentCoporateTab(){

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
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_coporate_tab, container, false);
        initViews(view);
        return view;
    }
    public void initTitleTopTabs(){
        CharSequence newChance = getString(R.string.txt_new_chance);
        CharSequence interst = getString(R.string.txt_care_ing);
        CharSequence myProject = getString(R.string.txt_my_project);
        titlesTab[0] = newChance;
        titlesTab[1] = interst;
        titlesTab[2] = myProject;
        frmNewChance = new FragmentCoporateNew();
        Bundle bundle1 = new Bundle();
        bundle1.putString(Config.NAME_BUNDLE, Config.NEW_CHANCE_TAB);
        frmNewChance.setArguments(bundle1);
        listFrmTab.add(frmNewChance);
        frmInteresting = new FragmentCoporateNew();
        Bundle bundle2 = new Bundle();
        bundle2.putString(Config.NAME_BUNDLE, Config.INTERESTING_TAB);
        frmInteresting.setArguments(bundle2);
        listFrmTab.add(frmInteresting);
        frmMyProject = new FragmentCoporateNew();
        Bundle bundle3 = new Bundle();
        bundle3.putString(Config.NAME_BUNDLE, Config.MY_PROJECT_tAB);
        frmMyProject.setArguments(bundle3);
        listFrmTab.add(frmMyProject);
    }
    public void initViews(View v){
        initTitleTopTabs();
        adapter = new CoporatePagerAdapter(main.getSupportFragmentManager(), listFrmTab, titlesTab, numTab);
        coporatePager = (ViewPager)v.findViewById(R.id.coporate_pager);
        coporatePager.setAdapter(adapter);
        coporateTabs = (SlidingTabLayout)v.findViewById(R.id.coporate_top_tabs);

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
