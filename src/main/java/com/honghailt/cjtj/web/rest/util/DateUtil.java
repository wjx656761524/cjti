package com.honghailt.cjtj.web.rest.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    private static final DateTimeFormatter dateFormatter =
        new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
            .toFormatter()
            .withZone(ZoneId.systemDefault());

    private static final DateTimeFormatter dateTimeFormatter =
        new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd HH:mm:ss")
            .parseDefaulting(ChronoField.NANO_OF_SECOND, 0)
            .toFormatter()
            .withZone(ZoneId.systemDefault());

    /**
     * 转换日期为字符串(yyyy-MM-dd)
     *
     * @param instant
     * @return
     */
    public static String format(Instant instant) {
        return dateFormatter.format(instant);
    }

    /**
     * 转换日期时间为字符串(yyyy-MM-dd HH:mm:ss)
     *
     * @param instant
     * @return
     */
    public static String formatWithDateTime(Instant instant) {
        return dateTimeFormatter.format(instant);
    }

    /**
     * 转换字符串(yyyy-MM-dd) 为日期对象
     *
     * @param dateStr
     * @return
     */
    public static Instant parse(String dateStr) {
        return dateFormatter.parse(dateStr, Instant::from);
    }

    public static Instant parse(String dateStr, String datePattern) {
        DateFormat df = new SimpleDateFormat(datePattern);
        try {
            return df.parse(dateStr).toInstant();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换字符串(yyyy-MM-dd HH:mm:ss) 为日期对象
     *
     * @param dateTimeStr
     * @return
     */
    public static Instant parseWithDateTime(String dateTimeStr) {
        return dateTimeFormatter.parse(dateTimeStr, Instant::from);
    }

    /**
     * 获取今天的日期字符串(yyyy-MM-dd)
     *
     * @return
     */
    public static String getTodayDate() {
        return format(Instant.now());
    }


    /**
     * 剩余天数
     *
     * @param expiresDay
     * @return
     */
    public static Long getExpiresDays(Instant expiresDay) {
        Instant today = dateFormatter.parse(dateFormatter.format(Instant.now()), Instant::from);
        return expiresDay.isBefore(today) ? 0 : ChronoUnit.DAYS.between(today, expiresDay);
    }

    /**
     * 计算时间段内的天数
     * @param instant
     * @return
     */
    public static Long getTimeBetweenDays(Instant instant) {
        Instant today = dateFormatter.parse(dateFormatter.format(Instant.now()), Instant::from);
        return ChronoUnit.DAYS.between(instant,today);
    }

}
