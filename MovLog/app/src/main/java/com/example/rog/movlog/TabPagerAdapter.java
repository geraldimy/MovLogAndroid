package com.example.rog.movlog;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    String[] tabarray = new String[]{"Now Showing","UpComing","Top Rated"};
    Integer tabnumber = 3;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabarray[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                NowShowing nowShowing = new NowShowing();
                return  nowShowing;
            case 1:
                UpComing upComing = new UpComing();
                return  upComing;
            case 2:
                TopRated topRated = new TopRated();
                return topRated;
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabnumber;
    }
}
