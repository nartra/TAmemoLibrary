package com.tamemo.simplehttp.http;

/**
 * Created by ta on 12/02/2015.
 */

import com.squareup.okhttp.Call;
import com.tamemo.simplehttp.Connection;

import java.util.Map;

public class HTTPRequestData {

    public String url;
    public Connection.Method method;
    public Map<String, String> postData;
    public Call call;

}
