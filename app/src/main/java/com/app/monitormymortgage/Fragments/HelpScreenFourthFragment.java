package com.app.monitormymortgage.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.R;

public class HelpScreenFourthFragment extends BaseFragment {
    TextView screenFourTitleTextView;
    ImageView helpscreen_fourimageview;

    /**
     * fields
     */
    private static HelpScreenFourthFragment instance = null;

    public HelpScreenFourthFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fourth, container, false);
        screenFourTitleTextView=(TextView)v.findViewById(R.id.screenFourTitleTextView);
        helpscreen_fourimageview=(ImageView)v.findViewById(R.id.helpscreen_fourimageview);
        helpscreen_fourimageview.postDelayed(new Runnable() {
            @Override
            public void run() {
                helpscreen_fourimageview.setVisibility(View.VISIBLE);
            }
        },2000);
        screenFourTitleTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                screenFourTitleTextView.setVisibility(View.VISIBLE);
            }
        },4000);

        return v;
    }
    /**
     * Returns new instance.
     *
     * @param text
     * @return
     */
    public static HelpScreenFourthFragment newInstance(String text){

        if(instance == null){
            // new instance
            instance = new HelpScreenFourthFragment();

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
        helpscreen_fourimageview.postDelayed(new Runnable() {
            @Override
            public void run() {
                helpscreen_fourimageview.setVisibility(View.VISIBLE);
            }
        },2000);
        screenFourTitleTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                screenFourTitleTextView.setVisibility(View.VISIBLE);
            }
        }, 4000);
    }
}
