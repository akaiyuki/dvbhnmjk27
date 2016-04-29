/***
 Copyright (c) 2015 CommonsWare, LLC

 Licensed under the Apache License, Version 2.0 (the "License"); you may
 not use this file except in compliance with the License. You may obtain
 a copy of the License at http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.syaona.petalierapp.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.syaona.petalierapp.enums.Singleton;

public class PictureActivity extends Activity
    implements PictureFragment.Contract {
  private static final int REQUEST_CAMERA=1337;
  private static final String TAG_PLAYGROUND=PictureFragment.class.getCanonicalName();
  private static final String TAG_RESULT=ResultFragment.class.getCanonicalName();
  private static final String STATE_OUTPUT=
    "com.commonsware.cwac.cam2.playground.PictureActivity.STATE_OUTPUT";
  private PictureFragment playground=null;
  private ResultFragment result=null;
  private Uri output=null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (savedInstanceState!=null) {
      output=savedInstanceState.getParcelable(STATE_OUTPUT);
    }

    if (!Environment.MEDIA_MOUNTED
      .equals(Environment.getExternalStorageState())) {
      Toast
        .makeText(this, "Cannot access external storage!",
          Toast.LENGTH_LONG)
        .show();
      finish();
    }

    playground=(PictureFragment)getFragmentManager().findFragmentByTag(TAG_PLAYGROUND);
    result=(ResultFragment)getFragmentManager().findFragmentByTag(TAG_RESULT);

    if (playground==null) {
      playground=new PictureFragment();
      getFragmentManager()
          .beginTransaction()
          .add(android.R.id.content, playground, TAG_PLAYGROUND)
          .commit();
    }

    if (result==null) {
      result=ResultFragment.newInstance();
      getFragmentManager()
          .beginTransaction()
          .add(android.R.id.content, result, TAG_RESULT)
          .hide(result)
          .commit();
    }

    if (!playground.isVisible() && !result.isVisible()) {
      getFragmentManager()
          .beginTransaction()
          .hide(result)
          .show(playground)
          .commit();
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putParcelable(STATE_OUTPUT, output);
  }

  @Override
  public void takePicture(Intent i) {
    startActivityForResult(i, REQUEST_CAMERA);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode==REQUEST_CAMERA) {
      if (resultCode == Activity.RESULT_OK) {
        Bitmap bitmap=data.getParcelableExtra("data");



          int width = bitmap.getWidth();
          int height = bitmap.getHeight();
          int newWidth = bitmap.getWidth();
          int newHeight = bitmap.getHeight();

          // calculate the scale - in this case = 0.4f
          float scaleWidth = ((float) newWidth) / width;
          float scaleHeight = ((float) newHeight) / height;

          // createa matrix for the manipulation
          Matrix matrix = new Matrix();
          // resize the bit map
          matrix.postScale(scaleWidth, scaleHeight);
          // rotate the Bitmap
          matrix.postRotate(90);
          // recreate the new Bitmap
          Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                  bitmap.getWidth(), bitmap.getHeight(), matrix, true);

          BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);





          if (bitmap == null) {
          result.setImage(output);
        }
        else {
          result.setImage(resizedBitmap);
              Singleton.setImageBitmap(resizedBitmap);
              startActivity(new Intent(PictureActivity.this, PhotoFromCameraActivity.class));
              finish();

          }

        getFragmentManager()
            .beginTransaction()
            .hide(playground)
            .show(result)
            .addToBackStack(null)
            .commit();
      }
    }
  }

  @Override
  public void setOutput(Uri uri) {
    output=uri;
  }
}
