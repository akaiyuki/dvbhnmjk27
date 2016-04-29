package com.syaona.petalierapp.test;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.activity.MainActivity;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.BaseActivity;
import com.syaona.petalierapp.core.PRequest;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.StatusResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by smartwavedev on 4/27/16.
 */
public class TakePhotoActivity extends BaseActivity {

    ImageView imgFavorite;
    private Bitmap bp;
    final int mPicCrop = 1;
    public Button btnUpload;
    public Button btnCrop;
    public Button btnNewImage;
    public Uri mPicUri;
    public int TAKE_PICTURE = 1;
    public static TakePhotoActivity INSTANCE = null;

    static final int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);

        imgFavorite = (ImageView)findViewById(R.id.imageView1);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnNewImage = (Button) findViewById(R.id.btnNewImage);

        open();

        INSTANCE = this;

    }
    public void open(){
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);


//        if(resultCode != RESULT_CANCELED) {
//
//            setResult(Activity.RESULT.CANCELLED);
//
//            bp = (Bitmap) data.getExtras().get("data");
//            mPicUri = getImageUri(TakePhotoActivity.INSTANCE, bp);
//            imgFavorite.setImageBitmap(bp);
//
//            btnUpload.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    new ImageUploadTask().execute();
//                }
//            });
//
//
//
//        }
        if(resultCode == RESULT_CANCELED){
            onBackPressed();
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            bp = (Bitmap) data.getExtras().get("data");
            imgFavorite.setImageBitmap(bp);

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new ImageUploadTask().execute();
                }
            });
        }

        btnNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        final ProgressDialog loading = ProgressDialog.show(TakePhotoActivity.this,"Uploading...","Please wait...",false,false);


        @Override
        protected void onPreExecute(){
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... unused) {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(
                        PRequest.getApiRootForResource() + PRequest.apiMethodPostDepositSlip);

                httpPost.addHeader("petalier_session_token", PSharedPreferences.getSomeStringValue(AppController.getInstance(), "session_token"));

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.JPEG, 50, bos);
                byte[] data = bos.toByteArray();
                entity.addPart("orderId", new StringBody("995"));
                entity.addPart("deposit-slip", new ByteArrayBody(data,
                        "depositslip.jpg"));

                httpPost.setEntity(entity);
                HttpResponse response = httpClient.execute(httpPost,
                        localContext);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));

                String sResponse = reader.readLine();
                return sResponse;
            } catch (Exception e) {

                Toast.makeText(getApplicationContext(),
                        "error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
                onBackPressed();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Void... unused) {

        }

        @Override
        protected void onPostExecute(String sResponse) {

            loading.dismiss();

            try {

                if (sResponse != null) {
                    JSONObject JResponse = new JSONObject(sResponse);

                    if (JResponse.getInt("Status") == StatusResponse.STATUS_SUCCESS){

                        showSuccessDialog("Photo has been uploaded");

                    }
//                    Log.i("uploadimage", JResponse.getJSONObject("Data").getJSONObject("Deposit Slip").getString("meta_value"));

                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),
                        "error",
                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }

        }


    public void showSuccessDialog(String message){
        final Dialog dialog = new Dialog(ImageUploadActivity.INSTANCE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_success);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView mTextTitle = (TextView) dialog.findViewById(R.id.txterror);
        mTextTitle.setText(message);
//        mTextTitle.setTypeface(Fonts.gothambookregular);

        LinearLayout mButtonDone = (LinearLayout) dialog.findViewById(R.id.button_ok);
        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(TakePhotoActivity.this, MainActivity.class);
                i.putExtra("goto","collection");
                startActivity(i);
                finish();
                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }







}