package com.lee.boot.common.exception;

import com.lee.boot.common.exception.info.ExceptionInfo;
import com.lee.boot.common.utils.helper.ExceptionHelper;

/**
 * 航司基本受检异常，所有的异常都应继承于此
 *
 * @author chen chi
 * @version AirlinesBaseException, v 0.1 2017/6/29 10:45 chen chi Exp
 */
public class AirlinesBaseException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 异常描述
     */
    private String message;

    /**
     * 异常信息
     */
    private ExceptionInfo exceptionInfo;

    public AirlinesBaseException(ExceptionInfo info) {
        fillException(info);
    }

    public AirlinesBaseException(ExceptionInfo info, Throwable cause) {
        super(ExceptionHelper.unwrap(cause));
        fillException(info);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ExceptionInfo getExceptionInfo() {
        return exceptionInfo;
    }

    public String getExceptionCode() {
        return exceptionInfo.getCode();
    }

    private void fillException(ExceptionInfo info) {
        this.exceptionInfo = info;
        this.message = info.getContent();
    }

    @Override
    public String toString() {
        return "AirlinesBaseException{" +
                "message='" + message + '\'' +
                ", exceptionInfo=" + exceptionInfo +
                '}';
    }
}
