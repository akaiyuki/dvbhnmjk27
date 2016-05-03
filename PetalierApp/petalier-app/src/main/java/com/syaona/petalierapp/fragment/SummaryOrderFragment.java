package com.syaona.petalierapp.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PResponseErrorListener;
import com.syaona.petalierapp.core.PResponseListener;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryOrderFragment extends Fragment {

    private ListView mListView;
    private OrderListAdapter mAdapter;
    private ArrayList<JSONObject> mResultSet = new ArrayList<>();
    private String total;
    private TextView txtTotal;
    private CheckBox directBank;
    private CheckBox payPal;
    private int paymentMethod;
    private ArrayList<String> keySetCart = new ArrayList<>();
    private ArrayList<JSONObject> nameKey = new ArrayList<>();

    public SummaryOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestApiGetCart();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_order, container, false);

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
        mTxtTitle.setText("Summary of Order");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        Button btnCheckout = (Button) view.findViewById(R.id.btnplaceorder);
        btnCheckout.setTypeface(Fonts.gothambookregular);

        TextView txt1 = (TextView) view.findViewById(R.id.txt1);
        txt1.setTypeface(Fonts.gothambookregular);

        TextView txt2 = (TextView) view.findViewById(R.id.txt2);
        txt2.setTypeface(Fonts.gothambookregular);

        TextView txt3 = (TextView) view.findViewById(R.id.txt3);
        txt3.setTypeface(Fonts.gothambookregular);

        TextView txt4 = (TextView) view.findViewById(R.id.txt4);
        txt4.setTypeface(Fonts.gothambookregular);

        TextView txt5 = (TextView) view.findViewById(R.id.txt5);
        txt5.setTypeface(Fonts.gothambookregular);

        TextView txtOrderNumber = (TextView) view.findViewById(R.id.textodernumber);
        txtOrderNumber.setTypeface(Fonts.gothambold);

        TextView txtNote = (TextView) view.findViewById(R.id.txtnote);
        txtNote.setTypeface(Fonts.gothambookregular);

        TextView txtPayment = (TextView) view.findViewById(R.id.textpaymentmethod);
        txtPayment.setTypeface(Fonts.gothambookregular);

        TextView txtDirect = (TextView) view.findViewById(R.id.txtdirectbank);
        txtDirect.setTypeface(Fonts.gothambold);

        TextView txtDirectNote = (TextView) view.findViewById(R.id.txtdirectnote);
        txtDirectNote.setTypeface(Fonts.gothambookregular);

        TextView txtPaypal = (TextView) view.findViewById(R.id.txtpaypal);
        txtPaypal.setTypeface(Fonts.gothambookregular);

        TextView txtPaypalWhat = (TextView) view.findViewById(R.id.txtwhatpaypal);
        txtPaypalWhat.setTypeface(Fonts.gothambookregular);

        txtTotal = (TextView) view.findViewById(R.id.txt_total);


        mListView = (ListView) view.findViewById(R.id.listview);
//        mAdapter = new OrderListAdapter(getActivity(), R.layout.custom_row_summary, mResultSet);
//        mAdapter.notifyDataSetChanged();
//        mListView.setAdapter(mAdapter);


        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        directBank = (CheckBox) view.findViewById(R.id.check_default);
        payPal = (CheckBox) view.findViewById(R.id.check_default1);

        directBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (directBank.isChecked()){
                    paymentMethod = 1;
                }
            }
        });

        payPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (payPal.isChecked()){
                    paymentMethod = 2;
                }
            }
        });

        Button btnPlaceOrder = (Button) view.findViewById(R.id.btnplaceorder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestApiCreateOrder();
            }
        });

        return view;
    }


    public void requestApiGetCart() {

        HashMap<String, String> params = new HashMap<>();
//        params.put("id", PSharedPreferences.getSomeStringValue(AppController.getInstance(), "user_id"));

        PRequest request = new PRequest(PRequest.apiMethodGetCart, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {

                                mResultSet.add(jsonObject.getJSONObject("Data").getJSONObject("cart"));

                                total = jsonObject.getJSONObject("Data").getString("cart_total");
                                txtTotal.setText(total);



                                keySetCart.clear();
                                ArrayList<HashMap<String, String>> cartList = new ArrayList<>();
                                for (int i = 0; i < mResultSet.size(); i++) {
                                    HashMap<String, String> map = new HashMap<String, String>();

                                    try {
                                        JSONObject c = mResultSet.get(i);
                                        //Fill map
                                        Iterator<String> iter = c.keys();
                                        while(iter.hasNext())   {
                                            String currentKey = iter.next();
                                            map.put(currentKey, c.getString(currentKey));
                                        }


                                        cartList.add(map);

                                    }
                                    catch (JSONException e) {
                                        e.printStackTrace();

                                    }

                                };


                                HashMap<String, String> nhm = new HashMap<>();
                                for (HashMap xmlFileHm : cartList ) {
                                    nhm.putAll(xmlFileHm);
                                }

                                for ( String key : nhm.keySet() ) {
                                    keySetCart.add(key);
                                }



                                Log.i("cartresult", String.valueOf(mResultSet.size()));

                                mAdapter = new OrderListAdapter(getActivity(), R.layout.custom_row_summary, mResultSet);
                                mAdapter.notifyDataSetChanged();
                                mListView.setAdapter(mAdapter);

                                getValuesCart();

                            } else {
                                Log.i("error", jsonObject.getJSONObject("Data").getString("alert"));
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

    public class OrderListAdapter extends ArrayAdapter<JSONObject> {

        Context mContext;
        ArrayList<JSONObject> mData = new ArrayList<>();
        int mResId;

        public OrderListAdapter(Context context, int resource, ArrayList<JSONObject> data) {
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
                convertView = inflater.inflate(R.layout.custom_row_summary, null);
                holder = new ViewHolder();

                holder.text1 = (TextView) convertView.findViewById(R.id.product_name);
                holder.text2 = (TextView) convertView.findViewById(R.id.product_price);
                holder.text3 = (TextView) convertView.findViewById(R.id.product_quantity);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final JSONObject row = mData.get(position);


//            try {
//
////                String key = "";
////                for (int i = 0; i<keySetCart.size(); i++){
////                    key = keySetCart.get(i);
////
////                    holder.text1.setText(row.getJSONObject(key).getString("product_name"));
////                    holder.text2.setText(row.getJSONObject(key).getString("line_total"));
////                    holder.text3.setText(row.getJSONObject(key).getString("quantity"));
////
////
////                }
//
//                holder.text1.setText(row.getJSONObject(key).getString("product_name"));
//                holder.text2.setText(row.getJSONObject(key).getString("line_total"));
//                holder.text3.setText(row.getJSONObject(key).getString("quantity"));
//
//
//
//
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }


            return convertView;
        }

        class ViewHolder {
            TextView text1;
            TextView text2;
            TextView text3;
            TextView text4;
            ImageView imageView;
        }
    }


    public void requestApiCreateOrder() {

        HashMap<String, String> params = new HashMap<>();
        params.put("enum", String.valueOf(paymentMethod));
        params.put("heardAboutUs", PSharedPreferences.getSomeStringValue(AppController.getInstance(), "hear"));
        params.put("relationshipToReceiver", PSharedPreferences.getSomeStringValue(AppController.getInstance(),"relationship"));

        PRequest request = new PRequest(PRequest.apiMethodPostCreateOrder, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {



                            } else {
                                Log.i("error", jsonObject.getJSONObject("Data").getString("alert"));
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


    public void getValuesCart(){
        String key = "";

//        for (int i = 0; i<mResultSet.size(); i++){
////            key = keySetCart.get(i);
////
////            if (mResultSet.contains(key)){
////                nameKey.add(mResultSet);
////            }
//
//            JSONObject jsonObject1 = mResultSet.get(i);
//
//            try {
//                Log.i("result", jsonObject1.getString("product_name"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }

        for (int x =0; x<keySetCart.size(); x++){
            key = keySetCart.get(x);

//            if (mResultSet.contains(key)){
//
////                try {
//                    Log.i("resultkeyprodname", String.valueOf(mResultSet.get(x)));
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
//
//                nameKey.add(mResultSet);
//
//            }

            for (int i = 0; i<mResultSet.size(); i++){

                if (mResultSet.contains(key)){

                    JSONObject jsonObject1 = mResultSet.get(i);

                    try {
                        Log.i("result", jsonObject1.getString("product_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                    nameKey.add(jsonObject1);

                }



            }


        }





    }




}
