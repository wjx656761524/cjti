package com.honghailt.cjtj.api;

import java.util.List;

public class APIPage<T> {

    /*当前页内容*/
    private List<T> content;
    /*页号*/
    private Long pageNo;
    /*总条数，根据淘宝api规则只有第一页有，别的页就是空的*/
    private Long total;

    public APIPage(List<T> content, Long pageNo, Long total) {
        this.content = content;
        this.pageNo = pageNo;
        this.total = total;
    }

    public List<T> getContent() {
        return content;
    }

    public Long getPageNo() {
        return pageNo;
    }

    public Long getTotal() {
        return total;
    }
}
