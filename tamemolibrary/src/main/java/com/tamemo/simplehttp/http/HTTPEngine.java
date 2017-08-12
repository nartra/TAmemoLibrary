package com.tamemo.simplehttp.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.tamemo.Contextor;
import com.tamemo.dao.BaseDao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ta on 12/02/2015.
 */
public abstract class HTTPEngine{

    protected static HTTPEngine instance;

    public enum RequestMethod{
        METHOD_GET,
        METHOD_POST
    }

    private Context mContext;
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();

    protected HTTPEngine(){
        mContext = Contextor.getInstance().getContext();
    }

    protected abstract <T extends BaseDao> Call loadUrl(RequestMethod method, String url, Map<String, String> postData, final HTTPEngineListener<T> listener, final Class<T> tClass);

    protected abstract  <T extends BaseDao> Call loadUrl(RequestMethod method, String url, Map<String, String> postData, final HTTPEngineListener<T> listener, final Type tType);

    public <T extends BaseDao> Call loadPostUrl(String url, Map<String, String> postData, final HTTPEngineListener<T> listener, final Class<T> tClass){
        return loadUrl(RequestMethod.METHOD_POST, url, postData, listener, tClass);
    }

    public <T extends BaseDao> Call loadGetUrl(String url, final HTTPEngineListener<T> listener, final Class<T> tClass){
        return loadUrl(RequestMethod.METHOD_GET, url, null, listener, tClass);
    }
}