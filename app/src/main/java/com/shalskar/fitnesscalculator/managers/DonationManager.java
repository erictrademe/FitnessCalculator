package com.shalskar.fitnesscalculator.managers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.NonNull;

import com.android.vending.billing.IInAppBillingService;

/**
 * Created by Vincent on 17/06/2016.
 */
public class DonationManager {

    public static final String DONATION_ONE_DOLLAR = "donation_1";
    public static final String DONATION_TWO_DOLLARS = "donation_2";
    public static final String DONATION_FIVE_DOLLARS = "donation_3";
    public static final String DONATION_TEN_DOLLARS = "donation_4";
    public static final String DONATION_TWENTY_DOLLARS = "donation_5";

    public static void showDonationDialog(@NonNull Activity activity, @NonNull IInAppBillingService service, int amount) {
        Bundle buyIntentBundle = null;
        try {
            buyIntentBundle = service.getBuyIntent(3, activity.getPackageName(),
                    getSku(amount), "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");

        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if(buyIntentBundle != null) {
            try {
                PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                activity.startIntentSenderForResult(pendingIntent.getIntentSender(),
                        1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
                        Integer.valueOf(0));
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }

        }
    }

    public static String getSku(int amount){
        switch (amount){
            case 1: return DONATION_ONE_DOLLAR;
            case 2: return DONATION_TWO_DOLLARS;
            case 5: return DONATION_FIVE_DOLLARS;
            case 10: return DONATION_TEN_DOLLARS;
            case 20: return DONATION_TWENTY_DOLLARS;
        }
        return DONATION_ONE_DOLLAR;
    }
}
