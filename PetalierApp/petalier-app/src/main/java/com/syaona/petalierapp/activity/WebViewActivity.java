package com.syaona.petalierapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.felipecsl.gifimageview.library.GifImageView;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PResponseErrorListener;
import com.syaona.petalierapp.core.PResponseListener;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class WebViewActivity extends BaseActivity {

    private WebView mWebView;
    private ImageView mImageLoading;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);


        /* Initialize toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(WebViewActivity.this, DesignBoxActivity.class));
//                finish();
                onBackPressed();
            }
        });

        TextView mTxtTitle = (TextView) toolbar.findViewById(R.id.txtsub);
        mTxtTitle.setText("Color Calendar");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);


        mImageLoading = (ImageView) findViewById(R.id.loading);

        Glide.with(AppController.getInstance())
                .load(R.drawable.loading)
                .asGif()
                .placeholder(R.drawable.loading)
                .crossFade()
                .into(mImageLoading);


        mWebView = (WebView) findViewById(R.id.default_webview);
        mWebView.getSettings().setJavaScriptEnabled(true);


        requestApiColorCalendar();


//        mWebView.setWebViewClient(new CustomWebViewClient());
//        mWebView.loadUrl("https://www.google.com.ph");
//        this.finish();

    }

    private class CustomWebViewClient extends WebViewClient {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            stopAnimation();

        }
    }

    public void stopAnimation(){
        mImageLoading.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(WebViewActivity.this, DesignBoxActivity.class));
        finish();
    }


    public void requestApiColorCalendar(){

        HashMap<String, String> params = new HashMap<>();

        PRequest request = new PRequest(PRequest.apiMethodGetColorCalendar, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {

                                mUrl = jsonObject.getJSONObject("Data").getJSONObject("colorCalendar").getString("link");
                                mWebView.loadUrl(mUrl);

                                stopAnimation();

                            } else {
//                                Log.i("error", jsonObject.getJSONObject("Data").getString("alert"));
                                stopAnimation();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopAnimation();
                        }
                    }
                }, new PResponseErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                stopAnimation();
            }
        });

        request.execute();

    }




}
