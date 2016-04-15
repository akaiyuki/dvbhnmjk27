package com.syaona.petalierapp.core;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by smartwavedev on 4/15/16.
 */
public class PSharedPreferences {

    private static SharedPreferences mSharedPreferences;
    private static Context mContext;

    private static final String APP_PREFS= "APP_SETTINGS";

    private PSharedPreferences() {}

    public static void init(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSharedPreferences() {
        return mSharedPreferences;
    }

    public static String getSomeStringValue(Context context, String key) {
        return mSharedPreferences.getString(key , "");
    }

    public static void setSomeStringValue(Context context, String key, String newValue) {
        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key , newValue);
//        editor.commit();
        editor.apply();
    }

    public static void clearAllPreferences() {
        PDebug.logDebug("clearAllPreferences", "PREF CLEARED! LOGOUT!");
//        mSharedPreferences.edit().clear().commit();
        mSharedPreferences.edit().clear().apply();
    }

    public static void saveJsonToSharedPref(JSONObject jsonObj, String keyOffset) {

        Iterator<String> iter = jsonObj.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                PSharedPreferences.setSomeStringValue(AppController.getInstance(), keyOffset+key, jsonObj.getString(key));
                PDebug.logDebug("saveJsonToSharedPref", key + " = " + jsonObj.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
