package com.app.monitormymortgage.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.R;

/**
 * Fragment class.
 *
 * Created by wagatsumakenju on 2015/08/25.
 */
public class HelpScreenFirstFragment extends BaseFragment {
    TextView textView,textView2,textView3,textView4;
    ImageView helpscreen_oneimageview;

    /**
     * fields
     */
    private static HelpScreenFirstFragment instance = null;

    /**
     * Create fragment view when paginated.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_frag, container, false);
        textView=(TextView)v.findViewById(R.id.logoImageView);
        textView2=(TextView)v.findViewById(R.id.myMortgageMonitorTextView);
        textView3=(TextView)v.findViewById(R.id.textView3);
        textView4=(TextView)v.findViewById(R.id.sloganTextView);
        helpscreen_oneimageview=(ImageView)v.findViewById(R.id.helpscreen_oneimageview);
        helpscreen_oneimageview.postDelayed(new Runnable() {
            @Override
            public void run() {
                helpscreen_oneimageview.setVisibility(View.VISIBLE);
            }
        }, 2000);

        textView.postDelayed(new Runnable() {
            public void run() {
                textView.setVisibility(View.VISIBLE);
            }
        }, 4000);
        textView2.postDelayed(new Runnable() {
            public void run() {
                textView2.setVisibility(View.VISIBLE);
            }
        }, 6000);
        textView3.postDelayed(new Runnable() {
            public void run() {
                textView3.setVisibility(View.VISIBLE);
            }
        }, 8000);
        textView4.postDelayed(new Runnable() {
            public void run() {
                textView4.setVisibility(View.VISIBLE);
            }
        }, 10000);
       return v;
    }


    /**
     * Returns new instance.
     *
     * @param text
     * @return
     */
    public static HelpScreenFirstFragment newInstance(String text){

        if(instance == null){
            // new instance
            instance = new HelpScreenFirstFragment();

            // sets data to bundle
            Bundle bundle = new Bundle();
            bundle.putString("msg", text);

            // set data to fragment
            instance.setArguments(bundle);

            return instance;
        } else {

            return instance;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
//        helpscreen_oneimageview.setVisibility(View.VISIBLE);
//        textView.setVisibility(View.VISIBLE);
//        textView2.setVisibility(View.VISIBLE);
//        textView3.setVisibility(View.VISIBLE);
//        textView4.setVisibility(View.VISIBLE);
    }
}
