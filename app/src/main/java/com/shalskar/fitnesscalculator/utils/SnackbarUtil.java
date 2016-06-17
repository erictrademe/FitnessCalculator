package com.shalskar.fitnesscalculator.utils;

import android.app.Activity;
import android.content.ServiceConnection;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.android.vending.billing.IInAppBillingService;
import com.shalskar.fitnesscalculator.R;
import com.shalskar.fitnesscalculator.managers.DonationManager;

/**
 * Created by Vincent on 5/06/2016.
 */
public class SnackbarUtil {

    public static void showDonationSnackBar(@NonNull View view, final int amount, @NonNull final Activity activity, @NonNull final IInAppBillingService service){
        Snackbar.make(view, String.format(view.getResources().getString(R.string.donation_prompt), amount), Snackbar.LENGTH_LONG)
                .setAction(view.getResources().getString(R.string.donate), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DonationManager.showDonationDialog(activity, service, amount);
                    }
                })
                .show();
    }
}
