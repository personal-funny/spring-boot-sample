package com.lee.boot.common.exception.info;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 异常信息
 *
 * @author chen chi
 * @version ExceptionInfo, v 0.1 2017/6/29 10:50 chen chi Exp
 */
public class ExceptionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 异常描述（内容）
     */
    private String content;

    /**
     * 异常编码（对应于异常文本模板）
     *
     * 异常码（code）规范，如AE0310002025
     * 第1-2位是航司异常码标识，固定位：AE（Airlines Exception）；
     * 第3位是规范版本为，目前为0；
     * 第4位是异常级别，1-INFO, 3-WARN, 5-ERROR, 7-FATAL
     * 第5位是异常类别，0-系统异常, 1-业务异常, 2-第三方异常
     * 第6-9位是异常场景，全局分配
     * 第10-12位是业务场景下的具体编码
     */
    private String code;

    /**
     * 异常文本模板格式化参数
     */
    private Object[] formatterArgs;

    public ExceptionInfo(String content, String code, Object[] formatterArgs) {
        this.content = content;
        this.code = code;
        this.formatterArgs = formatterArgs;
    }

    public String getContent() {
        return content;
    }

    public String getCode() {
        return code;
    }

    public Object[] getFormatterArgs() {
        return formatterArgs;
    }

    @Override
    public String toString() {
        return "ExceptionInfo{" +
                "content='" + content + '\'' +
                ", code='" + code + '\'' +
                ", formatterArgs=" + Arrays.toString(formatterArgs) +
                '}';
    }
}
