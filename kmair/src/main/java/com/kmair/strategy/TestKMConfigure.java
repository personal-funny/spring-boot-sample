package com.kmair.strategy;

/**
 * @author lx48475
 * @version $Id: TestConfigure.java, v 0.1 2017年07月03 10:27 lx48475 Exp $
 */
public class TestKMConfigure {

    private String name;

    public String testKM(String str) {
        return new StringBuilder("This is KMConfigure").append(" ").append(str).append(" ").append(name).toString();
    }

    public void setName(String name) {
        this.name = name;
    }
}
