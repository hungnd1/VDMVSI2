package hao.bk.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import hao.bk.com.vdmvsi.FragmentChatPage;
import hao.bk.com.vdmvsi.FragmentNews;

/**
 * Created by T430 on 4/23/2016.
 */
public class NewsPagerAdapter extends FragmentStatePagerAdapter {
    private CharSequence Titles[];
    private int numTab;
    ArrayList<FragmentNews> listFrmTab;
    public NewsPagerAdapter (FragmentManager fm, ArrayList<FragmentNews> listFrmTab, CharSequence mTitles[], int mNumbOfTab) {
        super(fm);
        this.Titles = mTitles;
        this.numTab = mNumbOfTab;
        this.listFrmTab = listFrmTab;
    }

    @Override
    public Fragment getItem(int position) {
        return listFrmTab.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return numTab;
    }
}
