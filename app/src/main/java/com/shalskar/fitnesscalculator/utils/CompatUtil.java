package com.shalskar.fitnesscalculator.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;

import com.shalskar.fitnesscalculator.R;

/**
 * Created by Vincent on 12/06/2016.
 */
public class CompatUtil {

    /**
     * Bunch of methods to handle various compatability issues.
     */

    public static Drawable getDrawable(@NonNull Context context, int resourceId) {
        if (Build.VERSION.SDK_INT >= 21)
            return context.getDrawable(resourceId);
        else
            return context.getResources().getDrawable(resourceId);
    }
}
