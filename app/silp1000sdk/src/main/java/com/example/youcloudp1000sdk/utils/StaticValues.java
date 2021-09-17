package com.example.youcloudp1000sdk.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

/**
 * Created by shankar.savant on 30-03-2017.
 */

public class StaticValues {
    public static Bitmap custReceipt;
    public static Bitmap retailReceipt;
    public static Bitmap currenLocImg;
    public static Bitmap duplReceipt;
    public static Bitmap custSign;
    public static String deviceType;
    public static boolean isY2000;

    public static boolean displayMap = false;

    public static Bitmap getCustReceipt() {
        return custReceipt;
    }

    public static void setCustReceipt(Bitmap custReceipt) {
        StaticValues.custReceipt = scaleBitmap(custReceipt);
    }

    public static Bitmap getRetailReceipt() {
        return retailReceipt;
    }

    public static void setRetailReceipt(Bitmap retailReceipt) {
       StaticValues.retailReceipt = scaleBitmap(retailReceipt);
    }

    public static Bitmap getDuplReceipt() {
        return duplReceipt;
    }

    public static void setDuplReceipt(Bitmap duplReceipt) {
        StaticValues.duplReceipt = scaleBitmap(duplReceipt);
    }

    public static Bitmap getCustSign() {
        return custSign;
    }

    public static Bitmap getCurrenLocImg() {
        return currenLocImg;
    }

    public static void setCurrenLocImg(Bitmap currenLocImg) {
       StaticValues.currenLocImg = currenLocImg;
    }

    public static void setCustSign(Bitmap custSign) {
        Log.d("SIGN", "CUST SIGN SAVED");
        StaticValues.custSign = custSign;
    }

    public static boolean getIsY2000() {
        return isY2000;
    }

    public static void setIsY2000(boolean isY2000) {
        StaticValues.isY2000 = isY2000;
    }

    public static Bitmap scaleBitmap(Bitmap b) {
        int reqWidth = 390;
        int reqHeight = 1000;
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, b.getWidth(), b.getHeight()), new RectF(0, 0, reqWidth, reqHeight), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
    }

    public static String getDeviceType() {
        return deviceType;
    }

    public static void setDeviceType(String deviceType) {
        StaticValues.deviceType = deviceType;
    }
}
