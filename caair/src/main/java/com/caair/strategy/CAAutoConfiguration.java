package com.caair.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lee.boot.adapter.TickerCheckerAdapter;
import org.springframework.context.annotation.ImportResource;

/**
 * @author lx48475
 * @version $Id: CAAutoConfiguration.java, v 0.1 2017年07月03 08:51 lx48475 Exp $
 */
@Configuration
@ImportResource({"classpath:spring/ca-beans.xml"})
public class CAAutoConfiguration {

    @Autowired
    private CAStrategy caStrategy;

    @Bean
    public CAStrategy getCAStategy() {
        CAStrategy checker = new CAStrategy();
        return checker;
    }

    @Bean
    public String loadCAStrategy() {
        System.out.println("Loading CAStrategy checker");
        TickerCheckerAdapter.checkerMap.put("999", caStrategy);
        System.out.println("Loading Success");
        return null;
    }
}
