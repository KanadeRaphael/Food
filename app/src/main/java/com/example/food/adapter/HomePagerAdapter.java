package com.example.food.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.food.R;
import com.example.food.fragment.CustomizationFragment;
import com.example.food.fragment.HomeFragment;
import com.example.food.fragment.RecommendFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private final String[] titles;
    private Fragment[] fragments;

    public HomePagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles = context.getResources().getStringArray(R.array.sections);
        fragments = new Fragment[titles.length];
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) {
            switch (position) {
                case 0:
                    fragments[position] = CustomizationFragment.getInstance();
                    break;
                case 1:
                    fragments[position] = HomeFragment.getInstance();
                    break;
                case 2:
                    fragments[position] = RecommendFragment.getInstance();
                    break;
                default:
                    break;
            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public void rereshUI() {
        ((HomeFragment) fragments[1]).refreshUI();
    }
}
