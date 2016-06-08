package com.syaona.petalierapp.enums;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.davemorrissey.labs.subscaleview.ImageSource;

import org.rajawali3d.Object3D;

import java.util.ArrayList;

/**
 * Created by smartwavedev on 4/18/16.
 */
public class Singleton {

    public static int loginFromMain;
    public static int selectedDay;
    public static int selectedCollection;
    public static ImageSource imageUri;
    public static Bitmap imageBitmap;
    public static int selectedFlower;
    public static Bitmap chosenColor;
    public static Bitmap image3D;
    public static ArrayList<String> maxColor = new ArrayList<>();
    public static String colorId;
    public static int selectedCard;
    public static int chosenDay;
    public static String paypalUrl;
    public static String uploadedImage;
    public static ArrayList<String> selectedColorFlower = new ArrayList<>();
    public static Object3D object3D;
    public static Object3D flowerBox;

    public static Object3D getFlowerBox() {
        return flowerBox;
    }

    public static void setFlowerBox(Object3D flowerBox) {
        Singleton.flowerBox = flowerBox;
    }

    public static Object3D getObject3D() {
        return object3D;
    }

    public static void setObject3D(Object3D object3D) {
        Singleton.object3D = object3D;
    }

    public static ArrayList<String> getSelectedColorFlower() {
        return selectedColorFlower;
    }

    public static void setSelectedColorFlower(ArrayList<String> selectedColorFlower) {
        Singleton.selectedColorFlower = selectedColorFlower;
    }

    public static String getUploadedImage() {
        return uploadedImage;
    }

    public static void setUploadedImage(String uploadedImage) {
        Singleton.uploadedImage = uploadedImage;
    }

    public static String getPaypalUrl() {
        return paypalUrl;
    }

    public static void setPaypalUrl(String paypalUrl) {
        Singleton.paypalUrl = paypalUrl;
    }

    public static int getChosenDay() {
        return chosenDay;
    }

    public static void setChosenDay(int chosenDay) {
        Singleton.chosenDay = chosenDay;
    }

    public static int getSelectedCard() {
        return selectedCard;
    }

    public static void setSelectedCard(int selectedCard) {
        Singleton.selectedCard = selectedCard;
    }

    public static String getColorId() {
        return colorId;
    }

    public static void setColorId(String colorId) {
        Singleton.colorId = colorId;
    }

    public static ArrayList<String> getMaxColor() {
        return maxColor;
    }

    public static void setMaxColor(ArrayList<String> maxColor) {
        Singleton.maxColor = maxColor;
    }

    public static Bitmap getImage3D() {
        return image3D;
    }

    public static void setImage3D(Bitmap image3D) {
        Singleton.image3D = image3D;
    }

    public static Bitmap getChosenColor() {
        return chosenColor;
    }

    public static void setChosenColor(Bitmap chosenColor) {
        Singleton.chosenColor = chosenColor;
    }

    public static int getSelectedFlower() {
        return selectedFlower;
    }

    public static void setSelectedFlower(int selectedFlower) {
        Singleton.selectedFlower = selectedFlower;
    }

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
