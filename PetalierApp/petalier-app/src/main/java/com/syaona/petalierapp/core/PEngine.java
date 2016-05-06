package com.syaona.petalierapp.core;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.syaona.petalierapp.R;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by smartwavedev on 3/30/16.
 */
public class PEngine {
    public static void switchFragment(BaseActivity baseActivity, Fragment fragment, int frame) {

        FragmentManager fm = baseActivity.getSupportFragmentManager();

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(frame, fragment);
        transaction.addToBackStack(fragment.getClass().toString());
        transaction.commit();
    }

    public static void onHomePress(BaseActivity baseActivity) {
        FragmentManager fm = baseActivity.getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack();
        } else {
            baseActivity.finish();
        }

    }

    public static Fragment getActiveFragment(BaseActivity baseActivity) {
        FragmentManager fm = baseActivity.getSupportFragmentManager();

        if (fm.getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
        return fm.findFragmentByTag(tag);
    }

    public static void popFragment(BaseActivity baseActivity) {
        baseActivity.getSupportFragmentManager().popBackStack();
    }

    public static void hideSoftKeyboard(BaseActivity baseActivity) {

        if (baseActivity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) baseActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(baseActivity.getCurrentFocus().getWindowToken(), 0);

        }
    }

    public static String encriptFacebookCredentials() {
        try {
            String secret = AppController.getInstance().getResources().getString(R.string.facebook_app_secret);
            String message = PSharedPreferences.getSomeStringValue(AppController.getInstance(),
                    "fb_access_token") + "p3t@li3r";

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String hash = Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes()), Base64.NO_WRAP);
            Log.i("signature",hash);
            return hash;
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

}
