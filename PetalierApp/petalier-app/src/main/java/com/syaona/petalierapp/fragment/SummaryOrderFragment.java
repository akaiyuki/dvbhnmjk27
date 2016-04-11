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

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.view.Fonts;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryOrderFragment extends Fragment {


    public SummaryOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary_order, container, false);

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
                OrderActivity.INSTANCE.onBackPressed();
            }
        });

        TextView mTxtTitle = (TextView) toolbar.findViewById(R.id.txtsub);
        mTxtTitle.setText("Summary of Order");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

        Button btnCheckout = (Button) view.findViewById(R.id.btnplaceorder);
        btnCheckout.setTypeface(Fonts.gothambookregular);

        TextView txt1 = (TextView) view.findViewById(R.id.txt1);
        txt1.setTypeface(Fonts.gothambookregular);

        TextView txt2 = (TextView) view.findViewById(R.id.txt2);
        txt2.setTypeface(Fonts.gothambookregular);

        TextView txt3 = (TextView) view.findViewById(R.id.txt3);
        txt3.setTypeface(Fonts.gothambookregular);

        TextView txt4 = (TextView) view.findViewById(R.id.txt4);
        txt4.setTypeface(Fonts.gothambookregular);

        TextView txt5 = (TextView) view.findViewById(R.id.txt5);
        txt5.setTypeface(Fonts.gothambookregular);

        TextView txtOrderNumber = (TextView) view.findViewById(R.id.textodernumber);
        txtOrderNumber.setTypeface(Fonts.gothambookregular);

        TextView txtNote = (TextView) view.findViewById(R.id.txtnote);
        txtNote.setTypeface(Fonts.gothambookregular);

        TextView txtPayment = (TextView) view.findViewById(R.id.textpaymentmethod);
        txtPayment.setTypeface(Fonts.gothambookregular);

        TextView txtDirect = (TextView) view.findViewById(R.id.txtdirectbank);
        txtDirect.setTypeface(Fonts.gothambold);

        TextView txtDirectNote = (TextView) view.findViewById(R.id.txtdirectnote);
        txtDirectNote.setTypeface(Fonts.gothambookregular);

        TextView txtPaypal = (TextView) view.findViewById(R.id.txtpaypal);
        txtPaypal.setTypeface(Fonts.gothambookregular);

        TextView txtPaypalWhat = (TextView) view.findViewById(R.id.txtwhatpaypal);
        txtPaypalWhat.setTypeface(Fonts.gothambookregular);





        return view;
    }

}
