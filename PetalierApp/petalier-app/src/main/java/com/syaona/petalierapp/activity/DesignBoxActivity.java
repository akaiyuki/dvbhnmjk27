package com.syaona.petalierapp.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.Design3D.BeatrizViewerFragment;
import com.syaona.petalierapp.Design3D.ChloeViewerFragment;
import com.syaona.petalierapp.Design3D.DianaViewerFragment;
import com.syaona.petalierapp.Design3D.FelicisimaViewerFragment;
import com.syaona.petalierapp.Design3D.FriedaViewerFragment;
import com.syaona.petalierapp.Design3D.LaurenViewerFragment;
import com.syaona.petalierapp.Design3D.LucyViewerFragment;
import com.syaona.petalierapp.Design3D.ModelViewerFragment;
import com.syaona.petalierapp.R;

import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PConfiguration;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.enums.StatusResponse;
import com.syaona.petalierapp.photo.CustomScrollView;
import com.syaona.petalierapp.view.ExpandableHeightGridView;
import com.syaona.petalierapp.view.Fonts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.rajawali3d.util.ObjectColorPicker;

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

    public static ArrayList<String> color = new ArrayList<>();

    public static String pickedColor;

    private ImageView mImageLoading;

    public Button btnWhite;

    public Button btnBlack;

    private ExpandableHeightGridView mGridView;
    private ColorListAdapter colorListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_box);

        INSTANCE = this;

        if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("beatriz")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new BeatrizViewerFragment(), BeatrizViewerFragment.TAG)
                    .commit();
        } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lucy")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new LucyViewerFragment(), LucyViewerFragment.TAG)
                    .commit();
        } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("frieda")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FriedaViewerFragment(), FriedaViewerFragment.TAG)
                    .commit();
        } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("chloe")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new ChloeViewerFragment(), ChloeViewerFragment.TAG)
                    .commit();
        } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("felicisima")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new FelicisimaViewerFragment(), FelicisimaViewerFragment.TAG)
                    .commit();
        } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lauren")){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new LaurenViewerFragment(), LaurenViewerFragment.TAG)
                    .commit();
        }
        else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, new DianaViewerFragment(), DianaViewerFragment.TAG)
                    .commit();
        }


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


        Button mButtonRefresh = (Button) findViewById(R.id.btnrefresh);
        assert mButtonRefresh != null;
        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.content_frame, new ModelViewerFragment(), ModelViewerFragment.TAG)
//                        .commit();

                if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("beatriz")){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new BeatrizViewerFragment(), BeatrizViewerFragment.TAG)
                            .commit();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lucy")){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new LucyViewerFragment(), LucyViewerFragment.TAG)
                            .commit();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("frieda")){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new FriedaViewerFragment(), FriedaViewerFragment.TAG)
                            .commit();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("chloe")){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new ChloeViewerFragment(), ChloeViewerFragment.TAG)
                            .commit();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("felicisima")){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new FelicisimaViewerFragment(), FelicisimaViewerFragment.TAG)
                            .commit();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lauren")){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new LaurenViewerFragment(), LaurenViewerFragment.TAG)
                            .commit();
                }
                else {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, new DianaViewerFragment(), DianaViewerFragment.TAG)
                            .commit();
                }

                requestApiGetColors();

                if (color.size() != 0){
                    color.clear();
                    Singleton.getMaxColor().clear();
                }

            }
        });

        Button mButtonPreview = (Button) findViewById(R.id.btnpreview);
        assert mButtonPreview != null;
        mButtonPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("beatriz")){
                    BeatrizViewerFragment.INSTANCE.displayImage();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lucy")){
                    LucyViewerFragment.INSTANCE.displayImage();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("frieda")){
                    FriedaViewerFragment.INSTANCE.displayImage();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("chloe")){
                    ChloeViewerFragment.INSTANCE.displayImage();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("felicisima")){
                    FelicisimaViewerFragment.INSTANCE.displayImage();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lauren")){
                    LaurenViewerFragment.INSTANCE.displayImage();
                } else {
                    DianaViewerFragment.INSTANCE.displayImage();
                }




                int myTimer = 3000;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("beatriz")){
                            BeatrizViewerFragment.INSTANCE.returntoNormal();
                        } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lucy")){
                            LucyViewerFragment.INSTANCE.returntoNormal();
                        } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("frieda")){
                            FriedaViewerFragment.INSTANCE.returntoNormal();
                        } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("chloe")){
                            ChloeViewerFragment.INSTANCE.returntoNormal();
                        } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("felicisima")){
                            FelicisimaViewerFragment.INSTANCE.returntoNormal();
                        } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lauren")){
                            LaurenViewerFragment.INSTANCE.returntoNormal();
                        } else {
                            DianaViewerFragment.INSTANCE.returntoNormal();
                        }
                    }
                }, myTimer);

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

                Intent intent = new Intent(DesignBoxActivity.this, MainActivity.class);
                intent.putExtra("goto", "collection");
                startActivity(intent);
                finish();
            }
        });

        TextView mTxtTitle = (TextView) toolbar.findViewById(R.id.txtsub);
        mTxtTitle.setText("Design your Box");
        mTxtTitle.setTypeface(Fonts.gothambookregular);


        ImageView mCheck = (ImageView) toolbar.findViewById(R.id.check);
        mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(DesignBoxActivity.this, MainActivity.class);
//                intent.putExtra("goto", "send_card");
//                startActivity(intent);
//                finish();


                if (Singleton.getImage3D() != null ) {
                    Intent intent = new Intent(DesignBoxActivity.this, MainActivity.class);
                    intent.putExtra("goto", "send_card");
                    startActivity(intent);
                    finish();
//                    PDialog.displayBitmap(DesignBoxActivity.INSTANCE);
                } else {
                    showDialogError("Preview the arrangement before proceeding with your order.");
                }


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
        PSharedPreferences.setSomeStringValue(AppController.getInstance(),"box_color","black");

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


        btnWhite = (Button) findViewById(R.id.btnwhite);
        assert btnWhite != null;


        btnBlack = (Button) findViewById(R.id.btnblack);
        assert btnBlack != null;

        /* trial */
//        btnWhite.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                btnWhite.setPressed(true);
//                btnBlack.setPressed(false);
//                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "box_color", "white");
//                return true;
//
//
//                // try
//
//
//            }
//        });


        btnWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnWhite.setPressed(true);
                btnBlack.setPressed(false);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "box_color", "white");




                if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("beatriz")){
                    BeatrizViewerFragment.INSTANCE.changeBoxColorToWhite();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lucy")){
                    LucyViewerFragment.INSTANCE.changeBoxColorToWhite();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("frieda")){
                    FriedaViewerFragment.INSTANCE.changeBoxColorToWhite();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("chloe")){
                    ChloeViewerFragment.INSTANCE.changeBoxColorToWhite();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("felicisima")){
                    FelicisimaViewerFragment.INSTANCE.changeBoxColorToWhite();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lauren")){
                    LaurenViewerFragment.INSTANCE.changeBoxColorToWhite();
                } else {
                    DianaViewerFragment.INSTANCE.changeBoxColorToWhite();
                }




                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    btnWhite.setBackground(getResources().getDrawable(R.drawable.button_background_black));
                    btnBlack.setBackground(getResources().getDrawable(R.drawable.button_box));
                }


            }
        });



//        btnBlack.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                btnBlack.setPressed(true);
//                btnWhite.setPressed(false);
//                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "box_color", "black");
//                return true;
//            }
//        });

        btnBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnBlack.setPressed(true);
                btnWhite.setPressed(false);
                PSharedPreferences.setSomeStringValue(AppController.getInstance(), "box_color", "black");



                if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("beatriz")){
                    BeatrizViewerFragment.INSTANCE.changeBoxColorToBlack();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lucy")){
                    LucyViewerFragment.INSTANCE.changeBoxColorToBlack();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("frieda")){
                    FriedaViewerFragment.INSTANCE.changeBoxColorToBlack();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("chloe")){
                    ChloeViewerFragment.INSTANCE.changeBoxColorToBlack();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("felicisima")){
                    FelicisimaViewerFragment.INSTANCE.changeBoxColorToBlack();
                } else if (PSharedPreferences.getSomeStringValue(AppController.getInstance(), "flower_name").equalsIgnoreCase("lauren")){
                    LaurenViewerFragment.INSTANCE.changeBoxColorToBlack();
                } else {
                    DianaViewerFragment.INSTANCE.changeBoxColorToBlack();
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
                    btnBlack.setBackground(getResources().getDrawable(R.drawable.button_background_black));
                    btnWhite.setBackground(getResources().getDrawable(R.drawable.button_box));
                }


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



        mImageLoading = (ImageView) findViewById(R.id.loading);

        Glide.with(AppController.getInstance())
                .load(R.drawable.loading)
                .asGif()
                .placeholder(R.drawable.loading)
                .crossFade()
                .into(mImageLoading);

        requestApiGetColors();


        if (color.size() != 0){
            color.clear();
            Singleton.getMaxColor().clear();
        }



        mGridView = (ExpandableHeightGridView) findViewById(R.id.gridview);
        mGridView.setExpanded(true);


        Button mButtonColor = (Button) findViewById(R.id.btncolorcalendar);
        assert mButtonColor != null;
        mButtonColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DesignBoxActivity.this, WebViewActivity.class));
//                finish();
            }
        });





    }

    public void stopAnimation(){
        mImageLoading.setVisibility(View.GONE);
    }



    public void displayPreview(){
        Dialog dialog = new Dialog(DesignBoxActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bitmap_display);

        ImageView iv = (ImageView) dialog.findViewById(R.id.imageView_bitmap);
        iv.setImageBitmap(getImageFromStorage());

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }

    private Bitmap getImageFromStorage() {
        String bitmapStoragePath = Environment.getExternalStorageDirectory() + "/screenshot.png";

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        final Bitmap b = BitmapFactory.decodeFile(bitmapStoragePath, options);

        return b;
    }




    public class RegularFlowerAdapter extends RecyclerView.Adapter<RegularFlowerAdapter.ViewHolder> {

        private ArrayList<JSONObject> mData = new ArrayList<>();

//        private SparseBooleanArray selectedItems;

        private int selected = 0;

        public RegularFlowerAdapter(ArrayList<JSONObject> data) {
            mData = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_row_flowers, viewGroup, false);
            return new ViewHolder(v);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final JSONObject data = mData.get(position);
            String url = "";
            try {
                if(data != null) {
                    url = data.getString("color_image_link");
                    holder.mTextColor.setText(data.getString("color_name"));


//                    if (!color.contains(data.getString("id"))) {
//                        holder.mTextColor.setTypeface(Fonts.gothambookregular);
//                        holder.mTextColor.setTextColor(Color.parseColor("#2D2C2C"));
//                    } else {
//                        holder.mTextColor.setTypeface(Fonts.gothambold);
//                        holder.mTextColor.setTextColor(Color.parseColor("#dbab40"));
//                    }

//                    holder.mTextColor.setSelected(false);

                    if (selected == Integer.parseInt(data.getString("id"))){
                        holder.mTextColor.setSelected(true);
                    } else {
                        holder.mTextColor.setSelected(false);
                    }





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
                        Log.i("coloridflower", data.getString("id"));

                       PSharedPreferences.setSomeStringValue(AppController.getInstance(),"pick_color",holder.mTextColor.getText().toString());


                        Bitmap bitmap = getBitmapFromURL(data.getString("texture_image_link"));

                        Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 32, 32, false);


                        Singleton.setChosenColor(bitmap);

                        Singleton.setColorId(data.getString("id"));


//                        int maxColor = Integer.parseInt(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "max_color"));

                        if (color.size() >= 1) {
                            if (!color.contains(data.getString("id"))) {
                                color.add(data.getString("id"));
//                                holder.mTextColor.setTypeface(Fonts.gothambold);
//                                holder.mTextColor.setTextColor(Color.parseColor("#dbab40"));
                            }
                        }

//                        if (color.contains(data.getString("id"))) {
//                            holder.mTextColor.setTypeface(Fonts.gothambold);
//                            holder.mTextColor.setTextColor(Color.parseColor("#dbab40"));
//                        } else {
//                            holder.mTextColor.setTypeface(Fonts.gothambookregular);
//                            holder.mTextColor.setTextColor(Color.parseColor("#2D2C2C"));
//                        }


//                            if (holder.getAdapterPosition() == position){
//                                holder.mTextColor.setSelected(true);
//                                notifyDataSetChanged();
//
//                                Log.e("highlightcolor","true");
//                            }


//                        if (holder.mTextColor.getText() == data.getString("color_name")){
//                            holder.mTextColor.setSelected(true);
//                            notifyDataSetChanged();
//
//                            Log.e("highlightcolor","true");
//                        }

                        selected = Integer.parseInt(data.getString("id"));
                        notifyDataSetChanged();


                        Singleton.setMaxColor(color);


//                        Log.i("maxcolorselected", String.valueOf(Singleton.getMaxColor()));


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
            ImageView checkMark;
            public ViewHolder(View itemView) {
                super(itemView);
                mImageFlower = (ImageView) itemView.findViewById(R.id.ic_flower);
                mTextColor = (TextView) itemView.findViewById(R.id.text_color);
                checkMark = (ImageView) itemView.findViewById(R.id.checkmark);
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

                                mResultSpecial.clear();
                                mResultRegular.clear();

                                JSONArray jsonArray = jsonObject.getJSONObject("Data").getJSONObject("colors").getJSONArray("special");
                                JSONArray jsonArray1 = jsonObject.getJSONObject("Data").getJSONObject("colors").getJSONArray("regular");

                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    mResultSpecial.add(jsonObject1);
                                    mResultRegular.add(jsonObject1);
                                }

                                for (int x = 0; x < jsonArray1.length(); x++){
                                    JSONObject jsonObject1 = jsonArray1.getJSONObject(x);
                                    mResultRegular.add(jsonObject1);
                                }

//                                mAdapter = new RegularFlowerAdapter(mResultRegular);
//                                mAdapter.notifyDataSetChanged();
//                                mRecyclerViewRegular.setAdapter(mAdapter);
//
//                                mAdapterSpecial = new RegularFlowerAdapter(mResultSpecial);
//                                mAdapterSpecial.notifyDataSetChanged();
//                                mRecyclerViewSpecial.setAdapter(mAdapterSpecial);

                                colorListAdapter = new ColorListAdapter(DesignBoxActivity.this, R.layout.custom_row_flowers, mResultRegular);
                                colorListAdapter.notifyDataSetChanged();
                                mGridView.setAdapter(colorListAdapter);


                            }

                            stopAnimation();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopAnimation();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        stopAnimation();
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


    // trial
    public class ColorListAdapter extends ArrayAdapter<JSONObject> {

        Context mContext;
        ArrayList<JSONObject> mData = new ArrayList<>();
        int mResId;
        private int selected = 0;

        public ColorListAdapter(Context context, int resource, ArrayList<JSONObject> data) {
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

                holder.text1 = (TextView) convertView.findViewById(R.id.text_color);
                holder.imageView = (ImageView) convertView.findViewById(R.id.ic_flower);


                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final JSONObject data = mData.get(position);

            try {
                holder.text1.setText(data.getString("color_name"));
                holder.text1.setTypeface(Fonts.gothambookregular);

                if (selected == Integer.parseInt(data.getString("id"))){
                    holder.text1.setSelected(true);
                } else {
                    holder.text1.setSelected(false);
                }

                Picasso.with(mContext)
                        .load(data.getString("color_image_link"))
                        .fit()
                        .into(holder.imageView);

                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try {
                            assert data != null;
                            Log.i("coloridflower", data.getString("id"));

                            PSharedPreferences.setSomeStringValue(AppController.getInstance(),"pick_color",holder.text1.getText().toString());


                            Bitmap bitmap = getBitmapFromURL(data.getString("texture_image_link"));

                            Bitmap resizeBitmap = Bitmap.createScaledBitmap(bitmap, 32, 32, false);


                            Singleton.setChosenColor(bitmap);

                            Singleton.setColorId(data.getString("id"));


//                            int maxColor = Integer.parseInt(PSharedPreferences.getSomeStringValue(AppController.getInstance(), "max_color"));

                            if (color.size() >= 0) {
                                if (!color.contains(data.getString("id"))) {
                                    color.add(data.getString("id"));
                                }
                            }

                            selected = Integer.parseInt(data.getString("id"));
                            notifyDataSetChanged();


                            Singleton.setMaxColor(color);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });



            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }

        class ViewHolder {
            TextView text1;
            ImageView imageView;
        }
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



    public void showDialogError(String message){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_error);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        TextView mTextTitle = (TextView) dialog.findViewById(R.id.txterror);
        mTextTitle.setText(message);
        mTextTitle.setTypeface(Fonts.gothambookregular);


        LinearLayout mButtonDone = (LinearLayout) dialog.findViewById(R.id.button_ok);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();

            }
        });

        dialog.show();


    }



}
