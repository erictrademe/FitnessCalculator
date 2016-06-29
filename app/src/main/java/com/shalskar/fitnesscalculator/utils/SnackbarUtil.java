package com.shalskar.fitnesscalculator.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

/**
 * Created by Vincent on 5/06/2016.
 */
public class SnackbarUtil {

    public static void showMessageSnackbar(@NonNull Activity activity, @NonNull String message){
        Snackbar.make(activity.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .show();
    }
}
