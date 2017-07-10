package com.lee.boot.common.base;

import java.util.Collection;

/**
 * @author chen chi
 * @version PageResult, v 0.1 2017/6/29 15:37 chen chi Exp
 */
public class PageResult<T> extends ListResult<T> {

    /** 分页信息 */
    private PageInfo pageInfo = new PageInfo();

    public PageResult() {
        super();
    }

    public PageResult(Collection<T> values, boolean success, String errorCode, String errorMessage) {
        super(values, success, errorCode, errorMessage);
    }

    public PageResult(Collection<T> values, String errorCode, String errorMessage) {
        super(values, errorCode, errorMessage);
    }

    public PageResult(Collection<T> values) {
        super(values);
    }

    public PageResult(Collection<T> values, int page, int itemsPerPage, int items) {
        super(values);
        this.pageInfo = new PageInfo();
        this.pageInfo.setItems(items);
        this.pageInfo.setItemsPerPage(itemsPerPage);
        this.pageInfo.setPage(page);
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
