package com.syaona.petalierapp.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.felipecsl.gifimageview.library.GifImageView;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends BaseActivity {

    public static SplashActivity INSTANCE = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        INSTANCE = this;


        final GifImageView gifImageView = (GifImageView) findViewById(R.id.gifImageView);
        try {
            InputStream is = getAssets().open("splash.gif");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[2048];
            int len = 0;
            while ((len = is.read(b, 0, 2048)) != -1) {
                baos.write(b, 0, len);
            }
            baos.flush();
            byte[] bytes = baos.toByteArray();
            gifImageView.setBytes(bytes);
            gifImageView.startAnimation();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        int myTimer = 6000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.INSTANCE, MainActivity.class);
                i.putExtra("goto","collection");
                startActivity(i);
                finish(); // close this activity

                assert gifImageView != null;
                gifImageView.stopAnimation();

            }
        }, myTimer);



    }

}
