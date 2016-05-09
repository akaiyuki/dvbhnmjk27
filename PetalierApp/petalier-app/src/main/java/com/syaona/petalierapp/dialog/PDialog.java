package com.syaona.petalierapp.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.Design3D.BaseViewerFragment;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.test.ImageUploadActivity;
import com.syaona.petalierapp.test.PictureActivity;
import com.syaona.petalierapp.test.TakePhotoActivity;
import com.syaona.petalierapp.view.Fonts;

/**
 * Created by smartwavedev on 4/19/16.
 */
public class PDialog {

    public static void showDialogError(BaseActivity baseActivity, String message){

        final Dialog dialog = new Dialog(baseActivity);
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

    public static void showChoosePhoto(final BaseActivity baseActivity){


        final Dialog dialog = new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_upload_select);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        LinearLayout buttonCamera = (LinearLayout) dialog.findViewById(R.id.button_camera);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                baseActivity.startActivity(new Intent(baseActivity, PictureActivity.class));
                baseActivity.finish();
                dialog.dismiss();

            }
        });

        LinearLayout buttonGallery = (LinearLayout) dialog.findViewById(R.id.button_gallery);
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                baseActivity.startActivity(new Intent(baseActivity, ImageUploadActivity.class));
                baseActivity.finish();
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    public static void displayBitmap(BaseActivity baseActivity) {
        Dialog dialog = new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bitmap_display);

        ImageView iv = (ImageView) dialog.findViewById(R.id.imageView_bitmap);
        iv.setImageBitmap(Singleton.getImage3D());

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }


    public static void displayOnlineImage(BaseActivity baseActivity) {
        Dialog dialog = new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bitmap_display);

        ImageView iv = (ImageView) dialog.findViewById(R.id.imageView_bitmap);

        Picasso.with(AppController.getInstance())
                .load(Singleton.getUploadedImage())
                .fit()
                .into(iv);


        Log.d("uploadedimage", Singleton.getUploadedImage());

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }






}
