/*
 * DateUtils.java
 *
 * Created on August 26, 2002, 3:18 PM
 */

package com.infusion.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * All static stuff helping deal with dates better
 *
 * @author eric
 */
public class DateUtils {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    public static final String CLASS_DATE = "Date (03/02/2004)";
    public static final String CLASS_DATEDAYNAME = "Date (Friday, March 08, 2004)";
    public static final String CLASS_DATENODAYNAME = "Date (March 08, 2004)";
    public static final String CLASS_DATEYEARFIRST = "Date (2004-03-02)";

    /**
     * 1 day, represented as millis
     */
    public static final long DAY = (1000 * 60 * 60) * 24;

    /**
     * The default when the workday ends (5PM)
     */
    public static final int END_WORKDAY = 17;

    /**
     * 1 hour, represented as millis
     */
    public static final long HOUR = (1000 * 60) * 60;

    /**
     * 1 minute, represented as millis
     */
    public static final long MINUTE = 1000 * 60;

    /**
     * 1 second, represented as millis
     */
    public static final long SECOND = 1000;

    /**
     * The default when the workday starts (7AM)
     */
    public static final int START_WORKDAY = 7;

    /**
     * 1 year, represented as millis
     */
    public static final long YEAR = DAY * 365;

    public static String LOCALE;

    public static String MMDD_DASH_FORMAT = "MM-dd";

    public static String MMDD_SLASH_FORMAT = "MM/dd";

    /**
     * Map of years ending at this year and starting one hundred years earlier.
     */
    public static Map<String, String> birthYears;

    public static Map ccStartYears;

    /**
     * Map of years for credit cards (won't show last year)
     */
    public static Map ccyears;

    /**
     * A map of numbers 1 to 31
     */
    public static Map<String, String> daysOfMonth;

    /**
     * Map of days of the week, number as key, name as value
     */
    public static Map<String, String> daysOfWeek;

    public static Map<String, String> hourMap = new LinkedHashMap<String, String>();

    /**
     * Map of months, using the number as the key, month name as the value
     */
    public static Map<String, String> monthNames;

    /**
     * Map of months, using month number as both key and value
     */
    public static Map months;

    /**
     * Map of years, starting with the last year and going 10 years in the future
     */
    public static Map years;

// ========================================================================================================================
//    Static Methods
// ========================================================================================================================

    static {
        months = CollectionUtil.StringToMap("01,02,03,04,05,06,07,08,09,10,11,12", ",");
        int currYear = Calendar.getInstance().get(Calendar.YEAR);
        years = CollectionUtil.NumberMap(currYear - 1, 11);// will go back two years and then go 9 years in the future
        birthYears = CollectionUtil.NumberMap(currYear - 99, 101);//hundred years of birthdays
        ccyears = CollectionUtil.NumberMap(currYear + 1, 15);   //credit card years.
        ccStartYears = CollectionUtil.NumberMap(currYear - 9, 12);   //credit card start years.

        daysOfWeek = new LinkedHashMap<String, String>();
        daysOfWeek.put("1", "Sunday");
        daysOfWeek.put("2", "Monday");
        daysOfWeek.put("3", "Tuesday");
        daysOfWeek.put("4", "Wednesday");
        daysOfWeek.put("5", "Thursday");
        daysOfWeek.put("6", "Friday");
        daysOfWeek.put("7", "Saturday");

        daysOfMonth = new LinkedHashMap<String, String>();
        for (int i = 1; i <= 31; i++) {
            daysOfMonth.put("" + i, "" + i);
        }

        monthNames = new LinkedHashMap<String, String>();
        for (int i = 1; i < 13; i++) {
            monthNames.put(i + "", DateUtils.getMonth(i - 1));
        }

        for (int i = 0; i < 24; i++) {
            int h = i;
            String ampm = "AM";

            if (h > 11) {
                ampm = "PM";
            }
            if (h == 0) {
                h = 12;
            }
            if (h > 12) {
                h = h - 12;
            }


            hourMap.put("" + i, h + " " + ampm);
        }
    }

    /**
     * Creates a date as of x days ago
     *
     * @param i
     * @return
     */
    public static Date DaysAgo(int i) {
        Calendar today = TodayCal();
        today.add(Calendar.DATE, -i);
        return today.getTime();
    }

    /**
     * Returns today (end of day)
     */
    public static Calendar EndOfTodayCal() {
        return TodayTime(23, 59, 59);
    }

    /**
     * Returns today (without the time)
     */
    public static Calendar TodayCal() {
        return TodayTime(0, 0, 0);
    }

    /**
     * Returns today (set to a particular time)
     */
    public static Calendar TodayTime(int hour, int minute, int second) {
        return setTime(new GregorianCalendar(), hour, minute, second);
    }

    /**
     * A null safe check to whether dt is after dt2.
     *
     * @return if dt2 is after dt
     */
    public static boolean after(Date dt, Date dt2) {
        return !(dt == null || dt2 == null) && dt.after(dt2);
    }

    public static Calendar ago(int cycle, int number) {
        return from(TodayTime(0, 0, 0), cycle, number);
    }

    /**
     * Date one comes before date two.
     *
     * @param dt  date one
     * @param dt2 date two
     * @return true if neither are null and the first is before the second
     */
    public static boolean before(Date dt, Date dt2) {
        return !(dt == null || dt2 == null) && dt.before(dt2);

    }

    /**
     * Takes in a Calendar and returns a Date.
     *
     * @return Date
     */
    public static Date calendarToDate(Calendar cal) {
        if (cal == null) {
            return null;
        }
        return cal.getTime();
    }

    /**
     * Compares 2 cal1 and cal2 based on the comparator passed in.
     * comparator can be one of the following strings: "<=", "==", ">=", "<", ">";
     * If comparator is any other string, false is returned.
     * If either cal1 OR cal2 is null, false is returned.
     */
    public static boolean compare(Calendar cal1, String comparator, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            return false;
        }

        if ("==".equals(comparator)) {
            return cal1.compareTo(cal2) == 0;
        }

        if ("<".equals(comparator)) {
            return cal1.compareTo(cal2) < 0;
        }

        if (">".equals(comparator)) {
            return cal1.compareTo(cal2) > 0;
        }

        if ("<=".equals(comparator)) {
            return cal1.compareTo(cal2) <= 0;
        }

        if (">=".equals(comparator)) {
            return cal1.compareTo(cal2) >= 0;
        }

        return false;
    }


    /**
     * Compares 2 date1 and date2 based on the comparator passed in.
     * comparator can be one of the following strings: "<=", "==", ">=", "<", ">";
     * If comparator is any other string, false is returned.
     * If either cal1 OR cal2 is null, false is returned.
     *
     * @param date1
     * @param comparator
     * @param date2
     * @return
     */
    public static boolean compare(Date date1, String comparator, Date date2) {
        return compare(dateToCal(date1), comparator, dateToCal(date2));
    }

    /**
     * A nullsafe that takes in a string and formats it as yyyy-MM-dd  ex: 2005-01-31
     */
    public static String convertToMySQL(String str) throws Exception {
        if (!StringUtils.is(str)) {
            return null;
        }
        return formatDate(strToDate(str), "yyyy-MM-dd");
    }

    /**
     * Takes in a calendar and returns a date formatted with dashes as MM-dd-yyyy ex: 01-31-2005
     *
     * @return Formatted date
     */
    public static String dashDate(Calendar cal) {
        return formatDate(calendarToDate(cal), MMDD_DASH_FORMAT + "-yyyy");
    }

    /**
     * Takes in a date and returns a date formatted with dashes as MM-dd-yyyy ex: 01-31-2005
     *
     * @return Formatted date
     */
    public static String dashDate(Date dt) {
        return formatDate(dt, MMDD_DASH_FORMAT + "-yyyy");
    }

    /**
     * Given a number of seconds, creates an int[] detailing the
     * months, days, and hours difference as well.
     * <p/>
     * 0: days<br>
     * 1: hours<br>
     * 2:minutes<br>
     * 3:seconds<br>
     *
     * @return int array
     */
    public static int[] dateArrayFromSeconds(int inSeconds) {
        int days = (int) (inSeconds / (60 * 60 * 24));
        inSeconds -= (days * 60 * 60 * 24);
        int hours = (int) (inSeconds / (60 * 60));
        inSeconds -= (hours * 60 * 60);
        int minutes = (int) (inSeconds / 60);
        inSeconds -= (minutes * 60);
        return new int[]{days, hours, minutes, inSeconds};
    }

    /**
     * Converts a date to a calendar object
     */
    public static Calendar dateToCal(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(dt);
        return cal;
    }

    /**
     * Returns the number of seconds difference between two dates
     *
     * @return Seconds difference
     */
    public static int diff(Date dt1, Date dt2) {
        int[] i = diffMin(dt1, dt2);
        return (i[3]) + (i[2] * 60) + (i[1] * 60 * 60) + (i[0] * 60 * 60 * 24);
    }

    /**
     * Returns the number of days difference between two dates
     * DUPLICATE METHOD?
     *
     * @return Number of days difference
     */
    public static int diffDay(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return (int) ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    /**
     * Diffs two dates - see other method
     *
     * @return Number of days difference between two dates
     * @see #diffMin(Calendar,Calendar)
     */
    public static int[] diffMin(Date start, Date end) {
        if (start == null || end == null) {
            return new int[4];
        }
        return diffMin(DateUtils.dateToCal(start), DateUtils.dateToCal(end));
    }

    /**
     * Returns an int array detailing the difference between to Calendars <br>
     * 0: days<br>
     * 1: hours<br>
     * 2:minutes<br>
     * 3:seconds<br>
     */
    public static int[] diffMin(Calendar start, Calendar end) {
        double numMinutes = ((double) (end.getTimeInMillis() - start.getTimeInMillis()) / 60000);
        int days = (int) (numMinutes / 1440);
        numMinutes -= days * 1440;
        int hours = (int) (numMinutes / 60);
        numMinutes -= hours * 60;
        int minutes = (int) (numMinutes);
        numMinutes -= minutes;
        int seconds = (int) (numMinutes * 60);
        return new int[]{days, hours, minutes, seconds};
    }

    /**
     * Same as diffMin, but returns diffed values in a map for easier access:
     * <p/>
     * The keys are as follows:
     * <p/>
     * day<BR>
     * hour<BR>
     * minute<BR>
     * second<BR>
     *
     * @return Map of diffed values
     * @see #diffMin(Calendar,Calendar)
     */
    public static Map diffMinMap(Date startDate, Date endDate) {
        int[] diff = diffMin(startDate, endDate);
        Map map = new HashMap();
        map.put("day", new Integer(diff[0]));
        map.put("hour", new Integer(diff[1]));
        map.put("minute", new Integer(diff[2]));
        map.put("second", new Integer(diff[3]));
        return map;
    }

    /**
     * Returns the number of days that exists between two calendars
     *
     * @return Number of days between start and end
     */
    public static int diffMonth(Calendar start, Calendar end) {
        return (int) (end.get(Calendar.MONTH) - start.get(Calendar.MONTH) + (12 * (end.get(Calendar.YEAR) - start.get(Calendar.YEAR))));
    }

    /**
     * Returns the number of months difference between two dates
     *
     * @return difference between two months
     */
    public static int diffMonth(Date start, Date end) {
        return diffMonth(dateToCal(start), dateToCal(end));
    }

    /**
     * Diffs two dates and return a formatted String
     *
     * @return Formatted diff
     * @see #diffMin(Date,Date)
     * @see #formatArray(int[])
     */
    public static String diffString(Date startTime, Date endTime) {
        return DateUtils.formatArray(DateUtils.diffMin(startTime, endTime));
    }

    /**
     * Number of weeks difference between two dates
     *
     * @return difference in weeks
     */
    public static int diffWeeks(Date start, Date end) {
        if (start == null || end == null) {
            return -1;
        }
        return (int) ((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24 * 7) + 1);
    }

    /**
     * Number of weeks difference between two dates
     *
     * @return difference in weeks
     */
    public static int diffWeeks(Calendar start, Calendar end) {
        return diffWeeks(start.getTime(), end.getTime());
    }

    /**
     * Diffs the number of working seconds between two dates (takes into consideration a 7-5
     * workday)
     */
    public static int diffWorkingSeconds(Calendar start, Calendar end) {
        if (start == null || end == null) {
            return 0;
        }

        if (end.before(start)) {
            Calendar newStart = end;
            Calendar newEnd = start;
            end = newEnd;
            start = newStart;
        }

        roundForward(start);
        roundBack(end);

        if (!end.after(start)) {
            return 0;
        }

        int startDayOfWeek = start.get(Calendar.DAY_OF_WEEK);
        int endDayOfWeek = end.get(Calendar.DAY_OF_WEEK);

        int weeksDiff = 0;
        int daysDiff = end.get(Calendar.DAY_OF_YEAR) - start.get(Calendar.DAY_OF_YEAR);
        if (daysDiff < 0) {
            weeksDiff += 52;
            daysDiff = Math.abs(daysDiff);
        }
        weeksDiff += daysDiff / 7;
        if (endDayOfWeek < startDayOfWeek) {
            weeksDiff++;
        }

        int secondsDiff = diff(start.getTime(), end.getTime());
        secondsDiff -= (((24 - getWorkDayLength()) * 60 * 60 * daysDiff) + ((2 * getWorkDayLength()) * weeksDiff * 60 * 60));
        return secondsDiff;
    }

    /**
     * Takes in a date and returns a date formatted as yyyyMMdd ex: 20050131
     *
     * @return Formatted date
     */
    public static String dumpDate(Date dt) {
        return formatDate(dt, "yyyyMMdd");
    }

    /**
     * Takes in a calendar and returns a calendar formatted as yyyyMMdd ex: 20050131
     *
     * @return Formatted Date
     */
    public static String dumpDate(Calendar cal) {
        return formatDate(cal, "yyyyMMdd");
    }

    /**
     * Takes in a date and returns a date formatted as yyyyMMddhhmmss
     *
     * @return Formatted date
     */
    public static String dumpDateTime(Date dt) {
        return formatDate(dt, "yyyyMMddhhmmss");
    }

    /**
     * Returns a readable String based on diffMin(Calendar start,Calendar end)<BR>
     * i.e. 3d 4h 2m 1s
     *
     * @see #diffMin(java.util.Calendar,java.util.Calendar)
     */
    public static String formatArray(int[] i) {
        String results = "";
        if (i[0] > 0) {
            results += i[0] + "d ";
        }
        if (i[1] > 0) {
            results += i[1] + "h ";
        }
        if (i[2] > 0) {
            results += i[2] + "m ";
        }
        if (i[3] > 0) {
            results += i[3] + "s ";
        }
        if (results.length() == 0) {
            return "<1s";
        }
        return results;
    }

    /**
     * Formats date (dt) given format String (strFormat)
     */
    public static String formatDate(Date dt, String strFormat) {
        if (dt == null) {
            return null;
        }
        try {
            if (strFormat == null) {
                if ("uk".equalsIgnoreCase(LOCALE)) {
                    strFormat = "dd/MM/yyyy h:mm a";
                } else {
                    strFormat = "M/dd/yyyy h:mm a";
                }
            }
            return new SimpleDateFormat(strFormat).format(dt);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Formats date (dt) given format String (strFormat)
     */
    public static String formatDate(Calendar cal, String format) {
        if (cal == null) {
            return null;
        }
        return formatDate(cal.getTime(), format);
    }

    /**
     * Formats a date (as a string) given a certain format
     *
     * @param format Date format
     * @return Formatted date
     * @see #formatDateTime(Date)
     * @see DateFormat
     */
    public static String formatDate(String dateString, String format) {
        if (dateString == null) {
            return null;
        }
        Date date = parseDate(dateString);
        return formatDate(date, format);
    }

    /**
     * Formats a date and time int eh format:<BR>
     * 12/22/2003 11:24PM
     * <p/>
     * Won't print a time if the time = 12:00AM
     */
    public static String formatDateTime(Date sentDate) {
        if (sentDate == null) {
            return null;
        }
        StringBuilder date = new StringBuilder();
        date.append(slashDate(sentDate));
        String time = formatTime(sentDate);
        if (!"12:00 AM".equalsIgnoreCase(time)) {
            date.append(" ").append(time);
        }
        return date.toString();
    }

    public static Date beginningOfDay(Date dt) {
        Calendar cal = dateToCal(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return calendarToDate(cal);
    }

    public static Date endOfDay(Date dt) {
        Calendar cal = dateToCal(dt);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return calendarToDate(cal);
    }

    /**
     * Formats a date for mysql, but sets teh time for the end of teh day
     *
     * @return end of day sql for mysql
     */
    public static String formatForMySQLBeginningOfDay(Date dt) {
        return formatForMysql(beginningOfDay(dt));
    }

    /**
     * Formats a date for mysql, but sets teh time for the end of teh day
     *
     * @return end of day sql for mysql
     */
    public static String formatForMySQLEndOfDay(Date dt) {
        return formatForMysql(endOfDay(dt));
    }

    /**
     * Returns a calendar formatted for MySQL
     */
    public static String formatForMysql(Calendar cal) {
        return formatDate(cal, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Formats a date for MySQL
     */
    public static String formatForMysql(Date dt) {
        return formatDate(dt, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * Returns a date formatted as h:mm a  ex: 10:23 AM/PM
     *
     * @return Formatted date
     */
    public static String formatTime(Date dt) {
        return formatTime(dt, false);
    }

    /**
     * Returns a date formatted as h:mm a  ex: 10:23 AM/PM
     *
     * @return Formatted date
     */
    private static String formatTime(Date dt, boolean seconds) {
        String format = seconds? "h:mm:ss a" : "h:mm a";
        return formatDate(dt, format);
    }

    /**
     * Takes in a calendar and returns a string with that calendars time
     *
     * @return Formatted string
     */
    public static String formatTime(Calendar cal) {
        return formatTime(cal, false);
    }

    /**
     * Takes in a calendar and returns a string with that calendars time
     *
     * @return Formatted string
     */
    private static String formatTime(Calendar cal, boolean seconds) {
        return formatTime(cal.getTime(), seconds);
    }

    /**
     * Returns a calendar that has been modified
     *
     * @param start Calendar to be modified
     * @param cycle Attribute of calendar to be modified
     * @param number changes the attribute of calendar to this number
     * @return calendar modified
     */
    public static Calendar from(Calendar start, int cycle, int number) {
        Calendar work = (Calendar) start.clone();
        work.add(cycle, number);
        return work;
    }

    /**
     * Returns the name of the day of the week, given an int (Sunday==1)
     */
    public static String getDayOfWeek(int i) {
        switch (i) {
            case 1:
                return "Sunday";
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
        }
        return "N/A";
    }

    /**
     * Returns the dayofyear and the year for a given date
     * <p/>
     * ie.  The 230th day of the year in 2005 -> 2005230
     */
    public static int getDayYear(Calendar date) {
        return (date.get(Calendar.YEAR) * 1000) + date.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Returns the dayofyear and the year for a given date
     * <p/>
     * ie.  The 230th day of the year in 2005 -> 2005230
     *
     * @see #getDayYear(Calendar)
     */
    public static int getDayYear(Date date) {
        return getDayYear(dateToCal(date));
    }

    /**
     * Converts a calendar to the last day of the month
     */
    public static Calendar getEndOfMonth(Calendar cal) {
        cal.add(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH));
        return cal;
    }

    /**
     * Moves a calendar to the end of the week
     *
     * @return calendar passed in, moved to the end of the week
     */
    public static Calendar getEndOfWeek(Calendar cal) {
        cal.add(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK) - cal.get(Calendar.DAY_OF_WEEK));
        return cal;
    }

    /**
     * Returns the name of the month, given an int (January==0)
     */
    public static String getMonth(int i) {
        switch (i) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "Error";
    }

    /**
     * Returns a map of months
     *
     * @see #monthNames
     */
    public static Map<String, String> getMonthsMap() {
        return monthNames;
    }

    /**
     * Converts a calendar to the first day of the month
     */
    public static Calendar getStartOfMonth(Calendar scal) {
        scal.add(Calendar.DAY_OF_MONTH, -(scal.get(Calendar.DAY_OF_MONTH) - 1));
        return scal;
    }

    /**
     * Moves a calendar to the start of the week
     *
     * @return calendar passed in, moved to the start of the week
     */
    public static Calendar getStartOfWeek(Calendar cal) {
        cal.add(Calendar.DAY_OF_WEEK, -(cal.get(Calendar.DAY_OF_WEEK) - 1));
        return cal;
    }

    /**
     * Converts a calendar to the first day of the year
     */
    public static Calendar getStartOfYear(Calendar scal) {
        scal.add(Calendar.DAY_OF_YEAR, -(scal.get(Calendar.DAY_OF_YEAR) - 1));
        return scal;
    }

    /**
     * Returns the length of a work day (in hours)
     *
     * @see #END_WORKDAY
     * @see #START_WORKDAY
     */
    public static int getWorkDayLength() {
        return END_WORKDAY - START_WORKDAY;
    }

    /**
     * Given a start and end time
     *
     * @param startTime start hour
     * @param endTime   end hour
     * @return returns a map of times in fifteen minute intervals from a starting hour to an end hour
     */
    public static Map<String, String> getTimeMap(int startTime, int endTime) {
        Map<String, String> hours = new LinkedHashMap<String, String>();
        int currHour;
        hours.put("00:00", "Choose time");
        for (int i = startTime; i <= endTime; i++) {
            currHour = (i > 12) ? i - 12 : i;
            String strHour = currHour > 9 ? "" + currHour : "0" + currHour;
            String strRealHour = i > 9 ? "" + i : "0" + i;
            String ap = (i >= 12) ? "PM" : "AM";
            if(strHour.compareTo("00") == 0) {
                strHour = "12";
            }
            hours.put(strRealHour + ":00", strHour + ":00 " + ap);
            hours.put(strRealHour + ":15", strHour + ":15 " + ap);
            hours.put(strRealHour + ":30", strHour + ":30 " + ap);
            hours.put(strRealHour + ":45", strHour + ":45 " + ap);
        }
        return hours;
    }

    /**
     * Determines whether two dates are the same (is null-safe)
     *
     * @return true if they are different, false if they are the same date
     */
    public static boolean haveChanged(Date oldActionDate, Date newActionDate) {
        if (oldActionDate == null && newActionDate == null) {
            return false;
        }
        if (oldActionDate == null && newActionDate != null) {
            return true;
        }
        if (oldActionDate != null && newActionDate == null) {
            return true;
        }

        return oldActionDate.before(newActionDate) || oldActionDate.after(newActionDate);
    }

    /**
     * Determines whether or not the date is after the start of the work day
     *
     * @see #END_WORKDAY
     */
    public static boolean isAfterWorkTime(Calendar start) {
        final int hourOfDay = start.get(Calendar.HOUR_OF_DAY);
        return hourOfDay >= END_WORKDAY;
    }

    /**
     * Determines whether or not the date is before the start of the work day
     *
     * @see #START_WORKDAY
     */
    public static boolean isBeforeWorkTime(Calendar start) {
        final int hourOfDay = start.get(Calendar.HOUR_OF_DAY);
        return hourOfDay < START_WORKDAY;
    }

    /**
     * Determines whether or not the date falls on a work day (Mon-Fri)
     */
    public static boolean isInWorkDay(Calendar start) {
        final int dayOfWeek = start.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek > 1 && dayOfWeek < 7;
    }

    /**
     * Determines whether or not the time of the date is within the work day
     *
     * @see #START_WORKDAY
     * @see #END_WORKDAY
     * @see #isBeforeWorkTime(Calendar)
     * @see #isAfterWorkTime(Calendar)
     */
    public static boolean isInWorkTime(Calendar start) {
        return !isBeforeWorkTime(start) && !isAfterWorkTime(start);
    }

    /**
     * Determines of the given calendar is for today's date
     */
    public static boolean isToday(Calendar today) {
        Calendar realToday = new GregorianCalendar();
        return realToday.get(Calendar.YEAR) == today.get(Calendar.YEAR) && realToday.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * compare two dates and return whether or not the one is outside of the week of the other
     *
     * @param test     test this date
     * @param base     for the week including this date
     * @param weekends include weekends?
     * @return true or false
     */
    public static boolean isOutsideWeek(Date test, Date base, boolean weekends) {
        if ((test == null) || (base == null)) {
            return false;
        }

        Calendar compare = DateUtils.dateToCal(test);
        Calendar begin = DateUtils.dateToCal(base);
        Calendar end = DateUtils.dateToCal(base);

        if (weekends) {
            begin.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            end.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        } else {
            begin.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            end.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        }

        begin.set(Calendar.HOUR_OF_DAY, 1);
        compare.set(Calendar.HOUR_OF_DAY, 2);

        // before
        if (compare.before(begin)) {
            return true;
        }

        end.set(Calendar.HOUR_OF_DAY, 2);
        compare.set(Calendar.HOUR_OF_DAY, 1);

        // after
        return compare.after(end);
    }

    /**
     * prints out a pretty date.  ex: March 20, 2008
     *
     * @param date date
     * @return formatted date
     */
    public static String makePrettyDate(Date date) {
        return makePrettyDate(dateToCal(date));
    }

    /**
     * prints out a pretty date.  ex: March 20, 2008
     *
     * @param cal cal
     * @return formatted date
     */
    public static String makePrettyDate(Calendar cal) {
        if (cal == null) {
            return null;
        }
        StringBuilder tmp = new StringBuilder();
        tmp.append(getMonth(cal.get(Calendar.MONTH))).append(" ")
                .append(cal.get(Calendar.DAY_OF_MONTH)).append(", ")
                .append(cal.get(Calendar.YEAR));
        return tmp.toString();
    }


    public static String makePrettyDateTimeWithSeconds(Date dt) {
        if (dt == null) {
            return null;
        }
        return makePrettyDateTimeWithSeconds(dateToCal(dt));
    }

    public static String makePrettyDateTimeWithSeconds(Calendar cal) {
        if (cal == null) {
            return null;
        }
        try {
            return getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)) + ", " + makePrettyDateTimeNoDay(cal, true);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Prints out a pretty date.  ex: Sunday, December 8, 2002 15:51 PM
     *
     * @param cal Cal to be formatted
     * @return Formatted date
     */
    public static String makePrettyDateTime(Calendar cal) {
        if (cal == null) {
            return null;
        }
        try {
            return getDayOfWeek(cal.get(Calendar.DAY_OF_WEEK)) + ", " + makePrettyDateTimeNoDay(cal);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Prints out a pretty date.  ex: Sunday, December 8, 2002 15:51 PM
     */
    public static String makePrettyDateTime(Date dt) {
        if (dt == null) {
            return null;
        }
        return makePrettyDateTime(dateToCal(dt));
    }

    /**
     * Prints out a pretty date without the day printed out.  ex: December 8, 2002 15:51 PM
     */
    public static String makePrettyDateTimeNoDay(Calendar cal) {
        return makePrettyDateTimeNoDay(cal, false);
    }

    /**
     * Prints out a pretty date without the day printed out.  ex: December 8, 2002 15:51 PM
     */
    private static String makePrettyDateTimeNoDay(Calendar cal, boolean seconds) {
        if (cal == null) {
            return null;
        }
        StringBuilder tmp = new StringBuilder(makePrettyDate(cal));
        if (cal.get(Calendar.HOUR) != 0 || cal.get(Calendar.MINUTE) != 0) {
            tmp.append(" ").append(formatTime(cal, seconds));
        }
        return tmp.toString();
    }

    /**
     * Takes in a varargs of calendars and returns the one with the earliest date
     *
     * @param dates These values can be null
     * @return Calendar with the least date or null if no valid dates found
     */
    public static Calendar min(Calendar... dates) {
        Calendar least = null;
        for (Calendar calendar : dates) {
            if (calendar != null) {
                if (least == null) {
                    least = calendar;
                } else {
                    if (calendar.before(least)) {
                        least = calendar;
                    }
                }
            }
        }
        return least;
    }

    /**
     * Current date, formatted for mysql
     *
     * @return Formatted date
     */
    public static String mysqlNow() {
        return mysqlNow(false);
    }

    /**
     * Returns a String formatted for Mysql representing NOW()
     */
    public static String mysqlNow(boolean quotes) {
        if (quotes) {
            return StringUtils.SQLString(formatForMysql(Calendar.getInstance()));
        } else {
            return formatForMysql(Calendar.getInstance());
        }
    }

    /**
     * Takes in a date String in the format yyyy-MM-dd, parses it and returns it
     * as MM-dd-yyyy
     */
    public static String normalDate(String dt) {
        if (dt == null) {
            return null;
        }
        try {
            Date newDt = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
            return normalDate(newDt);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Returns a date String formatted MM-dd-yyy
     *
     * @return Formatted date
     */
    public static String normalDate(Date dt) {
        if (dt == null || dt.toString().equals("2001-01-01") || dt.toString().equals("1980-01-01")) {
            return "";
        }
        return new SimpleDateFormat(MMDD_DASH_FORMAT + "-yyyy").format(dt);
    }

    /**
     * Returns a date String formatted yyyy-MM-dd
     *
     * @return Formatted date
     */
    public static String normalDate2(Date dt) {
        if (dt == null || dt.toString().equals("2001-01-01") || dt.toString().equals("1980-01-01")) {
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(dt);
    }

    /**
     * Returns a string which pads the integer time with a 0 before it, if less than 10  ex: if time = 8 returns 08
     *
     * @return The padded time string
     */
    public static String paddedTime(int time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return "" + time;
        }
    }

    /**
     * Parses a calendar given a String
     *
     * @return parsed Calendar
     */
    public static Calendar parseCal(String str) throws DateParseException {
        return dateToCal(parseDate(str));
    }

    private static void buildDateFormats(String dateString, Set<StringBuilder> formats,
                                         String separator, String ddmmformat) {
        if (dateString.indexOf(separator) > 3) {
            formats.add(new StringBuilder().append("yyyy" + separator + ddmmformat));
        } else {
            int max = 0;

            //This is to figure out whether we are dealing with a 4 or 2 day year.
            //First, we try to split the string out by major separators (space and comma)
            String[] spaceParts = dateString.split(" ");
            for (String spacePart : spaceParts) {
                final String[] commaParts = spacePart.split(",");
                for (String commaPart : commaParts) {
                    //Count the number of separators to detect if we're looking at the date or the time
                    final int numSeparator = StringUtils.count(commaPart, separator.charAt(0));
                    if (numSeparator > 1) {
                        String[] dateParts = commaPart.split(separator);

                        for (String datePart : dateParts) {
                            max = Math.max(max, datePart.length());
                        }
                    }
                }
            }

            if(max == 0) {
                max = 4;
            }

            if (max > 2) {
                formats.add(new StringBuilder().append(ddmmformat).append(separator).append("yyyy"));
            } else {
                formats.add(new StringBuilder().append(ddmmformat).append(separator).append("yy"));
            }

        }
    }

    /**
     * convert a request parameter value array to a date
     * @param array array
     * @return date
     */
    public static Date parseDate(String[] array) {
        Date ret = null;
        if (array != null && array.length == 2) {
            ret = parseDate(array[0] + "," + array[1]);
        }
        return ret;
    }
    
    /**
     * Converts a date in the form of a String to a Date object. Checks for tokens such as '/', ':', and '-' to help
     * separate out the month, day, and year.
     *
     * @param str date string
     * @return parsed Date
     */
    public static Date parseDate(String str) {
        if (!StringUtils.is(str)) {
            return null;
        }

        Set<StringBuilder> formats = new LinkedHashSet<StringBuilder>();

        //Start with dates
        if (str.indexOf("/") > -1) {
            buildDateFormats(str, formats, "/", MMDD_SLASH_FORMAT);
        } else if (str.indexOf("-") > -1) {
            buildDateFormats(str, formats, "-", MMDD_DASH_FORMAT);
        } else {
            formats.add(new StringBuilder().append("MMM dd yyyy"));
            formats.add(new StringBuilder().append("MMM dd, yyyy"));
            formats.add(new StringBuilder().append("MMM dd, yyyy"));
            formats.add(new StringBuilder().append("yyyyMMdd"));
            formats.add(new StringBuilder().append("MMdd"));
            formats.add(new StringBuilder().append(""));
        }

        //This means we have 'time' of some sort
        int count = StringUtils.count(str, ':');
        if (count > 0) {
            if (str.indexOf(",") > -1) {
                appendFormats(formats, true, ",", " ");
            } else {
                appendFormats(formats, false, " ");
            }

            final int indexOfColon = str.indexOf(":");
            int amIndex = str.toLowerCase().indexOf("am");
            int pmIndex = str.toLowerCase().indexOf("pm");

            final boolean hasAMPM = amIndex > indexOfColon || pmIndex > indexOfColon;
            if (hasAMPM) {
                appendFormats(formats, false, "hh");
            } else {
                appendFormats(formats, false, "HH");
            }

            appendFormats(formats, false, ":mm");
            if (count > 1) {
                appendFormats(formats, false, ":ss");
            }

            if (hasAMPM) {
                appendFormats(formats, false, " aa");
            }
        }

        //This one was already in there
        formats.add(new StringBuilder().append("E MMM dd HH:mm:ss zzz yyyy"));
        Date parsed = null;
        formats:
        for (StringBuilder format : formats) {
            try {
                DateFormat dateFormat = new SimpleDateFormat(format.toString());
                parsed = dateFormat.parse(str);
                break formats;
            } catch (Exception e) {
                System.out.println("Unable to parse date: " + str + StringUtils.parent(format));
            }
        }
        return parsed;
    }

    /**
     * Used in the parseDate method - will append a format string to a list of
     * existing formats
     *
     * @param formats
     * @param toAppend
     */
    private static void appendFormats(Set<StringBuilder> formats, boolean create, String... toAppend
    ) {
        boolean thisCreate = create;
        Set<StringBuilder> toAdd = null;
        for (StringBuilder format : formats) {
            for (String itemToAppend : toAppend) {
                if (thisCreate) {
                    toAdd = new LinkedHashSet<StringBuilder>();
                    toAdd.add(new StringBuilder(format.toString()).append(itemToAppend));
                    thisCreate = false;
                } else {
                    format.append(itemToAppend);
                }
            }
        }
        if (create && toAdd != null) {
            formats.addAll(toAdd);
        }
    }


    public static Calendar parseYearMonth(String string) {
        if (string != null && string.indexOf("-") > -1) {
            String[] strings = string.split("-");
            int year = NumberUtils.parseInt(strings[0]);
            int month = NumberUtils.parseInt(strings[1]);

            return new GregorianCalendar(year, month - 1, 1, 0, 0);
        } else {
            return null;
        }
    }

    /**
     * Rounds a date backwards if it found to be before th start of a work day.
     * It will round it back to the end of the previous work day
     *
     * @see #START_WORKDAY
     * @see #END_WORKDAY
     * @see #roundForward(Calendar)
     */
    public static void roundBack(Calendar start) {
        if (!(isInWorkDay(start) && isInWorkTime(start))) {
            if (isBeforeWorkTime(start) || !isInWorkDay(start)) {
                start.add(Calendar.DATE, -1);
            }

            if (start.get(Calendar.DAY_OF_WEEK) == 1) {
                start.add(Calendar.DATE, -2);
            }

            if (start.get(Calendar.DAY_OF_WEEK) == 7) {
                start.add(Calendar.DATE, -1);
            }

            start.set(Calendar.HOUR_OF_DAY, END_WORKDAY);
            start.set(Calendar.MINUTE, 0);
        }
    }

    /**
     * Rounds a date forward if it is considered to be after the current work day.
     * It will be rounded forward to the start of the next business day
     *
     * @see #START_WORKDAY
     * @see #END_WORKDAY
     * @see #roundBack(Calendar)
     */
    public static void roundForward(Calendar start) {
        if (!(isInWorkDay(start) && isInWorkTime(start))) {
            if (isAfterWorkTime(start) || !isInWorkDay(start)) {
                start.add(Calendar.DATE, 1);
            }

            if (start.get(Calendar.DAY_OF_WEEK) == 1) {
                start.add(Calendar.DATE, 1);
            }

            if (start.get(Calendar.DAY_OF_WEEK) == 7) {
                start.add(Calendar.DATE, 2);
            }

            start.set(Calendar.HOUR_OF_DAY, START_WORKDAY);
            start.set(Calendar.MINUTE, 0);
        }
    }

    /**
     * Determines if two dates are the same date (ignores time)
     *
     * @return true if they are the same date, false if not
     */
    public static boolean sameDate(Calendar c1, Calendar c2) {
        if (c1 == null || c2 == null) {
            return false;
        }
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Takes in two dates and indicates whether they are the same day and year.
     *
     * @return true if same day/year false if not
     */
    public static boolean sameDay(Date dt1, Date dt2) {
        return sameDay(dateToCal(dt1), dateToCal(dt2));
    }

    /**
     * Takes in two calendars and indicates whether it is the same day of the same year. Assumes that neither calendar
     * is null.
     *
     * @param cal  calendar 1
     * @param cal2 calendar 2
     * @return true if same day/year false if not
     */
    public static boolean sameDay(Calendar cal, Calendar cal2) {
        return cal.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
                cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Seconds difference between two dates
     *
     * @return Difference in seconds
     */
    public static long secondsDifference(Date start, Date end) {
        int[] results = diffMin(start, end);
        return (results[0] * 1440) +
                (results[1] * 60) +
                (results[2] * 60) +
                (results[3]);
    }

    /**
     * Takes in a string and returns a calendar
     *
     * @return parsed Calendar
     */
    public static Calendar setCal(String str) {
        return dateToCal(setDate(str));
    }

    /**
     * Returns a date from a String, based on the format MM-dd-yyyy (any other format will not work)
     *
     * @return Parsed Date
     */
    public static Date setDate(String str) {
        try {
            return new SimpleDateFormat(MMDD_DASH_FORMAT + "-yyyy").parse(str);
        } catch (Exception e) {
            try {
                return new SimpleDateFormat(MMDD_SLASH_FORMAT + "/yyyy").parse(str);
            } catch (Exception ee) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static Calendar setTime(Calendar calendar, int hour, int minute, int second) {
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static void setupLocale(String locale) {
        LOCALE = locale;
        if ("uk".equalsIgnoreCase(locale)) {
            MMDD_DASH_FORMAT = "dd-MM";
            MMDD_SLASH_FORMAT = "dd/MM";
        } else {
            MMDD_DASH_FORMAT = "MM-dd";
            MMDD_SLASH_FORMAT = "MM/dd";
        }
    }

    /**
     * Returns a preformatted Date i.e. 12/8/2002
     */
    public static String slashDate(Calendar cal) {
        if (cal == null) {
            return null;
        }
        try {
            if ("uk".equalsIgnoreCase(LOCALE)) {
                return (cal.get(Calendar.DAY_OF_MONTH)) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
            } else {
                return (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.YEAR);
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a preformatted Date i.e. 12/8/2002
     */
    public static String slashDate(Date dt) {
        if (dt == null) {
            return null;
        }
        return slashDate(dateToCal(dt));
    }

    /**
     * Parses a date, uses parseDate function
     *
     * @return parsed Date
     * @see #parseDate(String)
     */
    public static Date strToDate(String str) throws DateParseException {
        try {
            if (!StringUtils.is(str)) {
                return null;
            }
            if ("00:00".equalsIgnoreCase(str)) {
                return null;
            }
            return parseDate(str);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DateParseException();
        }
    }

    /**
     * Takes a number of seconds and returns a formatted string, ie 2d 12h 22m
     *
     * @param seconds
     * @return
     */
    public static String formatSeconds(int seconds) {
        if (seconds < 0) {
            return "N/A";
        }
        return formatArray(DateUtils.dateArrayFromSeconds(seconds));
    }

    /**
     * returns abbreviated day of week
     *
     * @param date date
     * @return abbreviated day of week
     */
    public static String dayOfWeekAbr(Date date) {
        if (date == null) {
            return "";
        }

        return new SimpleDateFormat("EEE").format(date);
    }

    /**
     * create a link to the calendar for the appropriate month
     *
     * @param cal specifies the desired month
     * @return link to monthView.jsp
     */
    public static String monthViewLink(Calendar cal) {
        if (cal == null) {
            cal = Calendar.getInstance();
        }
        return "/Calendar/calendar_full.jsp?view=Month&month=" + cal.get(Calendar.MONTH) + "&year=" + cal.get(Calendar.YEAR);
    }

    public static int getServerOffset(boolean useDaylightSavingTime) {
        boolean observingDaylightSavingTime = false;
        final int DST = 1;        

        // New calendar to use as a reference point.
        Calendar today = new GregorianCalendar();

        // TimeZone set to 'EST' for GMT comparison so we can determine the offset.
        TimeZone serverTimeZone = TimeZone.getTimeZone("America/New_York");

        // First getTime() returns date, the second getTime() converts that to milliseconds since the default date in java.
        int offsetMilliseconds = serverTimeZone.getOffset(today.getTime().getTime());

        // 3600000 = 1000 (ms in a sec) * 60 (sec in a min) * 60 (min in a hour)
        int offsetHours = (offsetMilliseconds / 3600000);

        // If server is on DST since our servers are EDT
        if(offsetHours == -4) {
            observingDaylightSavingTime = true;
        }

        if(observingDaylightSavingTime && useDaylightSavingTime) {
            offsetHours -= DST;
        }

        return offsetHours;
    }

    /**
     * determine whether or not the two dates fall within the same month
     * @param date1 date 1
     * @param date2 date 2
     * @return true or false
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        } else {
            Calendar cal1 = dateToCal(date1);
            Calendar cal2 = dateToCal(date2);
            return cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                    cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        }
    }

    /**
     * Returns a date of the beginning of today.
     *
     * @return beginning of today
     */
    public static Calendar getBeginningOfDay() {
        Calendar today = new GregorianCalendar();
        today.set(Calendar.HOUR, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today;
    }

    /**
     * Returns a calendar that has been rounded to the next quarter hour and zeros out the seconds and milliseconds.
     *
     * @param cal calendar to preform calculations on
     * @return cal calendar to return
     */
    public static GregorianCalendar roundCalToNextQuarterHour(GregorianCalendar cal) {
        int minutes = cal.get(Calendar.MINUTE);

        if (minutes > 45) {
            cal.set(Calendar.MINUTE, 0);
            cal.add(Calendar.HOUR, 1);
        } else if (minutes > 30) {
            cal.set(Calendar.MINUTE, 45);
        } else if (minutes > 15){
            cal.set(Calendar.MINUTE, 30);
        } else if (minutes > 0) {
            cal.set(Calendar.MINUTE, 15);
        } else {
            cal.set(Calendar.MINUTE, 0);
        }

        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal;
    }

    public static Calendar yesterday() {
        Calendar rtn = new GregorianCalendar();
        rtn.add(Calendar.DATE, -1);
        return rtn;
    }

    public static Calendar getStartOfDayRelative(int daysFromToday) {
        Calendar rtn = getBeginningOfDay();
        rtn.add(Calendar.DATE, daysFromToday);
        return rtn;
    }

    public static Iterable<Calendar> calendarIterator(final int iterateType, final Calendar startCal, final Calendar endCal) {
        return new Iterable<Calendar>() {
            final Calendar workCal = (Calendar) startCal.clone();
            public Iterator<Calendar> iterator() {
                return new Iterator<Calendar>() {
                    public boolean hasNext() {
                        return !workCal.after(endCal);
                    }

                    public Calendar next() {
                        workCal.add(iterateType, 1);
                        return (Calendar) workCal.clone();
                    }

                    public void remove() {

                    }
                };
            }
        };
    }
}
