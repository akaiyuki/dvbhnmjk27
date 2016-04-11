package com.syaona.petalierapp.fragment;


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
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PEngine;
import com.syaona.petalierapp.view.CircleTransform;
import com.syaona.petalierapp.view.Fonts;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseCardFragment extends Fragment {

    private ImageView mImageCard1;
    private ImageView mImageCard2;
    private ImageView mImageCard3;

    public ChooseCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_choose_card, container, false);

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
        mTxtTitle.setText("Choose your Card");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        Button btnNext = (Button) view.findViewById(R.id.next);
        btnNext.setTypeface(Fonts.gothambookregular);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PEngine.switchFragment((BaseActivity) getActivity(), new SendNoteFragment(), ((BaseActivity) getActivity()).getFrameLayout());

            }
        });

        Button btnSkip = (Button) view.findViewById(R.id.skip);
        btnSkip.setTypeface(Fonts.gothambookregular);

        TextView txtCard1 = (TextView) view.findViewById(R.id.txtcard1);
        txtCard1.setTypeface(Fonts.gothambold);

        TextView txtCard2 = (TextView) view.findViewById(R.id.txtcard2);
        txtCard2.setTypeface(Fonts.gothambold);

        TextView txtCard3 = (TextView) view.findViewById(R.id.txtcard3);
        txtCard3.setTypeface(Fonts.gothambold);



        mImageCard1 = (ImageView) view.findViewById(R.id.card1);
        mImageCard2 = (ImageView) view.findViewById(R.id.card2);
        mImageCard3 = (ImageView) view.findViewById(R.id.card3);

        Picasso.with(AppController.getInstance())
                .load(R.drawable.card1)
                .transform(new CircleTransform())
                .fit()
                .into(mImageCard1);


        Picasso.with(AppController.getInstance())
                .load(R.drawable.card2)
                .transform(new CircleTransform())
                .fit()
                .into(mImageCard2);

        Picasso.with(AppController.getInstance())
                .load(R.drawable.card3)
                .transform(new CircleTransform())
                .fit()
                .into(mImageCard3);





        return view;
    }

}
