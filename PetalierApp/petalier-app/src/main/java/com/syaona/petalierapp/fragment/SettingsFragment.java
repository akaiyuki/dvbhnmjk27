package com.syaona.petalierapp.fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.SplashActivity;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PConfiguration;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.view.Fonts;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

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
        mTxtTitle.setText("Settings");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);


        TextView txtRate = (TextView) view.findViewById(R.id.txtrate);
        txtRate.setTypeface(Fonts.gothambookregular);

        TextView txtInquire = (TextView) view.findViewById(R.id.txtinquire);
        txtInquire.setTypeface(Fonts.gothambookregular);

        TextView txtContact = (TextView) view.findViewById(R.id.txtcontact);
        txtContact.setTypeface(Fonts.gothambookregular);

        Button btnLogout = (Button) view.findViewById(R.id.btnlogout);
        btnLogout.setTypeface(Fonts.gothambookregular);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                PSharedPreferences.clearAllPreferences();
                startActivity(new Intent(getActivity(), SplashActivity.class));
            }
        });

        ImageButton buttonRate = (ImageButton) view.findViewById(R.id.button_rate);
        buttonRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showRateApp();
            }
        });

        ImageButton buttonInquire = (ImageButton) view.findViewById(R.id.button_inquire);
        buttonInquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent send = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode("petalier@yahoo.com") +
                        "?subject=" + Uri.encode("Inquire");
                Uri uri = Uri.parse(uriText);

                send.setData(uri);
                startActivity(Intent.createChooser(send, "Send mail..."));

//                getActivity().finish();

            }
        });

        ImageButton buttonContact = (ImageButton) view.findViewById(R.id.button_contact);
        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String callForwardString = "+639778417738";
                Intent intentCallForward = new Intent(Intent.ACTION_CALL); // ACTION_CALL
                Uri uri2 = Uri.fromParts("tel", callForwardString, "#");
                intentCallForward.setData(uri2);
                startActivity(intentCallForward);

            }
        });


        return view;
    }

    public void showRateApp(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(PConfiguration.playstoreInstalledURL));

        if(!startActivityRate(intent)){
            intent.setData(Uri.parse(PConfiguration.rateURL));

            if(!startActivityRate(intent)){
                Toast.makeText(getActivity(), "Could not open Android Playstore. Please try install the app.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean startActivityRate(Intent intentStart){
        try
        {
            startActivity(intentStart);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }

}
