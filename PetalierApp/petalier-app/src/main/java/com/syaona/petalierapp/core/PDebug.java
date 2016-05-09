package com.syaona.petalierapp.core;

import android.util.Log;

/**
 * Created by smartwavedev on 3/30/16.
 */
public class PDebug {
    public static void logDebug(String tag, String message) {
        if (!PConfiguration.DEBUG) {
            Log.i("DEBUG_" + tag, message);
        }
    }
}
