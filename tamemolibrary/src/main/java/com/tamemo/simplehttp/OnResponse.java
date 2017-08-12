package com.tamemo.simplehttp;

/**
 * Created by Ta on 2017-08-12.
 */
public interface OnResponse {

    public void onSuccess(Response res);

    public void onFail(Response res);

    public void onNoInternetConnection();

    public void onCancel();

    public void onComplete();

}
