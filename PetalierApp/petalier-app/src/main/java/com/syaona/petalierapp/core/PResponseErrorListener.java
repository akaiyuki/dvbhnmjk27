package com.syaona.petalierapp.core;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by smartwavedev on 3/30/16.
 */
public class PResponseErrorListener implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError volleyError) {

//        PDebug.logDebug("API_onErrorResponse", volleyError.toString());
//
//        if (volleyError instanceof NoConnectionError) {
//            PSnackbarManager.show(BaseActivity.INSTANCE, "Oooops!", "There is no internet connection. Try again");
//        } else if (volleyError instanceof TimeoutError) {
//            PSnackbarManager.show(BaseActivity.INSTANCE, "Oooops!", "Looks like you're having a bad connection");
//        } else if (volleyError instanceof AuthFailureError) {
//            PSnackbarManager.show(BaseActivity.INSTANCE, "Warning", "Authentication Error");
//        } else if (volleyError instanceof NetworkError) {
//            PSnackbarManager.show(BaseActivity.INSTANCE, "Warning", "Network Error");
//        } else if (volleyError instanceof ServerError) {
//            PSnackbarManager.show(BaseActivity.INSTANCE, "Warning", "Server Error");
//        } else {
//            PSnackbarManager.show(BaseActivity.INSTANCE, "Warning", "An error occurred. Please try again");
//        }
    }
}
