package com.tamemo.simplehttp.http;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

/**
 * Created by ta on 12/02/2015.
 */
public class HTTPClient extends OkHttpClient {

    public final Object tag;

    public HTTPClient(Object tag) {
        this.tag = tag;
    }

    @Override
    public Call newCall(Request request) {
        Request.Builder requestBuilder = request.newBuilder();
        requestBuilder.tag(tag);
        return super.newCall(requestBuilder.build());
    }
}