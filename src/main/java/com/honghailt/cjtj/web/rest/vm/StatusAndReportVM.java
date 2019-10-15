package com.honghailt.cjtj.web.rest.vm;

import com.honghailt.cjtj.domain.AbstractReport;

import java.util.List;

public class StatusAndReportVM {

    /**
     * 状态数据
     */
    private Object info;

    /**
     * 多个日期合并的报表数据
     */
    private AbstractReport report;

    /**
     * 所有的报表数据，每一天的数据
     */
    private List<? extends AbstractReport> allReport;

    public StatusAndReportVM(Object info, AbstractReport report, List<? extends AbstractReport> allReport) {
        this.info = info;
        this.report = report;
        this.allReport = allReport;
    }

    public Object getInfo() {
        return info;
    }

    public void setInfo(Object info) {
        this.info = info;
    }

    public AbstractReport getReport() {
        return report;
    }

    public void setReport(AbstractReport report) {
        this.report = report;
    }

    public List<? extends AbstractReport> getAllReport() {
        return allReport;
    }

    public void setAllReport(List<? extends AbstractReport> allReport) {
        this.allReport = allReport;
    }

    @Override
    public String toString() {
        return "StatusAndReportVM{" +
            "info=" + info +
            ", report=" + report +
            ", allReport=" + allReport +
            '}';
    }
}
