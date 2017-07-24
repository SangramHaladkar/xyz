package com.app.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

/**
 * Created by Rohan Pawar on 21/04/16.
 */
public class IconButton extends com.joanzapata.iconify.widget.IconButton {

    public IconButton(Context context) {
        super(context);
        if (!isInEditMode()) {
            init(null, context);
        }
    }

    public IconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(attrs, context);
    }

    public IconButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, context);
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
