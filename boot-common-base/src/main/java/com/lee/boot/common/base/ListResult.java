package com.lee.boot.common.base;

import java.util.Collection;

/**
 * @author chen chi
 * @version ListResult, v 0.1 2017/6/29 15:30 chen chi Exp
 */
public class ListResult<T> extends BaseResult {

    private static final long serialVersionUID = -7503565254583158949L;

    private Collection<T> values;

    public ListResult() {
        super();
    }

    public ListResult(Collection<T> values) {
        super();
        this.values = values;
    }

    public ListResult(Collection<T> values, String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.values = values;
    }

    public ListResult(Collection<T> values, boolean success, String errorCode, String errorMessage) {
        super(success, errorCode, errorMessage);
        this.values = values;
    }

    public Collection<T> getValues() {
        return values;
    }

    public void setValues(Collection<T> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "ListResult{" +
                "values=" + values +
                '}';
    }
}
