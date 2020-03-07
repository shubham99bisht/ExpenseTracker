package com.example.shadrak.expensestracker;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

class ViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> strings = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                return new bills_fragment();
            case 1:
                return new camera_fragment();
            case 2:
                return new summary_fragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void add(Fragment fragment, String string) {
        fragments.add(fragment);
        strings.add(string);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return strings.get(position);
    }
}
