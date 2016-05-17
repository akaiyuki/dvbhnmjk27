package com.syaona.petalierapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.LoginActivity;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.view.Fonts;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendNoteFragment extends Fragment {

    private EditText mEditTo;
    private EditText mEditMessage;


    public SendNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_note, container, false);

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
        mTxtTitle.setText("Send a Message");
        mTxtTitle.setTypeface(Fonts.gothambookregular);

        ImageView mImageProfile = (ImageView) toolbar.findViewById(R.id.profile);
        mImageProfile.setVisibility(View.GONE);

//        mEditTo = (EditText) view.findViewById(R.id.edit_to);
        mEditMessage = (EditText) view.findViewById(R.id.edit_message);

        PSharedPreferences.setSomeStringValue(AppController.getInstance(),"note","");


        Button btnNext = (Button) view.findViewById(R.id.btnnext);
        btnNext.setTypeface(Fonts.gothambookregular);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (mEditMessage.getText().length() != 0){

                    PSharedPreferences.setSomeStringValue(AppController.getInstance(),"note",mEditMessage.getText().toString());

                    if (!PSharedPreferences.getSomeStringValue(AppController.getInstance(),"session_token").isEmpty()){
                        Intent intent = new Intent(getActivity(), OrderActivity.class);
                        intent.putExtra("goto","delivery");
                        startActivity(intent);
                        getActivity().finish();
                    } else {
                        Singleton.setLoginFromMain(0);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }

//                }

            }
        });


//        TextView txtTo = (TextView) view.findViewById(R.id.txtto);
//        txtTo.setTypeface(Fonts.gothambookregular);

//        TextView txtFrom = (TextView) view.findViewById(R.id.txtfrom);
//        txtFrom.setTypeface(Fonts.gothambookregular);

        TextView txtNote = (TextView) view.findViewById(R.id.txtnote);
        txtNote.setTypeface(Fonts.gothambookregular);



        return view;
    }

}
