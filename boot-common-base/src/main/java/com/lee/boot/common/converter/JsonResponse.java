package com.lee.boot.common.converter;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by taoping on 2016/10/17.
 */
public interface JsonResponse extends Serializable {
    static final int SUCCESS_CODE = HttpStatus.OK.value();
    static final int ERROR_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();
    static final int BADREQUEST_CODE = HttpStatus.BAD_REQUEST.value();
}
