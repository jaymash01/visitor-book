package com.jaymash.visitorbook.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class NumberUtils {

    public static <T extends Number> String numberFormat(T number) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        ((DecimalFormat) nf).applyPattern("###,###");
        return nf.format(number);
    }

    public static <T extends Number> String numberFormat(T number, int decimals) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        StringBuilder builder = new StringBuilder();
        int i = 0;

        for (; i < decimals; i++) {
            builder.append("#");
        }

        ((DecimalFormat) nf).applyPattern("###,###." + builder.toString());
        return nf.format(number);
    }

    public static <T extends Number> double round(T number, int decimals) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        StringBuilder builder = new StringBuilder();
        int i = 0;

        for (; i < decimals; i++) {
            builder.append("#");
        }

        ((DecimalFormat) nf).applyPattern("###." + builder.toString());
        return Double.parseDouble(nf.format(number));
    }
}
