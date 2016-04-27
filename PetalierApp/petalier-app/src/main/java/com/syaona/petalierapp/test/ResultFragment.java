package com.syaona.petalierapp.test;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

/**
 * Created by smartwavedev on 4/27/16.
 */
public class ResultFragment extends Fragment {
    private static final String ARG_BITMAP="bitmap";
    private static final String ARG_URI="uri";

    static ResultFragment newInstance() {
        ResultFragment f=new ResultFragment();

        f.setArguments(new Bundle());

        return(f);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return(new SubsamplingScaleImageView(getActivity()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getSSSIV().setOrientation(SubsamplingScaleImageView.ORIENTATION_90);

        Bitmap bitmap=getArguments().getParcelable(ARG_BITMAP);

        if (bitmap == null) {
            Uri uri=getArguments().getParcelable(ARG_URI);

            if (uri != null) {
                setImage(uri);
            }
        }
        else {
            setImage(bitmap);
        }
    }

    void setImage(Bitmap bitmap) {
        getArguments().putParcelable(ARG_BITMAP, bitmap);
        getArguments().remove(ARG_URI);

        if (getView()!=null) {
            getSSSIV().setImage(ImageSource.bitmap(bitmap));
        }
    }

    void setImage(Uri uri) {
        getArguments().putParcelable(ARG_URI, uri);
        getArguments().remove(ARG_BITMAP);

        if (getView()!=null) {
            getSSSIV().setImage(ImageSource.uri(uri));
        }
    }

    private SubsamplingScaleImageView getSSSIV() {
        return((SubsamplingScaleImageView)getView());
    }
}
