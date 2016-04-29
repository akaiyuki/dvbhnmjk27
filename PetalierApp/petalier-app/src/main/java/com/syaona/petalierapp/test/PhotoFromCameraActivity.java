package com.syaona.petalierapp.test;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.syaona.petalierapp.enums.Singleton;
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
import java.io.InputStreamReader;

/**
 * Created by smartwavedev on 4/29/16.
 */
public class PhotoFromCameraActivity extends BaseActivity implements View.OnClickListener {

    private Bitmap bitmap;
    private ImageView imageView;

    private Button buttonChoose;
    private Button buttonUpload;

    public static PhotoFromCameraActivity INSTANCE = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);

        INSTANCE = this;

        imageView  = (ImageView) findViewById(R.id.imageView1);

        buttonChoose = (Button) findViewById(R.id.btnNewImage);
        buttonUpload = (Button) findViewById(R.id.btnUpload);


        bitmap = Singleton.getImageBitmap();

        imageView.setImageBitmap(bitmap);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);


    }

    class ImageUploadTask extends AsyncTask<Void, Void, String> {

        final ProgressDialog loading = ProgressDialog.show(PhotoFromCameraActivity.INSTANCE,"Uploading...","Please wait...",false,false);

        @Override
        protected void onPreExecute() {
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
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
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

//                Toast.makeText(getApplicationContext(),
//                        "error",
//                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
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

                    Log.i("uploadimage", JResponse.getJSONObject("Data").getJSONObject("Deposit Slip").getString("meta_value"));

                }
            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(),
//                        "error",
//                        Toast.LENGTH_LONG).show();
                Log.e(e.getClass().getName(), e.getMessage(), e);
            }
        }


    }


    public void showSuccessDialog(String message){
        final Dialog dialog = new Dialog(PhotoFromCameraActivity.INSTANCE);
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

                startActivity(new Intent(PhotoFromCameraActivity.this, MainActivity.class));
                finish();
                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View v) {

        if(v == buttonChoose){
            startActivity(new Intent(PhotoFromCameraActivity.this, PictureActivity.class));
        }

        if(v == buttonUpload){
            new ImageUploadTask().execute();
        }
    }

}
