package com.honghailt.cjtj.domain;

import java.util.List;

/**
 * 一个小工具，list分页处理
 */
public class ListPage<E> {

    private final List<E> sourceElements;
    private int pageSize = 100;
    private int currentPage = 0;

    public ListPage(List<E> sourceElements, int pageSize) {
        this.sourceElements = sourceElements;
        this.pageSize = pageSize;
    }

    public boolean hasNext() {
        return pageSize * currentPage < sourceElements.size();
    }

    public List<E> next() {
        if (!hasNext()) {
            throw new RuntimeException("已经循环完了...");
        }
        currentPage ++;
        int startIndex = (currentPage-1) * pageSize;
        int endIndex = currentPage * pageSize ;
        int lastIndex = sourceElements.size();

        return endIndex > lastIndex ? sourceElements.subList(startIndex, lastIndex) : sourceElements.subList(startIndex, endIndex);
    }
}
