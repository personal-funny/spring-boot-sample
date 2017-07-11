package com.lee.boot.common.converter;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by taoping on 2016/9/9.
 */
@Configuration
@ConditionalOnWebApplication
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class GlobalExceptionAutoConfiguration {

    public GlobalExceptionAutoConfiguration() {

    }

    @Bean
    public GlobalExceptionHandler globalErrorController() {
        return new GlobalExceptionHandler();
    }
}
