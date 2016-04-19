package com.syaona.petalierapp.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.view.CircleTransform;
import com.syaona.petalierapp.view.Fonts;
import com.syaona.petalierapp.view.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private ImageView mProfile;

    protected String[] tabTitleList = {"Past Orders", "Recent Orders", "Confirmed Orders"};
    private ViewPager mViewPager;
    private SamplePagerAdapter mPageAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    private int mLastPage = 0;

    private ListView mListViewPager;
    private OrderListAdapter mAdapter;
    private ArrayList<JSONObject> mResultset = new ArrayList<>();

    private ArrayList<JSONObject> mResultProfile = new ArrayList<>();

    private TextView mTextName;
    private TextView mTextEmail;

    private ArrayList<JSONObject> mResultOrders = new ArrayList<>();

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestApiGetProfile();
        requestApiGetAllOrders();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


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
        mTxtTitle.setText("Profile");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mSettings = (ImageView) toolbar.findViewById(R.id.settings);
        mSettings.setVisibility(View.VISIBLE);
        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PEngine.switchFragment((BaseActivity) getActivity(), new SettingsFragment(), ((BaseActivity) getActivity()).getFrameLayout());

            }
        });

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        mProfile = (ImageView) view.findViewById(R.id.profilepic);
        Picasso.with(AppController.getInstance())
                .load(R.drawable.card1)
                .transform(new CircleTransform())
                .fit()
                .into(mProfile);


        ImageView mEditProfile = (ImageView) view.findViewById(R.id.editprofile);
        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                PEngine.switchFragment((BaseActivity) getActivity(), new OrderSummaryFragment(), ((BaseActivity) getActivity()).getFrameLayout());

            }
        });


        mTextName = (TextView) view.findViewById(R.id.name);
        mTextEmail = (TextView) view.findViewById(R.id.email);


        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mPageAdapter = new SamplePagerAdapter();
        mViewPager.setAdapter(mPageAdapter);
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setRowCount(3);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(pageListener);
        // END_INCLUDE (setup_slidingtablayout)
    }



    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link android.support.v4.view.ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitleList[position];
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.pager_item,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);

            // Retrieve a ListView and populate
            mListViewPager = (ListView) view.findViewById(R.id.listview);

            if (position == 0) {
                mAdapter = new OrderListAdapter(getActivity(), R.layout.custom_row_pager, mResultOrders);
                mAdapter.notifyDataSetChanged();
            }


            mListViewPager.setAdapter(mAdapter);

            mListViewPager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    PEngine.switchFragment((BaseActivity) getActivity(), new OrderSummaryFragment(), ((BaseActivity) getActivity()).getFrameLayout());

                }
            });





            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    ViewPager.OnPageChangeListener pageListener =  new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mLastPage = position;
            updateLastPage(mLastPage);


            if (position == 0){

            } else if (position == 1){

            }


        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void updateLastPage(int page) {

    }










    public class OrderListAdapter extends ArrayAdapter<JSONObject> {

        Context mContext;
        ArrayList<JSONObject> mData = new ArrayList<>();
        int mResId;
        int column;

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
                convertView = inflater.inflate(mResId, null);
//                convertView = inflater.inflate(R.layout.custom_row_pager, null);
                holder = new ViewHolder();

                holder.text1 = (TextView) convertView.findViewById(R.id.textview_order);
                holder.text2 = (TextView) convertView.findViewById(R.id.textview_price);
                holder.text3 = (TextView) convertView.findViewById(R.id.textview_date);
                holder.imageView = (ImageView) convertView.findViewById(R.id.imageview_flower);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            JSONObject row = mData.get(position);

            try {
                holder.text1.setText(row.getString("post_title"));
                holder.text1.setTypeface(Fonts.gothambold);
                holder.text2.setText(row.getString("post_date"));
                holder.text2.setTypeface(Fonts.gothambookregular);
                holder.text3.setText(row.getString("post_status"));
                holder.text3.setTypeface(Fonts.gothambookregular);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            TextView text1;
            TextView text2;
            TextView text3;
            ImageView imageView;
        }
    }




//    /* api call */
//    public void requestApiGetProfile() {
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
//                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {
//
//                                mResultProfile.add(jsonObject.getJSONObject("Data").getJSONObject("profile"));
//
//                            }
//
//                            Log.i("resultprofile", String.valueOf(mResultProfile.size()));
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





//    public void requestApiGetProfile() {
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        String url = PConfiguration.testURL+"v1/profile/getProfile?id=1";
//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.d("Response", response);
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {
//
//                                mResultProfile.add(jsonObject.getJSONObject("Data").getJSONObject("profile"));
//
//                            }
//
//                            populateUserInfo();
//                            Log.i("resultprofile", String.valueOf(mResultProfile.size()));
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//                        Log.d("ERROR", "error => " + error.toString());
//
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("User-Agent", "Nintendo Gameboy");
////                params.put("Accept-Language", "fr");
//
//                return params;
//            }
//        };
//        queue.add(postRequest);
//
//    }


    public void populateUserInfo(){

        for (int i = 0; i<mResultProfile.size(); i++){

            try {
                String fName = mResultProfile.get(i).getJSONObject("userDetails").getString("first_name");
                String lName = mResultProfile.get(i).getJSONObject("userDetails").getString("last_name");

                String email = mResultProfile.get(i).getJSONObject("accountDetails").getString("user_email");

                mTextName.setText(fName+" "+lName);
                mTextName.setTypeface(Fonts.gothambold);
                mTextEmail.setText(email);
                mTextEmail.setTypeface(Fonts.gothambookregular);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }


//    public void requestApiGetAllOrders() {
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        String url = PConfiguration.testURL+"v1/history/get?userId=1";
//        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>()
//                {
//                    @Override
//                    public void onResponse(String response) {
//                        // response
//                        Log.d("Response", response);
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {
//
//                                JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONArray("history");
//
//                                for (int i = 0; i<jsonArray.length(); i++){
//
//                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                                    mResultOrders.add(jsonObject1);
//
//                                }
//
//                            }
//
//                            mViewPager.setAdapter(mPageAdapter);
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // TODO Auto-generated method stub
//                        Log.d("ERROR", "error => " + error.toString());
//
//                    }
//                }
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("User-Agent", "Nintendo Gameboy");
////                params.put("Accept-Language", "fr");
//
//                return params;
//            }
//        };
//        queue.add(postRequest);
//
//    }



    public void requestApiGetProfile() {

        MainActivity.INSTANCE.startAnim();

        HashMap<String, String> params = new HashMap<>();
        params.put("id", PSharedPreferences.getSomeStringValue(AppController.getInstance(),"user_id"));

        PRequest request = new PRequest(PRequest.apiMethodGetProfileById, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {

                                    mResultProfile.add(jsonObject.getJSONObject("Data").getJSONObject("profile"));

                                    Log.i("resultprofile", String.valueOf(mResultProfile.size()));
                            }

                            populateUserInfo();

                            MainActivity.INSTANCE.stopAnim();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            MainActivity.INSTANCE.stopAnim();
                        }
                    }
                }, new PResponseErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                MainActivity.INSTANCE.stopAnim();
            }
        });

        request.execute();
    }


    public void requestApiGetAllOrders() {

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", PSharedPreferences.getSomeStringValue(AppController.getInstance(),"user_id"));

        PRequest request = new PRequest(PRequest.apiMethodGetOrderHistory, params,
                new PResponseListener(){
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        super.onResponse(jsonObject);

                        try {

                            if (jsonObject.getInt("Status") == StatusResponse.STATUS_SUCCESS) {

                                JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONArray("history");

                                for (int i = 0; i<jsonArray.length(); i++){

                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    mResultOrders.add(jsonObject1);

                                }

                            }

                            mViewPager.setAdapter(mPageAdapter);

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
