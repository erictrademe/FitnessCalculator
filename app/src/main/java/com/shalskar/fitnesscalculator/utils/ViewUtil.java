package com.shalskar.fitnesscalculator.utils;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shalskar.fitnesscalculator.R;

/**
 * Created by Vincent on 7/06/2016.
 */
public class ViewUtil {

    public static void setTabLayoutTypeFace(@NonNull TabLayout tabLayout, @NonNull Typeface typeface) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }

    /**
     * Get the logo from the toolbar and manually adjust it's padding to keep it from
     * being too large.
     *
     * @param toolbar
     */
    public static void correctLogoPadding(@NonNull Toolbar toolbar) {
        int padding = (int) toolbar.getContext().getResources().getDimension(R.dimen.logo_padding);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View logo = toolbar.getChildAt(i);
            if (logo instanceof ImageView) {
                logo.setPadding(0, padding, 0, padding);
                break;
            }
        }

    }
}
