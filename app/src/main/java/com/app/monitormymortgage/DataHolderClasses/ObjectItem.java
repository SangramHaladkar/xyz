package com.app.monitormymortgage.DataHolderClasses;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 * Created by Sangram on 02/05/16.
 */
public class ObjectItem {
    public String base46String;
    public String lenderId;
    public String lenderName;
    public Bitmap imageBitmap;

    public String getLenderName() {
        return lenderName;
    }

    public void setLenderName(String lenderName) {
        this.lenderName = lenderName;
    }

    public String getLenderId() {
        return lenderId;
    }

    public void setLenderId(String lenderId) {
        this.lenderId = lenderId;
    }

    public String getBase46String() {
        return base46String;
    }

    public void setBase46String(String base46String) {

        this.base46String = base46String;
        this.imageBitmap = convertBase64toBitmap(base46String);
    }

    public Bitmap convertBase64toBitmap(String base64String) {

        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

}
