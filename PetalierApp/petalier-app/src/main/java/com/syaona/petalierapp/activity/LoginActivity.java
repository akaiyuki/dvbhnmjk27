package com.syaona.petalierapp.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.felipecsl.gifimageview.library.GifImageView;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.fragment.LoginFragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class LoginActivity extends BaseActivity {

    public static LoginActivity INSTANCE = null;
    public GifImageView gifImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        INSTANCE = this;

         /* Initialize Frame Layout */
        setFrameLayout(R.id.framelayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #DDDDDD
            int actionBarColor = Color.parseColor("#dbab40");
            tintManager.setStatusBarTintColor(actionBarColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #DDDDDD
            int actionBarColor = Color.parseColor("#dbab40");
            tintManager.setStatusBarTintColor(actionBarColor);
        }

        PEngine.switchFragment(INSTANCE, new LoginFragment(), getFrameLayout());

        gifImageView = (GifImageView) findViewById(R.id.gifImageView);

//        InputStream is = this.getResources().openRawResource(R.drawable.loading);

        try {
            InputStream is = getAssets().open("loading.gif");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = new byte[2048];
            int len = 0;
            while ((len = is.read(b, 0, 2048)) != -1) {
                baos.write(b, 0, len);
            }
            baos.flush();
            byte[] bytes = baos.toByteArray();
            gifImageView.setBytes(bytes);
//            gifImageView.startAnimation();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


//        try {
//            //animation.gif is just an example, use the name of your file
//            //that is inside the assets folder.
//
//            InputStream is = getAssets().open("loading.gif");
//            byte[] bytes = new byte[is.available()];
//            is.read(bytes);
//            is.close();
//
//            gifImageView = new GifImageView(this);
//            gifImageView.setBytes(bytes);
//            gifImageView.startAnimation();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }



    }

    public void startAnim(){
//        findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
        try {
            InputStream is = getAssets().open("loading.gif");
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

    }

    public void stopAnim(){
//        findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);

        gifImageView.stopAnimation();
    }


}
