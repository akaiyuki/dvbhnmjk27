package com.syaona.petalierapp.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.dialog.PDialog;
import com.syaona.petalierapp.test.ImageUploadActivity;
import com.syaona.petalierapp.view.Fonts;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderSummaryFragment extends Fragment {


    public OrderSummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);

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
                MainActivity.INSTANCE.onBackPressed();
            }
        });

        TextView mTxtTitle = (TextView) toolbar.findViewById(R.id.txtsub);
        mTxtTitle.setText("Order Summary");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        TextView txtOrder = (TextView) view.findViewById(R.id.txtorder);
        txtOrder.setTypeface(Fonts.gothambold);
        txtOrder.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "order_name"));

        TextView txtDate = (TextView) view.findViewById(R.id.txtdate);
        txtDate.setTypeface(Fonts.gothambookregular);
        txtDate.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "date_created"));

        TextView txtNote = (TextView) view.findViewById(R.id.txtnote);
        txtNote.setTypeface(Fonts.gothambookregular);

        TextView txtUpload = (TextView) view.findViewById(R.id.txtupload);
        txtUpload.setTypeface(Fonts.gothambold);

        TextView txtTotal = (TextView) view.findViewById(R.id.txt_total);
        txtTotal.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "order_total"));

        Button btnDone = (Button) view.findViewById(R.id.btndone);
        btnDone.setTypeface(Fonts.gothambookregular);

        LinearLayout uploadButton = (LinearLayout) view.findViewById(R.id.linear3);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PDialog.showChoosePhoto((BaseActivity) getActivity());

            }
        });



        return view;
    }






}
