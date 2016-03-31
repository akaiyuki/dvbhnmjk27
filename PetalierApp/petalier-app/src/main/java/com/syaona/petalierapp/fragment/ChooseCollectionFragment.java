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

import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.view.CircleTransform;

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

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorTextWhite), PorterDuff.Mode.SRC_ATOP);
        ((BaseActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Button mButtonDesign = (Button) view.findViewById(R.id.btndesign);
        mButtonDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PEngine.switchFragment((BaseActivity) getActivity(), new DesignBoxFragment(), ((BaseActivity) getActivity()).getFrameLayout());


            }
        });

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
