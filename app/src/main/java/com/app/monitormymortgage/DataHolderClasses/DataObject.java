package com.app.monitormymortgage.DataHolderClasses;

/**
 * Created by Sangram on 19/02/16.
 */
public class DataObject {
     String mText1;
     String mText2;

    public String getmText3() {
        return mText3;
    }

    public void setmText3(String mText3) {
        this.mText3 = mText3;
    }

    String mText3;

    public DataObject (String text1, String text2, String text3){
        mText1 = text1;
        mText2 = text2;
        mText3=text3;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }
}
