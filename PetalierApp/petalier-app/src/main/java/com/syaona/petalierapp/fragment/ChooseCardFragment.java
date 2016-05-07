package com.syaona.petalierapp.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.R;
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
public class ChooseCardFragment extends Fragment {

    private ArrayList<JSONObject> mResultCards = new ArrayList<>();
    private String mTextTitleMain;
    private CardListAdapter mCardAdapter;
    private GridView mGridView;

    public String selectedCard;


    public ChooseCardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestApiGetCards();

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_card, container, false);

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
        mTxtTitle.setText("Choose your Card");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        Button btnNext = (Button) view.findViewById(R.id.next);
        btnNext.setTypeface(Fonts.gothambookregular);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PEngine.switchFragment((BaseActivity) getActivity(), new SendNoteFragment(), ((BaseActivity) getActivity()).getFrameLayout());

            }
        });

        Button btnSkip = (Button) view.findViewById(R.id.skip);
        btnSkip.setTypeface(Fonts.gothambookregular);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PEngine.switchFragment((BaseActivity) getActivity(), new SendNoteFragment(), ((BaseActivity) getActivity()).getFrameLayout());

            startActivity(new Intent(getActivity(), OrderActivity.class));

            }
        });

        mGridView = (GridView) view.findViewById(R.id.gridview);

        mCardAdapter = new CardListAdapter(getActivity(), R.layout.custom_row_cards,  mResultCards);
        mCardAdapter.notifyDataSetChanged();
        mGridView.setAdapter(mCardAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCard = adapterView.getItemAtPosition(i).toString();

                Singleton.setSelectedCard(1);

                mCardAdapter.notifyDataSetChanged();


            }
        });


        /* trial display image */
//        PDialog.displayBitmap(Singleton.getImage3D(), (BaseActivity) getActivity());

        PSharedPreferences.setSomeStringValue(AppController.getInstance(),"card","");


        return view;
    }




    /* api call */
//    public void requestApiGetCards() {
//
//        HashMap<String, String> params = new HashMap<>();
////        params.put("items_per_page", "5");
////        params.put("page_number", "0");
//
//        PRequest request = new PRequest(PRequest.apiMethodGetCards, params,
//                new PResponseListener(){
//                    @Override
//                    public void onResponse(JSONObject jsonObject) {
//                        super.onResponse(jsonObject);
//
//
//                        try {
//
//                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS){
//
//                                JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONArray("cards");
//
//                                for (int i = 0; i<jsonArray.length(); i++){
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                    mResultCards.add(jsonObject1);
//
//                                    Log.i("cardslist", String.valueOf(mResultCards.size()));
//                                }
//
//
//
//                            }
//
//                            mTextTitleMain = jsonObject.getJSONObject("Data").getString("alert");
//
//                            mCardAdapter = new CardListAdapter(getActivity(), R.layout.custom_row_cards,  mResultCards);
//                            mCardAdapter.notifyDataSetChanged();
//                            mGridView.setAdapter(mCardAdapter);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new PResponseErrorListener(){
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                super.onErrorResponse(volleyError);
//            }
//        });
//
//        request.execute();
//    }



    public class CardListAdapter extends ArrayAdapter<JSONObject> {

        Context mContext;
        ArrayList<JSONObject> mData = new ArrayList<>();
        int mResId;

        public CardListAdapter(Context context, int resource, ArrayList<JSONObject> data) {
            super(context, resource, data);
            this.mContext = context;
            this.mResId = resource;
            this.mData = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                //Inflate layout
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mResId, null);
                holder = new ViewHolder();

                holder.text1 = (TextView) convertView.findViewById(R.id.txtcard1);
                holder.imageView = (ImageView) convertView.findViewById(R.id.card1);

                holder.lineActiveImage = (ImageView) convertView.findViewById(R.id.lineactive);
                holder.lineInactiveImage = (ImageView) convertView.findViewById(R.id.lineinactive);



                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final JSONObject row = mData.get(position);

            try {
                holder.text1.setText(row.getString("card_name"));
                holder.text1.setTypeface(Fonts.gothambold);

                Picasso.with(mContext)
                        .load(row.getString("card_image_link"))
                        .fit()
//                        .transform(new CircleTransform())
                        .into(holder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                            }
                        });

            } catch (JSONException e) {
                e.printStackTrace();
            }



//            holder.imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    try {
//                        PSharedPreferences.setSomeStringValue(AppController.getInstance(),"card",row.getString("card_image_link"));
//                        Log.i("cardlink",row.getString("card_image_link"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//
//                }
//            });



            if (selectedCard != null){


                try {
                    JSONObject jsonObject = new JSONObject(selectedCard);

                    if (jsonObject.getString("card_name").equalsIgnoreCase(holder.text1.getText().toString())){
                        holder.lineActiveImage.setVisibility(View.VISIBLE);
                        holder.lineInactiveImage.setVisibility(View.GONE);

                        PSharedPreferences.setSomeStringValue(AppController.getInstance(), "card", row.getString("card_image_link"));
                        Log.i("cardlink", row.getString("card_image_link"));

                    } else {
                        holder.lineActiveImage.setVisibility(View.GONE);
                        holder.lineInactiveImage.setVisibility(View.VISIBLE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }




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



    public void requestApiGetCards() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = PRequest.getApiRootForResource()+PRequest.apiMethodGetCards;
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

                                JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONArray("cards");

                                for (int i = 0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    mResultCards.add(jsonObject1);
                                }


                            mTextTitleMain = jsonObject.getJSONObject("Data").getString("alert");

                            mCardAdapter = new CardListAdapter(getActivity(), R.layout.custom_row_cards,  mResultCards);
                            mCardAdapter.notifyDataSetChanged();
                            mGridView.setAdapter(mCardAdapter);

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("User-Agent", "Nintendo Gameboy");
//                params.put("Accept-Language", "fr");

                return params;
            }
        };
        queue.add(postRequest);

    }










}
