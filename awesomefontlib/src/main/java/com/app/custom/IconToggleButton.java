package com.app.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * Created by Rohan Pawar on 21/04/16.
 */
public class IconToggleButton extends com.joanzapata.iconify.widget.IconToggleButton {

    public IconToggleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, context);
    }

    public IconToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(attrs, context);
    }

    public IconToggleButton(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(null, context);
        }
    }

    private void init(AttributeSet attrs, Context mContext) {
        if (attrs != null) {

            try {
                TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
                String fontFamily = a.getString(R.styleable.CustomTextView_fontFamily);

                if (fontFamily != null) {
                    CustomTypefaces.setTypeFaceFontFile(fontFamily);
                    CustomTypefaces.setTypefacesToTextView(mContext, this);
                    a.recycle();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    }
}
