package com.syaona.petalierapp.test;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.PRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by smartwavedev on 4/26/16.
 */
public class UploadPhoto extends Activity {

    public static final int GET_FROM_GALLERY = 3;
    public static UploadPhoto INSTANCE = null;
    final int mPicCrop = 1;
    public ImageView imgFavorite;
    public Button btnCrop;
    public Button btnUpload;
    public Button btnNewImage;
    Bitmap bitmap = null;

    HttpEntity httpEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);

        imgFavorite = (ImageView) findViewById(R.id.imageView1);
        btnCrop = (Button) findViewById(R.id.btnCrop);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnNewImage = (Button) findViewById(R.id.btnNewImage);
        open();

        INSTANCE = this;
    }

    public void open() {

        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

    }

    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            final Uri selectedImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                imgFavorite.setImageBitmap(bitmap);
                btnCrop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        performCrop(selectedImage);
                    }
                });
                btnUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        new ImageUploadTask().execute();
                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (requestCode == mPicCrop) {
            if (data != null) {

                if (resultCode != RESULT_CANCELED) {
                    Bundle extras = data.getExtras();
                    bitmap = extras.getParcelable("data");
                    imgFavorite.setImageBitmap(bitmap);

                }

            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            onBackPressed();
        }

        btnNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open();
            }
        });

//        else if(data.getExtras() == null){
//            Toast.makeText(this, "Please Choose Photo", Toast.LENGTH_SHORT).show();
//
//        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

//    private void performCrop(Uri picUri) {
//        try {
//
//            Intent cropIntent = new Intent("com.android.camera.action.CROP");
//            // indicate image type and Uri
//            cropIntent.setDataAndType(picUri, "image/*");
//            //try
//            //cropIntent.setType("image/*");
//            // set crop properties
//            cropIntent.putExtra("crop", "true");
//            // indicate aspect of desired crop
//            cropIntent.putExtra("aspectX", 1);
//            cropIntent.putExtra("aspectY", 1);
//            // indicate output X and Y
//            cropIntent.putExtra("outputX", 128);
//            cropIntent.putExtra("outputY", 128);
//            // retrieve data on return
//            cropIntent.putExtra("return-data", true);
//            // start the activity - we handle returning in onActivityResult
//            startActivityForResult(cropIntent, mPicCrop);
//        }
//        // respond to users whose devices do not support the crop action
//        catch (ActivityNotFoundException anfe) {
//            // display an error message
//            String errorMessage = "Whoops - your device doesn't support the crop action!";
//            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }

//    class ImageUploadTask extends AsyncTask<Void, Void, String> {
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//
//        @Override
//        protected String doInBackground(Void... unused) {
//
//            try {
//
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpContext localContext = new BasicHttpContext();
//                HttpPost httpPost = new HttpPost(
//                        PRequest.getApiRootForResource());
//
//                MultipartEntity entity = new MultipartEntity(
//                        HttpMultipartMode.BROWSER_COMPATIBLE);
//
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
//                byte[] data = bos.toByteArray();
//                entity.addPart("photo_file", new ByteArrayBody(data,
//                        "myImage.jpg"));
//
//                httpPost.setEntity(entity);
//                HttpResponse response = httpClient.execute(httpPost,
//                        localContext);
//                BufferedReader reader = new BufferedReader(
//                        new InputStreamReader(
//                                response.getEntity().getContent(), "UTF-8"));
//
//                String sResponse = reader.readLine();
//                return sResponse;
//            } catch (Exception e) {
//
//                Toast.makeText(getApplicationContext(),
//                        "error",
//                        Toast.LENGTH_LONG).show();
//                Log.e(e.getClass().getName(), e.getMessage(), e);
//                return null;
//            }
//        }
//
//        @Override
//        protected void onProgressUpdate(Void... unused) {
//        }
//
//        @Override
//        protected void onPostExecute(String sResponse) {
//
//            try {
//
//                if (sResponse != null) {
//                    JSONObject JResponse = new JSONObject(sResponse);
//                    int status = JResponse.getInt("Status");
//                    String message = JResponse.getString("Message");
//                    if (status == 200) {
//                        Log.i("Message", JResponse.getString("Message") + " " + JResponse.getInt("Status"));
//
//
//                        String imageUrl = JResponse.getJSONObject("Data").getString("img_url");
//                        Log.i("Image", "imageUrl: " + imageUrl);
//
//
//
//                        onBackPressed();
//
//                    } else {
//                        Toast.makeText(getApplicationContext(),
//                                "Photo uploaded unsuccessfully",
//                                Toast.LENGTH_SHORT).show();
//                        Log.i("Message", message + " " + status);
//
//                    }
//                }
//            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(),
//                        "error",
//                        Toast.LENGTH_LONG).show();
//                Log.e(e.getClass().getName(), e.getMessage(), e);
//            }
//        }
//
//
//    }


}
