package com.syaona.petalierapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.OrderActivity;
import com.syaona.petalierapp.view.Fonts;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        Button btnLoginEmail = (Button) view.findViewById(R.id.btnloginemail);
        btnLoginEmail.setTypeface(Fonts.gothambookregular);
        btnLoginEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), OrderActivity.class));
            }
        });

        TextView txtLogin = (TextView) view.findViewById(R.id.txtlogin);
        txtLogin.setTypeface(Fonts.gothambookregular);

        return view;
    }

}
