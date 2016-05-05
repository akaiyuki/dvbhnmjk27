package com.syaona.petalierapp.Design3D;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.syaona.petalierapp.R;
import com.syaona.petalierapp.core.AppController;
import com.syaona.petalierapp.enums.Singleton;
import com.syaona.petalierapp.test.DesignBoxActivity;
import com.syaona.petalierapp.view.CircleTransform;

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


public class ModelViewerFragment extends BaseViewerFragment implements View.OnTouchListener {

    public static String TAG = "ModelViewerFragment";

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

        //Get Photo on button click
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((LoadModelRenderer) mRenderer).setToTopView();
                View v = ((View) mRenderSurface);
                v.setDrawingCacheEnabled(true);
                Bitmap bitmap = v.getDrawingCache();
//
//
//                /* trial */
//                v.buildDrawingCache();
//                Bitmap bitmap = ((LoadModelRenderer) mRenderer).setToTopView();

                /* save image to singleton */

//                if (bitmap != null && !bitmap.isRecycled()) {
//                    bitmap.recycle();
//                    bitmap = null;
//                }
                /* sample bitmap image from drawable */
//                Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.card2);
//                v.setDrawingCacheEnabled(false);

                /* save bitmap to singleton */
//                Singleton.setImage3D(bitmap.createBitmap(v.getDrawingCache()));
                Singleton.setImage3D(bitmap);
//                v.setDrawingCacheEnabled(false);


                displayBitmap(bitmap);
            }
        });


        return mLayout;
    }

    private void displayBitmap(Bitmap bitmap) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bitmap_display);

        ImageView iv = (ImageView) dialog.findViewById(R.id.imageView_bitmap);
        iv.setImageBitmap(bitmap);


//                if (bitmap != null && !bitmap.isRecycled()) {
//                    bitmap.recycle();
//                    bitmap = null;
//                }

//        Singleton.setImage3D(bitmap.createBitmap());


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
            LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),
                    mTextureManager, R.raw.boxcollection_obj);
            try {

                objParser.parse();
                mObjectGroup = objParser.getParsedObject();

                //Register objects in picker
                for (int i = 0; i < mObjectGroup.getNumChildren(); i++) {

                    Object3D obj = mObjectGroup.getChildAt(i);
                    obj.setName("obj_" + i);

                    if (i > 0) {

                        //Create new material for each 3d object
                        Material material = new Material();
                        material.setColorInfluence(0);
                        try {
                            material.addTexture(new Texture("petalTexture", FlowerTexture.Red.getResource()));
                        } catch (ATexture.TextureException e) {
                            e.printStackTrace();
                        }

                        DiffuseMethod.Lambert diffuseMethod = new DiffuseMethod.Lambert();
                        diffuseMethod.setIntensity(05.f);

                        material.setDiffuseMethod(diffuseMethod);
                        material.enableLighting(true);
                        material.setCurrentObject(obj);

                        obj.setMaterial(material);

                        if (obj.getNumChildren() > 0) {
                            obj = obj.getChildAt(0);
                            obj.setMaterial(material);
                        }
                    }

                    mPicker.registerObject(obj);
                    getCurrentScene().addChild(obj);

                    Log.d("Test", "obj_" + i + " registered" + " numChild: " + obj.getNumChildren());
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

        }

        public void getObjectAt(float x, float y) {
            mPicker.getObjectAt(x, y);
        }

        @Override
        public void onObjectPicked(Object3D object) {

            if (object.getName().equalsIgnoreCase("obj_0")) return;

            Material material = object.getMaterial();
            material.getTextureList().clear();
            try {
//                material.addTexture(new Texture("petalTexture", FlowerTexture.randomize().getResource()));


//                    material.addTexture(new Texture("petalTexture", Singleton.getChosenColor()));


                Texture mytexture = new Texture("texture", Singleton.getChosenColor());
                material.addTexture(mytexture);


            } catch (ATexture.TextureException e) {
                e.printStackTrace();
            }

            object.setMaterial(material);

        }

        public void setToTopView() {

            Camera camera = new Camera();
            camera.setPosition(0, 20, 5);
            camera.setLookAt(0, 0, 0);

            getCurrentScene().replaceAndSwitchCamera(mArcballCamera, camera);
        }

//        public Bitmap setToTopView() {
//
//            Camera camera = new Camera();
//            camera.setPosition(0, 20, 5);
//            camera.setLookAt(0, 0, 0);
//
//            getCurrentScene().replaceAndSwitchCamera(mArcballCamera, camera);
//
//            View v = ((View) mRenderSurface);
//            v.setDrawingCacheEnabled(true);
////                Bitmap bitmap = v.getDrawingCache();
//
//
//                /* trial */
//            v.buildDrawingCache(true);
////            Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache());
//
////            Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
////            Canvas c = new Canvas(b);
////            v.draw(c);
//
//            Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.card2);
//
//            return b;
//        }


        public void setToNormalView() {

            mArcballCamera.setPosition(0, 5, 15);
            mArcballCamera.setLookAt(0, 0, 0);

            getCurrentScene().replaceAndSwitchCamera(getCurrentCamera(), mArcballCamera);

        }
    }
}