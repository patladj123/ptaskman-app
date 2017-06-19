package org.patladj.ptaskman.util;

/**
 * Created by PatlaDJ on 18.6.2017 г..
 *
 * Some frequently used utils
 */

import org.apache.commons.lang.StringEscapeUtils;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Util {

    /**
     * Escapes any possible string to be put into double quotet javascript string
     * @param s The string to be escaped
     * @return The escaped string
     */
    public static final String JSString(String s) {
        return StringEscapeUtils.escapeJavaScript(s);
    }

    /**
     * I use these to format numbers 3 various ways
     */
    public static final NumberFormat digits1Format = NumberFormat.getInstance(Locale.US);
    public static final NumberFormat digits2Format = NumberFormat.getInstance(Locale.US);
    public static final NumberFormat digits3Format = NumberFormat.getInstance(Locale.US);
    static {
        digits2Format.setMaximumFractionDigits(2);
        digits2Format.setMinimumFractionDigits(2);

        digits1Format.setMaximumFractionDigits(1);
        digits1Format.setMinimumFractionDigits(1);

        digits3Format.setMaximumFractionDigits(3);
        digits3Format.setMinimumFractionDigits(3);
    }

    /**
     * Generates random integer number
     * @param lowerBound The smallest possible number to be generated
     * @param upperBound The largest possible number to be generated
     * @return The number
     */
    public static final long rand(long lowerBound,long upperBound) {
        return lowerBound + (long) ( Math.random()*(upperBound -lowerBound) + 0.5D );
    }

    /**
     * Generates string of random ASCI small letters with no other characters
     * @param count Length of the string to be generated
     * @return The generated random string
     */
    public static final String generateRandomLetters(int count) {
        StringBuilder sb=new StringBuilder();
        for (int i=0; i<count; i++) {
            sb.append(Character.toString((char)((int)rand(97,122))));
        }
        return sb.toString();
    }

    /**
     * Round a double number to given precision
     * @param number The number to be rounded
     * @param precisionInt Precision - digits after the point
     * @return The rounded number
     */
    public static final double roundToPrec(double number, int precisionInt) {
        double decNum=Math.pow(10d,(double)precisionInt);
        double v = (double) (Math.round(number * decNum)) / decNum;
        return v;
    }

    /**
     * Round a float number to given precision
     * @param number The number to be rounded
     * @param precisionInt Precision - digits after the point
     * @return The rounded number
     */
    public static final float roundToPrec(float number, int precisionInt) {
        float decNum=(float)Math.pow(10d,(double)precisionInt);
        float v=(float)(Math.round(number*decNum))/decNum;
//        System.out.println("roundToPrec float="+v);
        return v;
    }

    /**
     * Builds a string in human readable format for a time that has been passed given by seconds
     * @param seconds Seconds that needs to be converted to human readable format
     * @return Human readable format string
     */
    public static final String secondsToReadable(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) -
                TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) -
                TimeUnit.DAYS.toMinutes(day) -
                TimeUnit.HOURS.toMinutes(hours);
        long second = TimeUnit.SECONDS.toSeconds(seconds) -
                TimeUnit.DAYS.toSeconds(day) -
                TimeUnit.HOURS.toSeconds(hours) -
                TimeUnit.MINUTES.toSeconds(minute);
        return "" + day + " days " + hours + " hrs " + minute + " mts & " + second + " s";
    }
}

