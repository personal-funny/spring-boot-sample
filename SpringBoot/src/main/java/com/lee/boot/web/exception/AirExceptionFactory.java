package com.lee.boot.web.exception;

import com.lee.boot.common.exception.factory.AbstractExceptionInfoFactory;

/**
 * @author lx48475
 * @version $Id: AirExceptionFactory.java, v 0.1 2017年06月30 10:02 lx48475 Exp $
 */
public class AirExceptionFactory extends AbstractExceptionInfoFactory {

    private static final class AirExceptionFactoryHolder {
        private static final AirExceptionFactory INSTANCE = new AirExceptionFactory();
    }

    public static AirExceptionFactory getInstance() {
        return AirExceptionFactoryHolder.INSTANCE;
    }

    @Override
    protected String provideBundleName() {
        return "spring-boot";
    }
}
