package com.tamemo.dao;

/**
 * Created by Ta on 31/3/2015.
 */

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSON extends BaseDao{

    public static final JSON EMPTY = new JSON("{}");
    protected Object o;
    protected Gson gson = new Gson();
    protected final static String TMP_KEY_NAME = "key";

    public JSON() {
        set("{}");
    }

    public JSON(String src) {
        set(src != null ? src : "{}");
    }

    public JSON(Object src) {
        //if(true || o instanceof JSONObject || o instanceof JSONArray) {
        if (src == null) {
            set("{}");
        } else {
            o = src;
        }
    }

    public JSON set(String src) {
        try {
            o = new JSONObject(src);
        } catch (JSONException e) {
            try {
                o = new JSONArray(src);
            } catch (JSONException e2) {
                o = src;
            }
        }
        return this;
    }

    public boolean isNull(String name) {
        try {
            Object j = ((JSONObject) o).get(name);
            return JSONObject.NULL.equals(j);
        } catch (Exception e) {
            return true;
        }
    }

    public <E> E get(String name, Class<E> type) {
        return get(name, type, null);
    }

    public boolean has(String key) {
        try {
            return ((JSONObject) o).has(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Iterator<String> keys() {
        try {
            return ((JSONObject) o).keys();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isEmpty() {
        try {
            if (o instanceof JSONObject) {
                return ((JSONObject) o).names().length() == 0;
            } else return length() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public JSON get(String name) {
        try {
            try {
                return new JSON(((JSONObject) o).get(name));
            } catch (JSONException e) {
                //e.printStackTrace();
                return new JSON(((JSONObject) o).getJSONArray(name));
            }
        } catch (Exception e) {
            //e.printStackTrace();
            return new JSON(o);
        }
    }

    public <E> E get(String name, Class<E> type, E defaultValue) {
        try {
            E e = defaultValue;
            try {
                Object j = ((JSONObject) o).get(name);
                if (JSONObject.NULL.equals(j)) {
                    return e;
                }

                e = gson.fromJson(j.toString(), type);

                if (e == null) {
                    throw new JsonSyntaxException("dummy Exception");
                }
            } catch (JsonSyntaxException j) {
                e = (E) ((JSONObject) o).get(name);
            }
            return e;

        } catch (Exception e) {
            return defaultValue;
        }
    }


    public JSON get(int index) {
        try {
            if (!(o instanceof JSONArray)) {
                o = ((JSONObject) o).getJSONArray(TMP_KEY_NAME);
            }
            return new JSON(((JSONArray) o).get(index).toString());
        } catch (Exception e) {
            return new JSON(o);
        }
    }

    public <E> E get(int index, Class<E> type) {
        try {

            if (!(o instanceof JSONArray)) {
                o = ((JSONObject) o).getJSONArray(TMP_KEY_NAME);
            }

            if (false && gson == null) {
                return (E) ((JSONArray) o).get(index);
            } else {
                return gson.fromJson(((JSONArray) o).get(0).toString(), type);
            }

        } catch (Exception e) {
            return null;
        }
    }

    public int length() {
        if (o instanceof JSONArray) {
            return ((JSONArray) o).length();
        }
        return 0;
    }

    @Override
    public String toString() {
        try {
            return o.toString();
        } catch (Exception e) {
            return "";
        }
    }
}

