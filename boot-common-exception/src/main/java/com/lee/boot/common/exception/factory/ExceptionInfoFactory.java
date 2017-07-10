package com.lee.boot.common.exception.factory;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author chen chi
 * @version ExceptionInfoFactory, v 0.1 2017/6/29 12:38 chen chi Exp
 */
public class ExceptionInfoFactory {

    protected static String getBundlePath(String bundleName) {
        return "META-INF.templates.exception." + bundleName;
    }

    protected String getContent(String bundlePath, String code, Object[] formatterArgs) {
        ResourceBundle bundle = getBundle(bundlePath);

        try {
            String templateContent = bundle.getString(code);
            return MessageFormat.format(templateContent, formatterArgs);
        } catch (MissingResourceException e) {
            return "";
        }
    }

    private ResourceBundle getBundle(String bundlePath) {
        Locale locale = Locale.getDefault();
        return ResourceBundle.getBundle(bundlePath, locale, getClassLoader());
    }

    protected ClassLoader getClassLoader() {
        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        return ccl == null ? getClass().getClassLoader() : ccl;
    }
}
