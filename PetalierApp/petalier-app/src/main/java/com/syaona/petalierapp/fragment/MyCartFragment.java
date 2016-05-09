package com.syaona.petalierapp.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hudomju.swipe.SwipeToDismissTouchListener;
import com.hudomju.swipe.adapter.ListViewAdapter;
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
import java.util.List;

import biz.kasual.materialnumberpicker.MaterialNumberPicker;

import static android.widget.Toast.LENGTH_SHORT;

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
    private String total;
    private static final int TIME_TO_AUTOMATICALLY_DISMISS_ITEM = 3000;

    public MyCartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestApiGetCart();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        requestApiGetCart();

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



                if (mResultSetOrder.size() != 0){
//                    PEngine.switchFragment((BaseActivity) getActivity(), new BillingInfoFragment(), ((BaseActivity) getActivity()).getFrameLayout());

                    Intent intent = new Intent(getActivity(), OrderActivity.class);
                    intent.putExtra("goto","billing");
                    startActivity(intent);
//                    getActivity().finish();

                }

            }
        });

        mTextTotal = (TextView) view.findViewById(R.id.txt_total);

        mListView = (ListView) view.findViewById(R.id.listview);

//        mAdapter = new OrderListAdapter(getActivity(), R.layout.custom_row_summary, mResultSetOrder);
//        mAdapter.notifyDataSetChanged();
//        mListView.setAdapter(mAdapter);

//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(final AdapterView<?> adapterView, View view, final int i, long l) {
////                dialogNumberPicker();
//
//                final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(getActivity())
//                        .minValue(1)
//                        .maxValue(100)
//                        .defaultValue(1)
//                        .backgroundColor(Color.WHITE)
//                        .separatorColor(Color.TRANSPARENT)
//                        .textColor(Color.BLACK)
//                        .textSize(20)
//                        .enableFocusability(false)
//                        .wrapSelectorWheel(true)
//                        .build();
//
//                new AlertDialog.Builder(getActivity())
//                        .setTitle("Choose Quantity")
//                        .setView(numberPicker)
//                        .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                selectedNumber = String.valueOf(numberPicker.getValue());
//
//                                Orders order = mResultSetOrder.get(i);
//                                selectedCartKey = order.getKeyId();
//
//                                updateItemAtPosition(i);
//
//
//                                mResultSetOrder.clear();
//                                mResultSet.clear();
//                                mAdapter.notifyDataSetChanged();
//                                mListView.setAdapter(mAdapter);
//                                requestApiPostUpdateCart();
//
//
//                            }
//                        })
//                        .show();
//
//
//            }
//        });




        init(mListView);



        return view;
    }



    private void init(final ListView listView) {
//        final MyBaseAdapter mAdapter = new MyBaseAdapter(mResultSetOrder);
        mAdapter = new OrderListAdapter(getActivity(), R.layout.custom_row_summary, mResultSetOrder);
        listView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged();

        final SwipeToDismissTouchListener<ListViewAdapter> touchListener =
                new SwipeToDismissTouchListener<>(
                        new ListViewAdapter(listView),
                        new SwipeToDismissTouchListener.DismissCallbacks<ListViewAdapter>() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

//                            @Override
                            public void onPendingDismiss(ListViewAdapter recyclerView, int position) {

                            }

                            @Override
                            public void onDismiss(ListViewAdapter view, int position) {

//                                Orders order = mResultSetOrder.get(position);
//                                selectedCartKey = order.getKeyId();
                                mAdapter.remove(position);

                                Log.i("adapterposition", String.valueOf(position));

                            }
                        });

//        touchListener.setDismissDelay(TIME_TO_AUTOMATICALLY_DISMISS_ITEM);
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener((AbsListView.OnScrollListener) touchListener.makeScrollListener());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (touchListener.existPendingDismisses()) {
                    touchListener.undoPendingDismiss();
                } else {
//                    Toast.makeText(getActivity(), "Position " + position, LENGTH_SHORT).show();
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

                                    Orders order = mResultSetOrder.get(position);
                                    selectedCartKey = order.getKeyId();

                                    updateItemAtPosition(position);


                                    mResultSetOrder.clear();
                                    mResultSet.clear();
                                    mAdapter.notifyDataSetChanged();
                                    mListView.setAdapter(mAdapter);
                                    requestApiPostUpdateCart();


                                }
                            })
                            .show();
                }
            }
        });
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

                                total = jsonObject.getJSONObject("Data").getString("cart_total");
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
//                        order.setSpecialInstructions(jsonObject1.getJSONObject(key).getString("specialInstructions"));
//                        order.setCardLink(jsonObject1.getJSONObject(key).getString("cardLink"));
                        order.setFirstName(jsonObject1.getJSONObject(key).getString("firstName"));
                        order.setLastName(jsonObject1.getJSONObject(key).getString("lastName"));
//                        order.setContactNumber(jsonObject1.getJSONObject(key).getString("contactNumber"));
                        order.setStreet(jsonObject1.getJSONObject(key).getString("deliveryAddress"));
//                        order.setSubdivision(jsonObject1.getJSONObject(key).getString("subdivision"));
//                        order.setCity(jsonObject1.getJSONObject(key).getString("city"));
//                        order.setLandMark(jsonObject1.getJSONObject(key).getString("landMark"));
//                        order.setCountry(jsonObject1.getJSONObject(key).getString("country"));
                        order.setProductId(jsonObject1.getJSONObject(key).getString("productId"));
                        order.setQuantity(jsonObject1.getJSONObject(key).getString("quantity"));
//                        order.setProductNote(jsonObject1.getJSONObject(key).getString("note"));
                        order.setLine_total(jsonObject1.getJSONObject(key).getString("line_total_display"));
                        order.setProduct_name(jsonObject1.getJSONObject(key).getString("product_name"));
                        order.setImage_base_64(jsonObject1.getJSONObject(key).getString("image_base_64"));


                        mResultSetOrder.add(order);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }

//        mAdapter = new OrderListAdapter(getActivity(), R.layout.custom_row_summary, mResultSetOrder);
//        mAdapter.notifyDataSetChanged();
//        mListView.setAdapter(mAdapter);

        init(mListView);

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
        public int getCount() {
            return mData.size();
        }

        @Override
        public Orders getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void remove(int position) {
            mData.remove(position);
            notifyDataSetChanged();

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


                holder.text4 = (TextView) convertView.findViewById(R.id.txt_delete);
                holder.text4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("deleteitem", "clicke");
                        selectedCartKey = row.getKeyId();
                        requestApiPostRemoveCart();
                    }
                });


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


//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//
//            ViewHolder viewHolder = convertView == null
//                    ? new ViewHolder(convertView = ((Activity) mContext).getLayoutInflater());
//                    .inflate(R.layout.custom_row_summary, null))
//                    : (ViewHolder) convertView.getTag();
//
//
//            final Orders row = mData.get(position);
//
//            if (row != null) {
//                viewHolder.text1.setText(row.getProduct_name());
//                viewHolder.text2.setText("PHP " + row.getLine_total());
//
//
//                viewHolder.text3.setText(row.getQuantity());
//                Picasso.with(mContext)
//                        .load(row.getImage_base_64())
//                        .fit()
//                        .transform(new CircleTransform())
//                        .into(viewHolder.imageView);
//
//
//
//            }
//
//            return convertView;
//        }


//        class ViewHolder {
//            TextView text1;
//            TextView text2;
//            TextView text3;
//            TextView text4;
//            ImageView imageView;
//            LinearLayout quantity;
//            ViewHolder(View view) {
//                text1 = (TextView) view.findViewById(R.id.textview_order);
//               text2 = (TextView) view.findViewById(R.id.textview_price);
//                text3 = (TextView) view.findViewById(R.id.textview_quantity);
//                imageView = (ImageView) view.findViewById(R.id.imageview_flower);
//                quantity = (LinearLayout) view.findViewById(R.id.linear_quantity);
//                view.setTag(this);
//            }
//        }


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


    public void requestApiPostRemoveCart() {

        HashMap<String, String> params = new HashMap<>();
        params.put("cartKey", selectedCartKey);

        PRequest request = new PRequest(PRequest.apiMethodRemoveCartItem, params,
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
