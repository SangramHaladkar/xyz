package com.app.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Rohan Pawar on 21/04/16.
 */
public class CustomTextView extends TextView {
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, Context mContext) {
        if (attrs != null) {

            try {
                TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomTextView);
                String fontFamily = a.getString(R.styleable.CustomTextView_fontFamily);

                if (fontFamily != null) {
                    CustomTypefaces.setTypeFaceFontFile(fontFamily);
                    CustomTypefaces.setTypefacesToTextView(mContext,this);
                    a.recycle();
                }
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
    }
}
