package com.tamemo.simplehttp;

/**
 * Created by Ta on 2017-08-12.
 */
public abstract class OnResponseString implements OnResponse {
    @Override
    public void onSuccess(Response res) {
        onSuccess(res.body());
    }

    public abstract void onSuccess(String message);

    @Override
    public void onFail(Response res) {

    }

    @Override
    public void onNoInternetConnection() {

    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onComplete() {

    }
}
