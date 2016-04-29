package com.syaona.petalierapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.fragment.ChooseCollectionFragment;
import com.syaona.petalierapp.fragment.SendNoteFragment;

public class MainActivity extends BaseActivity {

    public static MainActivity INSTANCE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        INSTANCE = this;

          /* Initialize Frame Layout */
        setFrameLayout(R.id.framelayout);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #DDDDDD
            int actionBarColor = Color.parseColor("#dbab40");
            tintManager.setStatusBarTintColor(actionBarColor);


//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));



        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            // Holo light action bar color is #DDDDDD
            int actionBarColor = Color.parseColor("#dbab40");
            tintManager.setStatusBarTintColor(actionBarColor);
        }

//        PEngine.switchFragment(INSTANCE, new ChooseCollectionFragment(), getFrameLayout());




        /* switch fragment */
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            Bundle bundle = new Bundle();

            Fragment newFragment = null;
            if (extras.getString("goto").equalsIgnoreCase("collection")) {
                newFragment = new ChooseCollectionFragment();
            } else if (extras.getString("goto").equalsIgnoreCase("send_note")) {
                newFragment = new SendNoteFragment();
            }

            assert newFragment != null;
            newFragment.setArguments(bundle);
            PEngine.switchFragment(INSTANCE, newFragment, getFrameLayout());

        }







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    public void startAnim(){
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.VISIBLE);
    }

    public void stopAnim(){
        findViewById(R.id.avloadingIndicatorView).setVisibility(View.GONE);
    }

}
