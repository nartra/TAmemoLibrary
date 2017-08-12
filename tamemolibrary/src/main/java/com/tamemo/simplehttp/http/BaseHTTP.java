package com.tamemo.simplehttp.http;

import com.squareup.okhttp.Call;
import com.tamemo.dao.BaseDao;
import com.tamemo.simplehttp.Params;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by ta on 12/02/2015.
 */
public abstract class BaseHTTP extends HTTPEngine{

    protected <T extends BaseDao> Call load(RequestMethod method, String url, Params data, final HTTPEngineListener<T> listener, final Class<T> tClass){
        data = data == null ? Params.blank() : data;
        return loadUrl(method, url, data.build(), listener, tClass);
    }

    protected <T extends BaseDao> Call load(RequestMethod method, String url, Params data, final HTTPEngineListener<T> listener, final Type tType){
        data = data == null ? Params.blank() : data;
        return loadUrl(method, url, data.build(), listener, tType);
    }

    public <T extends BaseDao> Call GET(String url, Params data, final HTTPEngineListener<T> listener, final Class<T> tClass){
        url += "?";
        for(Map.Entry<String, String> entry : data.build().entrySet()) {
            url += entry.getKey() + (entry.getValue() == null ? "" : "=" + entry.getValue()) + "&";
        }
        url = url.substring(0, url.length() - 1);
        return load(RequestMethod.METHOD_GET, url, null, listener, tClass);
    }

    public <T extends BaseDao> Call GET(String url, Params data, final HTTPEngineListener<T> listener, final Type tType){
        url += "?";
        for(Map.Entry<String, String> entry : data.build().entrySet()) {
            url += entry.getKey() + (entry.getValue() == null ? "" : "=" + entry.getValue()) + "&";
        }
        url = url.substring(0, url.length() - 1);
        return load(RequestMethod.METHOD_GET, url, null, listener, tType);
    }

    public <T extends BaseDao> Call POST(String url, Params data, final HTTPEngineListener<T> listener, final Class<T> tClass){
        return load(RequestMethod.METHOD_POST, url, data, listener, tClass);
    }
}
