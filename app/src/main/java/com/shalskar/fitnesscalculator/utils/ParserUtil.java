package com.shalskar.fitnesscalculator.utils;

import android.content.Context;
import android.nfc.FormatException;
import android.support.annotation.NonNull;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Created by Vincent on 7/05/2016.
 */
public class ParserUtil {

    /** Try and parse the double using the traditional way, if that fails, then try using the current locale. **/
    public static double parseDouble(@NonNull Context context, @NonNull String doubleString){
        double d = 0;
        try {
            d = Double.parseDouble(doubleString);
        } catch (Exception e){
            d = parseDoubleLocale(context, doubleString);
        }
        return d;
    }

    /** To account for locale, we use our own parsing method. **/
    public static double parseDoubleLocale(@NonNull Context context, @NonNull String doubleString){
        double d = 0;
        try {
            NumberFormat format = NumberFormat.getInstance(context.getResources().getConfiguration().locale);
            Number number = format.parse(doubleString);
            d = number.doubleValue();
        } catch (ParseException e){
            e.printStackTrace();
        }
        return d;
    }
}
