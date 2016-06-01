package hao.bk.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import hao.bk.com.vdmvsi.FragmentDelivery;
import hao.bk.com.vdmvsi.FragmentNews;

/**
 * Created by T430 on 4/23/2016.
 */
public class DeliveryPagerAdapter extends FragmentStatePagerAdapter {
    private CharSequence Titles[];
    private int numTab;
    ArrayList<FragmentDelivery> listFrmTab;
    public DeliveryPagerAdapter (FragmentManager fm, ArrayList<FragmentDelivery> listFrmTab, CharSequence mTitles[], int mNumbOfTab) {
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
