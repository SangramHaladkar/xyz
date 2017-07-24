package com.app.monitormymortgage.Adapters;

/**
 * Created by Sangram on 18/03/16.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.monitormymortgage.TabFragments.TabFragmentTwo;
import com.app.monitormymortgage.TabFragments.TabFragmentOne;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragmentOne tab1 = new TabFragmentOne();
                return tab1;
            case 1:
                TabFragmentTwo tab2 = new TabFragmentTwo();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
