package com.lee.boot.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * AbstractTicketChecker.
 *
 * Created by chris on 17-7-1.
 */
public class TickerCheckerAdapter {

  public String checkTicket(String ticketNo, String idNo) {
    String header = ticketNo.substring(0, 3);
    ITicketChecker checker = checkerMap.get(header);
    return checker.checkTicket(ticketNo, idNo);
  }

  public final static Map<String, ITicketChecker> checkerMap = new HashMap<String, ITicketChecker>();
}
