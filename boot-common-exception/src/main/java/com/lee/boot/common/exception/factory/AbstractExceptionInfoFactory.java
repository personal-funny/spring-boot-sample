package com.lee.boot.common.exception.factory;

import com.lee.boot.common.exception.info.ExceptionInfo;

/**
 * @author chen chi
 * @version AbstractExceptionInfoFactory, v 0.1 2017/6/29 12:57 chen chi Exp
 */
public abstract class AbstractExceptionInfoFactory extends ExceptionInfoFactory {

    /**
     * 错误描述文本文件名，不包含.properties后缀
     *
     * @return 资源文件名称
     */
    protected abstract String provideBundleName();

    public final ExceptionInfo createExceptionInfo(String code, Object... formatterArgs) {
        String bundleName = provideBundleName();
        if (bundleName == null || bundleName == "") {
            throw new IllegalArgumentException("The name of the resource bundle is required");
        }

        String content = getContent(getBundlePath(bundleName), code, formatterArgs);;
        return new ExceptionInfo(content, code, formatterArgs);
    }
}
