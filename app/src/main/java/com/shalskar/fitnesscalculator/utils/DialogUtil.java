package com.shalskar.fitnesscalculator.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * Created by RachelTeTau on 18/05/16.
 */
public class DialogUtil {

    public static void showMessageDialog(@NonNull Activity activity, @NonNull String title, @NonNull String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setMessage(message)
                .setTitle(title);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
