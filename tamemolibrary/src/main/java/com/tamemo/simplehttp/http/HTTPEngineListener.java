package com.tamemo.simplehttp.http;

import com.tamemo.dao.BaseDao;

/**
 * Created by ta on 12/02/2015.
 */
public interface HTTPEngineListener<T extends BaseDao> {

    public void onResponse(T response, String rawResponse);

    public void onFailure(T response, String rawResponse, HTTPEngineException error);

}
