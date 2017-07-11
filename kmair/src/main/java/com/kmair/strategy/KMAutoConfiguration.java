package com.kmair.strategy;

import com.lee.boot.adapter.TickerCheckerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * @author lx48475
 * @version $Id: KMAutoConfiguration.java, v 0.1 2017年07月03 08:54 lx48475 Exp $
 */
@Configuration
@ImportResource({"classpath:spring/km-beans.xml"})
public class KMAutoConfiguration {

//    @Autowired
//    private KMStrategy kmStrategy;

    @Bean
    public KMStrategy getKMStrategy() {
        KMStrategy checker = new KMStrategy();
        System.out.println("Loading KMStrategy checker");
        TickerCheckerAdapter.checkerMap.put("089", checker);
        System.out.println("Loading Success");
        return checker;
    }

//    @Bean
//    public String loadKMStrategy() {
//        System.out.println("Loading KMStrategy checker");
//        StrategyFactory.map.put("089", kmStrategy);
//        System.out.println("Loading Success");
//        return null;
//    }
}
