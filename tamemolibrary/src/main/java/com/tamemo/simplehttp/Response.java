package com.tamemo.simplehttp;

/**
 * Created by Ta on 2017-08-12.
 */
public class Response {

    private int statusCode;
    private String body;

    public Response(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public Response(String body) {
        this(200, body);
    }

    public String body() {
        return body;
    }

}
