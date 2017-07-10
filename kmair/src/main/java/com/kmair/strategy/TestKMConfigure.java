package com.kmair.strategy;

import org.springframework.context.annotation.ComponentScan;

/**
 * @author lx48475
 * @version $Id: TestConfigure.java, v 0.1 2017年07月03 10:27 lx48475 Exp $
 */

public class TestKMConfigure {

    private String name;

    public String testKM(String str) {
        System.out.println("This is TestConfigure .......................");
        System.out.println("------------------ " + str + " " + name + " ----------------------");
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }
}
