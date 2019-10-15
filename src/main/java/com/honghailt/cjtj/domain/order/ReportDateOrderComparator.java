package com.honghailt.cjtj.domain.order;

import com.honghailt.cjtj.domain.AbstractReport;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;

public class ReportDateOrderComparator implements Comparator<AbstractReport> {
    @Override
    public int compare(AbstractReport o1, AbstractReport o2) {
        return StringUtils.compare(o1.getLogDate(), o2.getLogDate());
    }
}
