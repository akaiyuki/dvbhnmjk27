package com.syaona.petalierapp.fragment;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PDatePicker;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.view.Fonts;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeliveryDetailsFragment extends Fragment {

    private EditText mEditDelDate;
    private EditText mEditInstructions;
    private EditText mEditContact;
    private EditText mEditEmail;
    private EditText mEditStreet;
    private EditText mEditUnit;
    private EditText mEditTown;
    private EditText mEditLandmark;
//    private EditText mEditPostal;


    public DeliveryDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_details, container, false);

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
        mTxtTitle.setText("Delivery Details");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        Button btnCheckout = (Button) view.findViewById(R.id.next);
        btnCheckout.setTypeface(Fonts.gothambookregular);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Singleton.getSelectedDay() != 1){
                    PEngine.switchFragment((BaseActivity) getActivity(), new SummaryOrderFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                }


            }
        });

        Button btnOrderMore = (Button) view.findViewById(R.id.btnorder);
        btnOrderMore.setTypeface(Fonts.gothambookregular);



        mEditDelDate = (EditText) view.findViewById(R.id.edit_deldate);
        mEditInstructions = (EditText) view.findViewById(R.id.edit_instructions);
        mEditContact = (EditText) view.findViewById(R.id.edit_contact);
        mEditEmail = (EditText) view.findViewById(R.id.edit_email);
        mEditStreet = (EditText) view.findViewById(R.id.edit_street);
        mEditUnit = (EditText) view.findViewById(R.id.edit_unit);
        mEditTown = (EditText) view.findViewById(R.id.edit_town);
        mEditLandmark = (EditText) view.findViewById(R.id.edit_landmark);
//        mEditPostal = (EditText) view.findViewById(R.id.edit_postal);


        TextView txtDeliveryDate = (TextView) view.findViewById(R.id.txtdeldate);
        txtDeliveryDate.setTypeface(Fonts.gothambold);

        TextView txtSpecial = (TextView) view.findViewById(R.id.txtspecial);
        txtSpecial.setTypeface(Fonts.gothambold);

//        TextView txtFullname = (TextView) view.findViewById(R.id.txtfullname);
//        txtFullname.setTypeface(Fonts.gothambold);

        TextView txtContact = (TextView) view.findViewById(R.id.txtcontact);
        txtContact.setTypeface(Fonts.gothambold);

        TextView txtEmail = (TextView) view.findViewById(R.id.txtemail);
        txtEmail.setTypeface(Fonts.gothambold);

        TextView txtLocation = (TextView) view.findViewById(R.id.txtlocation);
        txtLocation.setTypeface(Fonts.gothambold);

        TextView txtGPRS = (TextView) view.findViewById(R.id.txtgprs);
        txtGPRS.setTypeface(Fonts.gothambookregular);

        TextView txtStreet = (TextView) view.findViewById(R.id.txtstreet);
        txtStreet.setTypeface(Fonts.gothambold);

        TextView txtUnit = (TextView) view.findViewById(R.id.txtunit);
        txtUnit.setTypeface(Fonts.gothambold);

        TextView txtTown = (TextView) view.findViewById(R.id.txttown);
        txtTown.setTypeface(Fonts.gothambold);

        TextView txtLandmark = (TextView) view.findViewById(R.id.txtlandmark);
        txtLandmark.setTypeface(Fonts.gothambold);

//        TextView txtPostal = (TextView) view.findViewById(R.id.txtpostal);
//        txtPostal.setTypeface(Fonts.gothambold);

        TextView txtFirstName = (TextView) view.findViewById(R.id.txtfname);
        txtFirstName.setTypeface(Fonts.gothambold);

        TextView txtLastName = (TextView) view.findViewById(R.id.txtlname);
        txtLastName.setTypeface(Fonts.gothambold);


//        populateEditTexts();


        mEditDelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PDatePicker datePicker = new PDatePicker((BaseActivity) getActivity(), (EditText)view);

            }
        });


        return view;
    }



//    public void populateEditTexts(){
//        mEditDelDate.setText("");
//        mEditInstructions.setText("");
//        mEditContact.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"contact"));
//        mEditEmail.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"email"));
//        mEditStreet.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"street"));
//        mEditUnit.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"unit"));
//        mEditTown.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"city"));
//        mEditLandmark.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"state"));
//        mEditPostal.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"postal_code"));


//    }






}
