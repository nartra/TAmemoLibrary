package com.tamemo.simplehttp.http;


import android.os.AsyncTask;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by ta on 12/02/2015.
 */

public class HTTPRequestTask extends AsyncTask<HTTPRequestData, Void, HTTPRequestTask.ContentMessage> {

    HTTPRequestListener mListener;
    private boolean cancel = false;

    public HTTPRequestTask(HTTPRequestListener aListener) {
        mListener = aListener;
    }

    @Override
    protected ContentMessage doInBackground(HTTPRequestData... params) {
        HTTPRequestData data = params[0];
        ContentMessage message = new ContentMessage();

        try {
            Response response = data.call.execute();
            if (response.isSuccessful()) {
                message.success = true;
                message.statusCode = response.code();
            } else {
                message.success = false;
                message.statusCode = response.code();
            }
            message.body = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            message.success = false;
            cancel = true;
        }

        return message;
    }

    @Override
    protected void onPostExecute(ContentMessage s) {
        super.onPostExecute(s);
        if (mListener != null) {
            if (s.success) {
                mListener.onMessageReceived(s.statusCode, s.body);
            } else if (cancel) {
                mListener.onCancel();
            } else {
                mListener.onMessageError(s.statusCode, s.body);
            }
        }
    }

    public class ContentMessage {
        boolean success;
        int statusCode;
        String body;
    }
}
