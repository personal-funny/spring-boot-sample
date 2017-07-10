package com.caair.strategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author lx48475
 * @version $Id: TestConfigure.java, v 0.1 2017年07月03 10:27 lx48475 Exp $
 */
public class TestCAConfigure {

    public String testCA(String str) {
        System.out.println("This is TestConfigure ++++++++++++++");
        System.out.println("------------------ " + str + " ----------------------");
        return null;
    }
}
