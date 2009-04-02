/*
 * NumberTools.java
 *
 * Created on September 10, 2002, 10:54 PM
 */

package com.infusion.util;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author eric
 */

public class NumberUtils {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    public static Map<Integer, String> ones = new HashMap<Integer, String>();
    public static Map<Integer, String> postfix = new HashMap<Integer, String>();
    public static Map<Integer, String> teens = new HashMap<Integer, String>();
    public static Map<Integer, String> tens = new HashMap<Integer, String>();

// ========================================================================================================================
//    Static Methods
// ========================================================================================================================

    static {
        postfix.put(1, "thousand");
        postfix.put(2, "million");
        postfix.put(3, "billion");
        postfix.put(4, "trillion");
        tens.put(2, "twenty");
        tens.put(3, "thirty");
        tens.put(4, "forty");
        tens.put(5, "fifty");
        tens.put(6, "sixty");
        tens.put(7, "seventy");
        tens.put(8, "eighty");
        tens.put(9, "ninety");
        ones.put(1, "one");
        ones.put(2, "two");
        ones.put(3, "three");
        ones.put(4, "four");
        ones.put(5, "five");
        ones.put(6, "six");
        ones.put(7, "seven");
        ones.put(8, "eight");
        ones.put(9, "nine");
        teens.put(0, "ten");
        teens.put(1, "eleven");
        teens.put(2, "twelve");
        teens.put(3, "thirteen");
        teens.put(4, "fourteen");
        teens.put(5, "fifteen");
        teens.put(6, "sixteen");
        teens.put(7, "seventeen");
        teens.put(8, "eighteen");
        teens.put(9, "nineteen");
    }

    /**
     * Given an integer returns the number with the appropriate appreviation after it.
     * (ie 1st, 2nd, 24th)
     *
     * @param number some number
     * @return string with number and appreviation
     */
    public static String Nd(int number) {
        String n = String.valueOf(number);
        char c = n.charAt(n.length() - 1);
        if (n.length() > 1) {
            char t = n.charAt(n.length() - 2);
            if (t == '1') {
                return number + "th";
            }
        }
        switch (c) {
            case'1':
                return number + "st";
            case'2':
                return number + "nd";
            case'3':
                return number + "rd";
            default:
                return number + "th";
        }
    }

    public static int Nz(int i, int x) {
        if (i <= 0)
            return x;
        else
            return i;
    }

    public static double Nz(double i, double x) {
        if (i <= 0)
            return x;
        else
            return i;
    }

    public static String ZeroNz(double amount) {
        if (amount == 0) {
            return "";
        }
        return "" + amount;
    }

    public static String dollarDouble(double dbl) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(dbl);
    }

    /**
     * Formats a double to two decimal places
     *
     * @param d Double to format
     * @return Formatted double
     */
    public static String doubleFormat(Double d) {
        return doubleFormat(d, 0);
    }

    public static String doubleFormat(double d) {
        return doubleFormat(new Double(d));
    }

    public static String doubleFormat(Double d, int minFraction) {
        if (d == null) {
            return null;
        }
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        if (minFraction > 0) {
            nf.setMinimumFractionDigits(minFraction);
        }
        return nf.format(d);
    }

    public static String doubleFormat(double d, int minFraction) {
        return doubleFormat(new Double(d), minFraction);
    }

    public static <X extends Number> X first(X... args) {
        X curr = null;
        for (X number : args) {
            if (number != null) {
                curr = number;
                if (number.doubleValue() > 0) {
                    return number;
                }
            }
        }
        return curr;
    }

    public static String formatDollar(Double d, Locale locale) {
        if (d == null) {
            return null;
        }

        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
        String f = df.format(d);
        f = f.replaceAll(" ", "");// for some reason South African Rands add a space

        if (f.indexOf("(") > -1) {
            f = "-" + f.replaceAll("\\(|\\)", "");
        }

        // todo: find a better solution
        // broswers don't like it for some reason - unless you do this, the £ shows as a ?
//        if (f.contains("£")) {
//            f = f.replace("£", "&pound;");
//        }

        return f;
    }

    /**
     * Formats an amount depending on the locale. Gets rid of trailing decimals only if they are zeros and the flag is
     * set.
     *
     * @param amount double amount
     * @param locale country format to display the amount in
     * @param includeZeros whether to keep trailing zeros in the decimal or not (ie .00)
     * @return currency string of amount given in the format desired by a locale
     */
    public static String formatCurrency(double amount, Locale locale, boolean includeZeros) {
        String currencyString = formatDollar(amount, locale);
        if (!includeZeros) {
            currencyString = currencyString.replaceAll("(\\.0$)|(\\.00$)", "");
        }
        return currencyString;
    }

    public static String formatDollar(double d, Locale locale) {
        return formatDollar(new Double(d), locale);
    }

    /**
     * Returns a double formatted with the cents
     *
     * @param dbl
     * @return
     * @param locale where user is located
     */
    public static String formatDollarInt(double dbl, Locale locale) {
        return formatDollar((int) dbl, locale);
    }

    public static String formatPercent(double v) {
        return formatPercent(new Double(v));
    }

    public static String formatPercent(Double v) {
        if (v == null) {
            return null;
        }
        return doubleFormat(v * 100) + "%";
    }

    public static String formatPercentWholeNumber(Double v) {
        if (v == null) {
            return null;
        }
        return doubleFormat(v) + "%";
    }

    public static String formatPercentWholeNumber(double v) {
        return formatPercentWholeNumber(new Double(v));
    }

    /**
     * Given two numbers, it will give you the percentage difference between the two.  FOr example, if I passed:
     * <p/>
     * 1 and 4, it would return 75 <BR>
     * 2 and 3, it would return 33 <BR>
     *
     * @param i1
     * @param i2
     * @return
     */
    public static int getPercentDiff(int i1, int i2) {
        return 100 * (i1 - i2) / Math.max(i1, i2);
    }

    /**
     * Formats an integer
     *
     * @param i Integer to format
     * @return
     */
    public static String intFormat(int i) {
        return NumberFormat.getInstance().format(i);
    }

    public static boolean is(double v) {
        return v != 0;
    }

    public static <X extends Number> X min(Set<X> args) {
        if (args == null){
            return null;
        }
        X curr = null;
        for (X x : args) {
            curr = min(curr, x);
        }
        return curr;
    }

    public static <X extends Number> X min(X... args) {
        X curr = null;
        for (X x : args) {
            curr = min(curr, x);
        }
        return curr;
    }

    public static <X extends Number> X min(X a, X b) {
        if (a == null) return b;
        if (b == null) return a;
        if (a.doubleValue() > b.doubleValue()) {
            return b;
        } else {
            return a;
        }
    }

    /**
     * calculate the absolute value of a double
     *
     * @param d double
     * @return absolute value of d
     */
    public static double abs(Double d) {
        if (d < 0) {
            return -1 * d;
        } else {
            return d;
        }
    }

    /**
     * Parses a boolean from a string.
     *
     * @param str
     * @return
     */
    public static boolean parseBoolean(String str) {
        if (
                "1".equalsIgnoreCase(str)
                        ||
                        "yes".equalsIgnoreCase(str)
                        ||
                        "true".equalsIgnoreCase(str)
                ) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Parses a double, accepts null values
     */
    public static double parseDouble(String str) {
        if (!StringUtils.is(str)) {
            return 0.0;
        } else {
            String stripString = StringUtils.stripNonNumber(str);
            if (!StringUtils.is(stripString)) {
                return 0.0;
            } else {
                return Double.parseDouble(stripString);
            }
        }
    }

    /**
     * Parses a double, accepts null values
     */
    public static float parseFloat(String str) {
        if (!StringUtils.is(str)) {
            return 0.0f;
        } else {
            String stripString = StringUtils.stripNonNumber(str);
            if (!StringUtils.is(stripString)) {
                return 0.0f;
            } else {
                return Float.parseFloat(stripString);
            }
        }
    }

    /**
     * Parses an int, accepts null values
     *
     * @param str input string to parse
     * @return integer version of string. Returns 0 if the string is null
     */
    public static int parseInt(String str) {
        if (str == null) return 0;

        //Check for simple y/n
        if (str.length() == 1 && Character.isLetter(str.charAt(0))) {
            return "y".equalsIgnoreCase(str) ? 1 : 0;
        }
        // check if the number is in scientific notation
//        if (str.contains("E") && str.charAt(1) == '.') {
//            str = str.substring(0, str.indexOf("E")).replaceAll("\\.", "");
//        }
        str = StringUtils.stripNonNumber(str);
        if (!StringUtils.is(str))
            return 0;
        else
            return new Double(str).intValue();
    }

     public static long parseLong(String str) {
        if (str == null) return 0;

        str = StringUtils.stripNonNumber(str);
        if (!StringUtils.is(str)) {
            return 0;
        } else {
            return new Double(str).longValue();
        }
    }

    /**
     * Returns a random number between 0 and the int you pass
     *
     * @param ceil Max value for the random number
     * @return Random number
     */
    public static int randomInt(int ceil) {
        Random m = new Random(Calendar.getInstance().getTimeInMillis());
        Random e = new Random(m.nextInt());
        return (int) (e.nextFloat() * (ceil + 1));
    }

    public static int randomInt(int floor, int ceil) {
        return randomInt(Calendar.getInstance().getTimeInMillis(), floor, ceil);
    }

    public static int randomInt(long seed, int floor, int ceil) {
        Random m = new Random(seed);
        Random e = new Random(m.nextInt());
        return (int) (e.nextFloat() * (ceil - floor + 1)) + floor;
    }

    public static double round(double d) {
        return round(d, 2);
    }

    public static double round(double d, int places) {
        BigDecimal decimal = new BigDecimal(d);
        return decimal.divide(new BigDecimal(1), places, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    public static double weightedValue(double total, int weightPercent) {
        return total * ((double) weightPercent / 100);
    }

    public static double discountedValue(double total, int discountPercent) {
        return total * ((double)(100 - discountPercent) / 100);
    }

// ========================================================================================================================
//    Constructors
// ========================================================================================================================

    /**
     * Creates a new instance of NumberTools
     */
    public NumberUtils() {
    }

}