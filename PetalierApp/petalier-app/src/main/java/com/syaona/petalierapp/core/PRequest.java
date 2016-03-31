package com.syaona.petalierapp.core;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by smartwavedev on 1/20/16.
 */
public class PRequest {

    /* Get */


    /* Post */




    private int mReqMethod;
    private String mUrl;
    private String mResource;
    private JSONObject mJsonParams = null;
    private HashMap<String, String> mParams;
    private PResponseListener mResponseListener;
    private PResponseErrorListener mResponseErrorListener;


    public PRequest(String resource,
                    HashMap<String, String> params,
                    PResponseListener pResListener,
                    PResponseErrorListener pResErrorListener) {
        this.mResource = resource;
        this.mParams = params;
        this.mResponseListener = pResListener;
        this.mResponseErrorListener = pResErrorListener;

    }

//    public static String getApiRootForResource() {
//        if (PSConfiguration.DEBUG) {
//            return PSConfiguration.testURL;
//        } else {
//            return PSConfiguration.liveURL;
//        }
//    }

    public void execute() {
//        this.mUrl = getApiRootForResource() + mResource;
        this.mReqMethod = getRequestMethod(mResource);


//        CustomRequest req = createJsonRequest();
//        if (mResource.equalsIgnoreCase(kApiMethodGetVenues)) {
//            req.setRetryPolicy(new DefaultRetryPolicy(10000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        }
//
//        if(mResource.equalsIgnoreCase(kApiMethodBuyPartyPoints)){
//            req.setRetryPolicy(new DefaultRetryPolicy(15000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        }
//
//        if(mResource.equalsIgnoreCase(kApiMethodCheckoutViaPartyPoints)){
//            req.setRetryPolicy(new DefaultRetryPolicy(15000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        }
//
//        if(mResource.equalsIgnoreCase(kApiMethodGetUserActivitiesv2)){
//            req.setRetryPolicy(new DefaultRetryPolicy(10000,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        }

//        AppController.getRequestQueue().addToRequestQueue(req, mResource);
    }

//    private CustomRequest createJsonRequest() {
//
//        PDebug.logDebug("PRequest", "Request url: " + mUrl);
//
//        return new CustomRequest(mReqMethod, mUrl, mParams, mResponseListener, mResponseErrorListener) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                //Add session token header
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Session-Token", PSSharedPreferences.getSomeStringValue(AppController.getInstance(), "session_token"));
//                return headers;
//            }
//        };
//    }

    private int getRequestMethod(String resource) {
        if (
                resource.contains("get")
                ) {
            addGetParams();
            return Request.Method.GET;
        } else {
            return Request.Method.POST;
        }
    }

    private void addGetParams() {
        mUrl = mUrl + "?";
        Iterator it = mParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            mUrl = mUrl + pair.getKey() + "=" + pair.getValue() + "&";
            it.remove(); // avoids a ConcurrentModificationException
        }
    }


}