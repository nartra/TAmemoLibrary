package com.tamemo.simplehttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ta on 24/2/2015.
 */
public class Params {

    public static Params blank(){
        return new Params();
    }

    Map<String, String> params;

    public Params(){
        params = new HashMap<String, String>();
    }

    public Params with(String key){
        params.put(key, null);
        return this;
    }

    public Params with(String key, Object value){
        params.put(key, value == null ? null : value.toString());
        return this;
    }

    public Map<String, String> build(){
        return params;
    }


}
