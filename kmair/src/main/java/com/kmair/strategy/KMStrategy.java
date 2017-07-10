package com.kmair.strategy;

import org.springframework.beans.factory.annotation.Autowired;

import com.lee.boot.adapter.ITicketChecker;

/**
 * @author lx48475
 * @version $Id: KMStrategy.java, v 0.1 2017年07月03 08:53 lx48475 Exp $
 */
public class KMStrategy implements ITicketChecker {

    @Autowired
    private TestKMConfigure testKMConfigure;


    @Override
    public boolean checkTicket(String s, String s1) {
        System.out.println("This is KMStrategy ...............");
        testKMConfigure.testKM(s);
        return false;
    }
}
