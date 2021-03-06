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
public class HelpScreenSecondFragment extends BaseFragment {
    TextView secureTextView,mobileTextView,eastToUseTextView;
    ImageView helpscreen_twoimageview;

    /**
     * fields
     */
    private static HelpScreenSecondFragment instance = null;

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
        View v = inflater.inflate(R.layout.second_frag, container, false);
        secureTextView=(TextView)v.findViewById(R.id.secureTextView);
        mobileTextView=(TextView)v.findViewById(R.id.mobileTextView);
        eastToUseTextView=(TextView)v.findViewById(R.id.eastToUseTextView);
        helpscreen_twoimageview=(ImageView)v.findViewById(R.id.helpscreen_twoimageview);
        helpscreen_twoimageview.postDelayed(new Runnable() {
            @Override
            public void run() {
                helpscreen_twoimageview.setVisibility(View.VISIBLE);
            }
        },2000);
        secureTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                secureTextView.setVisibility(View.VISIBLE);
            }
        },4000);
        mobileTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mobileTextView.setVisibility(View.VISIBLE);
            }
        },6000);
        eastToUseTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                eastToUseTextView.setVisibility(View.VISIBLE);
            }
        }, 8000);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        helpscreen_twoimageview.postDelayed(new Runnable() {
            @Override
            public void run() {
                helpscreen_twoimageview.setVisibility(View.VISIBLE);
            }
        },2000);
        secureTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                secureTextView.setVisibility(View.VISIBLE);
            }
        },4000);
        mobileTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mobileTextView.setVisibility(View.VISIBLE);
            }
        },6000);
        eastToUseTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                eastToUseTextView.setVisibility(View.VISIBLE);
            }
        }, 8000);
    }

    /**
     * Returns new instance.
     *
     * @param text
     * @return
     */
    public static HelpScreenSecondFragment newInstance(String text){

        if(instance == null){
            // new instance
            instance = new HelpScreenSecondFragment();

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
}
