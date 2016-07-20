package com.shalskar.fitnesscalculator;

import android.support.annotation.NonNull;

import com.shalskar.fitnesscalculator.managers.SharedPreferencesManager;
import com.shalskar.fitnesscalculator.model.Breakdown;
import com.shalskar.fitnesscalculator.model.MetabolicEquivalent;
import com.shalskar.fitnesscalculator.model.Physique;

/**
 * Created by Vincent on 7/05/2016.
 */
public class FitnessCalculator {

    // Prevent instantiation
    private FitnessCalculator() {
    }

    public static double calculateBMI(double weight, double height) {
        height = height / 100;
        return weight / (height * height);
    }

    //Harris-Benedict formula
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
     * Macros are returned in protein/carbohydrate/fat format
     */

    public static double[] calculateMacros(@NonNull Breakdown breakdown, float calorieIntake) {
        double[] macros = new double[3];

        macros[0] = breakdown.getProteinRatio() * calorieIntake / 4;
        macros[1] = breakdown.getCarbohydratesRatio() * calorieIntake / 4;
        macros[2] = breakdown.getFatRatio() * calorieIntake / 9;

        return macros;
    }

    public static double[] calculateIdealBodyWeight(double height) {
        height = height / 100;
        double lowerBound = (height * height) * 18.5;
        double upperBound = (height * height) * 25;
        return new double[]{lowerBound, upperBound};
    }

    private static float calculateOneRepMax(int oneRepMaxFormula, int repsLifted, float weightLifted) {
        if (repsLifted == 1) return weightLifted;
        switch (oneRepMaxFormula) {
            case Constants.ONE_REP_MAX_FORMULA_EPLEY:
                return weightLifted * (1 + (repsLifted / 30f));
            case Constants.ONE_REP_MAX_FORMULA_BRZYCKI:
                return weightLifted * (36f / (37f - repsLifted));
            case Constants.ONE_REP_MAX_FORMULA_LANDER:
                return (100 * weightLifted) / (101.3f - (2.67123f * repsLifted));
            case Constants.ONE_REP_MAX_FORMULA_LOMBARDI:
                return weightLifted * (float) Math.pow(repsLifted, 0.1);
            case Constants.ONE_REP_MAX_FORMULA_MAYHEW:
                return (float) ((100 * weightLifted) / (52.2 + 41.9 * Math.exp(-0.055 * repsLifted)));
            case Constants.ONE_REP_MAX_FORMULA_OCONNER:
                return weightLifted * (1 + 0.025f * repsLifted);
            case Constants.ONE_REP_MAX_FORMULA_WATHEN:
                return (float) (100 * weightLifted / (48.8 + 53.8 * Math.exp(-0.075 * repsLifted)));
        }
        return 0;
    }

    /**
     * Calculate the amount of reps one could theoretically lift, given a weight lifted and for how many reps.
     **/
    private final static float[] REP_MAX_PERCENTAGES = {1, 0.95f, 0.9f, 0.88f, 0.86f, 0.83f, 0.8f, 0.78f, 0.76f, 0.75f, 0.72f, 0.7f};

    public static float calculateRepMax(int reps, int oneRepMaxFormula, int repsLifted, float weightLifted) {
        float oneRepMax = calculateOneRepMax(oneRepMaxFormula, repsLifted, weightLifted);
        if (reps == repsLifted) return weightLifted;
        return oneRepMax * REP_MAX_PERCENTAGES[reps - 1];
    }

    public static float calculateWilks(int gender, float weight, float total) {
        // Coefficients
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

    /**
     * Calculate the bodyfat of the user, if the fields have been filled out correctly,
     * return -1;
     *
     * @return
     */
    public static float calculateBodyFat() {
        int bodyfatCalculatorType = SharedPreferencesManager.getBodyfatCalculatorType();
        int gender = SharedPreferencesManager.getGender();
        int age = SharedPreferencesManager.getAge();

        if (bodyfatCalculatorType == Constants.BODYFAT_CALCULATOR_TYPE_3_POINT) {
            if (gender == Constants.GENDER_FEMALE) {
                float skinfoldSuprailiac = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_SUPRAILIAC);
                float skinfoldThigh = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_THIGH);
                float skinfoldTriceps = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_TRICEPS);
                if (skinfoldSuprailiac >= 0 && skinfoldThigh >= 0 && skinfoldTriceps >= 0)
                    return FitnessCalculator.calculateBodyfatFemale(skinfoldThigh, skinfoldTriceps, skinfoldSuprailiac, age);
            } else if (gender == Constants.GENDER_MALE) {
                float skinfoldThigh = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_THIGH);
                float skinfoldPectoral = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_PECTORAL);
                float skinfoldAbdominal = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_ABDOMINAL);
                if (skinfoldThigh >= 0 && skinfoldAbdominal >= 0 && skinfoldPectoral >= 0)
                    return FitnessCalculator.calculateBodyfatMale(skinfoldPectoral, skinfoldAbdominal, skinfoldThigh, age);
            }
        } else if (bodyfatCalculatorType == Constants.BODYFAT_CALCULATOR_TYPE_7_POINT) {
            float skinfoldAbdominal = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_ABDOMINAL);
            float skinfoldAxilla = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_AXILLA);
            float skinfoldPectoral = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_PECTORAL);
            float skinfoldSubscapular = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_SUBSCAPULAR);
            float skinfoldSuprailiac = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_SUPRAILIAC);
            float skinfoldThigh = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_THIGH);
            float skinfoldTriceps = SharedPreferencesManager.getSkinfold(Constants.SKINFOLD_TRICEPS);
            if (skinfoldSuprailiac >= 0 && skinfoldAbdominal >= 0 && skinfoldPectoral >= 0 && skinfoldAxilla >= 0
                    && skinfoldSubscapular >= 0 && skinfoldThigh >= 0 && skinfoldTriceps >= 0)
                return FitnessCalculator.calculateBodyfat(skinfoldPectoral, skinfoldAbdominal, skinfoldThigh, skinfoldTriceps, skinfoldSubscapular,
                        skinfoldSuprailiac, skinfoldAxilla, gender, age);
        }
        return -1;
    }

    /**
     * Calculate bodyfat using 7 site skinfold caliper test.
     */
    public static float calculateBodyfat(float pectoralSkinfold, float abdominalSkinfold, float thighSkinfold,
                                         float tricepsSkinfold, float subscapularSkinfold, float suprailiacSkinfold,
                                         float axillaSkingold, int gender, int age) {
        float sum = pectoralSkinfold + abdominalSkinfold + thighSkinfold + tricepsSkinfold + subscapularSkinfold +
                suprailiacSkinfold + axillaSkingold;

        float bodyfat = 0;
        if (gender == Constants.GENDER_FEMALE) {
            bodyfat = convertBodyDensityToBodyfat(1.097f - (0.00046971f * sum) + (0.00000056f * sum * sum) - (0.00012828f * age));
        } else if (gender == Constants.GENDER_MALE) {
            bodyfat = convertBodyDensityToBodyfat(1.112f - (0.00043499f * sum) + (0.00000055f * sum * sum) - (0.00028826f * age));
        }
        return bodyfat;
    }

    /**
     * Calculate bodyfat using 3 site skinfold caliper test.
     */
    public static float calculateBodyfatFemale(float thighSkinfold, float tricepsSkinfold, float suprailiacSkinfold, int age) {
        float sum = thighSkinfold + tricepsSkinfold + suprailiacSkinfold;
        return convertBodyDensityToBodyfat(1.0994291f - (0.0009929f * sum) + (0.0000023f * sum * sum) - (0.0001392f * age));
    }

    public static float calculateBodyfatMale(float pectoralSkinfold, float abdominalSkinfold, float thighSkinfold, int age) {
        float sum = pectoralSkinfold + abdominalSkinfold + thighSkinfold;
        return convertBodyDensityToBodyfat(1.10938f - (0.0008267f * sum) + (0.0000016f * sum * sum) - (0.0002574f * age));
    }

    public static float convertBodyDensityToBodyfat(float bodyDensity) {
        return ((4.95f / bodyDensity) - 4.5f) * 100;
    }

    public static int getBodyfatCategory(int gender, float bodyfat) {
        if (gender == Constants.GENDER_FEMALE) {
            if (bodyfat <= 12) return Constants.BODYFAT_CATEGORY_ESSENTIAL;
            else if (bodyfat <= 20) return Constants.BODYFAT_CATEGORY_ATHLETES;
            else if (bodyfat <= 24) return Constants.BODYFAT_CATEGORY_FITNESS;
            else if (bodyfat <= 31) return Constants.BODYFAT_CATEGORY_ACCEPTABLE;
            else return Constants.BODYFAT_CATEGORY_OBESE;
        } else if (gender == Constants.GENDER_MALE) {
            if (bodyfat <= 4) return Constants.BODYFAT_CATEGORY_ESSENTIAL;
            else if (bodyfat <= 13) return Constants.BODYFAT_CATEGORY_ATHLETES;
            else if (bodyfat <= 17) return Constants.BODYFAT_CATEGORY_FITNESS;
            else if (bodyfat <= 25) return Constants.BODYFAT_CATEGORY_ACCEPTABLE;
            else return Constants.BODYFAT_CATEGORY_OBESE;
        }
        return Constants.BODYFAT_CATEGORY_ACCEPTABLE;
    }

    /**
     * Strength standards.
     * Bench press, Squat and Deadlift sourced from exrx
     * Other lifts from http://strengthlevel.com/strength-standards
     **/

    public static final float[][] SQUAT_STANDARDS_MALE =
            {{52, 35, 60, 80, 107.5f, 145},
                    {56, 37.5f, 70, 87.5f, 117.5f, 157.5f},
                    {60, 40, 77.5f, 92.5f, 127.5f, 167.5f},
                    {67, 45, 85, 105, 142.5f, 185.5f},
                    {75, 50, 92.5f, 112.5f, 155, 202.5f},
                    {82, 55, 100, 122.5f, 167.5f, 217.5f},
                    {90, 57.5f, 105, 130, 177.5f, 230},
                    {100, 60, 110, 135, 185, 240},
                    {110, 62.5f, 115, 140, 192.5f, 250},
                    {125, 65, 117.5f, 145, 197.5f, 257.5f},
                    {145, 67.5f, 122.5f, 147.5f, 202.5f, 262.5f},
                    {999, 70, 125, 150, 207.5f, 270}};

    public static final float[][] SQUAT_STANDARDS_FEMALE =
            {{44, 20, 37.5f, 45, 60, 75},
                    {48, 22.5f, 40, 47.5f, 65, 80},
                    {52, 25, 45, 52.5f, 67.5f, 87.5f},
                    {56, 25, 47.5f, 55, 72.5f, 90},
                    {60, 27.5f, 50, 60, 77.5f, 95},
                    {67, 30, 55, 62.5f, 85, 105},
                    {75, 32.5f, 57.5f, 67.5f, 90, 115},
                    {82, 35, 62.5f, 75, 97.5f, 122.5f},
                    {90, 37.5f, 67.5f, 80, 105, 132.5f},
                    {90, 40, 72.5f, 85, 110, 137.5f}};

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

    public static final float[][] DEADLIFT_STANDARDS_MALE =
            {{52, 42.5f, 82.5f, 92.5f, 135, 175},
                    {56, 47.5f, 87.5f, 100, 145, 187.5f},
                    {60, 50, 95, 110, 155, 200},
                    {67, 57.5f, 107.5f, 122.5f, 172.5f, 215.5f},
                    {75, 62.5f, 115, 135, 185, 235},
                    {82, 67.5f, 125, 142.5f, 200, 250},
                    {90, 70, 132.5f, 152.5f, 207.5f, 257.5f},
                    {100, 75, 137.5f, 160, 217.5f, 265},
                    {110, 77.5f, 145, 165, 222.5f, 270},
                    {125, 80, 147.5f, 170, 227.5f, 272.5f},
                    {145, 82.5f, 152.5f, 230, 202.5f, 277.5f},
                    {999, 85, 155, 177.5f, 232.5f, 280}};


    public static final float[][] DEADLIFT_STANDARDS_FEMALE =
            {{44, 25, 47.5f, 50, 80, 105},
                    {48, 27.5f, 52.5f, 60, 85, 110},
                    {52, 30, 55, 62.5f, 90, 115},
                    {56, 32.5f, 60, 67.5f, 95, 120},
                    {60, 35, 62.5f, 72.5f, 100, 125},
                    {67, 37.5f, 67.5f, 80, 110, 135},
                    {75, 40, 72.5f, 85, 117.5f, 145},
                    {82, 42.5f, 80, 92.5f, 125, 150},
                    {90, 45, 87.5f, 97.5f, 130, 160},
                    {90, 50, 90, 105, 137.5f, 165}};

    public static final float[][] PRESS_STANDARDS_MALE =
            {{60, 23, 35, 51, 69, 89},
                    {65, 26, 39, 55, 74, 95},
                    {70, 29, 43, 60, 79, 101},
                    {75, 32, 46, 64, 84, 106},
                    {80, 35, 50, 68, 89, 111},
                    {85, 38, 53, 72, 93, 117},
                    {90, 41, 57, 76, 98, 121},
                    {95, 44, 60, 80, 102, 126},
                    {100, 46, 63, 83, 106, 131},
                    {105, 49, 66, 87, 110, 135},
                    {110, 52, 69, 90, 114, 139},
                    {115, 54, 72, 93, 118, 144},
                    {120, 57, 75, 97, 121, 148},
                    {125, 59, 78, 100, 125, 152},
                    {130, 61, 80, 103, 128, 155},
                    {135, 64, 83, 106, 132, 159},
                    {140, 66, 86, 109, 135, 163}};


    public static final float[][] PRESS_STANDARDS_FEMALE =
            {{40, 8, 15, 24, 36, 50},
                    {45, 9, 16, 26, 39, 53},
                    {50, 10, 18, 28, 39, 56},
                    {55, 11, 19, 30, 43, 58},
                    {60, 12, 21, 32, 45, 61},
                    {65, 13, 22, 33, 47, 63},
                    {70, 14, 23, 35, 49, 65},
                    {75, 15, 24, 36, 51, 67},
                    {80, 16, 26, 38, 53, 69},
                    {85, 17, 27, 39, 54, 71},
                    {90, 18, 28, 40, 56, 72},
                    {95, 19, 29, 42, 57, 74},
                    {100, 19, 30, 43, 58, 76},
                    {105, 20, 31, 44, 60, 77},
                    {110, 21, 32, 45, 61, 78},
                    {115, 22, 32, 46, 62, 80},
                    {120, 22, 33, 47, 63, 81}};

    public static final float[][] ROW_STANDARDS_MALE =
            {{60, 30, 46, 65, 89, 114},
                    {65, 34, 50, 71, 95, 122},
                    {70, 38, 55, 76, 101, 129},
                    {75, 42, 59, 82, 107, 135},
                    {80, 45, 64, 87, 113, 142},
                    {85, 49, 68, 92, 119, 148},
                    {90, 52, 72, 96, 124, 154},
                    {95, 55, 76, 101, 129, 160},
                    {100, 59, 80, 106, 135, 166},
                    {105, 62, 84, 110, 139, 171},
                    {110, 65, 87, 114, 144, 176},
                    {115, 68, 91, 118, 149, 181},
                    {120, 71, 94, 122, 153, 186},
                    {125, 74, 98, 126, 158, 191},
                    {130, 77, 101, 130, 162, 196},
                    {135, 80, 104, 133, 166, 200},
                    {140, 83, 108, 137, 170, 205}};


    public static final float[][] ROW_STANDARDS_FEMALE =
            {{40, 10, 19, 32, 48, 67},
                    {45, 10, 20, 33, 50, 70},
                    {50, 11, 21, 35, 52, 72},
                    {55, 12, 22, 37, 54, 74},
                    {60, 13, 24, 38, 56, 76},
                    {65, 14, 25, 39, 57, 78},
                    {70, 14, 26, 40, 59, 79},
                    {75, 15, 26, 42, 60, 81},
                    {80, 16, 27, 43, 61, 83},
                    {85, 16, 28, 44, 63, 84},
                    {90, 17, 29, 45, 64, 85},
                    {95, 18, 30, 46, 65, 87},
                    {100, 18, 30, 46, 66, 88},
                    {105, 19, 31, 47, 67, 89},
                    {110, 19, 32, 48, 68, 90},
                    {115, 20, 32, 49, 69, 91},
                    {120, 20, 33, 50, 70, 92}};

    public static final float[][] FRONT_SQUAT_STANDARDS_MALE =
            {{60, 41, 59, 81, 106, 134},
                    {65, 46, 64, 87, 113, 142},
                    {70, 50, 69, 93, 120, 149},
                    {75, 54, 74, 99, 127, 157},
                    {80, 59, 79, 105, 133, 164},
                    {85, 63, 84, 110, 139, 170},
                    {90, 67, 89, 115, 145, 177},
                    {95, 70, 93, 120, 151, 183},
                    {100, 74, 97, 125, 156, 189},
                    {105, 78, 101, 130, 161, 195},
                    {110, 81, 106, 134, 167, 201},
                    {115, 85, 110, 139, 171, 206},
                    {120, 88, 113, 143, 176, 211},
                    {125, 92, 117, 147, 181, 216},
                    {130, 95, 121, 151, 186, 221},
                    {135, 98, 124, 155, 190, 226},
                    {140, 101, 128, 159, 194, 231}};


    public static final float[][] FRONT_SQUAT_STANDARDS_FEMALE =
            {{40, 19, 31, 45, 63, 83},
                    {45, 22, 33, 49, 67, 87},
                    {50, 24, 36, 52, 71, 91},
                    {55, 26, 39, 55, 74, 95},
                    {60, 27, 41, 58, 77, 99},
                    {65, 29, 43, 60, 80, 102},
                    {70, 31, 45, 63, 83, 105},
                    {75, 33, 47, 65, 86, 108},
                    {80, 34, 49, 67, 88, 111},
                    {85, 36, 51, 69, 91, 114},
                    {90, 37, 52, 71, 93, 116},
                    {95, 38, 54, 73, 95, 119},
                    {100, 40, 56, 75, 97, 121},
                    {105, 41, 57, 77, 99, 124},
                    {110, 42, 59, 79, 101, 126},
                    {115, 44, 60, 80, 103, 128},
                    {120, 45, 61, 82, 105, 130}};

    public static int calculateStrengthStandard(@NonNull String exercise, int gender, float bodyweight, float weightLifted) {
        float[][] standards = {};
        if (exercise.equals(Constants.EXERCISE_BENCH_PRESS)) {
            if (gender == Constants.GENDER_MALE) standards = BENCH_PRESS_STANDARDS_MALE;
            else if (gender == Constants.GENDER_FEMALE) standards = BENCH_PRESS_STANDARDS_FEMALE;
        } else if (exercise.equals(Constants.EXERCISE_SQUAT)) {
            if (gender == Constants.GENDER_MALE) standards = SQUAT_STANDARDS_MALE;
            else if (gender == Constants.GENDER_FEMALE) standards = SQUAT_STANDARDS_FEMALE;
        } else if (exercise.equals(Constants.EXERCISE_DEADLIFT)) {
            if (gender == Constants.GENDER_MALE) standards = DEADLIFT_STANDARDS_MALE;
            else if (gender == Constants.GENDER_FEMALE) standards = DEADLIFT_STANDARDS_FEMALE;
        } else if (exercise.equals(Constants.EXERCISE_PRESS)) {
            if (gender == Constants.GENDER_MALE) standards = PRESS_STANDARDS_MALE;
            else if (gender == Constants.GENDER_FEMALE) standards = PRESS_STANDARDS_FEMALE;
        } else if (exercise.equals(Constants.EXERCISE_ROW)) {
            if (gender == Constants.GENDER_MALE) standards = ROW_STANDARDS_MALE;
            else if (gender == Constants.GENDER_FEMALE) standards = ROW_STANDARDS_FEMALE;
        } else if (exercise.equals(Constants.EXERCISE_FRONT_SQUAT)) {
            if (gender == Constants.GENDER_MALE) standards = FRONT_SQUAT_STANDARDS_MALE;
            else if (gender == Constants.GENDER_FEMALE) standards = FRONT_SQUAT_STANDARDS_FEMALE;
        }


        for (int weightClass = 0; weightClass < standards.length; weightClass++) {
            if (bodyweight <= standards[weightClass][0]
                    || weightClass == standards.length - 1) {
                if (weightLifted < standards[weightClass][1])
                    return Constants.STRENGTH_STANDARD_UNTRAINED;
                for (int standard = 1; standard < standards[weightClass].length; standard++) {
                    if (standard == standards[weightClass].length - 1)
                        return Constants.STRENGTH_STANDARD_ELITE;
                    if (weightLifted >= standards[weightClass][standard] &&
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

    public static float calculateCaloriesBurned(@NonNull MetabolicEquivalent MET, float bodyWeight, float duration) {
        return MET.value * bodyWeight * (duration / 60);
    }

}

