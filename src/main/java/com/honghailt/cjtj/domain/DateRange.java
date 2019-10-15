package com.honghailt.cjtj.domain;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 * 日期范围
 */
public class DateRange {

    private static final DateTimeFormatter formatter =
        new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
            .toFormatter()
            .withZone(ZoneId.systemDefault());

    /*开始时间*/
    private Instant startTime;
    /*结束时间*/
    private Instant endTime;
    /*是否是默认值*/
    private boolean isDefault;

    public DateRange(Instant startTime, Instant endTime) {
        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("开始结束时间不能为null");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public DateRange(String startTimeStr, String endTimeStr) {
        if (StringUtils.isBlank(startTimeStr) || StringUtils.isBlank(endTimeStr)) {
            throw new IllegalArgumentException("开始结束时间不能为空");
        }
        this.startTime = formatter.parse(startTimeStr, Instant::from);
        this.endTime = formatter.parse(endTimeStr, Instant::from);
    }

    /**
     * 获取默认的报表日期范围（昨天）
     *
     * @return
     */
    public static DateRange getDefaultDateRange() {
        DateRange range = new DateRange(Instant.now(), Instant.now());
        range.isDefault = true;
        return range;
    }

    /**
     * 获取默认的查询接口日期范围（过去30天）
     *
     * @return
     */
    public static DateRange getDefaultReportDateRange() {
        return new DateRange(Instant.now().plus(-30, ChronoUnit.DAYS), Instant.now().plus(-1, ChronoUnit.DAYS));
    }

    /**
     * 过去7天日期范围
     *
     * @return
     */
    public static DateRange get7DaysDateRange() {
        return new DateRange(Instant.now().plus(-7, ChronoUnit.DAYS), Instant.now().plus(-1, ChronoUnit.DAYS));

    }

    /**
     * 过去15天日期范围
     * @return
     */
    public static DateRange get15DaysDateRange() {
        return new DateRange(Instant.now().plus(-15, ChronoUnit.DAYS), Instant.now().plus(-1, ChronoUnit.DAYS));
    }


    public boolean isDefault() {
        return isDefault;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public String getStartTimeStr() {
        return formatter.format(startTime);
    }

    public Instant getEndTime() {
        return endTime;
    }

    public String getEndTimeStr() {
        return formatter.format(endTime);
    }

    @Override
    public String toString() {
        return "DateRange{" +
            "startTime=" + startTime +
            ", endTime=" + endTime +
            '}';
    }
}
