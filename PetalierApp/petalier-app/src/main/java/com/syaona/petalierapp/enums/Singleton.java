package com.syaona.petalierapp.enums;

import android.graphics.Bitmap;
import android.net.Uri;

import com.davemorrissey.labs.subscaleview.ImageSource;

/**
 * Created by smartwavedev on 4/18/16.
 */
public class Singleton {

    public static int loginFromMain;
    public static int selectedDay;
    public static int selectedCollection;
    public static ImageSource imageUri;
    public static Bitmap imageBitmap;

    public static ImageSource getImageUri() {
        return imageUri;
    }

    public static void setImageUri(ImageSource imageUri) {
        Singleton.imageUri = imageUri;
    }

    public static Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public static void setImageBitmap(Bitmap imageBitmap) {
        Singleton.imageBitmap = imageBitmap;
    }


    public static int getSelectedCollection() {
        return selectedCollection;
    }

    public static void setSelectedCollection(int selectedCollection) {
        Singleton.selectedCollection = selectedCollection;
    }

    public static int getSelectedDay() {
        return selectedDay;
    }

    public static void setSelectedDay(int selectedDay) {
        Singleton.selectedDay = selectedDay;
    }

    public static int getLoginFromMain() {
        return loginFromMain;
    }

    public static void setLoginFromMain(int loginFromMain) {
        Singleton.loginFromMain = loginFromMain;
    }


}
