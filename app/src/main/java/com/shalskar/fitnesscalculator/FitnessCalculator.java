package com.shalskar.fitnesscalculator;

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

    public static float calculateWilks(int unit, int gender, float weight, float total){
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
        return total * (500.0f / (float)denom);
    }

    /** Strength standard calculators. **/
}

