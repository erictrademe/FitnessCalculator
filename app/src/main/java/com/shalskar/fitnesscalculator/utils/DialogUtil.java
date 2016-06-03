package com.shalskar.fitnesscalculator.utils;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

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

    public static boolean validateWeightField(@NonNull TextInputLayout textInputLayout, @NonNull EditText editText, float value) {
        if (editText.length() == 0 && value <= 0) {
            textInputLayout.setError(" ");
            textInputLayout.setErrorEnabled(true);
            return false;
        } else
            return true;
    }

}
