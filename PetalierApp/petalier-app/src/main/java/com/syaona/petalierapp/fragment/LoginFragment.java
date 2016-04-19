package com.syaona.petalierapp.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.LoginActivity;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.PConfiguration;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PResponseErrorListener;
import com.syaona.petalierapp.core.PResponseListener;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText mEditEmail;
    private EditText mEditPassword;
    private TextView mTextError;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        mEditEmail = (EditText) view.findViewById(R.id.edit_email);
        mEditPassword = (EditText) view.findViewById(R.id.edit_password);


        Button btnLoginEmail = (Button) view.findViewById(R.id.btnloginemail);
        btnLoginEmail.setTypeface(Fonts.gothambookregular);
        btnLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditEmail.getText().length() != 0 && mEditPassword.getText().length() != 0){
                    requestApiLogin();
                }

            }
        });

//        TextView txtLogin = (TextView) view.findViewById(R.id.txtlogin);
//        txtLogin.setTypeface(Fonts.gothambookregular);

        mTextError = (TextView) view.findViewById(R.id.txterror);

        return view;
    }




    public void requestApiLogin() {

        LoginActivity.INSTANCE.startAnim();

        HashMap<String, String> params = new HashMap<>();
        params.put("username", mEditEmail.getText().toString());
        params.put("password", mEditPassword.getText().toString());

        PRequest request = new PRequest(PRequest.apiMethodPostLogin, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {
                                PSharedPreferences.saveJsonToSharedPref(jsonObject.getJSONObject("Data"), "");

                                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "user_id", jsonObject.getJSONObject("Data").getJSONObject("user").getString("id"));
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "session_token", jsonObject.getJSONObject("Data").getString("session_token"));

                                if (Singleton.getLoginFromMain() == 1){
                                    startActivity(new Intent(getActivity(), MainActivity.class));
                                    getActivity().finish();
                                } else {
                                    startActivity(new Intent(getActivity(), OrderActivity.class));
                                    getActivity().finish();
                                }

                            } else {
                                Log.i("Error login", jsonObject.getJSONObject("Data").getString("alert"));

//                                // Create a border programmatically
//                                ShapeDrawable shape = new ShapeDrawable(new RectShape());
//                                shape.getPaint().setColor(Color.RED);
//                                shape.getPaint().setStyle(Paint.Style.STROKE);
//                                shape.getPaint().setStrokeWidth(3);
//
//                                // Assign the created border to EditText widget
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                    mEditEmail.setBackground(shape);
//                                    mEditPassword.setBackground(shape);
//                                }

//                                // Initialize a new GradientDrawable
//                                GradientDrawable gd = new GradientDrawable();
//
//                                // Specify the shape of drawable
//                                gd.setShape(GradientDrawable.RECTANGLE);
//
//                                // Set the fill color of drawable
//                                gd.setColor(Color.TRANSPARENT); // make the background transparent
//
//                                // Create a 2 pixels width red colored border for drawable
//                                gd.setStroke(2, Color.RED); // border width and color
//
//                                // Make the border rounded
//                                gd.setCornerRadius(15.0f); // border corner radius
//
//                                // Finally, apply the GradientDrawable as TextView background
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                    mEditEmail.setBackground(gd);
//                                    mEditPassword.setBackground(gd);
//                                }


                                mTextError.setVisibility(View.VISIBLE);
                                mTextError.setText(jsonObject.getJSONObject("Data").getString("alert"));

                            }

                            LoginActivity.INSTANCE.stopAnim();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            LoginActivity.INSTANCE.stopAnim();
                        }
                    }
                }, new PResponseErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                LoginActivity.INSTANCE.stopAnim();
            }
        });

        request.execute();
    }





}
