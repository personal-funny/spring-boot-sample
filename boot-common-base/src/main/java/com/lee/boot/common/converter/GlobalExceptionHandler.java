/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.lee.boot.common.converter;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zdh48304
 * @version GlobalExceptionHandler 2017/6/30 13:44 Exp $
 * @description
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    public static final String DEFAULT_ERROR_VIEW = "error";


    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public JsonResponse defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        LocalJsonResponse response = new LocalJsonResponse();
        response.setStatus(500);
        response.setMessage(e.getMessage());
        return response;
    }
}
