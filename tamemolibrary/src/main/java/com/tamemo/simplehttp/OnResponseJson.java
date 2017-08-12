package com.tamemo.simplehttp;

import com.tamemo.dao.JSON;

/**
 * Created by Ta on 2017-08-12.
 */
public abstract class OnResponseJson extends OnResponseString {

    @Override
    public void onSuccess(String message) {
        onSuccess(new JSON(message));
    }

    public abstract void onSuccess(JSON res);

    @Override
    public void onFail(Response res) {
        onFail(404, res.body());
    }

    public void onFail(int statusCode, String message) {

    }
}
