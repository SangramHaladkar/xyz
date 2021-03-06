package com.app.monitormymortgage.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.R;

public class FourthFragmentAfterLogin extends BaseFragment {
    TextView screenFourTitleTextView;
    ImageView helpscreen_fourimageview;
    Button closeHelpButton;

    /**
     * fields
     */
    private static FourthFragmentAfterLogin instance = null;

    public FourthFragmentAfterLogin() {
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
        View v = inflater.inflate(R.layout.fragment_fourth_after_login, container, false);
        screenFourTitleTextView=(TextView)v.findViewById(R.id.screenFourTitleTextView);
        helpscreen_fourimageview=(ImageView)v.findViewById(R.id.helpscreen_fourimageview);
        closeHelpButton=(Button)v.findViewById(R.id.closeHelpButton);
        closeHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
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
    public static FourthFragmentAfterLogin newInstance(String text){

        if(instance == null){
            // new instance
            instance = new FourthFragmentAfterLogin();

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
