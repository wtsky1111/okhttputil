package com.wt_sky.okHttp.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A persistent cookie store which implements the Apache HttpClient CookieStore interface.
 * Cookies are stored and will persist on the user's device between application sessions since they
 * are serialized and stored in SharedPreferences. Instances of this class are
 * designed to be used with AsyncHttpClient#setCookieStore, but can also be used with a
 * regular old apache HttpClient/HttpContext if you prefer.
 */
public class PersistentCookieStore implements CookieStore {

    private static final String LOG_TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS = "CookiePrefsFile";

    private final HashMap<String, ConcurrentHashMap<String, HttpCookie>> cookies;
    private final SharedPreferences cookiePrefs;

    /**
     * Construct a persistent cookie store.
     *
     * @param context Context to attach cookie store to
     */
    public PersistentCookieStore(Context context) {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        cookies = new HashMap<>();

        Map<String, String> prefsMap = (Map<String, String>) cookiePrefs.getAll();
        if (prefsMap != null) {
            for (Map.Entry<String, String> entry : prefsMap.entrySet()) {
                ConcurrentHashMap<String, HttpCookie> map = new ConcurrentHashMap<>();
                if (!TextUtils.isEmpty(entry.getValue())) {
                    String value = entry.getValue();
                    String[] key_value = TextUtils.split(value, "&");
                    for (String str : key_value) {
                        if (!TextUtils.isEmpty(str)) {
                            String[] cookieNames = TextUtils.split(str, ";");
                            for (String httpCookie : cookieNames) {
                                String[] cookieName = TextUtils.split(httpCookie, "=");
                                HttpCookie cookie = new HttpCookie(cookieName[0], cookieName[1]);
                                map.put(cookieNames[0], cookie);
                            }
                        }
                    }
                }
                cookies.put(entry.getKey(), map);
            }
        }
    }

    @Override
    public void add(URI uri, HttpCookie cookie) {
        // Save cookie into local store, or remove if expired
        if (!cookies.containsKey(uri.getHost())) {
            cookies.put(uri.getHost(), new ConcurrentHashMap<String, HttpCookie>());
        }
        cookies.get(uri.getHost()).put(cookie.getName(), cookie);

//        if (!cookie.hasExpired()) {
//            if (!cookies.containsKey(uri.getHost()))
//                cookies.put(uri.getHost(), new ConcurrentHashMap<String, HttpCookie>());
//            cookies.get(uri.getHost()).put(cookie.getName(), cookie);
//        } else {
//            if (cookies.containsKey(uri.getHost()))
//                cookies.get(uri.getHost()).remove(cookie.getName());
//        }

        // Save cookie into persistent store
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.putString(uri.getHost(), cookie2Str(cookies.get(uri.getHost())));
        prefsWriter.apply();
    }

    private String cookie2Str(ConcurrentHashMap<String, HttpCookie> map) {
        if (map == null) {
            return "";
        } else {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, HttpCookie> entry : map.entrySet()) {
                builder.append(entry.getValue()).append("&");
            }
            return builder.toString();
        }
    }

    @Override
    public List<HttpCookie> get(URI uri) {
        ArrayList<HttpCookie> ret = new ArrayList<>();
        if (cookies.containsKey(uri.getHost()))
            ret.addAll(cookies.get(uri.getHost()).values());
        return ret;
    }

    @Override
    public boolean removeAll() {
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        cookies.clear();
        return true;
    }


    @Override
    public boolean remove(URI uri, HttpCookie cookie) {
        if (cookies.containsKey(uri.getHost())) {
            cookies.get(uri.getHost()).remove(cookie.getName());
            SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
            prefsWriter.putString(uri.getHost(), cookie2Str(cookies.get(uri.getHost())));
            prefsWriter.apply();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<HttpCookie> getCookies() {
        ArrayList<HttpCookie> ret = new ArrayList<>();
        for (String key : cookies.keySet())
            ret.addAll(cookies.get(key).values());

        return ret;
    }

    @Override
    public List<URI> getURIs() {
        ArrayList<URI> ret = new ArrayList<>();
        for (String key : cookies.keySet())
            try {
                ret.add(new URI(key));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

        return ret;
    }
}