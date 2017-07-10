package com.caair.strategy;

import org.springframework.beans.factory.annotation.Autowired;

import com.lee.boot.adapter.ITicketChecker;

/**
 * @author lx48475
 * @version $Id: CAStrategy.java, v 0.1 2017年07月03 08:50 lx48475 Exp $
 */
public class CAStrategy implements ITicketChecker {

    @Autowired
    private TestCAConfigure testCAConfigure;
    @Override
    public boolean checkTicket(String s, String s1) {
        System.out.println("This is CAStrategy ...............");
        testCAConfigure.testCA(s);
        return false;
    }
}
