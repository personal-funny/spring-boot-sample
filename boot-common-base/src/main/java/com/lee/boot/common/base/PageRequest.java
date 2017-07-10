package com.lee.boot.common.base;

import java.io.Serializable;

/**
 * @author chen chi
 * @version PageRequest, v 0.1 2017/6/29 15:35 chen chi Exp
 */
public class PageRequest<T> implements Serializable {

    private static final long serialVersionUID = -8928799542495209570L;

    /** 查询条件 */
    private T condition;

    /** 当前页码 */
    private int pageNum = 1;

    /** 分页列表大小 */
    private int pageSize = 20;

    public PageRequest() {
        super();
    }

    public PageRequest(T condition, int pageNum, int pageSize) {
        super();
        this.condition = condition;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public T getCondition() {
        return condition;
    }

    public void setCondition(T condition) {
        this.condition = condition;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageRequest{" +
                "condition=" + condition +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
