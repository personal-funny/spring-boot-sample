package com.lee.boot.common.converter;

import org.apache.catalina.filters.RemoteIpFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by taoping on 2016/8/24.
 */
@Configuration
public class AppAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(AppAutoConfiguration.class);


    private String applicationName;

    public AppAutoConfiguration() {

    }


    @Configuration
    @ConditionalOnWebApplication
    public static class WebApplicationAutoConfiguration extends WebMvcConfigurerAdapter {


        public WebApplicationAutoConfiguration() {

        }

        @Bean
        public RemoteIpFilter remoteIpFilter() {
            return new RemoteIpFilter();
        }



        @Bean
        public Jackson2HttpMessageConverter jackson2HttpMessageConverter() {
            logger.info("dynamic messageConverter init....");
            Jackson2HttpMessageConverter messageConverter = new Jackson2HttpMessageConverter();
            return messageConverter;
        }

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(jackson2HttpMessageConverter());
            super.configureMessageConverters(converters);
            logger.info("===>>Configure Message converters : \n{}", converters);
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new RequestContextFillInterceptor());
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**").allowedOrigins("*").maxAge(3600)
                    .allowCredentials(true).allowedHeaders("Content-Type, Token")
                    .allowedMethods("POST, GET, OPTIONS, DELETE, PUT, HEADER");
        }
    }
}
