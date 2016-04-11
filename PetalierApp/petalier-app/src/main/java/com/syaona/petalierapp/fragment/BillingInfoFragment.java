package com.syaona.petalierapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.view.Fonts;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillingInfoFragment extends Fragment {


    public BillingInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_billing_info, container, false);

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
                OrderActivity.INSTANCE.onBackPressed();
            }
        });

        TextView mTxtTitle = (TextView) toolbar.findViewById(R.id.txtsub);
        mTxtTitle.setText("Billing Information");
        mTxtTitle.setTypeface(Fonts.gothambookregular);


        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        Button btnCheckout = (Button) view.findViewById(R.id.btncheckout);
        btnCheckout.setTypeface(Fonts.gothambookregular);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PEngine.switchFragment((BaseActivity) getActivity(), new DeliveryDetailsFragment(), ((BaseActivity) getActivity()).getFrameLayout());

            }
        });

        TextView txtDefault = (TextView) view.findViewById(R.id.txtdefault);
        txtDefault.setTypeface(Fonts.gothambookregular);

        TextView txtNewBilling = (TextView) view.findViewById(R.id.txtnewbilling);
        txtNewBilling.setTypeface(Fonts.gothambookregular);

        TextView txtFullname = (TextView) view.findViewById(R.id.txtfullname);
        txtFullname.setTypeface(Fonts.gothambookregular);

        TextView txtContact = (TextView) view.findViewById(R.id.txtcontact);
        txtContact.setTypeface(Fonts.gothambookregular);

        TextView txtEmail = (TextView) view.findViewById(R.id.txtemail);
        txtEmail.setTypeface(Fonts.gothambookregular);

        TextView txtLocation = (TextView) view.findViewById(R.id.txtlocation);
        txtLocation.setTypeface(Fonts.gothambold);

        TextView txtGPRS = (TextView) view.findViewById(R.id.txtgprs);
        txtGPRS.setTypeface(Fonts.gothambookregular);

        TextView txtStreet = (TextView) view.findViewById(R.id.txtstreet);
        txtStreet.setTypeface(Fonts.gothambookregular);

        TextView txtUnit = (TextView) view.findViewById(R.id.txtunit);
        txtUnit.setTypeface(Fonts.gothambookregular);

        TextView txtTown = (TextView) view.findViewById(R.id.txttown);
        txtTown.setTypeface(Fonts.gothambookregular);

        TextView txtState = (TextView) view.findViewById(R.id.txtstate);
        txtState.setTypeface(Fonts.gothambookregular);

        TextView txtPostal = (TextView) view.findViewById(R.id.txtpostal);
        txtPostal.setTypeface(Fonts.gothambookregular);

        TextView txtOtherInfo = (TextView) view.findViewById(R.id.txtotherinfo);
        txtOtherInfo.setTypeface(Fonts.gothambold);

        TextView txtOccupation = (TextView) view.findViewById(R.id.txtoccupation);
        txtOccupation.setTypeface(Fonts.gothambookregular);

        TextView txtAge = (TextView) view.findViewById(R.id.txtage);
        txtAge.setTypeface(Fonts.gothambookregular);

        TextView txtRel = (TextView) view.findViewById(R.id.txtrel);
        txtRel.setTypeface(Fonts.gothambookregular);

        TextView txtAbout = (TextView) view.findViewById(R.id.txtabout);
        txtAbout.setTypeface(Fonts.gothambookregular);



        return view;
    }

}
