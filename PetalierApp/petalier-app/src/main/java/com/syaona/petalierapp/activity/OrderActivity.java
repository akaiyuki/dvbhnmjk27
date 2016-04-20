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

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.fragment.BillingInfoFragment;
import com.syaona.petalierapp.fragment.DeliveryDetailsFragment;

public class OrderActivity extends BaseActivity {

    public static OrderActivity INSTANCE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

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


        PEngine.switchFragment(INSTANCE, new DeliveryDetailsFragment(), getFrameLayout());



    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

}
