package com.tamemo.simplehttp.http;

/**
 * Created by ta on 12/02/2015.
 */
public class HTTPEngineException extends Exception {

    private int statusCode;

    public HTTPEngineException(int statusCode) {
        this.statusCode = statusCode;
    }

    public HTTPEngineException(String detailMessage, int statusCode) {
        super(detailMessage);
        this.statusCode = statusCode;
    }

    public HTTPEngineException(String detailMessage, Throwable throwable, int statusCode) {
        super(detailMessage, throwable);
        this.statusCode = statusCode;
    }

    public HTTPEngineException(Throwable throwable, int statusCode) {
        super(throwable);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
