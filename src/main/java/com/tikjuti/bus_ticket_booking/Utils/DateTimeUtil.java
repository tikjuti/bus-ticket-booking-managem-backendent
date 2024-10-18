package com.tikjuti.bus_ticket_booking.Utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeUtil {

    public static boolean checkMatch(String input, String regexPattern) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public static boolean isISOString(String s) {
        return checkMatch(s, "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z$");
    }

    public static boolean isSQLDateString(String s) {
        return checkMatch(s, "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{6}$");
    }

    public static Date stringToDate(String s) throws ParseException {
        if (isISOString(s)) {
            TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(s);
            Instant i = Instant.from(ta);
            Date d = Date.from(i);
            return d;
        } else if (isSQLDateString(s)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
            return dateFormat.parse(s);
        } else {
            return null;
        }
    }

    public static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }
}