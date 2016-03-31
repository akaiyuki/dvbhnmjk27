package com.syaona.petalierapp.core;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by smartwavedev on 3/30/16.
 */
public class PRequestQueue {
    public static final String TAG = "PRequestQueue";

    private static PRequestQueue mInstance;
    private static Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    protected PRequestQueue(Context context) {

        mContext = context;

        mRequestQueue = getRequestQueue();

        mImageLoader = getImageLoader();

    }

    public static synchronized PRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
//        if (mImageLoader == null) {
//
//            mImageLoader = new ImageLoader(this.mRequestQueue,
//                    new LruBitmapCache(mContext));
//        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {

        try {
            PDebug.logDebug("REQ", "header: " + req.getHeaders());
            PDebug.logDebug("REQ", "body: " + req.getBody().toString());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        } catch (NullPointerException npe) {
            //npe.printStackTrace();
        }

        PDebug.logDebug("REQ", "url: " + req.getUrl());
        PDebug.logDebug("REQ", "method: " + req.getMethod());
        PDebug.logDebug("REQ", "cacheKey: " + req.getCacheKey());
        PDebug.logDebug("REQ", "bodyContentType: " + req.getBodyContentType());

        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);

    }


    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

}
