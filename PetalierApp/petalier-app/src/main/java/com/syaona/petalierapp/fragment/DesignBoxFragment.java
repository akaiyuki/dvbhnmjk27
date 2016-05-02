package com.syaona.petalierapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class DesignBoxFragment extends Fragment {

    private RecyclerView mRecyclerViewRegular;
    private RecyclerView mRecyclerViewSpecial;
    private ArrayList<JSONObject> mResultRegular = new ArrayList<>();
    private ArrayList<JSONObject> mResultSpecial = new ArrayList<>();

    private RegularFlowerAdapter mAdapter;
    private RegularFlowerAdapter mAdapterSpecial;

    private TextView mTextQuantity;
    private ImageButton mButtonAdd;
    private ImageButton mButtonMinus;

    public DesignBoxFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestApiGetColors();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_design_box, container, false);

         /* Initialize toolbar */
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app_bar);
        ((BaseActivity) getActivity()).setSupportActionBar(toolbar);
        ((BaseActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        ((BaseActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle("");


        toolbar.setNavigationIcon(R.drawable.arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.INSTANCE.onBackPressed();
            }
        });

        TextView mTxtTitle = (TextView) toolbar.findViewById(R.id.txtsub);
        mTxtTitle.setText("Design your Box");
        mTxtTitle.setTypeface(Fonts.gothambookregular);


        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        mRecyclerViewRegular = (RecyclerView) view.findViewById(R.id.listview_color_regular);
        mRecyclerViewSpecial = (RecyclerView) view.findViewById(R.id.listview_color_special);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewRegular.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewSpecial.setLayoutManager(linearLayoutManager1);

        mTextQuantity = (TextView) view.findViewById(R.id.txtquantity);
        mButtonAdd = (ImageButton) view.findViewById(R.id.btnadd);
        mButtonMinus = (ImageButton) view.findViewById(R.id.btnminus);


        /* trial for quantity */
//        final int txtQuantity = Integer.parseInt(mTextQuantity.getText().toString());
        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int add;
                int txtQuantity = Integer.parseInt(mTextQuantity.getText().toString());
                add = txtQuantity + 1;
                String quantity = String.valueOf(add);
                mTextQuantity.setText(quantity);
            }
        });


        mButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int minus;
                int txtQuantity = Integer.parseInt(mTextQuantity.getText().toString());
                if (txtQuantity != 0){
                    minus = txtQuantity - 1;
                    String quantity = String.valueOf(minus);
                    mTextQuantity.setText(quantity);
                }
            }
        });


        Button btnWhite = (Button) view.findViewById(R.id.btnwhite);
        btnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PEngine.switchFragment((BaseActivity) getActivity(), new ChooseCardFragment(), ((BaseActivity) getActivity()).getFrameLayout());
            }
        });

        TextView mTextFlowerName = (TextView) view.findViewById(R.id.flowername);
        mTextFlowerName.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"flower_name"));

        TextView mTextFlowerPrice = (TextView) view.findViewById(R.id.flowerprice);
        mTextFlowerPrice.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"flower_price"));

        TextView mTextFlowerExcerpt = (TextView) view.findViewById(R.id.productexcerpt);
        mTextFlowerExcerpt.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"flower_excerpt"));

        return view;
    }


    public void requestApiGetColors(){

        MainActivity.INSTANCE.startAnim();

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = PConfiguration.testURL+PRequest.apiMethodGetColors;
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

                                JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONObject("colors").getJSONArray("special");
                                JSONArray jsonArray1 = jsonObject.getJSONObject("Data").getJSONObject("colors").getJSONArray("regular");

                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    mResultSpecial.add(jsonObject1);
                                }

                                for (int x = 0; x < jsonArray1.length(); x++){
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(x);
                                    mResultRegular.add(jsonObject1);
                                }

                                mAdapter = new RegularFlowerAdapter(mResultRegular);
                                mAdapter.notifyDataSetChanged();
                                mRecyclerViewRegular.setAdapter(mAdapter);

                                mAdapterSpecial = new RegularFlowerAdapter(mResultSpecial);
                                mAdapterSpecial.notifyDataSetChanged();
                                mRecyclerViewSpecial.setAdapter(mAdapterSpecial);

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



    public class RegularFlowerAdapter extends RecyclerView.Adapter<RegularFlowerAdapter.ViewHolder> {

        private ArrayList<JSONObject> mData = new ArrayList<>();

        public RegularFlowerAdapter(ArrayList<JSONObject> data) {
            mData = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_flowers, viewGroup, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {

            final JSONObject data = mData.get(position);
            String url = "";
            try {
                if(data != null) {
                    url = data.getString("color_image_link");
                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }

            Picasso.with(getActivity())
                    .load(url)
                    .into(holder.mImageFlower);


            holder.mImageFlower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        assert data != null;
                        Log.i("hexcodeflower", data.getString("content"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });



        }
        @Override
        public int getItemCount() {
            return mData != null ? mData.size() : 0;
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView mImageFlower;
            public ViewHolder(View itemView) {
                super(itemView);
                mImageFlower = (ImageView) itemView.findViewById(R.id.ic_flower);
            }
        }
    }




}
