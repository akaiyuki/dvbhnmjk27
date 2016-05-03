package com.syaona.petalierapp.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PDatePicker;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PResponseErrorListener;
import com.syaona.petalierapp.core.PResponseListener;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.dialog.PDialog;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.view.Fonts;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;

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
    private Bitmap bitmap;

    private String delDate;
    private String instructions;
    private String contact;
    private String email;
    private String street;
    private String unit;
    private String town;
    private String landmark;
    private String fName;
    private String lName;

    private EditText mEditFname;
    private EditText mEditLname;


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

                if (Singleton.getSelectedDay() != 1) {
                    PEngine.switchFragment((BaseActivity) getActivity(), new BillingInfoFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                }


            }
        });

        Button btnOrderMore = (Button) view.findViewById(R.id.btnorder);
        btnOrderMore.setTypeface(Fonts.gothambookregular);
        btnOrderMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                delDate = mEditDelDate.getText().toString();
                instructions = mEditInstructions.getText().toString();
                contact = mEditContact.getText().toString();
                email = mEditEmail.getText().toString();
                street = mEditStreet.getText().toString();
                unit = mEditUnit.getText().toString();
                town = mEditTown.getText().toString();
                landmark = mEditLandmark.getText().toString();
                fName = mEditFname.getText().toString();
                lName = mEditLname.getText().toString();

                Log.i("params", delDate + " " + instructions + " " + contact + " " + email + " " + street + " " + unit + " " + town + " " + landmark + " " + fName + " " + lName);


                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "deldate", delDate);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"instructions", instructions);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "contact", contact);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"email", email);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"street",street);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"unit",unit);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"town",town);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"landmark",landmark);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"fname",fName);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"lname",lName);

                Log.i("savedpreferences",PSharedPreferences.getSomeStringValue(AppController.getInstance(),"quantity")+" "+
                PSharedPreferences.getSomeStringValue(AppController.getInstance(),"id")+" "+
                PSharedPreferences.getSomeStringValue(AppController.getInstance(),"note")+" "+
                PSharedPreferences.getSomeStringValue(AppController.getInstance(),"card"));

                new AddItemTask().execute();

            }
        });



        mEditDelDate = (EditText) view.findViewById(R.id.edit_deldate);
        mEditInstructions = (EditText) view.findViewById(R.id.edit_instructions);
        mEditContact = (EditText) view.findViewById(R.id.edit_contact);
        mEditEmail = (EditText) view.findViewById(R.id.edit_email);
        mEditStreet = (EditText) view.findViewById(R.id.edit_street);
        mEditUnit = (EditText) view.findViewById(R.id.edit_unit);
        mEditTown = (EditText) view.findViewById(R.id.edit_town);
        mEditLandmark = (EditText) view.findViewById(R.id.edit_landmark);
//        mEditPostal = (EditText) view.findViewById(R.id.edit_postal);
        mEditFname = (EditText) view.findViewById(R.id.edit_fname);
        mEditLname = (EditText) view.findViewById(R.id.edit_lname);



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


        bitmap = Singleton.getImage3D();
//        delDate = mEditDelDate.getText().toString();
//        instructions = mEditInstructions.getText().toString();
//        contact = mEditContact.getText().toString();
//        email = mEditEmail.getText().toString();
//        street = mEditStreet.getText().toString();
//        unit = mEditUnit.getText().toString();
//        town = mEditTown.getText().toString();
//        landmark = mEditLandmark.getText().toString();
//        fName = mEditFname.getText().toString();
//        lName = mEditLname.getText().toString();


//        PDialog.displayBitmap(bitmap, (BaseActivity) getActivity());




        return view;
    }


    class AddItemTask extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected String doInBackground(Void... unused) {

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(
                        PRequest.getApiRootForResource() + PRequest.apiMethodPostAddItem);

                httpPost.addHeader("petalier_session_token", PSharedPreferences.getSomeStringValue(AppController.getInstance(), "session_token"));

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                byte[] data = bos.toByteArray();
                entity.addPart("deliveryDate", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"deldate")));
                entity.addPart("specialInstructions", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"instructions")));
                entity.addPart("cardLink", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "card")));
                entity.addPart("firstName", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"fname")));
                entity.addPart("lastName", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"lname")));
                entity.addPart("contactNumber", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"contact")));
                entity.addPart("street", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"street")));
                entity.addPart("subdivision", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"unit")));
                entity.addPart("city", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"town")));
                entity.addPart("landMark", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"landmark")));
                entity.addPart("country", new StringBody("PH"));
                entity.addPart("productId", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"id")));

                entity.addPart("image_base_64", new ByteArrayBody(data,
                        "3Dimage.jpg"));
                entity.addPart("quantity", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"quantity")));
                entity.addPart("note", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "note")));

                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost,
                        localContext);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));

                String sResponse = reader.readLine();
                return sResponse;
            } catch (Exception e) {

                Log.e(e.getClass().getName(), e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... unused) {
        }

        @Override
        protected void onPostExecute(String sResponse) {


            try {

                if (sResponse != null) {
                    JSONObject JResponse = new JSONObject(sResponse);

                    if (JResponse.getInt("Status") == StatusResponse.STATUS_SUCCESS){

//                        Log.i("successupload", JResponse.getJSONObject("Data").getJSONObject("cart").getString("image_base_64"));

                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        intent.putExtra("goto","collection");
                        startActivity(intent);
                        getActivity().finish();

                    }

                    Log.i("uploadimage", JResponse.getJSONObject("Data").getString("alert"));
                }
            } catch (Exception e) {
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }


    }






}
