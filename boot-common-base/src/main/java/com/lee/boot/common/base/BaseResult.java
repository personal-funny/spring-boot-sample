package com.lee.boot.common.base;

import java.io.Serializable;

/**
 * @author chen chi
 * @version BaseResult, v 0.1 2017/6/29 15:25 chen chi Exp
 */
public class BaseResult implements Serializable {

    private static final long serialVersionUID = 8703437522881436978L;

    private boolean success = false;

    private String errorCode;

    private String errorMessage;

    /**
     * 默认构造函数
     */
    public BaseResult() {
        super();
        this.success = true;
    }

    /**
     * 带参数构造函数
     *
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     */
    public BaseResult(String errorCode, String errorMessage) {
        this(false, errorCode, errorMessage);
    }

    /**
     * 带参数构造函数
     *
     * @param success 服务是否执行成功
     * @param errorCode 错误码
     * @param errorMessage 错误信息
     */
    public BaseResult(boolean success, String errorCode, String errorMessage) {
        super();
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "success=" + success +
                ", errorCode='" + errorCode + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
