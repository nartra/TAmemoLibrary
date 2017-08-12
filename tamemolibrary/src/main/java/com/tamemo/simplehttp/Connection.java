package com.tamemo.simplehttp;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.tamemo.simplehttp.http.HTTPRequestData;
import com.tamemo.simplehttp.http.HTTPRequestListener;
import com.tamemo.simplehttp.http.HTTPRequestTask;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Ta on 2017-08-12.
 */
public class Connection {

    public enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }

    protected static int debug_connection_counter = 0;
    protected Session session;
    protected Call call;

    protected Method method;
    protected String url;
    protected Params params;
    protected OnResponse res;

    public Connection(Session session) {
        this.session = session;
    }

    public Connection GET(String url, OnResponse res) {
        return loadUrl(Method.GET, url, Params.blank(), res);
    }

    public Connection GET(String url, Params params, OnResponse res) {
        return loadUrl(Method.GET, url, params, res);
    }

    public Connection POST(String url, OnResponse res) {
        return loadUrl(Method.POST, url, Params.blank(), res);
    }

    public Connection POST(String url, Params params, OnResponse res) {
        return loadUrl(Method.POST, url, params, res);
    }

    public Connection connect(Method method, String url, Params params, final OnResponse res) {
        this.method = method;
        this.url = url;
        this.params = params;
        this.res = res;
        return this;
    }

    public Connection call() {
        if (url != null) {
            return loadUrl(method, url, params, res);
        }
        return this;
    }

    protected Connection loadUrl(Method method, String url, Params params, final OnResponse res) {

        Map<String, String> postData = params.build();
        if (session.mocking && session.getPreProcessHandler().handle(method, url, params, res)) {
            return this;
        }

        final int connection_counter_id = ++debug_connection_counter;
        Log.i("aaa", "start loadURL: [" + connection_counter_id + "]");

        if (!session.isNetworkOnline()) {
            Log.e("aaa", "connect : network offline");
            if (res != null) {
                res.onNoInternetConnection();
                res.onComplete();
            }
            return null;
        }

        Request request;
        if (method == Method.GET) {
            if (postData.size() > 0) {
                url += url.indexOf("?") == -1 ? "?" : "&";
                url += mapToPostString(postData);
            }
            request = new Request.Builder().url(url).build();
        } else {
            Map<String, String> postParams = new HashMap<>();
            if (postData != null) {
                for (Map.Entry<String, String> entry : postData.entrySet()) {
                    postParams.put(entry.getKey(), entry.getValue());
                }
            }
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
            RequestBody body = RequestBody.create(mediaType, mapToPostString(postParams));

            if (method == Method.POST) {
                request = new Request.Builder().url(url).post(body).build();
            } else if (method == Method.PUT) {
                request = new Request.Builder().url(url).put(body).build();
            } else if (method == Method.DELETE) {
                request = new Request.Builder().url(url).method("DELETE", body).build();
            } else {
                return null;
            }
        }

        Log.i("aaa", "connect [" + connection_counter_id + "] : prepare request complete");

        call = session.getClientHandler().newCall(request);

        final HTTPRequestData data = new HTTPRequestData();
        data.url = url;
        data.method = method;
        data.postData = postData;
        data.call = call;

        try {
            String debug_param = "", n_debug = "\n";
            if (postData.size() > 0) {
                n_debug = "\n";
                debug_param += '\n';
                Map<String, String> postParams = new HashMap<String, String>();
                if (postData != null) {
                    for (Map.Entry<String, String> entry : postData.entrySet()) {
                        postParams.put(entry.getKey(), entry.getValue());
                    }
                }
                for (Map.Entry<String, String> entry : postParams.entrySet()) {
                    try {
                        //content.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                        if (entry.getKey().equals("password") || entry.getKey().equals("client_secret")) {
                            debug_param += "        - " + encodeURI(entry.getKey()) + '=' + (entry.getValue().charAt(0) + "*****อ๊ะ! ตรงนี้เป็นพาสเวิร์ด ห้ามดูนะจ๊ะ*****" + entry.getValue().charAt(entry.getValue().length() - 1)) + '\n';
                        } else {
                            debug_param += "        - " + encodeURI(entry.getKey()) + '=' + encodeURI(entry.getValue()) + '\n';
                        }
                    } catch (Exception e) {
                    }
                }
            }
            Log.i("aaa", "url: (" + method + ") " + n_debug + "[ " + connection_counter_id + " ] " + url + debug_param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("aaa", "connect [" + connection_counter_id + "] : prepare parems complete");
        AsyncTask curTask;

        HTTPRequestTask httpRequestTask = new HTTPRequestTask(new HTTPRequestListener() {
            @Override
            public void onMessageReceived(int statusCode, String message) {
                Log.i("aaa", "[" + connection_counter_id + "] onMessageReceived " + statusCode + ":" + message);
                if (res != null) {
                    res.onSuccess(new Response(statusCode, message));
                    res.onComplete();
                }
            }

            @Override
            public void onMessageError(int statusCode, String message) {
                Log.e("aaa", "[" + connection_counter_id + "] onMessageError " + statusCode + ":" + message);
                if (res != null) {
                    res.onFail(new Response(statusCode, message));
                    res.onComplete();
                }
            }

            @Override
            public void onCancel() {
                res.onCancel();
                res.onComplete();
            }
        });

        Log.i("aaa", "connect [" + connection_counter_id + "] : prepare task complete");

        try {
            Log.i("aaa", "connect task: [" + connection_counter_id + "]");
            curTask = httpRequestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
        } catch (Exception e) {
            e.printStackTrace();
            ((ThreadPoolExecutor) AsyncTask.THREAD_POOL_EXECUTOR).shutdownNow();
            Log.i("aaa", "connect task: [" + connection_counter_id + "]");
            curTask = httpRequestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
        }

        Log.i("aaa", "connect end: [" + connection_counter_id + "]");
        return this;
    }


    public void cancel() {
        if (call != null && !call.isExecuted()) {
            call.cancel();
        }
    }

    public boolean isDone() {
        return call == null ? true : call.isExecuted() || call.isCanceled();
    }


    private String mapToPostString(Map<String, String> data) {
        StringBuilder content = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (content.length() > 0) {
                content.append('&');
            }
            try {
                //content.append(URLEncoder.encode(entry.getKey(), "UTF-8")).append('=').append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                content.append(encodeURI(entry.getKey())).append('=').append(encodeURI(entry.getValue()));
            } catch (Exception e) {
                //throw new AssertionError(e);
            }
        }
        return content.toString();
    }


    private static String encodeURI(String s) {
        try {
            s = URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            s = "";
        }
        return s;
    }

}
