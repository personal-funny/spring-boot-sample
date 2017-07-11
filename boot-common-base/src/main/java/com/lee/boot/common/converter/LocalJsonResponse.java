package com.lee.boot.common.converter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by taoping on 2016/8/13.
 */
public class LocalJsonResponse implements JsonResponse {

    public static final String JSON_KEY_CODE = "Code";
    public static final String JSON_KEY_MSG = "Msg";
    public static final String JSON_KEY_DATA = "Data";
    public static final String JSON_KEY_SUCCESS = "Success";
    public static final String JSON_KEY_ERROR_DATA = "ErrorData";
    private static final long serialVersionUID = -8601352502902193959L;
    @JsonProperty(value = JSON_KEY_CODE, required = true)
    private int status = SUCCESS_CODE;
    @JsonProperty(value = JSON_KEY_MSG)
    private String message = "";
    @JsonProperty(value = JSON_KEY_DATA)
    private Object data;
    @JsonProperty(value = JSON_KEY_SUCCESS, required = true)
    private Boolean success;
    @JsonIgnore
    private Object errorData;

    public LocalJsonResponse() {
    }

    public LocalJsonResponse(Object data) {
        this.data = data;
    }

    public LocalJsonResponse(int status, String message) {
        this.status = status == 0 ? SUCCESS_CODE : status;
        this.message = message;
    }

    public static LocalJsonResponse ok(Object data) {
        return new LocalJsonResponse(data);
    }

    public static LocalJsonResponse error(String message) {
        LocalJsonResponse response = new LocalJsonResponse(ERROR_CODE, message);
        return response;
    }

    public static LocalJsonResponse badRequest() {
        LocalJsonResponse response = new LocalJsonResponse(BADREQUEST_CODE, null);
        return response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return (status == SUCCESS_CODE);
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getErrorData() {
        return errorData;
    }

    public void setErrorData(Object errorData) {
        this.errorData = errorData;
    }

    @Override
    public String toString() {
        return "LocalJsonResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", success=" + success +
                ", errorData=" + errorData +
                '}';
    }
}
