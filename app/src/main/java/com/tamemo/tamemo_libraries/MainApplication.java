package com.tamemo.tamemo_libraries;

import android.app.Application;

import com.tamemo.Contextor;

/**
 * Created by Ta on 2017-08-12.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Contextor.getInstance().init(this);
    }
}
