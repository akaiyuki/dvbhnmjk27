package com.syaona.petalierapp.Design3D;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.syaona.petalierapp.R;

import org.rajawali3d.renderer.ISurfaceRenderer;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.IDisplay;
import org.rajawali3d.view.ISurface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class BaseViewerFragment extends Fragment implements IDisplay {

    public static final String TAG = "BaseViewerFragment";

    protected ProgressBar mProgressBarLoader;
    protected FrameLayout mLayout;
    protected ISurface mRenderSurface;
    protected ISurfaceRenderer mRenderer;

    protected Button mButton;

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

    }

}
