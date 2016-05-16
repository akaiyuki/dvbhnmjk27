package com.syaona.petalierapp.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.PRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by smartwavedev on 5/9/16.
 */
public class ImagePreviewActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;

    private EditText editTextName;

    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL = PRequest.getApiRootForResource()+PRequest.apiMethodPostDepositSlip;

    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    public static ImagePreviewActivity INSTANCE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);

        INSTANCE = this;

        buttonChoose = (Button) findViewById(R.id.btnNewImage);
        buttonUpload = (Button) findViewById(R.id.btnUpload);


        imageView  = (ImageView) findViewById(R.id.imageView1);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        openGallery();

//        showFileChooser();


    }

    public void openGallery() {

        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), PICK_IMAGE_REQUEST);

    }



    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onClick(View v) {

        if(v == buttonChoose){
//            showFileChooser();
            openGallery();
        }

        if(v == buttonUpload){
//            uploadImage();
//            requestApiUploadPhoto();
//            new ImageUploadTask().execute();
        }
    }

}
