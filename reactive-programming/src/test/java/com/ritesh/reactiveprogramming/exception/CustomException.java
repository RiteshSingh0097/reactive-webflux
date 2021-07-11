package com.ritesh.reactiveprogramming.exception;

public class CustomException extends Throwable{
    private String msg;

    public CustomException(Throwable e) {
        this.msg = e.getMessage();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
