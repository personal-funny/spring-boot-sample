package com.caair.strategy;

/**
 * @author lx48475
 * @version $Id: TestConfigure.java, v 0.1 2017年07月03 10:27 lx48475 Exp $
 */
public class TestCAConfigure {

    private String name;

    public String testCA(String str) {
        return new StringBuilder("This is CAConfigure").append(" ").append(str).append(" ").append(name).toString();
    }

    public void setName(String name) {
        this.name = name;
    }
}
