package com.tamemo.simplehttp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ta on 2017-08-12.
 */
public class SimpleHttp {

    private static Map<Integer, Session> sessions = new HashMap<>();

    public static Session session(int id) {
        if (!sessions.containsKey(id)) {
            sessions.put(id, new Session());
        }
        return sessions.get(id);
    }

    public static Session defaultSession() {
        return session(0);
    }

    /*
    session helper
     */

    public static void cancelAll() {
        defaultSession().cancelAll();
    }

    public static void disconnect() {
        defaultSession().disconnect();
    }

    public static void reconnect() {
        defaultSession().reconnect();
    }

    public static void mock(boolean mocking) {
        defaultSession().mock(mocking);
    }

    public static void mock(boolean mocking, Session.PreProcess handler) {
        defaultSession().mock(mocking, handler);
    }

    /*
    connection helper
     */


    public static Connection GET(String url, OnResponse res) {
        return defaultSession().GET(url, res);
    }

    public static Connection GET(String url, Params params, OnResponse res) {
        return defaultSession().GET(url, params, res);
    }

    public static Connection POST(String url, OnResponse res) {
        return defaultSession().POST(url, res);
    }

    public static Connection POST(String url, Params params, OnResponse res) {
        return defaultSession().POST(url, params, res);
    }

    public static Connection connect(Connection.Method method, String url, Params params, final OnResponse res) {
        return defaultSession().connect(method, url, params, res);
    }

}
