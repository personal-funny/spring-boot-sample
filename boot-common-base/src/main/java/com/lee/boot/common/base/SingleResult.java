package com.lee.boot.common.base;

/**
 * @author chen chi
 * @version SingleResult, v 0.1 2017/6/29 15:27 chen chi Exp
 */
public class SingleResult<T> extends BaseResult {

    private T data;

    public SingleResult() {
        super();
    }

    public SingleResult(T data) {
        super();
        this.data = data;
    }

    public SingleResult(T data, String errorCode, String errorMessage) {
        super(errorCode, errorMessage);
        this.data = data;
    }

    public SingleResult(T data, boolean success, String errorCode, String errorMessage) {
        super(success, errorCode, errorMessage);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SingleResult{" +
                "data=" + data +
                '}';
    }
}
