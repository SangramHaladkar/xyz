package com.app.monitormymortgage.BaseClasses;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.monitormymortgage.R;

/**
 * Created by Sangram on 09/03/16.
 */
public class ToastMessage extends ViewGroup {

    Context mContext;
    Activity activity;
    LayoutInflater inflater;
    View toastLayout;
    ImageView image;
    TextView text;
    DisplayMetrics displaymetrics;
    int height;
    int width;
    Toast toast;

    public ToastMessage(Context context) {
        super(context);
        mContext = context;
    }

    public void showToastMsg(int message, int timeDuration) {
        try {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            toastLayout = inflater.inflate(R.layout.toast_layout,
                    (ViewGroup) findViewById(R.id.toast_layout_root));


            image = (ImageView) toastLayout.findViewById(R.id.image);
            image.setImageResource(R.mipmap.iconlogowhite);
            text = (TextView) toastLayout.findViewById(R.id.toastText);
            text.setText(message);

            displaymetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            height = displaymetrics.heightPixels;
            width = displaymetrics.widthPixels;

            toast = new Toast(mContext);
            toast.setGravity(Gravity.BOTTOM, 0, height / 4);
            toast.setDuration(timeDuration);
            toast.setView(toastLayout);
            toast.show();
        } catch (Exception e) {
//            Utility.saveExceptionDetails(mContext, e);
            e.printStackTrace();
        }

    }
    public void showToastMsg(String message, int timeDuration) {
        try {
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            toastLayout = inflater.inflate(R.layout.toast_layout,
                    (ViewGroup) findViewById(R.id.toast_layout_root));


            image = (ImageView) toastLayout.findViewById(R.id.image);
            image.setImageResource(R.mipmap.iconlogowhite);
            text = (TextView) toastLayout.findViewById(R.id.toastText);
            text.setText(message);

            displaymetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            height = displaymetrics.heightPixels;
            width = displaymetrics.widthPixels;

            toast = new Toast(mContext);
            toast.setGravity(Gravity.BOTTOM, 0, height / 4);
            toast.setDuration(timeDuration);
            toast.setView(toastLayout);
            toast.show();
        } catch (Exception e) {
//            Utility.saveExceptionDetails(mContext, e);
            e.printStackTrace();
        }

    }

	/*	public Dialog getCustomeDialog(String message){
        Dialog dialog = null;
		try
		{
			dialog = new Dialog(mContext, android.R.style.Theme_Translucent);
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.custome_progressdialog,
			                               (ViewGroup)findViewById(R.id.cust_progress));
			dialog.setContentView(view);
			dialog.setCancelable(true);
			TextView text = (TextView) dialog.findViewById(R.id.txtProgressMsg);
			text.setText(message);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return dialog;
	}*/


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub

    }
}