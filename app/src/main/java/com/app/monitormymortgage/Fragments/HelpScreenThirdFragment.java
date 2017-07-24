package com.app.monitormymortgage.Fragments;

import android.graphics.Typeface;
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
public class HelpScreenThirdFragment extends BaseFragment {
    TextView screenThreeTitleTextView,attachDocumentTextview;
    ImageView helpscreen_threeimageview,imageViewCheck,imageViewAttachment;


    /**
     * fields
     */
    private static HelpScreenThirdFragment instance = null;



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
        View v = inflater.inflate(R.layout.third_frag, container, false);
        screenThreeTitleTextView=(TextView)v.findViewById(R.id.screenThreeTitleTextView);
        attachDocumentTextview=(TextView)v.findViewById(R.id.attachDocumentTextview);
        attachDocumentTextview.setTypeface(attachDocumentTextview.getTypeface(), Typeface.ITALIC);
        helpscreen_threeimageview=(ImageView)v.findViewById(R.id.helpscreen_threeimageview);
        imageViewCheck=(ImageView)v.findViewById(R.id.imageViewCheck);
        imageViewAttachment=(ImageView)v.findViewById(R.id.imageViewAttachment);
        helpscreen_threeimageview.postDelayed(new Runnable() {
            @Override
            public void run() {
                helpscreen_threeimageview.setVisibility(View.VISIBLE);
            }
        },2000);
        screenThreeTitleTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                screenThreeTitleTextView.setVisibility(View.VISIBLE);
            }
        },4000);
        attachDocumentTextview.postDelayed(new Runnable() {
            @Override
            public void run() {
                attachDocumentTextview.setVisibility(View.VISIBLE);
                imageViewCheck.setVisibility(View.VISIBLE);
                imageViewAttachment.setVisibility(View.VISIBLE);
            }
        },6000);

        return v;
    }

    /**
     * Returns new instance.
     *
     * @param text
     * @return
     */
    public static HelpScreenThirdFragment newInstance(String text){

        if(instance == null){
            // new instance
            instance = new HelpScreenThirdFragment();

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
        helpscreen_threeimageview.postDelayed(new Runnable() {
            @Override
            public void run() {
                helpscreen_threeimageview.setVisibility(View.VISIBLE);
            }
        },2000);
        screenThreeTitleTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                screenThreeTitleTextView.setVisibility(View.VISIBLE);
            }
        },4000);
        attachDocumentTextview.postDelayed(new Runnable() {
            @Override
            public void run() {
                attachDocumentTextview.setVisibility(View.VISIBLE);
            }
        }, 6000);
    }
}
