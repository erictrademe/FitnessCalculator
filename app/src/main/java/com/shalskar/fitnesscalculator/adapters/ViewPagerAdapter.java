package com.shalskar.fitnesscalculator.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.fragments.BodyFragment;
import com.shalskar.fitnesscalculator.fragments.StrengthFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 7/05/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private static final int FRAGMENT_BODY = 0;
    private static final int FRAGMENT_STRENGTH = 1;

    private SparseArray<Fragment> registeredFragments = new SparseArray<>();

    public ViewPagerAdapter(@NonNull FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BodyFragment();
            case 1:
                return new StrengthFragment();
        }
        return null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Body calculators";
            case 1:
                return "Strength calculators";
        }
        return "";
    }

    public void refreshAll(){
        getBodyFragment().refresh();
        getStrengthFragment().refresh();
    }

    public BodyFragment getBodyFragment() {
        return (BodyFragment) registeredFragments.get(FRAGMENT_BODY);
    }

    public StrengthFragment getStrengthFragment() {
        return (StrengthFragment) registeredFragments.get(FRAGMENT_STRENGTH);
    }
}