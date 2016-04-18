package com.syaona.petalierapp.fragment;


import android.content.Intent;
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

        return view;
    }




    public void requestApiLogin() {

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

                            }

                            Log.i("userid", jsonObject.getJSONObject("Data").getJSONObject("user").getString("id"));

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
