package com.syaona.petalierapp.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PBirthdayPicker;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PResponseErrorListener;
import com.syaona.petalierapp.core.PResponseListener;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.dialog.PDialog;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.view.CircleTransform;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {

    private EditText mFirstName;
    private EditText mLastName;
    private EditText mBirthday;
    private GifImageView mImageLoading;


    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);


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
        mTxtTitle.setText("Edit Profile");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);


        ImageView mProfilePic = (ImageView) view.findViewById(R.id.profilepic);

        if (!PSharedPreferences.getSomeStringValue(AppController.getInstance(),"profile_pic").isEmpty()){
            Picasso.with(getActivity())
                    .load(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"profile_pic"))
                    .transform(new CircleTransform())
                    .fit()
                    .into(mProfilePic);
        }

        mFirstName = (EditText) view.findViewById(R.id.edit_firstname);
        mLastName = (EditText) view.findViewById(R.id.edit_lastname);
        mBirthday = (EditText) view.findViewById(R.id.edit_birthday);

        mBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PBirthdayPicker datePicker = new PBirthdayPicker((BaseActivity) getActivity(), (EditText)view);
            }
        });


        mImageLoading = (GifImageView) view.findViewById(R.id.loading);
        mImageLoading.setVisibility(View.GONE);
//        Glide.with(AppController.getInstance())
//                .load(R.drawable.loading)
//                .asGif()
//                .placeholder(R.drawable.loading)
//                .crossFade()
//                .into(mImageLoading);



        Button mButtonUpdate = (Button) view.findViewById(R.id.btnupdate);
        mButtonUpdate.setTypeface(Fonts.gothambookregular);
        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageLoading.setVisibility(View.VISIBLE);
                requestApiUpdateProfile();
            }
        });




        return view;
    }



    public void stopAnimation(){
        mImageLoading.setVisibility(View.GONE);
    }




    public void requestApiUpdateProfile() {


        HashMap<String, String> params = new HashMap<>();
        params.put("id", PSharedPreferences.getSomeStringValue(AppController.getInstance(),"user_id"));
        params.put("firstName", mFirstName.getText().toString());
        params.put("lastName", mLastName.getText().toString());
        params.put("birthDate", mBirthday.getText().toString());

        PRequest request = new PRequest(PRequest.apiMethodUpdateProfile, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {

                                Log.d("update_status", "success");

                                showDialogSuccess("Profile updated successfully");

                            } else if (jsonObject.getInt("Status") == 701){
                                PDialog.showDialogError((BaseActivity) getActivity(),"Please complete profile information");
                            }

                            stopAnimation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopAnimation();
                        }
                    }
                }, new PResponseErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                stopAnimation();
            }
        });

        request.execute();
    }

    public void showDialogSuccess(String message){

        final Dialog dialog = new Dialog(MainActivity.INSTANCE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView mTextTitle = (TextView) dialog.findViewById(R.id.txterror);
        mTextTitle.setText(message);
        mTextTitle.setTypeface(Fonts.gothambookregular);


        LinearLayout mButtonDone = (LinearLayout) dialog.findViewById(R.id.button_ok);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();

//                PEngine.switchFragment((BaseActivity) getActivity(), new ProfileFragment(), ((BaseActivity) getActivity()).getFrameLayout());

//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                intent.putExtra("goto", "profile");
//                startActivity(intent);
//                getActivity().finish();
                getActivity().onBackPressed();


            }
        });

        dialog.show();


    }



}
