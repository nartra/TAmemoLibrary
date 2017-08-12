package com.tamemo.tamemo_libraries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tamemo.dao.JSON;
import com.tamemo.simplehttp.Connection;
import com.tamemo.simplehttp.OnResponse;
import com.tamemo.simplehttp.OnResponseJson;
import com.tamemo.simplehttp.OnResponseString;
import com.tamemo.simplehttp.Params;
import com.tamemo.simplehttp.Response;
import com.tamemo.simplehttp.Session;
import com.tamemo.simplehttp.SimpleHttp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleHttp.mock(true, new Session.PreProcess() {
            @Override
            public boolean handle(Connection.Method method, String url, Params params, OnResponse res) {
                if (url.equals("test")) {
                    res.onSuccess(new Response("mock data"));
                    return true;
                }
                return false;
            }
        });

        SimpleHttp.GET("test", new OnResponseString() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        }).cancel();

        SimpleHttp.GET("http://api.nainee.com/test", new OnResponseJson() {
            @Override
            public void onSuccess(JSON res) {
                String s = res.get("data").get(0).get("desc", String.class);
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "-- cancel --", Toast.LENGTH_SHORT).show();
            }
        }).cancel();
    }
}
