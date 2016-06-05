package com.shalskar.fitnesscalculator.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.shalskar.fitnesscalculator.R;

/**
 * Created by Vincent on 5/06/2016.
 */
public class SnackbarUtil {

    public static void showDonationSnackBar(@NonNull View view, int amount){
        Snackbar.make(view, String.format(view.getResources().getString(R.string.donation_prompt), amount), Snackbar.LENGTH_LONG)
                .setAction(view.getResources().getString(R.string.donate), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // todo implement donation
                    }
                })
                .show();
    }
}
