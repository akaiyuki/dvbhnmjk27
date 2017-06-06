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
import com.bumptech.glide.Glide;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.LoginActivity;
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
import java.util.GregorianCalendar;
import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

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

    private int orderPlacement;

    private String delAddress;
    private GifImageView mImageLoading;

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

        mEditFname = (EditText) view.findViewById(R.id.edit_fname);
        mEditLname = (EditText) view.findViewById(R.id.edit_lname);
        mEditContact = (EditText) view.findViewById(R.id.edit_contact);
        mEditLandmark = (EditText) view.findViewById(R.id.edit_landmark);
        mEditTown = (EditText) view.findViewById(R.id.edit_town);
        mEditEmail = (EditText) view.findViewById(R.id.edit_email);
        mEditDelDate = (EditText) view.findViewById(R.id.edit_deldate);
        mEditInstructions = (EditText) view.findViewById(R.id.edit_instructions);


        mImageLoading = (GifImageView) view.findViewById(R.id.loading);
        mImageLoading.setVisibility(View.GONE);

//        Glide.with(AppController.getInstance())
//                .load(R.drawable.loading)
//                .asGif()
//                .placeholder(R.drawable.loading)
//                .crossFade()
//                .into(mImageLoading);


        Button btnCheckout = (Button) view.findViewById(R.id.next);
        btnCheckout.setTypeface(Fonts.gothambookregular);
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (mEditFname.getText().length() != 0 &&
                        mEditLname.getText().length() != 0 &&
                        mEditContact.getText().length() != 0 &&
                        mEditLandmark.getText().length() != 0 &&
                        mEditTown.getText().length() != 0 &&
                        Singleton.getSelectedDay() != 1 &&
                        mEditDelDate.getText().length() != 0) {

                    delDate = mEditDelDate.getText().toString();
                    instructions = mEditInstructions.getText().toString();
                    contact = mEditContact.getText().toString();
                    email = mEditEmail.getText().toString();
                    town = mEditTown.getText().toString();
                    landmark = mEditLandmark.getText().toString();
                    fName = mEditFname.getText().toString();
                    lName = mEditLname.getText().toString();

                    PSharedPreferences.setSomeStringValue(AppController.getInstance(), "deldate", delDate);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"instructions", instructions);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(), "contact", contact);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"email", email);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"town",town);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"landmark",landmark);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"fname",fName);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"lname",lName);


                    Log.i("deliverydetails", delDate + " " + instructions + " " + contact + " " + town + " " + landmark + " " + fName + " " + lName + " " +
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(), "id") + " " +
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(), "quantity") + " " +
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(), "note") + " " +
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(), "box_color") + " " +
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(), "card") + " " +
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(), "id"));




                    bitmap = Singleton.getImage3D();

                    Log.i("deldate", mEditDelDate.getText().toString());

                    if (bitmap != null) {

                        orderPlacement = 1;
                        new AddItemTask().execute();
                        mImageLoading.setVisibility(View.VISIBLE);

                    }

                } else {

                    if (Singleton.getSelectedDay() == 1){
                        PDialog.showDialogError((BaseActivity) getActivity(),"Invalid Delivery Date");
                    }
                    else {
                            PDialog.showDialogError((BaseActivity) getActivity(), "Please Complete Delivery Information");
                        }
                }


            }
        });

        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "deldate", "");
        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "instructions", "");
        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "contact", "");
        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "email", "");
        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "street", "");
        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "unit", "");
        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "town", "");
        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "landmark", "");
        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "fname", "");
        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "lname", "");


        Button btnOrderMore = (Button) view.findViewById(R.id.btnorder);
        btnOrderMore.setTypeface(Fonts.gothambookregular);
        btnOrderMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (mEditFname.getText().length() != 0 &&
                        mEditLname.getText().length() != 0 &&
                        mEditContact.getText().length() != 0 &&
                        mEditLandmark.getText().length() != 0 &&
                        mEditTown.getText().length() != 0 &&
                        Singleton.getSelectedDay() != 1 &&
                        mEditDelDate.getText().length() != 0
                        ) {

                    delDate = mEditDelDate.getText().toString();
                    instructions = mEditInstructions.getText().toString();
                    contact = mEditContact.getText().toString();
                    email = mEditEmail.getText().toString();
                    town = mEditTown.getText().toString();
                    landmark = mEditLandmark.getText().toString();
                    fName = mEditFname.getText().toString();
                    lName = mEditLname.getText().toString();

                    bitmap = Singleton.getImage3D();

                    PSharedPreferences.setSomeStringValue(AppController.getInstance(), "deldate", delDate);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"instructions", instructions);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(), "contact", contact);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"email", email);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"town",town);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"landmark",landmark);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"fname",fName);
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"lname",lName);

                    Log.i("deliverydetails",delDate+" "+instructions+" "+contact+" "+town+" "+landmark+" "+fName+" "+lName+" "+
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(),"id")+" "+
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(),"quantity")+" "+
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(), "note")+" "+
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(),"box_color")+" "+
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(), "card")+" "+
                            PSharedPreferences.getSomeStringValue(AppController.getInstance(),"id"));

                    if (bitmap != null) {

                        orderPlacement = 0;
                        new AddItemTask().execute();
                        mImageLoading.setVisibility(View.VISIBLE);

                    }

                } else {

                    if (Singleton.getSelectedDay() == 1){
                        PDialog.showDialogError((BaseActivity) getActivity(),"Invalid Delivery Date");
                    } else {
                        PDialog.showDialogError((BaseActivity) getActivity(), "Please Complete Delivery Information");
                    }
                }

            }
        });


        editFontsLabels(view);

        mEditDelDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PDatePicker datePicker = new PDatePicker((BaseActivity) getActivity(), (EditText)view);

            }
        });



        TextView mText1 = (TextView) view.findViewById(R.id.txt1);
        mText1.setTypeface(Fonts.gothambookregular);

        TextView mText2 = (TextView) view.findViewById(R.id.txt2);
        mText2.setTypeface(Fonts.gothambookregular);

        TextView mText3 = (TextView) view.findViewById(R.id.txt3);
        mText3.setTypeface(Fonts.gothambookregular);

        TextView mText4 = (TextView) view.findViewById(R.id.txtdisclaimer);
        mText4.setTypeface(Fonts.gothambookregular);





        return view;
    }


    public void editFontsLabels(View view){
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
    }


    public void stopAnimation(){
        mImageLoading.setVisibility(View.GONE);
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
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] data = bos.toByteArray();
                entity.addPart("deliveryDate", new StringBody(delDate));
                entity.addPart("specialInstructions", new StringBody(instructions));
                entity.addPart("cardLink", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "card")));
                entity.addPart("firstName", new StringBody(fName));
                entity.addPart("lastName", new StringBody(lName));
                entity.addPart("contactNumber", new StringBody(contact));
                entity.addPart("deliveryAddress", new StringBody(town));
                entity.addPart("landmark", new StringBody(landmark));
                entity.addPart("productId", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"id")));

                entity.addPart("image_base_64", new ByteArrayBody(data,
                        "3Dimage.png"));
                entity.addPart("quantity", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"quantity")));
                entity.addPart("note", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "note")));
                entity.addPart("boxColor", new StringBody(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"box_color")));


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

            /* stop loading animation */
            stopAnimation();

            try {

                if (sResponse != null) {
                    JSONObject JResponse = new JSONObject(sResponse);

                    if (JResponse.getInt("Status") == StatusResponse.STATUS_SUCCESS){

                        if (orderPlacement == 0) {

                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("goto", "collection");
                            startActivity(intent);
                            getActivity().finish();

                        } else {
                            PEngine.switchFragment((BaseActivity) getActivity(), new BillingInfoFragment(), ((BaseActivity) getActivity()).getFrameLayout());

                        }

                    } else {
                            // Initialize a new GradientDrawable
                            GradientDrawable gd = new GradientDrawable();
                            // Specify the shape of drawable
                            gd.setShape(GradientDrawable.RECTANGLE);
                            // Set the fill color of drawable
                            gd.setColor(Color.TRANSPARENT); // make the background transparent
                            // Create a 2 pixels width red colored border for drawable
                            gd.setStroke(2, Color.RED); // border width and color
                            // Make the border rounded
                            gd.setCornerRadius(15.0f); // border corner radius
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                mEditDelDate.setBackground(gd);
                            }
                        PDialog.showDialogError((BaseActivity) getActivity(),JResponse.getJSONObject("Data").getString("alert"));
                    }
                    Log.i("uploadimage", JResponse.getJSONObject("Data").getString("alert"));
                }
            } catch (Exception e) {
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }


    }






}
