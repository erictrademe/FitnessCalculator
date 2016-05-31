package com.shalskar.fitnesscalculator;

import android.support.annotation.NonNull;

import com.shalskar.fitnesscalculator.utils.ConverterUtil;

/**
 * Created by Vincent on 7/05/2016.
 */
public class FitnessCalculator {

    public static double calculateBMI(double weight, double height) {
        height = height / 100;
        return weight / (height * height);
    }

    public static int calculateDailyCalorieIntake(double weight, double height, int gender, int age, double activityLevel) {
        double BMR = 0;
        if (gender == Constants.GENDER_FEMALE) {
            BMR = 665.09 + (9.56 * weight) + (1.84 * height) - (4.67 * age);
        } else if (gender == Constants.GENDER_MALE) {
            BMR = 66.47 + (13.75 * weight) + (5.0 * height) - (6.75 * age);
        }
        return (int) (BMR * activityLevel);
    }

    /**
     * Macros are returned in carbohydrate/fat/protein format
     */
    public static double[] calculateMacros(double weight, double height, int gender, int age, double activityLevel, int goal) {
        double[] macros = new double[3];
        int dailyCalorieIntake = calculateDailyCalorieIntake(weight, height, gender, age, activityLevel);

        // todo switch from dummy values
        switch (goal) {
            case Constants.GOAL_GAIN_MUSCLE:
                macros[0] = 0.4 * dailyCalorieIntake / 4;
                macros[1] = 0.2 * dailyCalorieIntake / 9;
                macros[2] = 0.4 * dailyCalorieIntake / 4;
                break;
            case Constants.GOAL_FAT_LOSS:
                macros[0] = 0.4 * dailyCalorieIntake / 4;
                macros[1] = 0.3 * dailyCalorieIntake / 9;
                macros[2] = 0.3 * dailyCalorieIntake / 4;
                break;
            case Constants.GOAL_MAINTAIN:
                macros[0] = 0.3 * dailyCalorieIntake / 4;
                macros[1] = 0.3 * dailyCalorieIntake / 9;
                macros[2] = 0.4 * dailyCalorieIntake / 4;
                break;

        }
        return macros;
    }

    public static double[] calculateIdealBodyWeight(double height) {
        height = height / 100;
        double lowerBound = (height * height) * 18.5;
        double upperBound = (height * height) * 25;
        return new double[]{lowerBound, upperBound};
    }

    public static float calculateOneRepMax(int repsLifted, float weightLifted) {
        // Epley formula
        return weightLifted * (1 + (repsLifted / 30f));
    }

    public static float calculateWilks(int unit, int gender, float weight, float total) {
        float a;
        float b;
        float c;
        float d;
        float e;
        float f;
        if (gender == Constants.GENDER_MALE) {
            a = -216.0475144f;
            b = 16.2606339f;
            c = -0.002388645f;
            d = -0.00113732f;
            e = 7.01863E-06f;
            f = -1.291E-08f;
        } else {
            a = 594.31747775582f;
            b = -27.23842536447f;
            c = 0.82112226871f;
            d = -0.00930733913f;
            e = 0.00004731582f;
            f = -0.00000009054f;
        }

        if (unit == Constants.UNIT_IMPERIAL) {
            weight = ConverterUtil.poundsToKgs(weight);
            total = ConverterUtil.poundsToKgs(total);
        }
        double denom = a + b * weight + c * Math.pow(weight, 2.0) + d * Math.pow(weight, 3.0)
                + e * Math.pow(weight, 4.0) + f * Math.pow(weight, 5.0);
        return total * (500.0f / (float) denom);
    }

    public static Physique calculateIdealPhysique(float wrist, float ankle) {
        float chest = (6.5f * wrist * 100) / 100;
        float shoulder = (1.618f * 0.7f * 6.5f * wrist * 100) / 100;
        float neck = (0.37f * 6.5f * wrist * 100) / 100;
        float waist = (0.7f * 6.5f * wrist * 100) / 100;
        float arm = (2.5f * wrist * 100) / 100;
        float forearm = (0.3f * 6.5f * wrist * 100) / 100;
        float thigh = (2.9f * ankle * 100) / 100;
        float calf = (1.9f * ankle * 100) / 100;
        return new Physique(chest, shoulder, neck, waist, arm, forearm, thigh, calf);
    }

    public static final float[][] BENCH_PRESS_STANDARDS_MALE =
            {{52, 37.5f, 50, 60, 82.5f, 100},
                    {56, 40, 52.5f, 62.5f, 90, 110},
                    {60, 45f, 57.5f, 70, 95, 117.5f},
                    {67, 50, 65, 77.5f, 107.5f, 132.5f},
                    {75, 55, 70, 85, 115, 145},
                    {82, 60, 75, 90, 125, 157.5f},
                    {90, 62.5f, 80, 97.5f, 132.5f, 162.5f},
                    {100, 62.5f, 82.5f, 102.5f, 137.5f, 172.5f},
                    {110, 65, 85, 105, 142.5f, 180},
                    {125, 67.5f, 87.5f, 107.5f, 147.5f, 185},
                    {145, 70, 90, 112.5f, 152.5f, 190},
                    {999, 72.5f, 92.5f, 115, 155, 192.5f}};

    public static final float[][] BENCH_PRESS_STANDARDS_FEMALE =
                   {{44, 22.5f, 30, 35, 42.5f, 52.5f},
                    {48, 25, 32.5f, 37.5f, 45, 57.5f},
                    {52, 27.5f, 35, 37.5f, 50, 62.5f},
                    {56, 30, 37.5f, 40, 52.5f, 65},
                    {60, 32.5f, 40, 42.5f, 57.5f, 67.5f},
                    {67, 35, 40, 47.5f, 62.5f, 75},
                    {75, 37.5f, 42.5f, 52.5f, 65, 85},
                    {82, 37.5f, 50, 55, 72.5f, 90},
                    {90, 40, 52.5f, 60, 75, 95},
                    {90, 42.5f, 55f, 62.5f, 80, 100}};


    public static int calculateStrengthStandard(@NonNull String exercise, int gender, float bodyweight, float weightLifted){
        float[][] standards = {};
        if(exercise.equals(Constants.EXERCISE_BENCH_PRESS)){
            if(gender == Constants.GENDER_MALE) standards = BENCH_PRESS_STANDARDS_MALE;
            else if(gender == Constants.GENDER_FEMALE) standards = BENCH_PRESS_STANDARDS_FEMALE;
        }

        for (int weightClass = 0; weightClass < standards.length; weightClass++) {
            if (bodyweight <= standards[weightClass][0]
                    || weightClass == standards.length - 1) {
                for (int standard = 1; standard < standards[weightClass].length; standard++) {
                    if (standard == standards[weightClass].length - 1)
                        return Constants.STRENGTH_STANDARD_ELITE;
                    if (weightLifted > standards[weightClass][standard] &&
                            weightLifted < standards[weightClass][standard + 1])
                        return getStrengthStandard(standard - 1);
                }
            }
        }
        return Constants.STRENGTH_STANDARD_UNTRAINED;
    }

    private static int getStrengthStandard(int standard) {
        switch (standard) {
            case 0:
                return Constants.STRENGTH_STANDARD_UNTRAINED;
            case 1:
                return Constants.STRENGTH_STANDARD_NOVICE;
            case 2:
                return Constants.STRENGTH_STANDARD_INTERMEDIATE;
            case 3:
                return Constants.STRENGTH_STANDARD_ADVANCED;
            case 4:
                return Constants.STRENGTH_STANDARD_ELITE;
        }
        return Constants.STRENGTH_STANDARD_NOVICE;
    }
}

