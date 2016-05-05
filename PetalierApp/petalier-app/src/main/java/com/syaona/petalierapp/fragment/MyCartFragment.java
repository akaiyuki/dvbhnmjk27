package com.syaona.petalierapp.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PResponseErrorListener;
import com.syaona.petalierapp.core.PResponseListener;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.object.Orders;
import com.syaona.petalierapp.view.CircleTransform;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartFragment extends Fragment {

    private TextView mTextTotal;
    private ArrayList<JSONObject> mResultSet = new ArrayList<>();
    private ArrayList<String> keySetCart = new ArrayList<>();
    private OrderListAdapter mAdapter;
    private ArrayList<Orders> mResultSetOrder = new ArrayList<>();
    private ListView mListView;
    private String selectedNumber;
    private String selectedCartKey;


    public MyCartFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);


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
        mTxtTitle.setText("My Cart");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        Button mButtonCheckOut = (Button) view.findViewById(R.id.checkout_button);
        mButtonCheckOut.setTypeface(Fonts.gothambookregular);
        mButtonCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PEngine.switchFragment((BaseActivity) getActivity(), new BillingInfoFragment(), ((BaseActivity) getActivity()).getFrameLayout());

//                dialogNumberPicker();

            }
        });

        mTextTotal = (TextView) view.findViewById(R.id.txt_total);

        mListView = (ListView) view.findViewById(R.id.listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
//                dialogNumberPicker();

                final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getActivity())
                        .minValue(1)
                        .maxValue(100)
                        .defaultValue(1)
                        .backgroundColor(Color.WHITE)
                        .separatorColor(Color.TRANSPARENT)
                        .textColor(Color.BLACK)
                        .textSize(20)
                        .enableFocusability(false)
                        .wrapSelectorWheel(true)
                        .build();

                new AlertDialog.Builder(getActivity())
                        .setTitle("Choose Quantity")
                        .setView(numberPicker)
                        .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedNumber = String.valueOf(numberPicker.getValue());


                                Orders order = mResultSetOrder.get(i);

                                Log.i("ordersposition", order.getKeyId());

                                selectedCartKey = order.getKeyId();

                                updateItemAtPosition(i);


                                mResultSetOrder.clear();
                                mResultSet.clear();
                                mAdapter.notifyDataSetChanged();
                                mListView.setAdapter(mAdapter);
                                requestApiPostUpdateCart();


                            }
                        })
                        .show();


            }
        });


        return view;
    }

    private void updateItemAtPosition(int position) {
        int visiblePosition = mListView.getFirstVisiblePosition();
        View view = mListView.getChildAt(position - visiblePosition);
        mListView.getAdapter().getView(position, view, mListView);
    }


    /* dialog for selecting quantity */
    public void dialogNumberPicker(){

        final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getActivity())
                .minValue(1)
                .maxValue(100)
                .defaultValue(1)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();

        new AlertDialog.Builder(getActivity())
                .setTitle("Choose Quantity")
                .setView(numberPicker)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedNumber = String.valueOf(numberPicker.getValue());
                    }
                })
                .show();

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

                                mResultSet.clear();
                                mResultSet.add(jsonObject.getJSONObject("Data").getJSONObject("cart"));

                                String total = jsonObject.getJSONObject("Data").getString("cart_total");
                                mTextTotal.setText("PHP " + total);


                                keySetCart.clear();
                                ArrayList<HashMap<String, String>> cartList = new ArrayList<>();
                                cartList.clear();
                                for (int i = 0; i < mResultSet.size(); i++) {
                                    HashMap<String, String> map = new HashMap<String, String>();

                                    try {
                                        JSONObject c = mResultSet.get(i);
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


    public void getValuesCart(){
        String key = "";

        mResultSetOrder.clear();

        for (int x =0; x<keySetCart.size(); x++){
            key = keySetCart.get(x);

            for (int i = 0; i<mResultSet.size(); i++){

                JSONObject jsonObject1 = mResultSet.get(i);

                if (jsonObject1.has(key)){

                    try {

                        Orders order = new Orders();
                        order.setKeyId(key);
                        order.setDeliveryDate(jsonObject1.getJSONObject(key).getString("deliveryDate"));
                        order.setSpecialInstructions(jsonObject1.getJSONObject(key).getString("specialInstructions"));
                        order.setCardLink(jsonObject1.getJSONObject(key).getString("cardLink"));
                        order.setFirstName(jsonObject1.getJSONObject(key).getString("firstName"));
                        order.setLastName(jsonObject1.getJSONObject(key).getString("lastName"));
                        order.setContactNumber(jsonObject1.getJSONObject(key).getString("contactNumber"));
                        order.setStreet(jsonObject1.getJSONObject(key).getString("street"));
                        order.setSubdivision(jsonObject1.getJSONObject(key).getString("subdivision"));
                        order.setCity(jsonObject1.getJSONObject(key).getString("city"));
                        order.setLandMark(jsonObject1.getJSONObject(key).getString("landMark"));
                        order.setCountry(jsonObject1.getJSONObject(key).getString("country"));
                        order.setProductId(jsonObject1.getJSONObject(key).getString("productId"));
                        order.setQuantity(jsonObject1.getJSONObject(key).getString("quantity"));
                        order.setProductNote(jsonObject1.getJSONObject(key).getString("note"));
                        order.setLine_total(jsonObject1.getJSONObject(key).getString("line_total"));
                        order.setProduct_name(jsonObject1.getJSONObject(key).getString("product_name"));
                        order.setImage_base_64(jsonObject1.getJSONObject(key).getString("image_base_64"));


                        mResultSetOrder.add(order);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

        mAdapter = new OrderListAdapter(getActivity(), R.layout.custom_row_summary, mResultSetOrder);
        mAdapter.notifyDataSetChanged();
        mListView.setAdapter(mAdapter);

    }

    public class OrderListAdapter extends ArrayAdapter<Orders> {

        Context mContext;
        ArrayList<Orders> mData = new ArrayList<>();
        int mResId;

        public OrderListAdapter(Context context, int resource, ArrayList<Orders> data) {
            super(context, resource, data);
            this.mContext = context;
            this.mResId = resource;
            this.mData = data;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;

            if (convertView == null) {
                //Inflate layout
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(R.layout.custom_row_cart, null);
                holder = new ViewHolder();

                holder.text1 = (TextView) convertView.findViewById(R.id.textview_order);
                holder.text2 = (TextView) convertView.findViewById(R.id.textview_price);
                holder.text3 = (TextView) convertView.findViewById(R.id.textview_quantity);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageview_flower);
                holder.quantity = (LinearLayout) convertView.findViewById(R.id.linear_quantity);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Orders row = mData.get(position);

            if (row != null) {
                holder.text1.setText(row.getProduct_name());
                holder.text2.setText("PHP " + row.getLine_total());


//                if (selectedNumber != null){
//                    holder.text3.setText(selectedNumber);
//                } else {
                    holder.text3.setText(row.getQuantity());
//                }

                Picasso.with(mContext)
                        .load(row.getImage_base_64())
                        .fit()
                        .transform(new CircleTransform())
                        .into(holder.imageView);



            }


            return convertView;
        }

        class ViewHolder {
            TextView text1;
            TextView text2;
            TextView text3;
            TextView text4;
            ImageView imageView;
            LinearLayout quantity;
        }
    }



    public void requestApiPostUpdateCart() {

        HashMap<String, String> params = new HashMap<>();
        params.put("cartKey", selectedCartKey);
        params.put("quantity", selectedNumber);

        PRequest request = new PRequest(PRequest.apiMethodPostUpdateCart, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {

                                requestApiGetCart();

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



}
