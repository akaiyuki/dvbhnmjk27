package com.syaona.petalierapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PResponseErrorListener;
import com.syaona.petalierapp.core.PResponseListener;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class BillingInfoFragment extends Fragment {

    private EditText mEditFirstName;
    private EditText mEditLastName;
    private EditText mEditEmail;
    private EditText mEditStreet;
    private EditText mEditUnit;
    private EditText mEditTown;
    private EditText mEditCity;
    private EditText mEditPostal;
    private EditText mEditOccupation;
    private EditText mEditAge;
    private EditText mEditRelationship;
    private EditText mEditHear;
    private EditText mEditContact;
    private Button mButtonCheckout;
    private EditText mEditCompany;


    public BillingInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        mButtonCheckout = (Button) view.findViewById(R.id.btncheckout);
        mButtonCheckout.setTypeface(Fonts.gothambookregular);
        mButtonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestApiBillingInfo();
            }
        });

        mEditFirstName = (EditText) view.findViewById(R.id.edit_firstname);
        mEditLastName = (EditText) view.findViewById(R.id.edit_lastname);
        mEditContact = (EditText) view.findViewById(R.id.edit_contact);
        mEditEmail = (EditText) view.findViewById(R.id.edit_email);
        mEditStreet = (EditText) view.findViewById(R.id.edit_street);
        mEditUnit = (EditText) view.findViewById(R.id.edit_unit);
        mEditTown = (EditText) view.findViewById(R.id.edit_town);
        mEditCity = (EditText) view.findViewById(R.id.edit_state);
        mEditPostal = (EditText) view.findViewById(R.id.edit_postal);
        mEditOccupation = (EditText) view.findViewById(R.id.edit_occupation);
        mEditAge = (EditText) view.findViewById(R.id.edit_age);
        mEditRelationship = (EditText) view.findViewById(R.id.edit_relationship);
        mEditHear = (EditText) view.findViewById(R.id.edit_hear);
        mEditCompany = (EditText) view.findViewById(R.id.edit_company);


        TextView txtDefault = (TextView) view.findViewById(R.id.txtdefault);
        txtDefault.setTypeface(Fonts.gothambookregular);

        TextView txtNewBilling = (TextView) view.findViewById(R.id.txtnewbilling);
        txtNewBilling.setTypeface(Fonts.gothambookregular);

        TextView txtFname = (TextView) view.findViewById(R.id.txtfirstname);
        txtFname.setTypeface(Fonts.gothambookregular);

        TextView txtLname = (TextView) view.findViewById(R.id.txtlastname);
        txtLname.setTypeface(Fonts.gothambookregular);

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

        TextView txtCompany = (TextView) view.findViewById(R.id.txtcompany);
        txtCompany.setTypeface(Fonts.gothambookregular);



        return view;
    }



    public void requestApiBillingInfo() {

        HashMap<String, String> params = new HashMap<>();
        params.put("firstName", mEditFirstName.getText().toString());
        params.put("lastName", mEditLastName.getText().toString());
        params.put("id", PSharedPreferences.getSomeStringValue(AppController.getInstance(), "user_id"));
        params.put("phone", mEditContact.getText().toString());
        params.put("country", "Philippines");
        params.put("street", mEditStreet.getText().toString());
        params.put("address", mEditUnit.getText().toString());
        params.put("city", mEditCity.getText().toString());
        params.put("state", mEditTown.getText().toString());
        params.put("postcode", mEditPostal.getText().toString());
        params.put("company", mEditCompany.getText().toString());
        params.put("email", mEditEmail.getText().toString());



        PRequest request = new PRequest(PRequest.apiMethodPostBillingInfo, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {

                                PSharedPreferences.saveJsonToSharedPref(jsonObject.getJSONObject("Data"), "");

                                String contact = jsonObject.getJSONObject("Data").getJSONObject("billing").getJSONObject("billing").getString("billing_phone");
                                String email = jsonObject.getJSONObject("Data").getJSONObject("billing").getJSONObject("billing").getString("billing-email");
                                String street = jsonObject.getJSONObject("Data").getJSONObject("billing").getJSONObject("billing").getString("billing_address_1");
                                String unit = jsonObject.getJSONObject("Data").getJSONObject("billing").getJSONObject("billing").getString("billing_address_2");
                                String city = jsonObject.getJSONObject("Data").getJSONObject("billing").getJSONObject("billing").getString("billing_city");
                                String state = jsonObject.getJSONObject("Data").getJSONObject("billing").getJSONObject("billing").getString("billing_state");
                                String postCode = jsonObject.getJSONObject("Data").getJSONObject("billing").getJSONObject("billing").getString("billing_postcode");

                                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"contact",contact);
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"email",email);
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"street",street);
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"unit",unit);
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"city",city);
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"state",state);
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "postal_code", postCode);

                                PEngine.switchFragment((BaseActivity) getActivity(), new DeliveryDetailsFragment(), ((BaseActivity) getActivity()).getFrameLayout());

//                                Log.i("billing_info", fName+lName+contact+email+city);
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
