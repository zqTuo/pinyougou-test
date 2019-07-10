package com.pinyougou.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Result implements Serializable {
    private boolean success;
    private String message;
//    错误信息
    private List<Error>errorList=new ArrayList<>();

    public List<Error> getErrorList() {
        return errorList;
    }

    public void setErrorList(List<Error> errorList) {
        this.errorList = errorList;
    }

    public Result(boolean success, String message, List<Error> errorList) {
        this.success = success;
        this.message = message;
        this.errorList = errorList;
    }

    public Result() {
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", errorList=" + errorList +
                '}';
    }
}
