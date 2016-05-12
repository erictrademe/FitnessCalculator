package com.shalskar.fitnesscalculator.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.shalskar.fitnesscalculator.Constants;

/**
 * Created by Vincent on 7/05/2016.
 */
public class SharedPreferencesManager {

    private static final String KEY_UNIT = "unit";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ACTIVITY_LEVEL = "activity_level";

    private static SharedPreferencesManager sharedPreferencesManager;

    private SharedPreferences sharedPreferences;

    public SharedPreferencesManager (@NonNull Context context){
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferencesManager = this;
    }

    public static void saveUnit(int unit){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putInt(KEY_UNIT, unit);
        editor.apply();
    }

    public static int getUnit(){
        return sharedPreferencesManager.sharedPreferences.getInt(KEY_UNIT, Constants.UNIT_METRIC);
    }

    public static void saveWeight(double weight){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_WEIGHT, (float) weight);
        editor.apply();
    }

    public static double getWeight(){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_WEIGHT, 0);
    }

    public static void saveHeight(double height){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_HEIGHT, (float) height);
        editor.apply();
    }

    public static double getHeight(){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_HEIGHT, 0);
    }

    public static void saveGender(int gender){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putInt(KEY_GENDER, gender);
        editor.apply();
    }

    public static int getGender(){
        return sharedPreferencesManager.sharedPreferences.getInt(KEY_GENDER, -1);
    }

    public static void saveAge(int age){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putInt(KEY_HEIGHT, age);
        editor.apply();
    }

    public static int getAge(){
        return sharedPreferencesManager.sharedPreferences.getInt(KEY_AGE, 0);
    }

    public static void saveActivityLevel(double activityLevel){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_ACTIVITY_LEVEL, (float) activityLevel);
        editor.apply();
    }

    public static double getActivityLevel(){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_ACTIVITY_LEVEL, -1);
    }

}
