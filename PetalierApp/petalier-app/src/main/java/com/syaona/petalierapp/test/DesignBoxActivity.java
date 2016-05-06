package com.syaona.petalierapp.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.Design3D.ModelViewerFragment;
import com.syaona.petalierapp.R;

import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PConfiguration;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.fragment.SendNoteFragment;
import com.syaona.petalierapp.test.CustomScrollView;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DesignBoxActivity extends BaseActivity {

    public static DesignBoxActivity INSTANCE = null;

    public CustomScrollView scrollView;

    private RecyclerView mRecyclerViewRegular;
    private RecyclerView mRecyclerViewSpecial;
    private ArrayList<JSONObject> mResultRegular = new ArrayList<>();
    private ArrayList<JSONObject> mResultSpecial = new ArrayList<>();

    private RegularFlowerAdapter mAdapter;
    private RegularFlowerAdapter mAdapterSpecial;

    private TextView mTextQuantity;
    private ImageButton mButtonAdd;
    private ImageButton mButtonMinus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_box);

        INSTANCE = this;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new ModelViewerFragment(), ModelViewerFragment.TAG)
                .commit();


        scrollView = (CustomScrollView) findViewById(R.id.scrollView);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativelayout);
        assert relativeLayout != null;
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.setEnableScrolling(true);
                return false;
            }
        });



         /* Initialize toolbar */
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_design);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("");


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


        ImageView mCheck = (ImageView) toolbar.findViewById(R.id.check);
        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DesignBoxActivity.this, MainActivity.class);
                intent.putExtra("goto","send_card");
                startActivity(intent);
                finish();
            }
        });

        mRecyclerViewRegular = (RecyclerView) findViewById(R.id.listview_color_regular);
        mRecyclerViewSpecial = (RecyclerView) findViewById(R.id.listview_color_special);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DesignBoxActivity.INSTANCE);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewRegular.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(DesignBoxActivity.INSTANCE);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewSpecial.setLayoutManager(linearLayoutManager1);

        mTextQuantity = (TextView) findViewById(R.id.txtquantity);
        mButtonAdd = (ImageButton) findViewById(R.id.btnadd);
        mButtonMinus = (ImageButton) findViewById(R.id.btnminus);


        /* trial for quantity */
//        final int txtQuantity = Integer.parseInt(mTextQuantity.getText().toString());

        PSharedPreferences.setSomeStringValue(AppController.getInstance(),"quantity","1");

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int add;
                int txtQuantity = Integer.parseInt(mTextQuantity.getText().toString());
                add = txtQuantity + 1;
                String quantity = String.valueOf(add);
                mTextQuantity.setText(quantity);

                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "quantity", quantity);

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
                    PSharedPreferences.setSomeStringValue(AppController.getInstance(), "quantity", quantity);
                }
            }
        });


        Button btnWhite = (Button) findViewById(R.id.btnwhite);
        assert btnWhite != null;
        btnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "box_color", "white");
            }
        });

        Button btnBlack = (Button) findViewById(R.id.btnblack);
        assert btnBlack != null;
        btnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PSharedPreferences.setSomeStringValue(AppController.getInstance(),"box_color","black");
            }
        });

        TextView mTextFlowerName = (TextView) findViewById(R.id.flowername);
        assert mTextFlowerName != null;
        mTextFlowerName.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name"));

        TextView mTextFlowerPrice = (TextView) findViewById(R.id.flowerprice);
        assert mTextFlowerPrice != null;
        mTextFlowerPrice.setText("Php "+PSharedPreferences.getSomeStringValue(AppController.getInstance(),"flower_price"));

        TextView mTextFlowerExcerpt = (TextView) findViewById(R.id.productexcerpt);
        assert mTextFlowerExcerpt != null;
        mTextFlowerExcerpt.setText(PSharedPreferences.getSomeStringValue(AppController.getInstance(),"flower_excerpt"));




        requestApiGetColors();



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
                    holder.mTextColor.setText(data.getString("color_name"));

                }
            } catch (JSONException e) {
                //e.printStackTrace();
            }

            Picasso.with(DesignBoxActivity.INSTANCE)
                    .load(url)
                    .into(holder.mImageFlower);


            holder.mImageFlower.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        assert data != null;
                        Log.i("hexcodeflower", data.getString("content"));


                        Bitmap bitmap = getBitmapFromURL(data.getString("texture_image_link"));

                        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 32, 32, false);


                        Singleton.setChosenColor(bitmap);


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
            TextView mTextColor;
            public ViewHolder(View itemView) {
                super(itemView);
                mImageFlower = (ImageView) itemView.findViewById(R.id.ic_flower);
                mTextColor = (TextView) itemView.findViewById(R.id.text_color);
            }
        }
    }


    public void requestApiGetColors(){

        RequestQueue queue = Volley.newRequestQueue(DesignBoxActivity.INSTANCE);
        String url = PConfiguration.testURL+ PRequest.apiMethodGetColors;
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
                return params;
            }
        };
        queue.add(postRequest);
    }


    public static Bitmap getBitmapFromURL(String src) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}
