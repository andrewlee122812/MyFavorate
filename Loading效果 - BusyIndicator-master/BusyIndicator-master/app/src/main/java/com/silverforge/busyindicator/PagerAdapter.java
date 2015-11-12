package com.silverforge.busyindicator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

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
                RectangleFragment rectangleFragment = new RectangleFragment();
                return rectangleFragment;
            case 1:
                CircleFragment circleFragment = new CircleFragment();
                return circleFragment;
            case 2:
                TransFragment transFragment = new TransFragment();
                return transFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }}
