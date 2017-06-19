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

    public static final String JSString(String s) {
        return StringEscapeUtils.escapeJavaScript(s);
    }

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

    public static final long rand(long lowerBound,long upperBound) {
        return lowerBound + (long) ( Math.random()*(upperBound -lowerBound) + 0.5D );
    }

    public static final String generateRandomLetters(int count) {
        StringBuilder sb=new StringBuilder();
        for (int i=0; i<count; i++) {
            sb.append(Character.toString((char)((int)rand(97,122))));
        }
        return sb.toString();
    }

    public static final double roundToPrec(double number, int precisionInt) {
        double decNum=Math.pow(10d,(double)precisionInt);
        double v = (double) (Math.round(number * decNum)) / decNum;
//        System.out.println("roundToPrec double="+v);
        return v;
    }

    public static final float roundToPrec(float number, int precisionInt) {
        float decNum=(float)Math.pow(10d,(double)precisionInt);
        float v=(float)(Math.round(number*decNum))/decNum;
//        System.out.println("roundToPrec float="+v);
        return v;
    }

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

