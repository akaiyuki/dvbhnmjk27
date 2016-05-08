package com.syaona.petalierapp.fragment;


import android.app.Activity;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
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
import com.syaona.petalierapp.dialog.PDialog;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.object.Orders;
import com.syaona.petalierapp.test.ImageUploadActivity;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderSummaryFragment extends Fragment {

    private ExpandableHeightListView mListView;
    private OrderListAdapter mAdapter;
    private ArrayList<JSONObject> mResultSet = new ArrayList<>();
    private String total;
    private int paymentMethod;

    private TextView txtOrder;
    private TextView txtDate;
    private TextView txtTotal;

    private TextView mTextSubTotal;


    public OrderSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestApiGetOrdersById();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_summary, container, false);

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
//                getActivity().onBackPressed();

                if (Singleton.getUploadedImage().equalsIgnoreCase("")){
                    getActivity().onBackPressed();
                } else {
                    PEngine.switchFragment((BaseActivity) getActivity(), new ChooseCollectionFragment(), ((BaseActivity) getActivity()).getFrameLayout());
                }


            }
        });

        TextView mTxtTitle = (TextView) toolbar.findViewById(R.id.txtsub);
        mTxtTitle.setText("Order Summary");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        ImageView mImageReceipt = (ImageView) toolbar.findViewById(R.id.receipt);

//        if (!Singleton.getUploadedImage().equalsIgnoreCase("")){
            mImageReceipt.setVisibility(View.VISIBLE);
            mImageReceipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PDialog.displayOnlineImage((BaseActivity) getActivity());
                }
            });
//        } else {
//            mImageReceipt.setVisibility(View.GONE);
//        }



        txtOrder = (TextView) view.findViewById(R.id.txtorder);
        txtOrder.setTypeface(Fonts.gothambold);
//        txtOrder.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "order_name"));

        txtDate = (TextView) view.findViewById(R.id.txtdate);
        txtDate.setTypeface(Fonts.gothambookregular);
//        txtDate.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "date_created"));

        TextView txtNote = (TextView) view.findViewById(R.id.txtnote);
        txtNote.setTypeface(Fonts.gothambookregular);

        TextView txtUpload = (TextView) view.findViewById(R.id.txtupload);
        txtUpload.setTypeface(Fonts.gothambold);

        txtTotal = (TextView) view.findViewById(R.id.txt_total);
//        txtTotal.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "order_total"));

        Button btnDone = (Button) view.findViewById(R.id.btndone);
        btnDone.setTypeface(Fonts.gothambookregular);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PEngine.switchFragment((BaseActivity) getActivity(), new ChooseCollectionFragment(), ((BaseActivity) getActivity()).getFrameLayout());

            }
        });

        LinearLayout uploadButton = (LinearLayout) view.findViewById(R.id.linear3);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PDialog.showChoosePhoto((BaseActivity) getActivity());

            }
        });

        mListView = (ExpandableHeightListView) view.findViewById(R.id.listview);
        mAdapter = new OrderListAdapter(getActivity(), R.layout.custom_row_summary, mResultSet);
        mAdapter.notifyDataSetChanged();
        mListView.setAdapter(mAdapter);

        mListView.setExpanded(true);


//        mListView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                view.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });


        mTextSubTotal = (TextView) view.findViewById(R.id.txt_subtotal);

        return view;
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

//            final Orders row = mData.get(position);
//
//            if (row != null) {
//                holder.text1.setText(row.getProduct_name());
//                holder.text2.setText("PHP "+row.getLine_total());
//                holder.text3.setText(row.getQuantity());
//            }

            JSONObject row = mData.get(position);

            if (row != null){
                try {
                    holder.text1.setText(row.getString("product_name"));
                    holder.text2.setText("PHP "+row.getString("line_total_display"));
                    holder.text3.setText(row.getString("quantity"));
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
        }
    }


    public void requestApiGetOrdersById() {

        HashMap<String, String> params = new HashMap<>();
        params.put("id", PSharedPreferences.getSomeStringValue(AppController.getInstance(),"order_id"));

        PRequest request = new PRequest(PRequest.apiMethodGetOrderById, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {

                                JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONObject("order").getJSONArray("items");

                                for (int i = 0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    mResultSet.add(jsonObject1);
                                }

                                txtOrder.setText("Order #"+jsonObject.getJSONObject("Data").getJSONObject("order").getJSONObject("order").getString("ID"));
                                txtDate.setText(jsonObject.getJSONObject("Data").getJSONObject("order").getJSONObject("order").getString("post_date"));

                                txtTotal.setText("PHP " + jsonObject.getJSONObject("Data").getJSONObject("order").getJSONObject("order_meta").getString("order_total_display"));

                                mTextSubTotal.setText("PHP " + jsonObject.getJSONObject("Data").getJSONObject("order").getJSONObject("order_meta").getString("order_total_display"));

                                Singleton.setUploadedImage(jsonObject.getJSONObject("Data").getJSONObject("order").getJSONObject("order_meta").getString("deposit_slip"));

//                                PDialog.displayOnlineImage((BaseActivity) getActivity());

                                Log.e("deposit-slip", jsonObject.getJSONObject("Data").getJSONObject("order").getJSONObject("order_meta").getString("deposit_slip"));

                                mAdapter = new OrderListAdapter(getActivity(), R.layout.custom_row_summary, mResultSet);
                                mAdapter.notifyDataSetChanged();
                                mListView.setAdapter(mAdapter);

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
