package com.shalskar.fitnesscalculator;

/**
 * Created by Vincent on 7/05/2016.
 */
public class Constants {

    //Prevent instantiation
    private Constants() {
    }

    public final static String DEFAULT_FONT_PATH = "fonts/Raleway-Regular.ttf";

    public static final int UNDEFINED = -1;

    public static final int DETAIL_UNIT = 1233;
    public static final int DETAIL_WEIGHT = 1234;
    public static final int DETAIL_HEIGHT = 1235;
    public static final int DETAIL_AGE = 1236;
    public static final int DETAIL_GENDER = 1237;
    public static final int DETAIL_ACTIVITY_LEVEL = 1238;
    public static final int DETAIL_GOAL = 1239;
    public static final int DETAIL_ONE_REP_MAX = 1240;
    public static final int DETAIL_EXERCISE = 1241;
    public static final int DETAIL_MEASUREMENT = 1242;
    public static final int DETAIL_SKINFOLD = 1243;
    public static final int DETAIL_BODYFAT_CALCULATOR_TYPE = 1244;
    public static final int DETAIL_CALORIE_INTAKE = 1245;
    public static final int DETAIL_MACRONUTRIENT = 1246;

    public final static int UNIT_METRIC = 1111;
    public final static int UNIT_IMPERIAL = 2222;

    public final static float ACTIVITY_LEVEL_SEDENTARY = 1.2f;
    public final static float ACTIVITY_LEVEL_LIGHT = 1.375f;
    public final static float ACTIVITY_LEVEL_MODERATE = 1.55f;
    public final static float ACTIVITY_LEVEL_ACTIVE = 1.725f;
    public final static float ACTIVITY_LEVEL_EXTREME = 1.9f;

    public final static int ONE_REP_MAX_FORMULA_EPLEY = 3880;
    public final static int ONE_REP_MAX_FORMULA_BRZYCKI = 3881;
    public final static int ONE_REP_MAX_FORMULA_LANDER = 3882;
    public final static int ONE_REP_MAX_FORMULA_LOMBARDI = 3883;
    public final static int ONE_REP_MAX_FORMULA_MAYHEW = 3884;
    public final static int ONE_REP_MAX_FORMULA_OCONNER = 3885;
    public final static int ONE_REP_MAX_FORMULA_WATHEN = 3886;

    public final static int GOAL_GAIN_MUSCLE = 9999;
    public final static int GOAL_FAT_LOSS = 1111;
    public final static int GOAL_MAINTAIN = 2222;
    public final static int GOAL_CUSTOM = 3333;

    public final static int GENDER_FEMALE = 0;
    public final static int GENDER_MALE = 1;

    public final static int STRENGTH_STANDARD_UNTRAINED = 1;
    public final static int STRENGTH_STANDARD_NOVICE = 2;
    public final static int STRENGTH_STANDARD_INTERMEDIATE = 3;
    public final static int STRENGTH_STANDARD_ADVANCED = 4;
    public final static int STRENGTH_STANDARD_ELITE = 5;

    public static final String EXERCISE_SQUAT = "exercise_squat";
    public static final String EXERCISE_BENCH_PRESS = "exercise_bench_press";
    public static final String EXERCISE_DEADLIFT = "exercise_deadlift";

    public static final String BODY_PART_WRIST = "body_part_wrist";
    public static final String BODY_PART_ANKLE = "body_part_ankle";

    public static final String FORMAT_NUMBER = "###.#";

    public final static int BODYFAT_CALCULATOR_TYPE_3_POINT = 4774;
    public final static int BODYFAT_CALCULATOR_TYPE_7_POINT = 4775;

    public static final String SKINFOLD_PECTORAL = "skinfold_pectoral";
    public static final String SKINFOLD_ABDOMINAL = "skinfold_abdominal";
    public static final String SKINFOLD_THIGH = "skinfold_thigh";
    public static final String SKINFOLD_TRICEPS = "skinfold_triceps";
    public static final String SKINFOLD_SUBSCAPULAR = "skinfold_subscapular";
    public static final String SKINFOLD_SUPRAILIAC = "skinfold_suprailiac";
    public static final String SKINFOLD_AXILLA = "skinfold_axilla";

    public final static int BODYFAT_CATEGORY_ESSENTIAL = 6554;
    public final static int BODYFAT_CATEGORY_ATHLETES = 6555;
    public final static int BODYFAT_CATEGORY_FITNESS = 6556;
    public final static int BODYFAT_CATEGORY_ACCEPTABLE = 6557;
    public final static int BODYFAT_CATEGORY_OBESE = 6558;

    public final static String MACRONUTRIENT_PROTEIN = "macronutrient_protein";
    public final static String MACRONUTRIENT_CARBOHYDRATES = "macronutrient_carbohydrates";
    public final static String MACRONUTRIENT_FAT = "macronutrient_fat";

    public final static String IAP_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhu9lqEkiHo5cXgmC4d9wMVxUKwvdFEzIQFP8cgFGpYLBKZg7o7Km6ML7ulnGalcmkFhEbTWYfsM58NPz5q5veg8sHqTDiPFVYUvjE36+qc0dSnEgFChwvPVugCxBoR5mdbyUw4IUDDzmR1fP9Pxp9E6f7PzwKZMP3Hj9YWfr47Pg1S3Tf/tTC/ByjHAdck7ITsLFiW3py3DWnF82/1GSSGkG2SD0TvyymjK0oH+osYo7uzds4+fCwAytTjMnLcQoEcFEQdpZxabsLiiROJNB0Dnx04EjjZt5XZBmR0VOEvIXDXqmLJeSPFn6ulX5wEJw80OvSPD40hMt6q7jmO/9DwIDAQAB";

    public static final String DONATION_ONE_DOLLAR = "donation_1";
    public static final String DONATION_TWO_DOLLARS = "donation_2";
    public static final String DONATION_FIVE_DOLLARS = "donation_3";
    public static final String DONATION_TEN_DOLLARS = "donation_4";
    public static final String DONATION_TWENTY_DOLLARS = "donation_5";
}
