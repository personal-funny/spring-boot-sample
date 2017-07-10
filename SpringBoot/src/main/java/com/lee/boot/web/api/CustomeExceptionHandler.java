package com.lee.boot.web.api;

import com.lee.boot.common.exception.AirlinesBaseException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

public class CustomeExceptionHandler {

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public String exHandler(Exception e, HttpServletRequest request) {
    String result = null;
    if (e instanceof AirlinesBaseException) {
      result = e.getMessage();
    } else if (e instanceof Exception) {
      result = e.getMessage();
    } else {
      result = "Other";
    }
    return result;
  }
}
