package com.honghailt.cjtj.utils;

import com.google.common.collect.Lists;
import com.honghailt.cjtj.domain.AbstractReport;
import com.honghailt.cjtj.domain.DateRange;
import com.honghailt.cjtj.domain.order.ReportDateOrderComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * 报表工具类
 */
public class ReportUtils {

    private final static Logger log = LoggerFactory.getLogger(ReportUtils.class);

    private static final DateTimeFormatter formatter =
        new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd")
            .parseDefaulting(ChronoField.NANO_OF_DAY, 0)
            .toFormatter()
            .withZone(ZoneId.systemDefault());

    /*报表按照日期排序的排序器*/
    private static final ReportDateOrderComparator REPORT_DATE_ORDER_COMPARATOR = new ReportDateOrderComparator();

    /**
     * 根据日期范围过滤掉日期范围之外的报表
     *
     * @param reports
     * @param dateRange
     * @param <T>
     * @return
     */
    public static <T extends AbstractReport> List<T> filterReportsByDateRange(List<T> reports, DateRange dateRange) {
        List<T> results = Lists.newArrayList();

        int start = Integer.valueOf(dateRange.getStartTimeStr().replaceAll("-", ""));
        int end = Integer.valueOf(dateRange.getEndTimeStr().replaceAll("-", ""));

        if (!isEmpty(reports)) {
            for (T report : reports) {
                int date = Integer.valueOf(report.getLogDate().replaceAll("-", ""));
                if (date >= start && date <= end) {
                    results.add(report);
                }
            }
        }

        return results;
    }

    /**
     * 分组合并报表
     *
     * @param reports    所有的报表
     * @param groupByKey 分组键
     * @param <T>
     * @return
     */
    public static <T extends AbstractReport> List<T> groupByMergeReport(List<T> reports, Function<T, String> groupByKey) {
        if (isEmpty(reports)) {
            return Lists.newArrayList();
        }

        Map<String, List<T>> reportMap = reports.stream().collect(groupingBy((report) -> groupByKey.apply(report), Collectors.toList()));

        List<T> results = Lists.newArrayList();
        for (List<T> ts : reportMap.values()) {
            results.add(mergeReport(ts));
        }

        return results;
    }

    /**
     * 把多个报表合并成一个报表
     *
     * @param reports
     * @param <T>
     * @return
     */
    public static <T extends AbstractReport> T mergeReport(List<T> reports) {
        if (CollectionUtils.isEmpty(reports)) {
            return null;
        }
        T firstReport = reports.get(0);
        T report = null;
        try {
            report = (T) Class.forName(firstReport.getClass().getName()).newInstance();
            //填充一些计划id、推广组id之类的
            BeanUtils.copyProperties(firstReport, report);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ReportMerge reportMerge = new ReportMerge(reports, report);

        return (T) reportMerge.getMergedReport();
    }

    /**
     * 根据日期将报表排序
     *
     * @param reports
     * @param <T>
     */
    public static <T extends AbstractReport> void sortByDate(List<T> reports) {
        if (reports != null) {
            Collections.sort(reports, REPORT_DATE_ORDER_COMPARATOR);
        }
    }

    /**
     * 获取昨天的报表
     *
     * @param reports
     * @param <T>
     * @return
     */
    public static <T extends AbstractReport> List<T> getLastDayReports(List<T> reports) {
        if (CollectionUtils.isEmpty(reports)) {
            return null;
        }

        String lastDayStr = formatter.format(Instant.now().plus(-1, ChronoUnit.DAYS));

        List<T> result = Lists.newArrayList();
        for (T report : reports) {
            if (lastDayStr.equals(report.getLogDate())) {
                result.add(report);
            }
        }

        return result;
    }

    /**
     * 填充一些自定义字段，比如转化率、点击率之类的
     *
     * @param report
     */
    public static void fillCustomProperty(AbstractReport report) {
        // 点击量
        Long click = safeNumber(report.getClick());
        // 展现量
        Long impressions = safeNumber(report.getAdPv());
        // 消耗
        Double change = safeNumber(report.getCharge());
        // 成交订单金额
        Double alipayInshopAmt = safeNumber(report.getAlipayInshopAmt());
        // 成交订单数
        Long alipayInShopNum = safeNumber(report.getAlipayInShopNum());

        // 点击率
        Double ctr = division(click * 100*1.0, impressions*1.0);
        // 点击成本
        Double eCpc = division(change, click * 1.0);
        // 投资回报率
        Double roi = division(alipayInshopAmt, change);
        // 点击转化率
        Double cvr = division(alipayInShopNum * 100 *1.0, click * 1.0);
        // 千次展现成本
        Double eCpm = division(change * 1000, impressions * 1.0);

        report.setCtr(ctr);
        report.setEcpc(eCpc);
        report.setRoi(roi);
        report.setCvr(cvr);
        report.setEcpm(eCpm);
    }

    private static Double division(Double a, Double b) {
        if (a == null || a == 0 || b == null || b == 0) {
            return 0D;
        }
        Double result = a * 1.0 / b;
        BigDecimal bigDecimal = new BigDecimal(result);
        result = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return result;
    }
    private static long safeNumber(Long num) {
        return num != null ? num : 0;
    }

    private static Double safeNumber(Double num) {
        return num != null ? num : 0.0;
    }

    private static String safeNumber(String num) {
        return num != null ? num : "0";
    }

    /**
     * 取最少7天的报表
     *
     * @param webDateRange
     * @return
     */
    public static DateRange getRequestDateRangeWith7Days(DateRange webDateRange) {
        Instant minDate = Instant.now().plus(-7, ChronoUnit.DAYS);
        DateRange apiRequestDateRange = webDateRange;
        if (minDate.isBefore(webDateRange.getStartTime()) && webDateRange.isDefault()) {
            apiRequestDateRange = new DateRange(minDate, webDateRange.getEndTime());
        }
        return apiRequestDateRange;
    }

    /**
     * 拆分提取报表
     *
     * @param reports
     * @param step
     * @param <T>
     * @return
     */
    public static <T extends AbstractReport> List<T> extractReports(List<T> reports, int step) {
        if (CollectionUtils.isEmpty(reports)) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < reports.size(); i += step) {
            result.add(reports.get(i));
        }

        return result;
    }
}
