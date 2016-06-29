package com.shalskar.fitnesscalculator;

import android.app.Application;
import android.content.res.Configuration;

import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Vincent on 7/05/2016.
 */
public class FitnessApplication extends Application {

    private static FitnessApplication singleton;

    public FitnessApplication getInstance(){
        return singleton;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        new SharedPreferencesManager(this);
        singleton = this;

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(Constants.DEFAULT_FONT_PATH)
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
