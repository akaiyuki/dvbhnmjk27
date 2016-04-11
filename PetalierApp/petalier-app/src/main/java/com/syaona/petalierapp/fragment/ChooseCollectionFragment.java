package com.syaona.petalierapp.fragment;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.view.CircleTransform;
import com.syaona.petalierapp.view.Fonts;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseCollectionFragment extends Fragment {

    private ImageView mImageBeatriz;
    private ImageView mImageLucy;
    private ImageView mImageLauren;

    private ImageView mImageChloe;
    private ImageView mImageDiana;
    private ImageView mImageFelicima;


    public ChooseCollectionFragment() {
        // Required empty public constructor
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


        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PEngine.switchFragment((BaseActivity) getActivity(), new ProfileFragment(), ((BaseActivity) getActivity()).getFrameLayout());

            }
        });




        Button mButtonDesign = (Button) view.findViewById(R.id.btndesign);
        mButtonDesign.setTypeface(Fonts.gothambookregular);
        mButtonDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PEngine.switchFragment((BaseActivity) getActivity(), new DesignBoxFragment(), ((BaseActivity) getActivity()).getFrameLayout());


            }
        });



        TextView txtPriceBea = (TextView) view.findViewById(R.id.pricebea);
        txtPriceBea.setTypeface(Fonts.gothambookregular);

        TextView txtPriceLucy = (TextView) view.findViewById(R.id.pricelucy);
        txtPriceLucy.setTypeface(Fonts.gothambookregular);

        TextView txtPriceLauren = (TextView) view.findViewById(R.id.pricelauren);
        txtPriceLauren.setTypeface(Fonts.gothambookregular);

        TextView txtPriceChloe = (TextView) view.findViewById(R.id.pricechloe);
        txtPriceChloe.setTypeface(Fonts.gothambookregular);

        TextView txtPriceDiana = (TextView) view.findViewById(R.id.pricediana);
        txtPriceDiana.setTypeface(Fonts.gothambookregular);

        TextView txtPriceFeli = (TextView) view.findViewById(R.id.pricefeli);
        txtPriceFeli.setTypeface(Fonts.gothambookregular);


//        TextView mTextTitle = (TextView) view.findViewById(R.id.txttitle);
//        mTextTitle.setTypeface(Fonts.gothambookregular);
//
//        TextView mTextSub = (TextView) view.findViewById(R.id.txtsubtitle);
//        mTextSub.setTypeface(Fonts.gothambold);

        TextView txtBeatriz = (TextView) view.findViewById(R.id.txtbeatriz);
        txtBeatriz.setTypeface(Fonts.gothambold);

        TextView txtLucy = (TextView) view.findViewById(R.id.txtlucy);
        txtLucy.setTypeface(Fonts.gothambold);

        TextView txtLauren = (TextView) view.findViewById(R.id.txtlauren);
        txtLauren.setTypeface(Fonts.gothambold);

        TextView txtChloe = (TextView) view.findViewById(R.id.txtchloe);
        txtChloe.setTypeface(Fonts.gothambold);

        TextView txtDiana = (TextView) view.findViewById(R.id.txtdiana);
        txtDiana.setTypeface(Fonts.gothambold);

        TextView txtFelicima = (TextView) view.findViewById(R.id.txtfelicima);
        txtFelicima.setTypeface(Fonts.gothambold);


        mImageBeatriz = (ImageView) view.findViewById(R.id.beatriz);
        mImageLucy = (ImageView) view.findViewById(R.id.lucy);
        mImageLauren = (ImageView) view.findViewById(R.id.lauren);

        mImageChloe = (ImageView) view.findViewById(R.id.chloe);
        mImageDiana = (ImageView) view.findViewById(R.id.diana);
        mImageFelicima = (ImageView) view.findViewById(R.id.felicima);


        Picasso.with(AppController.getInstance())
                .load(R.drawable.beatriz)
                .transform(new CircleTransform())
                .fit()
                .into(mImageBeatriz);

        Picasso.with(AppController.getInstance())
                .load(R.drawable.lucy)
                .transform(new CircleTransform())
                .fit()
                .into(mImageLucy);

        Picasso.with(AppController.getInstance())
                .load(R.drawable.lauren)
                .transform(new CircleTransform())
                .fit()
                .into(mImageLauren);

        Picasso.with(AppController.getInstance())
                .load(R.drawable.chloe)
                .transform(new CircleTransform())
                .fit()
                .into(mImageChloe);

        Picasso.with(AppController.getInstance())
                .load(R.drawable.diana)
                .transform(new CircleTransform())
                .fit()
                .into(mImageDiana);

        Picasso.with(AppController.getInstance())
                .load(R.drawable.felicima)
                .transform(new CircleTransform())
                .fit()
                .into(mImageFelicima);

        return view;
    }

}
