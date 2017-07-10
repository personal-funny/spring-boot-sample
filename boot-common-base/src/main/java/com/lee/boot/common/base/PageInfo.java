package com.lee.boot.common.base;

import java.io.Serializable;

/**
 * @author chen chi
 * @version PageInfo, v 0.1 2017/6/29 15:33 chen chi Exp
 */
public class PageInfo implements Serializable {

    private static final long serialVersionUID = -5930425696981719734L;

    /** 总共项数 */
    private int items;

    /** 每页项数，默认20 */
    private int itemsPerPage = 20;

    /** 当前页码，默认1 */
    private int page = 1;

    public PageInfo() {
        super();
    }

    public PageInfo(int itemsPerPage, int page) {
        super();
        this.itemsPerPage = itemsPerPage;
        this.page = page;
    }

    public PageInfo(int items, int itemsPerPage, int page) {
        super();
        this.items = items;
        this.itemsPerPage = itemsPerPage;
        this.page = page;
    }

    public int getPages() {
        return (int) Math.ceil((double) items / itemsPerPage);
    }

    /**
     * XFIRE需要set方法
     */
    public void setPages(int pages) {
        // no-op
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
