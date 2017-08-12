package com.tamemo.simplehttp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.squareup.okhttp.OkHttpClient;
import com.tamemo.Contextor;
import com.tamemo.simplehttp.http.HTTPClient;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ta on 2017-08-12.
 */
public class Session {


    protected static final int TIMEOUT = 60;
    private boolean disableConnection = false;
    protected final HTTPClient client;
    protected boolean mocking = false;
    protected PreProcess handler;

    public Session() {
        client = new HTTPClient(this);
        client.setConnectTimeout(TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(TIMEOUT, TimeUnit.SECONDS);
    }

    public Connection connect() {
        return new Connection(this);
    }

    public boolean isNetworkOnline() {

        if (disableConnection) {
            return false;
        }

        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) Contextor.getInstance().getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
                    status = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    protected OkHttpClient getClientHandler() {
        return client;
    }

    public void cancelAll() {
        client.cancel(client.tag);
    }

    public void disconnect(){
        cancelAll();
        disableConnection = true;
    }

    public void reconnect(){
        disableConnection = false;
    }

    public void mock(boolean mocking){
        this.mocking = mocking;
    }

    public void mock(boolean mocking, PreProcess handler){
        mock(mocking);
        this.handler = handler;
    }

    protected PreProcess getPreProcessHandler(){
        return handler;
    }


    public Connection GET(String url, OnResponse res) {
        return connect().GET(url, res);
    }

    public Connection GET(String url, Params params, OnResponse res) {
        return connect().GET(url, params, res);
    }

    public Connection POST(String url, OnResponse res) {
        return connect().POST(url, res);
    }

    public Connection POST(String url, Params params, OnResponse res) {
        return connect().POST(url, params, res);
    }

    public Connection connect(Connection.Method method, String url, Params params, final OnResponse res) {
        return connect().connect(method, url, params, res);
    }

    public static abstract class PreProcess {
        public abstract boolean handle(Connection.Method method, String url, Params params, final OnResponse res);
    }

}
