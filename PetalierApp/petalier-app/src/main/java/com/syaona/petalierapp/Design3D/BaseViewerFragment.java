package com.syaona.petalierapp.Design3D;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.syaona.petalierapp.R;
import com.syaona.petalierapp.enums.Singleton;

import org.rajawali3d.renderer.ISurfaceRenderer;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.IDisplay;
import org.rajawali3d.view.ISurface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class BaseViewerFragment extends Fragment implements IDisplay {

    public static final String TAG = "BaseViewerFragment";

    protected ProgressBar mProgressBarLoader;
    protected FrameLayout mLayout;
    protected ISurface mRenderSurface;
    protected ISurfaceRenderer mRenderer;

    protected Button mButton;

    protected Button mReset;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the view
        mLayout = (FrameLayout) inflater.inflate(R.layout.rajawali_textureview_fragment, container, false);

        mLayout.findViewById(R.id.relative_layout_loader_container).bringToFront();

        // Find the TextureView
        mRenderSurface = (ISurface) mLayout.findViewById(R.id.rajwali_surface);

        // Create the loader
        mProgressBarLoader = (ProgressBar) mLayout.findViewById(R.id.progress_bar_loader);
        mProgressBarLoader.setVisibility(View.GONE);

        mButton = (Button) mLayout.findViewById(R.id.button_photo);

//        mReset = (Button) mLayout.findViewById(R.id.button_reset);


        // Create the renderer
        mRenderer = createRenderer();
        onBeforeApplyRenderer();
        applyRenderer();


        return mLayout;
    }


    protected void onBeforeApplyRenderer() {
    }

    @CallSuper
    protected void applyRenderer() {
        mRenderSurface.setSurfaceRenderer(mRenderer);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mLayout != null)
            mLayout.removeView((View) mRenderSurface);
    }

    @CallSuper
    protected void hideLoader() {
        mProgressBarLoader.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarLoader.setVisibility(View.GONE);
            }
        });
    }

    @CallSuper
    protected void showLoader() {
        mProgressBarLoader.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarLoader.setVisibility(View.VISIBLE);
            }
        });
    }

    protected static abstract class BaseViewerRenderer extends Renderer {

        final BaseViewerFragment baseViewerFragment;
        Bitmap lastScreenshot = null;
        boolean screenshot;

        public BaseViewerRenderer(Context context, @Nullable BaseViewerFragment fragment) {
            super(context);
            baseViewerFragment = fragment;
        }

        @Override
        public void onRenderSurfaceCreated(EGLConfig config, GL10 gl, int width, int height) {
            if (baseViewerFragment != null) baseViewerFragment.showLoader();
            super.onRenderSurfaceCreated(config, gl, width, height);
            if (baseViewerFragment != null) baseViewerFragment.hideLoader();
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        }

        @Override
        public void onTouchEvent(MotionEvent e) {

        }

        @Override
        public void onRenderFrame(GL10 gl) {
            super.onRenderFrame(gl);

            Activity activity = (Activity) mContext;
            View view = activity.findViewById(R.id.rajwali_surface);

            int mViewportWidth = view.getWidth();
            int mViewportHeight = view.getHeight();

            if(screenshot){
                int screenshotSize = mViewportWidth * mViewportHeight;
                ByteBuffer bb = ByteBuffer.allocateDirect(screenshotSize * 4);
                bb.order(ByteOrder.nativeOrder());
                gl.glReadPixels(0, 0, mViewportWidth, mViewportHeight, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
                int pixelsBuffer[] = new int[screenshotSize];
                bb.asIntBuffer().get(pixelsBuffer);
                bb = null;
                Bitmap bitmap = Bitmap.createBitmap(mViewportWidth, mViewportHeight, Bitmap.Config.RGB_565);
                bitmap.setPixels(pixelsBuffer, screenshotSize-mViewportWidth, -mViewportWidth, 0, 0, mViewportWidth, mViewportHeight);
                pixelsBuffer = null;

                short sBuffer[] = new short[screenshotSize];
                ShortBuffer sb = ShortBuffer.wrap(sBuffer);
                bitmap.copyPixelsToBuffer(sb);

                //Making created bitmap (from OpenGL points) compatible with Android bitmap
                for (int i = 0; i < screenshotSize; ++i) {
                    short v = sBuffer[i];
                    sBuffer[i] = (short) (((v&0x1f) << 11) | (v&0x7e0) | ((v&0xf800) >> 11));
                }
                sb.rewind();
                bitmap.copyPixelsFromBuffer(sb);
                lastScreenshot = bitmap;

                try {
                    saveScreenshot(lastScreenshot);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                screenshot = false;
            }

        }

        public void takeScreenshot() {
            screenshot = true;
        }

        public void saveScreenshot(Bitmap bitmap) throws IOException {
            File imagePath = new File(Environment.getExternalStorageDirectory() + "/screenshot.png");
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(imagePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                Log.e("3DCapture", e.getMessage(), e);
            } catch (IOException e) {
                Log.e("3DCapture", e.getMessage(), e);
            }
        }

    }

}
