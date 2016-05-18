package com.syaona.petalierapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.activity.PaypalActivity;
import com.syaona.petalierapp.activity.ProfileActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PDebug;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PResponseErrorListener;
import com.syaona.petalierapp.core.PResponseListener;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaypalFragment extends Fragment {

    private WebView webView;
    private ImageView mImageLoading;

    public PaypalFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_paypal, container, false);

        /* Initialize toolbar */
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle("");

        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        TextView mTxtTitle = (TextView) toolbar.findViewById(R.id.txtsub);
        mTxtTitle.setText("Paypal");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);


        webView = (WebView) view.findViewById(R.id.paypal_webview);

        mImageLoading = (ImageView) view.findViewById(R.id.loading);

        Glide.with(AppController.getInstance())
                .load(R.drawable.loading)
                .asGif()
                .placeholder(R.drawable.loading)
                .crossFade()
                .into(mImageLoading);

        loadPaypalUrl();

        return view;
    }

    public void stopAnimation(){
        mImageLoading.setVisibility(View.GONE);
    }


    private void loadPaypalUrl() {
        webView.setWebViewClient(new PaypalWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(Singleton.getPaypalUrl());

        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(webView, InputMethodManager.SHOW_IMPLICIT);
    }


    private class PaypalWebViewClient extends WebViewClient
    {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            PDebug.logDebug("Paypal", "url: " + url);

            stopAnimation();

            if (url.contains("success")) {



                webView.stopLoading();

                Handler mainHandler = new Handler(AppController.getInstance().getMainLooper());
                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {
//                        Intent intent = new Intent(getActivity(), MainActivity.class);
//                        intent.putExtra("goto", "collection");
//                        startActivity(intent);
//                        getActivity().finish();

                        requestApiClearCart();

                    }
                };
                mainHandler.post(myRunnable);

            }
        }
    }


    public void requestApiClearCart() {

        HashMap<String, String> params = new HashMap<>();
//        params.put("id", PSharedPreferences.getSomeStringValue(AppController.getInstance(), "user_id"));

        PRequest request = new PRequest(PRequest.apiMethodPostClearCart, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {

                                startActivity(new Intent(getActivity(), ProfileActivity.class));
                                getActivity().finish();

                            } else {
                                Log.i("error", jsonObject.getJSONObject("Data").getString("alert"));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new PResponseErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });

        request.execute();
    }



}
