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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.LoginActivity;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PConfiguration;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PResponseErrorListener;
import com.syaona.petalierapp.core.PResponseListener;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.dialog.PDialog;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import pl.droidsonroids.gif.GifImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText mEditEmail;
    private EditText mEditPassword;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private GraphRequest request;
    private GifImageView mImageLoading;

    private String mFirstName;
    private String mLastName;
    private String mEmail;


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


        mImageLoading = (GifImageView) view.findViewById(R.id.loading);
        mImageLoading.setVisibility(View.GONE);

//        Glide.with(AppController.getInstance())
//                .load(R.drawable.loading)
//                .asGif()
//                .placeholder(R.drawable.loading)
//                .crossFade()
//                .into(mImageLoading);


        Button btnLoginEmail = (Button) view.findViewById(R.id.btnloginemail);
        btnLoginEmail.setTypeface(Fonts.gothambookregular);
        btnLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEditEmail.getText().length() != 0 && mEditPassword.getText().length() != 0) {
                    mImageLoading.setVisibility(View.VISIBLE);
                    requestApiLogin();
                }

            }
        });

//        TextView txtLogin = (TextView) view.findViewById(R.id.txtlogin);
//        txtLogin.setTypeface(Fonts.gothambookregular);

//        mTextError = (TextView) view.findViewById(R.id.txterror);


        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) view.findViewById(R.id.button_fb);
//        loginButton.setReadPermissions("user_friends");
//        loginButton.setReadPermissions("AccessToken");
        loginButton.setReadPermissions("email", "user_friends", "public_profile");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code

                Log.i("name user", String.valueOf(loginResult.getAccessToken().getUserId()));
                final String fbAccesstoken = loginResult.getAccessToken().getToken();
                final String fbId = loginResult.getAccessToken().getUserId();

                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "fb_access_token", fbAccesstoken);


                final String signature = encriptFacebookCredentials(fbAccesstoken);


                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"signature",signature);

                Log.i("fblogin", signature);


                //sample
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                /* handle the result */


                        try {

                            Profile profile = Profile.getCurrentProfile();
                            final String email = object.optString("email");


                            AccessToken accessToken = loginResult.getAccessToken();

                            mFirstName = profile.getFirstName();
                            mLastName = profile.getLastName();
                            mEmail = email;


                            requestApiFbLogin(fbAccesstoken, fbId, profile.getFirstName(), profile.getLastName(), email, signature);

                        } catch (Exception e) {
                            e.printStackTrace();
//                                    Log.e("requesterror", e.getMessage());
                        }


                    }
                });


                //end


//                // App code
//                GraphRequest request = GraphRequest.newMeRequest(
//                        loginResult.getAccessToken(),
//                        new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(
//                                    JSONObject object,
//                                    GraphResponse response) {
//
//                                Log.e("response: ", response + "");
//                                try {
//
//                                    Profile profile = Profile.getCurrentProfile();
////                                    String firstName = profile.getFirstName();
////                                    String lastName = profile.getLastName();
//
//                                    final String email = object.optString("email");
//
//
//                                    AccessToken accessToken = loginResult.getAccessToken();
//
//
//                                    requestApiFbLogin(fbAccesstoken, fbId, profile.getFirstName(), profile.getLastName(), email, signature);
//
//
//
//                                    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
//                                        @Override
//                                        protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {
//
//                                        }
//                                    };
//                                    accessTokenTracker.startTracking();
//
//                                    ProfileTracker profileTracker = new ProfileTracker() {
//                                        @Override
//                                        protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
////                                            requestApiFbLogin(fbAccesstoken, fbId, profile.getFirstName(), profile.getLastName(), email, signature);
//
//                                        }
//                                    };
//                                    profileTracker.startTracking();
//
////                                    if (profile != null) {
////                                        //get data here
////                                        Log.e("firstnamefb", profile.getFirstName()+" "+email);
////
////                                        /* log out facebook if access token is not null */
//////                                        if(accessToken != null){
//////                                            LoginManager.getInstance().logOut();
//////                                        }
////
////                                        requestApiFbLogin(fbAccesstoken, fbId, profile.getFirstName(), profile.getLastName(), email, signature);
////
////                                    }
//
//
//
//
//
////                                    mImageLoading.setVisibility(View.VISIBLE);
////                                    requestApiFbLogin(fbAccesstoken, fbId, firstName, lastName, email, signature);
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
////                                    Log.e("requesterror", e.getMessage());
//                                }
//
//                            }
//
//                        });
//                //end

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();

//                requestApiFbLogin(fbAccesstoken, fbId, mFirstName, mLastName, mEmail, signature);


            }

            @Override
            public void onCancel() {
                // App code
                Log.i("login user", "canceled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i("login user", exception.toString());
            }
        });


        return view;
    }

    public void stopAnimation(){
        mImageLoading.setVisibility(View.GONE);
    }


    public static String encriptFacebookCredentials(String accessToken) {
        try {
            String secret = AppController.getInstance().getResources().getString(R.string.facebook_app_secret);
            String message = accessToken + ".p3t@li3r";

            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            String hash = Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes()), Base64.NO_WRAP);
//            String hash = Base64.encodeToString(sha256_HMAC.doFinal(message.getBytes()), Base64.DEFAULT);
            Log.i("signatureBase64",hash);
            return hash;
        }
        catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }


    public void requestApiLogin() {

        HashMap<String, String> params = new HashMap<>();
        params.put("username", mEditEmail.getText().toString());
        params.put("password", mEditPassword.getText().toString());

        PRequest request = new PRequest(PRequest.apiMethodPostLogin, params,
                new PResponseListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {
                                PSharedPreferences.saveJsonToSharedPref(jsonObject.getJSONObject("Data"), "");

                                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "user_id", jsonObject.getJSONObject("Data").getJSONObject("user").getString("id"));
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "session_token", jsonObject.getJSONObject("Data").getString("session_token"));
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "user_name", jsonObject.getJSONObject("Data").getJSONObject("user").getString("userName"));

                                if (Singleton.getLoginFromMain() == 1) {
                                    Intent i = new Intent(getActivity(), MainActivity.class);
                                    i.putExtra("goto", "collection");
                                    startActivity(i);
                                    getActivity().finish();
                                } else {
//                                    startActivity(new Intent(getActivity(), OrderActivity.class));
//                                    getActivity().finish();
                                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                                    intent.putExtra("goto","delivery");
                                    startActivity(intent);
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

                                PDialog.showDialogError((BaseActivity) getActivity(), jsonObject.getJSONObject("Data").getString("alert"));

//                                mTextError.setVisibility(View.VISIBLE);
//                                mTextError.setText(jsonObject.getJSONObject("Data").getString("alert"));

                            }

                            stopAnimation();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopAnimation();
                        }
                    }
                }, new PResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                stopAnimation();
            }
        });

        request.execute();
    }


    public void requestApiFbLogin(String accessToken, String fbId, String fName, String lName, String email, String signature) {

//        mImageLoading.setVisibility(View.VISIBLE);

        HashMap<String, String> params = new HashMap<>();
        params.put("fbAccessToken", accessToken);
        params.put("fbId", fbId);
        params.put("signature", signature);
        params.put("firstName", fName);
        params.put("lastName", lName);
        params.put("email", email);

        PRequest request = new PRequest(PRequest.apiMethodPostLoginFb, params,
                new PResponseListener() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {
                                PSharedPreferences.saveJsonToSharedPref(jsonObject.getJSONObject("Data"), "");

                                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "user_id", jsonObject.getJSONObject("Data").getJSONObject("user").getString("id"));
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "session_token", jsonObject.getJSONObject("Data").getString("session_token"));
                                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "user_name", jsonObject.getJSONObject("Data").getJSONObject("user").getString("userName"));

                                if (Singleton.getLoginFromMain() == 1) {
                                    Intent i = new Intent(getActivity(), MainActivity.class);
                                    i.putExtra("goto", "collection");
                                    startActivity(i);
                                    getActivity().finish();
                                } else {
//                                    startActivity(new Intent(getActivity(), OrderActivity.class));
//                                    getActivity().finish();
                                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                                    intent.putExtra("goto","delivery");
                                    startActivity(intent);
                                    getActivity().finish();
                                }

                            } else {
                                Log.i("Error login", jsonObject.getJSONObject("Data").getString("alert"));

                                PDialog.showDialogError((BaseActivity) getActivity(), jsonObject.getJSONObject("Data").getString("alert"));

                            }

                            stopAnimation();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopAnimation();
                        }
                    }
                }, new PResponseErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                stopAnimation();
            }
        });

        request.execute();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
