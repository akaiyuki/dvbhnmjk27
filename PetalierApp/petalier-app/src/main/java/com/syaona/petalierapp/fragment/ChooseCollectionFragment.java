package com.syaona.petalierapp.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.LoginActivity;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.ProfileActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PConfiguration;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PResponseErrorListener;
import com.syaona.petalierapp.core.PResponseListener;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.test.DesignBoxActivity;
import com.syaona.petalierapp.view.CircleTransform;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseCollectionFragment extends Fragment {

    private GridView mGridView;
    private ArrayList<JSONObject> mResultCollection = new ArrayList<>();
    private CollectionListAdapter mAdapter;

    public String selectedFlower;


    public ChooseCollectionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestApiGetCollections();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_collection, container, false);

         /* Initialize toolbar */
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle("");
//        ((BaseActivity) getActivity()).getSupportActionBar().setIcon(R.drawable.arrow);

        TextView mTxtTitle = (TextView) toolbar.findViewById(R.id.txtsub);
        mTxtTitle.setText("Choose your Collection");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        /* hide image cart for the mean time*/
        ImageView mImageCart = (ImageView) toolbar.findViewById(R.id.cart);
        mImageCart.setVisibility(View.VISIBLE);
        mImageCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!PSharedPreferences.getSomeStringValue(AppController.getInstance(),"session_token").isEmpty()
                        ){
                    PEngine.switchFragment((BaseActivity) getActivity(), new MyCartFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                } else {
                    Singleton.setLoginFromMain(1);
                    startActivity(new Intent(getActivity(), LoginActivity.class));

                }


            }
        });


        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!PSharedPreferences.getSomeStringValue(AppController.getInstance(),"session_token").isEmpty()
                        ){
                    PEngine.switchFragment((BaseActivity) getActivity(), new ProfileFragment(), ((BaseActivity) getActivity()).getFrameLayout());

//                    startActivity(new Intent(getActivity(), ProfileActivity.class));

                } else {
                    Singleton.setLoginFromMain(1);
//                    PEngine.switchFragment((BaseActivity) getActivity(), new LoginFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                    startActivity(new Intent(getActivity(), LoginActivity.class));

                }


            }
        });


//        Button mButtonDesign = (Button) view.findViewById(R.id.btndesign);
//        mButtonDesign.setTypeface(Fonts.gothambookregular);
//        mButtonDesign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                if (Singleton.getSelectedCollection() == 1) {
//////                    PEngine.switchFragment((BaseActivity) getActivity(), new DesignBoxFragment(), ((BaseActivity) getActivity()).getFrameLayout());
//
//                    startActivity(new Intent(getActivity(), DesignBoxActivity.class));
//
////                }
//
//            }
//        });

        mGridView = (GridView) view.findViewById(R.id.gridview);

        mAdapter = new CollectionListAdapter(getActivity(), R.layout.custom_row_collection, mResultCollection);
        mAdapter.notifyDataSetChanged();
        mGridView.setAdapter(mAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedFlower = adapterView.getItemAtPosition(i).toString();

                Singleton.setSelectedCollection(1);

                mAdapter.notifyDataSetChanged();

                startActivity(new Intent(getActivity(), DesignBoxActivity.class));

            }
        });


        return view;
    }




    public class CollectionListAdapter extends ArrayAdapter<JSONObject> {

        Context mContext;
        ArrayList<JSONObject> mData = new ArrayList<>();
        int mResId;

        public CollectionListAdapter(Context context, int resource, ArrayList<JSONObject> data) {
            super(context, resource, data);
            this.mContext = context;
            this.mResId = resource;
            this.mData = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                //Inflate layout
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mResId, null);
                holder = new ViewHolder();

                holder.text1 = (TextView) convertView.findViewById(R.id.txtbeatriz);
                holder.imageView = (ImageView) convertView.findViewById(R.id.beatriz);
                holder.text2 = (TextView) convertView.findViewById(R.id.pricebea);
                holder.lineActiveImage = (ImageView) convertView.findViewById(R.id.lineactive);
                holder.lineInactiveImage = (ImageView) convertView.findViewById(R.id.lineinactive);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final JSONObject row = mData.get(position);

            try {
                holder.text1.setText(row.getString("productHeader"));
                holder.text1.setTypeface(Fonts.gothambold);
                holder.text2.setText("Php "+row.getString("regular_price"));
                holder.text2.setTypeface(Fonts.gothambookregular);

//                if (holder.text1.getText().toString().equalsIgnoreCase("beatriz")){
//                    Picasso.with(mContext)
//                            .load(R.drawable.beatriz)
//                            .fit()
//                            .transform(new CircleTransform())
//                            .into(holder.imageView, new Callback() {
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onError() {
//                                }
//                            });
//                } if (holder.text1.getText().toString().equalsIgnoreCase("lucy")){
//                    Picasso.with(mContext)
//                            .load(R.drawable.lucy)
//                            .fit()
//                            .transform(new CircleTransform())
//                            .into(holder.imageView, new Callback() {
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onError() {
//                                }
//                            });
//                } if (holder.text1.getText().toString().equalsIgnoreCase("lauren")){
//                    Picasso.with(mContext)
//                            .load(R.drawable.lauren)
//                            .fit()
//                            .transform(new CircleTransform())
//                            .into(holder.imageView, new Callback() {
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onError() {
//                                }
//                            });
//                } if (holder.text1.getText().toString().equalsIgnoreCase("chloe")){
//                    Picasso.with(mContext)
//                            .load(R.drawable.chloe)
//                            .fit()
//                            .transform(new CircleTransform())
//                            .into(holder.imageView, new Callback() {
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onError() {
//                                }
//                            });
//                } if (holder.text1.getText().toString().equalsIgnoreCase("diana")){
//                    Picasso.with(mContext)
//                            .load(R.drawable.diana)
//                            .fit()
//                            .transform(new CircleTransform())
//                            .into(holder.imageView, new Callback() {
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onError() {
//                                }
//                            });
//                } if (holder.text1.getText().toString().equalsIgnoreCase("felicisima")){
//                    Picasso.with(mContext)
//                            .load(R.drawable.felicima)
//                            .fit()
//                            .transform(new CircleTransform())
//                            .into(holder.imageView, new Callback() {
//                                @Override
//                                public void onSuccess() {
//
//                                }
//
//                                @Override
//                                public void onError() {
//                                }
//                            });
//                }

                Picasso.with(mContext)
                        .load(row.getString("thumbnail"))
                        .fit()
                        .into(holder.imageView);



            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (selectedFlower != null){

                try {
                    JSONObject jsonObject = new JSONObject(selectedFlower);

                    if (jsonObject.getString("productHeader").equalsIgnoreCase(holder.text1.getText().toString())){
                        holder.lineActiveImage.setVisibility(View.VISIBLE);
                        holder.lineInactiveImage.setVisibility(View.GONE);

                        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "flower_name", jsonObject.getString("productName"));
                        PSharedPreferences.setSomeStringValue(AppController.getInstance(),"flower_price", jsonObject.getString("regular_price"));
                        PSharedPreferences.setSomeStringValue(AppController.getInstance(),"flower_excerpt",jsonObject.getString("productExcerpt"));

                        PSharedPreferences.setSomeStringValue(AppController.getInstance(),"id",row.getString("id"));

                        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "max_color", jsonObject.getString("max_color"));

                        Log.i("product_id", row.getString("id"));

                    } else {
                        holder.lineActiveImage.setVisibility(View.GONE);
                        holder.lineInactiveImage.setVisibility(View.VISIBLE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    if (holder.lineActiveImage.getVisibility() == View.VISIBLE) {
//                        holder.lineActiveImage.setVisibility(View.GONE);
//                        holder.lineInactiveImage.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.lineActiveImage.setVisibility(View.VISIBLE);
//                        holder.lineInactiveImage.setVisibility(View.GONE);
//                    }
//
//
//                }
//            });


            return convertView;
        }

        class ViewHolder {
            TextView text1;
            TextView text2;
            TextView text3;
            TextView text4;
            ImageView imageView;
            ImageView lineActiveImage;
            ImageView lineInactiveImage;
        }
    }



    public void requestApiGetCollections() {

        MainActivity.INSTANCE.startAnim();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = PConfiguration.testURL+PRequest.apiMethodGetAllProducts;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS){

                                JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONArray("products");

                                for (int i = 0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    mResultCollection.add(jsonObject1);

                                }

                                mAdapter = new CollectionListAdapter(getActivity(), R.layout.custom_row_collection, mResultCollection);
                                mAdapter.notifyDataSetChanged();
                                mGridView.setAdapter(mAdapter);


                            }

                            MainActivity.INSTANCE.stopAnim();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            MainActivity.INSTANCE.stopAnim();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());

                        MainActivity.INSTANCE.stopAnim();

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("User-Agent", "Nintendo Gameboy");
//                params.put("Accept-Language", "fr");

//                params.put("Session-Token", PSharedPreferences.getSomeStringValue(AppController.getInstance(), "session_token"));
//

                return params;
            }
        };
        queue.add(postRequest);

    }








}
