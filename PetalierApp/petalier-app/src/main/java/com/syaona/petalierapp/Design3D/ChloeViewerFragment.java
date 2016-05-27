package com.syaona.petalierapp.Design3D;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.core.PSharedPreferences;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.activity.DesignBoxActivity;

import org.rajawali3d.Object3D;
import org.rajawali3d.cameras.Camera;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.util.ObjectColorPicker;
import org.rajawali3d.util.OnObjectPickedListener;

import java.util.ArrayList;

/**
 * Created by smartwavedev on 5/27/16.
 */
public class ChloeViewerFragment extends BaseViewerFragment implements View.OnTouchListener {

    public static String TAG = "ChloeViewerFragment";
    private ArrayList<String> selectedColor = new ArrayList<>();
    public static ChloeViewerFragment INSTANCE = null;

    private Object3D obj;

    @Override
    public BaseViewerRenderer createRenderer() {
        return new LoadModelRenderer(getActivity(), this);
    }

    @Override
    public int getLayoutID() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        ((View) mRenderSurface).setOnTouchListener(this);

        INSTANCE = this;

        //Get Photo on button click
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((LoadModelRenderer) mRenderer).setToTopView();
            }
        });

        return mLayout;
    }

    private Bitmap getImageFromStorage() {
        String bitmapStoragePath = Environment.getExternalStorageDirectory() + "/screenshot.png";

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        final Bitmap b = BitmapFactory.decodeFile(bitmapStoragePath, options);

        return b;
    }

    public void displayBitmap(Bitmap bitmap) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bitmap_display);

        ImageView iv = (ImageView) dialog.findViewById(R.id.imageView_bitmap);
        iv.setImageBitmap(bitmap);

        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                ((LoadModelRenderer) mRenderer).setToNormalView();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ((LoadModelRenderer) mRenderer).getObjectAt(event.getX(), event.getY());

            DesignBoxActivity.INSTANCE.scrollView.setEnableScrolling(false);

        }

        return getActivity().onTouchEvent(event);
    }

    private final class LoadModelRenderer extends BaseViewerRenderer implements OnObjectPickedListener {
        private Object3D mObjectGroup;
        private CustomCamera mArcballCamera;

        //Object picker class
        private ObjectColorPicker mPicker;


        public LoadModelRenderer(Context context, @Nullable BaseViewerFragment fragment) {
            super(context, fragment);
        }

        @Override
        protected void initScene() {
            mPicker = new ObjectColorPicker(this);
            mPicker.setOnObjectPickedListener(this);

//            //Show guide grid
//            DebugVisualizer debugViz = new DebugVisualizer(this);
//            debugViz.addChild(new GridFloor());
//            getCurrentScene().addChild(debugViz);


            //Loading of model object
            LoaderOBJ  objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.chloe_obj);;

            try {

                objParser.parse();
                mObjectGroup = objParser.getParsedObject();

                Object3D box = new Object3D();


                //Register objects in picker
                for (int i = 0; i < mObjectGroup.getNumChildren(); i++) {

                    obj = mObjectGroup.getChildAt(i);
                    Singleton.setObject3D(obj);

                    if (obj.getName().equalsIgnoreCase("box")) {

                        Log.d("Test", "box come in!");

                        box = obj;

                        Material material = new Material();
                        material.setColorInfluence(0);

                        DiffuseMethod.Lambert diffuseMethod = new DiffuseMethod.Lambert();
                        diffuseMethod.setIntensity(05.f);

                        material.setDiffuseMethod(diffuseMethod);
                        material.enableLighting(true);
                        material.setCurrentObject(box);
                        try {
                            material.addTexture(new Texture("boxTexture", R.drawable.squarebox_black));
                        } catch (ATexture.TextureException e) {
                            e.printStackTrace();
                        }

                        box.setMaterial(material);
                        mPicker.registerObject(box);
                        getCurrentScene().addChild(box);

                    }
                    else {

                        //Create new material for each 3d object
                        Material material = new Material();
                        material.setColorInfluence(0);

                        DiffuseMethod.Lambert diffuseMethod = new DiffuseMethod.Lambert();
                        diffuseMethod.setIntensity(05.f);

                        material.setDiffuseMethod(diffuseMethod);
                        material.enableLighting(true);
                        material.setCurrentObject(obj);

                        try {
                            material.addTexture(new Texture("petalTexture", FlowerTexture.Red.getResource()));
                        } catch (ATexture.TextureException e) {
                            e.printStackTrace();
                        }

                        if (obj.getNumChildren() > 0) {
                            obj = obj.getChildAt(0);
                            obj.setMaterial(material);
                        }

                        if (obj.getName().equalsIgnoreCase("box")){

                            Log.d("input_box", "box");

                            material.getTextureList().clear();
                            try {
                                material.addTexture(new Texture("boxTexture", R.drawable.squarebox_black));
                            } catch (ATexture.TextureException e) {
                                e.printStackTrace();
                            }

                        }




                        if (!obj.getName().equalsIgnoreCase("box")){

                            for(int x=0; x<36; x++) {
                                // -- Clone from the main object and then set a position and rotation.
                                Object3D c = obj.clone();
                                c.setName("flower_"+x);
//                    c.setPosition(-25f + (float)(Math.random() * 50f), -25f + (float)(Math.random() * 50f), -25f + (float)(Math.random() * 50f));
//                    c.setRotation((float)Math.random() * 360, (float)Math.random() * 360, (float)Math.random() * 360);

                                if (x == 1){ /* 1st row */
                                    c.setPosition(1.6, -0.1, 1.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 2){
                                    c.setPosition(2.5, -0.1, 1.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 3){
                                    c.setPosition(3.5, -0.1, 1.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 4){
                                    c.setPosition(4.5, -0.1, 1.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 5){
                                    c.setPosition(5.5, -0.1, 1.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 6){
                                    c.setPosition(0.4, -0.1, 0.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 7){
                                    c.setPosition(0.4, -0.1, -0.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 8){
                                    c.setPosition(0.4, -0.1, -1.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 9){
                                    c.setPosition(0.4, -0.1, -2.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 10){
                                    c.setPosition(0.4, -0.1, -3.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 11){
                                    c.setPosition(1.4, -0.1, 0.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 12){
                                    c.setPosition(2.4, -0.1, 0.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 13){
                                    c.setPosition(3.4, -0.1, 0.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 14){
                                    c.setPosition(4.4, -0.1, 0.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 15){
                                    c.setPosition(5.4, -0.1, 0.2);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 16){
                                    c.setPosition(1.4, -0.1, -0.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 17){
                                    c.setPosition(2.4, -0.1, -0.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 18){
                                    c.setPosition(3.4, -0.1, -0.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 19){
                                    c.setPosition(4.4, -0.1, -0.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 20){
                                    c.setPosition(5.4, -0.1, -0.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 21){
                                    c.setPosition(1.4, -0.1, -1.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 22){
                                    c.setPosition(2.4, -0.1, -1.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 23){
                                    c.setPosition(3.4, -0.1, -1.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 24){
                                    c.setPosition(4.4, -0.1, -1.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 25){
                                    c.setPosition(5.4, -0.1, -1.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 26){
                                    c.setPosition(1.4, -0.1, -2.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 27){
                                    c.setPosition(2.4, -0.1, -2.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 28){
                                    c.setPosition(3.4, -0.1, -2.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 29){
                                    c.setPosition(4.4, -0.1, -2.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 30){
                                    c.setPosition(5.4, -0.1, -2.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 31){
                                    c.setPosition(1.4, -0.1, -3.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 32){
                                    c.setPosition(2.4, -0.1, -3.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 33){
                                    c.setPosition(3.4, -0.1, -3.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 34){
                                    c.setPosition(4.4, -0.1, -3.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                } else if (x == 35){
                                    c.setPosition(5.4, -0.1, -3.9);
                                    c.setRotation(0.0, 20.0, 5.0);
                                }



                                material = new Material();
                                material.setColorInfluence(0);

                                diffuseMethod = new DiffuseMethod.Lambert();
                                diffuseMethod.setIntensity(05.f);

                                material.setDiffuseMethod(diffuseMethod);
                                material.enableLighting(true);
                                material.setCurrentObject(c);
                                try {
                                    material.addTexture(new Texture("petalTexture", FlowerTexture.Red.getResource()));
//                            material.addTexture(new Texture("boxTexture", R.drawable.squarebox_black));
                                } catch (ATexture.TextureException e) {
                                    e.printStackTrace();
                                }

                                c.setMaterial(material);

                                mPicker.registerObject(c);
                                getCurrentScene().addChild(c);

//                    obj.addChild(c);

                                Log.d("flower_clone", c.getName());

                            }
                        }








                        obj.setMaterial(material);
                        mPicker.registerObject(obj);
                        getCurrentScene().addChild(obj);

                    }

                }


                //Set Camera to model
                mArcballCamera = new CustomCamera(mContext, mLayout);
                mArcballCamera.setTarget(mObjectGroup);
                mArcballCamera.setPosition(0, 5, 15);
                getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), mArcballCamera);
                mArcballCamera.setLookAt(0, 0, 0);

            } catch (ParsingException e) {
                e.printStackTrace();
            }


            //Set top light
            DirectionalLight topDirectionalLight = new DirectionalLight();
            topDirectionalLight.setLookAt(0, 0, 0);
            topDirectionalLight.setPower(0.8f);
            topDirectionalLight.setPosition(0, 40, 5);
            topDirectionalLight.enableLookAt();
            getCurrentScene().addLight(topDirectionalLight);

            //Set front direction light
            DirectionalLight frontDirectionalLight = new DirectionalLight();
            frontDirectionalLight.setLookAt(0, 0, 0);
            frontDirectionalLight.setPower(0.6f);
            frontDirectionalLight.setPosition(10, 10, 40);
            frontDirectionalLight.enableLookAt();
            getCurrentScene().addLight(frontDirectionalLight);

            //Set back direction light
            DirectionalLight backDirectionalLight = new DirectionalLight();
            backDirectionalLight.setLookAt(0, 0, 0);
            backDirectionalLight.setPower(0.6f);
            backDirectionalLight.setPosition(10, 10, -40);
            backDirectionalLight.enableLookAt();
            getCurrentScene().addLight(backDirectionalLight);

            //Set background color
            getCurrentScene().setBackgroundColor(Color.LTGRAY);


            BaseViewerFragment.INSTANCE.hideLoader();

        }

        public void getObjectAt(float x, float y) {
            mPicker.getObjectAt(x, y);
        }

        @Override
        public void onObjectPicked(Object3D object) {

            Singleton.setImage3D(null);

            if (object.getName().equalsIgnoreCase("box")) {

                String boxColorSelected = PSharedPreferences.getSomeStringValue(AppController.getInstance(),"box_color");

                Material material = object.getMaterial();
                material.getTextureList().clear();

                if (boxColorSelected.equalsIgnoreCase("white")) {
                    try {
                        material.addTexture(new Texture("boxTexture", R.drawable.squarebox_white));
//                        Log.d("boxcolor", "white");
                    } catch (ATexture.TextureException e) {
                        e.printStackTrace();
                    }
                } else if (boxColorSelected.equalsIgnoreCase("black")) {
                    try {
                        material.addTexture(new Texture("boxTexture", R.drawable.squarebox_black));
//                        Log.d("boxcolor", "black");
                    } catch (ATexture.TextureException e) {
                        e.printStackTrace();
                    }
                }

                object.setMaterial(material);
            } else {
                if (Singleton.getMaxColor().contains(Singleton.getColorId())) {
                    Material material = object.getMaterial();
                    material.getTextureList().clear();
                    try {
                        material.addTexture(new Texture("petalTexture", Singleton.getChosenColor()));
                    } catch (ATexture.TextureException e) {
                        e.printStackTrace();
                    }
                    object.setMaterial(material);
                }

            }

        }

        public void setToTopView() {

            Camera camera = new Camera();
            camera.setPosition(0, 20, 5);
            camera.setLookAt(0, 0, 0);

            getCurrentScene().replaceAndSwitchCamera(mArcballCamera, camera);

            ((LoadModelRenderer) mRenderer).takeScreenshot();

        }

        public void setToNormalView() {

            mArcballCamera.setPosition(0, 5, 15);
            mArcballCamera.setLookAt(0, 0, 0);

            getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), mArcballCamera);

//            Singleton.setImage3D(null);
//            Log.e("normalview","normal view");
        }


        public void resetFlower(){

            mPicker = new ObjectColorPicker(this);
            mPicker.setOnObjectPickedListener(this);

            Object3D box = new Object3D();

            Object3D obj = Singleton.getObject3D();

            if(obj.getName().equalsIgnoreCase("default")) {
//                Log.d("Test", "default come in!");
            }

            else if (obj.getName().equalsIgnoreCase("box")) {

                box = obj;

                Material material = new Material();
                material.setColorInfluence(0);

                DiffuseMethod.Lambert diffuseMethod = new DiffuseMethod.Lambert();
                diffuseMethod.setIntensity(05.f);

                material.setDiffuseMethod(diffuseMethod);
                material.enableLighting(true);
                material.setCurrentObject(box);
                try {
                    material.addTexture(new Texture("petalTexture", FlowerTexture.Red.getResource()));
                } catch (ATexture.TextureException e) {
                    e.printStackTrace();
                }

                box.setMaterial(material);
                mPicker.registerObject(box);
                getCurrentScene().addChild(box);

            } else {

                //Create new material for each 3d object
                Material material = new Material();
                material.setColorInfluence(0);

                DiffuseMethod.Lambert diffuseMethod = new DiffuseMethod.Lambert();
                diffuseMethod.setIntensity(05.f);

                material.setDiffuseMethod(diffuseMethod);
                material.enableLighting(true);
                material.setCurrentObject(obj);

                try {
                    material.addTexture(new Texture("petalTexture", FlowerTexture.Red.getResource()));
                } catch (ATexture.TextureException e) {
                    e.printStackTrace();
                }

                if (obj.getNumChildren() > 0) {
                    obj = obj.getChildAt(0);
                    obj.setMaterial(material);
                }

                obj.setMaterial(material);

                mPicker.registerObject(obj);
                getCurrentScene().addChild(obj);
            }

            //Set Camera to model
            mArcballCamera = new CustomCamera(mContext, mLayout);
            mArcballCamera.setTarget(mObjectGroup);
            mArcballCamera.setPosition(0, 5, 15);
            getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), mArcballCamera);
            mArcballCamera.setLookAt(0, 0, 0);



            //Set top light
            DirectionalLight topDirectionalLight = new DirectionalLight();
            topDirectionalLight.setLookAt(0, 0, 0);
            topDirectionalLight.setPower(0.8f);
            topDirectionalLight.setPosition(0, 40, 5);
            topDirectionalLight.enableLookAt();
            getCurrentScene().addLight(topDirectionalLight);

            //Set front direction light
            DirectionalLight frontDirectionalLight = new DirectionalLight();
            frontDirectionalLight.setLookAt(0, 0, 0);
            frontDirectionalLight.setPower(0.6f);
            frontDirectionalLight.setPosition(10, 10, 40);
            frontDirectionalLight.enableLookAt();
            getCurrentScene().addLight(frontDirectionalLight);

            //Set back direction light
            DirectionalLight backDirectionalLight = new DirectionalLight();
            backDirectionalLight.setLookAt(0, 0, 0);
            backDirectionalLight.setPower(0.6f);
            backDirectionalLight.setPosition(10, 10, -40);
            backDirectionalLight.enableLookAt();
            getCurrentScene().addLight(backDirectionalLight);

            //Set background color
            getCurrentScene().setBackgroundColor(Color.LTGRAY);




        }


    }


    public void displayImage(){
        ((LoadModelRenderer) mRenderer).setToTopView();
    }

    public void setImage(Bitmap bitmap){
        displayBitmap(bitmap);
    }

    public void returntoNormal(){

        ((LoadModelRenderer) mRenderer).setToNormalView();

    }




}