package com.shalskar.fitnesscalculator.fragments;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.adapters.ViewPagerAdapter;
import com.shalskar.fitnesscalculator.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Vincent on 7/05/2016.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private ViewPagerAdapter viewPagerAdapter;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        initialiseViews();
        return rootView;
    }

    private void initialiseViews() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(getActivity().getDrawable(R.drawable.logo));
        ViewUtil.correctLogoPadding(toolbar);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Medium.ttf");
        ViewUtil.setTabLayoutTypeFace(tabLayout, typeface);
    }

    private void setupViewPager(@NonNull ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void refreshAll() {
        viewPagerAdapter.refreshAll();
    }
}
