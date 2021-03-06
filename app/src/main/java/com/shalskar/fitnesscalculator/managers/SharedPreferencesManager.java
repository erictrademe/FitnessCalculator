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
    private static final String KEY_GOAL = "goal";
    private static final String KEY_REP_MAX_VALUE = "rep_max_value";
    private static final String KEY_WEIGHT_LIFTED = "weight_lifted";
    private static final String KEY_REPS_LIFTED = "reps_lifted";
    private static final String KEY_ONE_REP_MAX_FORMULA = "one_rep_max_formula";
    private static final String KEY_MEASUREMENT = "measurement";
    private static final String KEY_BODYFAT_CALCULATOR_TYPE = "bodyfat_calculator_type";
    private static final String KEY_SKINFOLD = "skinfold";
    private static final String KEY_CALORIE_INTAKE = "calorie_intake";
    private static final String KEY_MACRONUTRIENT = "macronutrient";

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

    public static void saveWeight(float weight){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_WEIGHT, weight);
        editor.apply();
    }

    public static float getWeight(){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_WEIGHT, 0);
    }

    public static void saveHeight(float height){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_HEIGHT, height);
        editor.apply();
    }

    public static float getHeight(){
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
        editor.putInt(KEY_AGE, age);
        editor.apply();
    }

    public static int getAge(){
        return sharedPreferencesManager.sharedPreferences.getInt(KEY_AGE, 0);
    }

    public static void saveActivityLevel(float activityLevel){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_ACTIVITY_LEVEL, activityLevel);
        editor.apply();
    }

    public static float getActivityLevel(){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_ACTIVITY_LEVEL, -1);
    }

    public static void saveGoal(int goal){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putInt(KEY_GOAL, goal);
        editor.apply();
    }

    public static int getGoal(){
        return sharedPreferencesManager.sharedPreferences.getInt(KEY_GOAL, -1);
    }

    public static void saveWeightLifted(float weight){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_WEIGHT_LIFTED, weight);
        editor.apply();
    }

    public static void saveWeightLifted(@NonNull String exercise, float weight){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_WEIGHT_LIFTED + exercise, weight);
        editor.apply();
    }

    public static void saveRepsMaxValue(int repsMaxValue){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putInt(KEY_REP_MAX_VALUE, repsMaxValue);
        editor.apply();
    }

    public static int getRepsMaxValue(){
        return sharedPreferencesManager.sharedPreferences.getInt(KEY_REP_MAX_VALUE, 1);
    }

    public static float getWeightLifted(){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_WEIGHT_LIFTED, -1);
    }

    public static float getWeightLifted(@NonNull String exercise){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_WEIGHT_LIFTED + exercise, -1);
    }

    public static void saveRepsLifted(int reps){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putInt(KEY_REPS_LIFTED, reps);
        editor.apply();
    }

    public static int getRepsLifted(@NonNull String exercise){
        return sharedPreferencesManager.sharedPreferences.getInt(KEY_REPS_LIFTED + exercise, -1);
    }

    public static void saveRepsLifted(@NonNull String exercise, int reps){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putInt(KEY_REPS_LIFTED + exercise, reps);
        editor.apply();
    }

    public static int getRepsLifted(){
        return sharedPreferencesManager.sharedPreferences.getInt(KEY_REPS_LIFTED, -1);
    }

    public static void saveOneRepMaxFormula(int oneRepMaxFormula){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putInt(KEY_ONE_REP_MAX_FORMULA, oneRepMaxFormula);
        editor.apply();
    }

    public static int getOneRepMaxFormula(){
        return sharedPreferencesManager.sharedPreferences.getInt(KEY_ONE_REP_MAX_FORMULA, -1);
    }

    public static void saveMeasurement(@NonNull String bodyPart, float measurement){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_MEASUREMENT + bodyPart, measurement);
        editor.apply();
    }

    public static float getMeasurement(@NonNull String bodyPart){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_MEASUREMENT + bodyPart, -1);
    }

    public static void saveSkinfold(@NonNull String skinfold, float measurement){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_SKINFOLD + skinfold, measurement);
        editor.apply();
    }

    public static float getSkinfold(@NonNull String skinfold){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_SKINFOLD + skinfold, -1);
    }

    public static int getBodyfatCalculatorType(){
        return sharedPreferencesManager.sharedPreferences.getInt(KEY_BODYFAT_CALCULATOR_TYPE, Constants.BODYFAT_CALCULATOR_TYPE_7_POINT);
    }

    public static void saveBodyfatCalculatorType(int bodyfatCalculatorType){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putInt(KEY_BODYFAT_CALCULATOR_TYPE, bodyfatCalculatorType);
        editor.apply();
    }

    public static void saveCalorieIntake(float calorieIntake){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_CALORIE_INTAKE, calorieIntake);
        editor.apply();
    }

    public static float getCalorieIntake(){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_CALORIE_INTAKE, -1);
    }

    public static void saveMacronutrient(@NonNull String macronutrient, float amount){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();
        editor.putFloat(KEY_MACRONUTRIENT + macronutrient, amount);
        editor.apply();
    }

    public static float getMacronutrient(@NonNull String macronutrient){
        return sharedPreferencesManager.sharedPreferences.getFloat(KEY_MACRONUTRIENT + macronutrient, -1);
    }

    public static void clearAll(){
        SharedPreferences.Editor editor = sharedPreferencesManager.sharedPreferences.edit();

        editor.remove(KEY_UNIT);
        editor.remove(KEY_AGE);
        editor.remove(KEY_GENDER);
        editor.remove(KEY_HEIGHT);
        editor.remove(KEY_WEIGHT);
        editor.remove(KEY_ACTIVITY_LEVEL);
        editor.remove(KEY_GOAL);
        editor.remove(KEY_REP_MAX_VALUE);
        editor.remove(KEY_WEIGHT_LIFTED);
        editor.remove(KEY_REPS_LIFTED);
        editor.remove(KEY_ONE_REP_MAX_FORMULA);

        editor.remove(KEY_WEIGHT_LIFTED + Constants.EXERCISE_SQUAT);
        editor.remove(KEY_WEIGHT_LIFTED + Constants.EXERCISE_BENCH_PRESS);
        editor.remove(KEY_WEIGHT_LIFTED + Constants.EXERCISE_DEADLIFT);

        editor.remove(KEY_MEASUREMENT + Constants.BODY_PART_ANKLE);
        editor.remove(KEY_MEASUREMENT + Constants.BODY_PART_WRIST);

        editor.remove(KEY_BODYFAT_CALCULATOR_TYPE);
        editor.remove(KEY_SKINFOLD + Constants.SKINFOLD_ABDOMINAL);
        editor.remove(KEY_SKINFOLD + Constants.SKINFOLD_AXILLA);
        editor.remove(KEY_SKINFOLD + Constants.SKINFOLD_PECTORAL);
        editor.remove(KEY_SKINFOLD + Constants.SKINFOLD_SUBSCAPULAR);
        editor.remove(KEY_SKINFOLD + Constants.SKINFOLD_SUPRAILIAC);
        editor.remove(KEY_SKINFOLD + Constants.SKINFOLD_THIGH);
        editor.remove(KEY_SKINFOLD + Constants.SKINFOLD_TRICEPS);

        editor.remove(KEY_CALORIE_INTAKE);

        editor.remove(KEY_MACRONUTRIENT + Constants.MACRONUTRIENT_PROTEIN);
        editor.remove(KEY_MACRONUTRIENT + Constants.MACRONUTRIENT_CARBOHYDRATES);
        editor.remove(KEY_MACRONUTRIENT + Constants.MACRONUTRIENT_FAT);

        editor.apply();
    }

}
