package com.lee.boot.common.utils.helper;

import java.lang.reflect.InvocationTargetException;

/**
 * @author chen chi
 * @version ExceptionHelper, v 0.1 2017/6/29 11:15 chen chi Exp
 */
public class ExceptionHelper {

    private ExceptionHelper() {
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T unwrap(T t) {
        if (t instanceof InvocationTargetException) {
            return ((T) ((InvocationTargetException) t).getTargetException());
        }
        return t;
    }
}
