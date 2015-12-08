package com.example.comtest.transportactiveageing;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


/**
 * Created by fuckface on 2015-10-20.
 */

public class CustomPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"Route", "Map"};
    private Context context;

    public CustomPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }



    @Override

    public Fragment getItem(int position) {

        Fragment routeFragment = new RouteView();
        Fragment googleFragment = new GoogleView();
        Fragment settingsFragment = new Settings();

        Fragment fragment = null;
        if (position == 0) {
            fragment = routeFragment;
        } else if (position == 1) {
            fragment = googleFragment;
        }

        return fragment;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles[position];
    }
}