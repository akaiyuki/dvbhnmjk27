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
 * Created by smartwavedev on 6/3/16.
 */
public class DianaViewerFragment extends BaseViewerFragment implements View.OnTouchListener {

    public static String TAG = "DianaViewerFragment";
    private ArrayList<String> selectedColor = new ArrayList<>();
    public static DianaViewerFragment INSTANCE = null;

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
            LoaderOBJ  objParser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.diana_obj);;

            try {

                objParser.parse();
                mObjectGroup = objParser.getParsedObject();

                Object3D box = new Object3D();


                //Register objects in picker
                for (int i = 0; i < mObjectGroup.getNumChildren(); i++) {

                    obj = mObjectGroup.getChildAt(i);
                    Singleton.setObject3D(obj);

                    if (obj.getName().equalsIgnoreCase("box")) {

                        Singleton.setFlowerBox(mObjectGroup);

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
                            material.addTexture(new Texture("boxTexture", R.drawable.roundbox_black));
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
                            material.addTexture(new Texture("petalTexture", FlowerTexture.White.getResource()));
                        } catch (ATexture.TextureException e) {
                            e.printStackTrace();
                        }

                        if (obj.getNumChildren() > 0) {
                            obj = obj.getChildAt(0);
                            obj.setMaterial(material);
                        }

                        if (obj.getName().equalsIgnoreCase("box")){

                            Singleton.setFlowerBox(mObjectGroup);

                            material.getTextureList().clear();
                            try {
                                material.addTexture(new Texture("boxTexture", R.drawable.roundbox_black));
                            } catch (ATexture.TextureException e) {
                                e.printStackTrace();
                            }

                        }




                        if (!obj.getName().equalsIgnoreCase("box")){

                            for(int x=0; x<53; x++) {
                                // -- Clone from the main object and then set a position and rotation.
                                Object3D c = obj.clone();
                                c.setName("flower_"+x);

                                if (x == 1){
                                    c.setPosition(0.0, 1.8, -1.8);
                                    c.setRotation(0.0, -45.0, 5.0);
                                } else if (x == 2){
                                    c.setPosition(0.3, -0.3, 0.0);
                                    c.setRotation(0.0, -15.0, 5.0);
                                } else if (x == 3){
                                    c.setPosition(0.6, 0.0, -0.4);
                                    c.setRotation(15.0, -20.0, 5.0);
                                } else if (x == 4){
                                    c.setPosition(0.0, 0.0, -0.1);
                                    c.setRotation(0.0, -60.0, 0.0);
                                } else if (x == 5){
                                    c.setPosition(0.1, 0.0, -0.1);
                                    c.setRotation(0.0, -78.0, 0.0);
                                } else if (x == 6){
                                    c.setPosition(0.0, 0.0, -0.8);
                                    c.setRotation(0.0, -86.0, 0.0);
                                } else if (x == 7){
                                    c.setPosition(-0.4, 0.0, -1.0);
                                    c.setRotation(0.0, -100.0, 0.0);
                                } else if (x == 8){
                                    c.setPosition(-0.7, 0.0, -1.4);
                                    c.setRotation(0.0, -113.0, 0.0);
                                } else if (x == 9){
                                    c.setPosition(-0.8, 0.0, -0.7);
                                    c.setRotation(0.0, -140.0, 0.0);
                                } else if (x == 10){
                                    c.setPosition(-1.0, 0.0, -0.4);
                                    c.setRotation(0.0, -160.0, 0.0);
                                } else if (x == 11){
                                    c.setPosition(-1.0, 0.0, -0.1);
                                    c.setRotation(0.0, -180.0, 0.0);
                                } else if (x == 12){
                                    c.setPosition(-1.8, 0.0, 0.5);
                                    c.setRotation(0.0, -190.0, 0.0);
                                } else if (x == 13){
                                    c.setPosition(-1.6, 0.0, 1.2);
                                    c.setRotation(0.0, -210.0, 0.0);
                                } else if (x == 14){
                                    c.setPosition(-1.0, 1.2, 1.9);
                                    c.setRotation(0.0, -220.0, 0.0);
                                } else if (x == 15){
                                    c.setPosition(-0.8, 0.0, 1.4);
                                    c.setRotation(0.0, -240.0, 0.0);
                                } else if (x == 16){
                                    c.setPosition(-0.4, 0.0, 1.5);
                                    c.setRotation(0.0, -260.0, 0.0);
                                } else if (x == 17){
                                    c.setPosition(0.0, 0.0, 0.8);
                                    c.setRotation(0.0, -290.0, 0.0);
                                } else if (x == 18){
                                    c.setPosition(0.8, 0.0, 1.9);
                                    c.setRotation(0.0, -290.0, 0.0);
                                } else if (x == 19){
                                    c.setPosition(0.5, 0.0, 0.5);
                                    c.setRotation(0.0, -330.0, 0.0);
                                }else if (x == 20){ // start 2nd row
                                    c.setPosition(0.6, 1.2, -0.8);
                                    c.setRotation(0.0, 0.0, 0.0);
                                } else if (x == 21){
                                    c.setPosition(-0.1, 1.1, -0.8);
                                    c.setRotation(0.0, -27.0, 0.0);
                                } else if (x == 22){
                                    c.setPosition(0.2, 1.1, -1.2);
                                    c.setRotation(0.0, -40.0, 0.0);
                                } else if (x == 23){
                                    c.setPosition(-0.5, 1.2, -0.6);
                                    c.setRotation(0.0, -90.0, 0.0);
                                } else if (x == 24){
                                    c.setPosition(-1.2, 1.2, -1.3);
                                    c.setRotation(0.0, -100.0, 0.0);
                                } else if (x == 25){
                                    c.setPosition(-1.6, 1.1, -1.0);
                                    c.setRotation(0.0, -120.0, 0.0);
                                } else if (x == 26){
                                    c.setPosition(-1.7, 1.1, 0.0);
                                    c.setRotation(0.0, -150.0, 0.0);
                                } else if (x == 27){
                                    c.setPosition(1.3, 1.1, -0.2);
                                    c.setRotation(0.0, -330.0, 0.0);
                                } else if (x == 28){
                                    c.setPosition(0.6, 1.1, -0.4);
                                    c.setRotation(0.0, -320.0, 0.0);
                                } else if (x == 29){
                                    c.setPosition(0.4, 1.1, -0.2);
                                    c.setRotation(0.0, -300.0, 0.0);
                                } else if (x == 30){
                                    c.setPosition(0.3, 1.2, -0.2);
                                    c.setRotation(0.0, -280.0, 0.0);
                                } else if (x == 31){
                                    c.setPosition(1.2, 2.3, 0.9);
                                    c.setRotation(0.0, -260.0, 0.0);
                                } else if (x == 32){
                                    c.setPosition(-0.6, 1.1, 1.2);
                                    c.setRotation(0.0, 150.0, 0.0);
                                } else if (x == 33){
                                    c.setPosition(-0.9, 2.1, 1.7);
                                    c.setRotation(0.0, -190.0, 0.0);
                                } else if (x == 34){
                                    c.setPosition(0.1, 1.1, 0.7);
                                    c.setRotation(0.0, -200.0, 0.0);
                                } else if (x == 35){ //3rd row
                                    c.setPosition(0.9, 2.3, -1.4);
                                    c.setRotation(0.0, 7.0, 0.0);
                                } else if (x == 36){
                                    c.setPosition(0.3, 2.3, -1.8);
                                    c.setRotation(0.0, -20.0, 0.0);
                                } else if (x == 37){
                                    c.setPosition(0.5, 1.1, -1.8);
                                    c.setRotation(0.0, -50.0, 0.0);
                                } else if (x == 38){
                                    c.setPosition(-1.0, 2.2, -2.2);
                                    c.setRotation(0.0, -70.0, 0.0);
                                } else if (x == 39){
                                    c.setPosition(1.7, 2.2, -0.3);
                                    c.setRotation(0.0, 45.0, 0.0);
                                } else if (x == 40){
                                    c.setPosition(1.6, 2.3, 1.5);
                                    c.setRotation(0.0, 90.0, 0.0);
                                } else if (x == 41){
                                    c.setPosition(-0.8, 2.2, 1.3);
                                    c.setRotation(0.0, 190.0, 0.0);
                                } else if (x == 42){
                                    c.setPosition(-1.8, 2.2, -0.2);
                                    c.setRotation(0.0, 240.0, 0.0);
                                } else if (x == 43){
                                    c.setPosition(1.1, 2.1, 1.1);
                                    c.setRotation(0.0, 120.0, 0.0);
                                } else if (x == 44){
                                    c.setPosition(1.5, 0.9, -1.4);
                                    c.setRotation(180.0, 180.0, 130.0);
                                } else if (x == 45){
                                    c.setPosition(-0.9, 1.1, 1.3);
                                    c.setRotation(180.0, 180.0, 100.0);
                                } else if (x == 46){
                                    c.setPosition(-0.6, 1.7, -1.8);
                                    c.setRotation(180.0, 180.0, 160.0);
                                } else if (x == 47){
                                    c.setPosition(0.8, 1.4, -1.0);
                                    c.setRotation(180.0, 180.0, 150.0);
                                } else if (x == 48){
                                    c.setPosition(2.0, 1.3, -1.7);
                                    c.setRotation(180.0, 180.0, 150.0);
                                } else if (x == 49){
                                    c.setPosition(1.0, 1.3, 1.5);
                                    c.setRotation(300.0, 300.0, 70.0);
                                } else if (x == 50){
                                    c.setPosition(0.2, 0.8, -0.3);
                                    c.setRotation(180.0, 180.0, 110.0);
                                }



                                material = new Material();
                                material.setColorInfluence(0);

                                diffuseMethod = new DiffuseMethod.Lambert();
                                diffuseMethod.setIntensity(05.f);

                                material.setDiffuseMethod(diffuseMethod);
                                material.enableLighting(true);
                                material.setCurrentObject(c);
                                try {
                                    material.addTexture(new Texture("petalTexture", FlowerTexture.White.getResource()));
                                } catch (ATexture.TextureException e) {
                                    e.printStackTrace();
                                }

                                c.setMaterial(material);

                                mPicker.registerObject(c);
                                getCurrentScene().addChild(c);
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

            Log.d("selected_flower", String.valueOf(object.getPosition()));

            Singleton.setImage3D(null);

            if (object.getName().equalsIgnoreCase("box")) {

                String boxColorSelected = PSharedPreferences.getSomeStringValue(AppController.getInstance(),"box_color");

                Material material = object.getMaterial();
                material.getTextureList().clear();

                if (boxColorSelected.equalsIgnoreCase("white")) {
                    try {
                        material.addTexture(new Texture("boxTexture", R.drawable.roundbox_white));
//                        Log.d("boxcolor", "white");
                    } catch (ATexture.TextureException e) {
                        e.printStackTrace();
                    }
                } else if (boxColorSelected.equalsIgnoreCase("black")) {
                    try {
                        material.addTexture(new Texture("boxTexture", R.drawable.roundbox_black));
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

    public void changeBoxColorToWhite(){

        Object3D object3D = Singleton.getFlowerBox();

        for (int i = 0; i <object3D.getNumChildren(); i++){
            Object3D object3D1 = object3D.getChildAt(i);


            Log.i("objects_button", object3D1.getName());

            if (object3D1.getName().equalsIgnoreCase("box")){
                final Material material = new Material();
                material.setColorInfluence(0);

                DiffuseMethod.Lambert diffuseMethod = new DiffuseMethod.Lambert();
                diffuseMethod.setIntensity(05.f);

                material.setDiffuseMethod(diffuseMethod);
                material.enableLighting(true);
                material.setCurrentObject(object3D1);

                try {
                    material.addTexture(new Texture("boxTexture", R.drawable.squarebox_white));
                } catch (ATexture.TextureException e) {
                    e.printStackTrace();
                }

                if (object3D1.getNumChildren() > 0) {
                    object3D1 = object3D1.getChildAt(0);
                    object3D1.setMaterial(material);
                }

                object3D1.setMaterial(material);
            }

        }

    }


    public void changeBoxColorToBlack(){
        Object3D object3D = Singleton.getFlowerBox();

        for (int i = 0; i <object3D.getNumChildren(); i++){
            Object3D object3D1 = object3D.getChildAt(i);


            Log.i("objects_button", object3D1.getName());

            if (object3D1.getName().equalsIgnoreCase("box")){
                final Material material = new Material();
                material.setColorInfluence(0);

                DiffuseMethod.Lambert diffuseMethod = new DiffuseMethod.Lambert();
                diffuseMethod.setIntensity(05.f);

                material.setDiffuseMethod(diffuseMethod);
                material.enableLighting(true);
                material.setCurrentObject(object3D1);

                try {
                    material.addTexture(new Texture("boxTexture", R.drawable.squarebox_black));
                } catch (ATexture.TextureException e) {
                    e.printStackTrace();
                }

                if (object3D1.getNumChildren() > 0) {
                    object3D1 = object3D1.getChildAt(0);
                    object3D1.setMaterial(material);
                }

                object3D1.setMaterial(material);
            }

        }
    }




}