package com.shalskar.fitnesscalculator.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.fragments.BodyFragment;
import com.shalskar.fitnesscalculator.fragments.StrengthFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 7/05/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private BodyFragment bodyFragment;
    private StrengthFragment strengthFragment;

    public ViewPagerAdapter(@NonNull Context context, @NonNull FragmentManager manager) {
        super(manager);
        this.context = context;
        bodyFragment = new BodyFragment();
        strengthFragment = new StrengthFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return bodyFragment;
            case 1:
                return strengthFragment;
        }
        return bodyFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.title_fragment_body);
            case 1:
                return context.getString(R.string.title_fragment_strength);
        }
        return "";
    }

    public void refreshAll(){
        bodyFragment.refresh();
        strengthFragment.refresh();
    }
}