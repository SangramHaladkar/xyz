package com.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;

import java.util.Hashtable;

/**
 * @author Rohan
 */
public class CustomTypefaces {

    public  enum ICON_GRAVITY{
        LEFT(0),
        RIGHT(1);

        private final int value;

        private ICON_GRAVITY(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }

    }
    private static final String TAG = "Typefaces";

    private static final Hashtable<String, Typeface> cache = new Hashtable<>();
    /**
     * add font list here
     */
    private static String fontTypeFaceFontFile = "";

    private static Typeface getFromAsset(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }


    public static String getTypeFaceFontFile() {
        return fontTypeFaceFontFile;
    }

    public static void setTypeFaceFontFile(String fontAwesomeTypeFaceFontFile) {
        CustomTypefaces.fontTypeFaceFontFile = fontAwesomeTypeFaceFontFile;
    }


    public static Typeface getFont(Context context) {
        return getFromAsset(context, fontTypeFaceFontFile);
    }

    /**
     * @param context
     * @param asset   (string font name/path)
     * @return Typeface
     */
    public static Typeface getFontCustom(Context context, String asset) {
        return getFromAsset(context, asset);
    }

    /**
     * set font to multiple textview at once
     *
     * @param context
     * @param textViews
     */
    public static void setTypefacesToTextView(Context context, TextView... textViews) {
        if (fontTypeFaceFontFile.isEmpty()) {
            throw new IllegalStateException("Please call 'setTypeFaceFontFile' first");
        }
        for (TextView txt : textViews) {
            txt.setTypeface(CustomTypefaces.getFont(context));
        }
    }

    /**
     * set font to multiple editview at once
     *
     * @param context
     * @param textViews
     */
    public static void setTypefacesToEditView(Context context, EditText... textViews) {
        if (fontTypeFaceFontFile.isEmpty()) {
            throw new IllegalStateException("Please call 'setTypeFaceFontFile' first");
        }
        for (EditText txt : textViews) {
            txt.setTypeface(CustomTypefaces.getFont(context));
        }
    }

    /**
     * set font to multiple buttons at once
     * @param context
     * @param buttons
     */
    public static void setTypefacesToButton(Context context, Button... buttons) {
        if (fontTypeFaceFontFile.isEmpty()) {
            throw new IllegalStateException("Please call 'setTypeFaceFontFile' first");
        }
        for (Button button : buttons) {
            button.setTypeface(CustomTypefaces.getFont(context));
        }
    }

    /**
     *
     * @param iconValue
     * @param context
     * @param size size of the icon in dp
     * @param color color value
     * @return
     */
    public static Drawable getIconIntDp(Context context,String iconKey,int size,int color) {
        IconDrawable dra=new IconDrawable(context, iconKey);
        dra.color(color);
        dra.sizeDp(size);
        return dra;
    }


    public static void setIconToEditTextView(Context context,String iconValue,EditText edittextView,int size,int color,int gravity)
    {

        Drawable toAttach=CustomTypefaces.getIconIntDp(context,iconValue,size,color);
        if(gravity==ICON_GRAVITY.LEFT.ordinal())
        {
            edittextView.setCompoundDrawablesWithIntrinsicBounds(toAttach,null,null,null);
        }
        else if(gravity==ICON_GRAVITY.RIGHT.ordinal())
        {
            edittextView.setCompoundDrawablesWithIntrinsicBounds(null,null,toAttach,null);
        }
        edittextView.setCompoundDrawablePadding(7);//space between text and icon
    }
}
