package com.lee.boot.web.api;

import com.lee.boot.common.exception.AirlinesBaseException;
import com.lee.boot.web.exception.AirExceptionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import com.lee.boot.adapter.TickerCheckerAdapter;

@RestController
public class RestApi extends CustomeExceptionHandler {

    private final AirExceptionFactory exceptionFactory = AirExceptionFactory.getInstance();

    @Autowired
    private TickerCheckerAdapter checker;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hello springboot !";
    }

    @RequestMapping(value = "/testcommon", method = RequestMethod.GET)
    public String testCommonException(String str) throws AirlinesBaseException {
        if ("C000001".equals(str)) {
            throw new AirlinesBaseException(exceptionFactory.createExceptionInfo("AE0310100001", new String[]{"COMMON EXCEPTION"}));
        } else {
            return "Success";
        }
    }

    @RequestMapping(value = "/testlist", method = RequestMethod.GET)
    public List<String> testList() {
        List<String> list = new ArrayList<>();
        list.add("Test1");
        list.add("Test2");
        return list;
    }

    @RequestMapping(value = "/checkticket", method = RequestMethod.GET)
    public boolean checkTicket(String idno, String ticket) {
        return checker.checkTicket(ticket, idno);
    }
}
