package com.app.monitormymortgage.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.monitormymortgage.Common.CommonConstants;
import com.facebook.Profile;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.app.monitormymortgage.Activity.CreateNewAccountActivity;
import com.app.monitormymortgage.Activity.LoginActivity;
import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.R;


public class HelpScreenFifthFragment extends BaseFragment {
    private static final String TAG = HelpScreenFifthFragment.class.getSimpleName();
    TextView urlTextView;
    TextView myMortgageMonitorTextView;
    TextView sloganTextView;
    TextView continueWithTextview;
    ImageView logoImageView;
    Button FBbutton, EmailButton;
    SignInButton googleButton;
    LoginButton login_button;
    Profile profile;
    String statusCode;
    private static HelpScreenFifthFragment instance = null;
    public static boolean googleSilentLoginFlag = false;
    ToastMessage toastMessage;


    public HelpScreenFifthFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fifth, container, false);

        urlTextView = (TextView) v.findViewById(R.id.urlTextView);
        String htmlString = "<font color='green'><u>" + getResources().getString(R.string.monitor_my_mortgage_link) + "</u></font>";
        urlTextView.setText(Html.fromHtml(htmlString));
        myMortgageMonitorTextView = (TextView) v.findViewById(R.id.myMortgageMonitorTextView);
        sloganTextView = (TextView) v.findViewById(R.id.sloganTextView);
        continueWithTextview = (TextView) v.findViewById(R.id.continueWithTextview);
        logoImageView = (ImageView) v.findViewById(R.id.logoImageView);
        FBbutton = (Button) v.findViewById(R.id.FBbutton);
        googleButton = (SignInButton) v.findViewById(R.id.googleButton);
        EmailButton = (Button) v.findViewById(R.id.EmailButton);
        login_button = (LoginButton) v.findViewById(R.id.login_button);
        setGooglePlusButtonText(googleButton, R.string.google);
        toastMessage = new ToastMessage(this.getContext());

        urlTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("TAG", "On URL click");
                Uri uri = Uri.parse("http://www.MonitorMyMortgage.com");
                Intent intent;
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        logoImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoImageView.setVisibility(View.VISIBLE);
            }
        }, 2000);
        myMortgageMonitorTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                myMortgageMonitorTextView.setVisibility(View.VISIBLE);
            }
        }, 4000);
        sloganTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                sloganTextView.setVisibility(View.VISIBLE);
            }
        }, 5000);
        urlTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                urlTextView.setVisibility(View.VISIBLE);
            }
        }, 6000);
        continueWithTextview.postDelayed(new Runnable() {
            @Override
            public void run() {
                continueWithTextview.setVisibility(View.VISIBLE);
            }
        }, 8000);
        FBbutton.postDelayed(new Runnable() {
            @Override
            public void run() {
                FBbutton.setVisibility(View.VISIBLE);
            }
        }, 8000);
        googleButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                googleButton.setVisibility(View.VISIBLE);
            }
        }, 8000);
        EmailButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                EmailButton.setVisibility(View.VISIBLE);
            }
        }, 8000);
        EmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalMethods.haveNetworkConnection(HelpScreenFifthFragment.this.getActivity())) {
                    Intent intent = new Intent(getActivity(), CreateNewAccountActivity.class);
                    startActivity(intent);
                    CommonConstants.flag = true;
                    getActivity().finish();
                } else {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                }
            }
        });
        FBbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalMethods.haveNetworkConnection(HelpScreenFifthFragment.this.getActivity())) {
                    ViewDialog.showProgress(R.string.help_screen_one, HelpScreenFifthFragment.this.getActivity(), getResources().getString(R.string.please_wait));
                    login_button.performClick();
                } else {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                    ViewDialog.showAlertPopUp(HelpScreenFifthFragment.this.getActivity(), getResources().getString(R.string.no_network_found), getResources().getString(R.string.error));
                }
            }
        });
        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalMethods.haveNetworkConnection(HelpScreenFifthFragment.this.getActivity())) {
                    Intent intent = new Intent(HelpScreenFifthFragment.this.getActivity(), LoginActivity.class);
                    googleSilentLoginFlag = true;
                    getActivity().finish();
                    startActivity(intent);
                } else {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                }
            }
        });
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
//        profile = Profile.getCurrentProfile();
//        if (profile != null) {
//            LoginManager.getInstance().logOut();
//        }
        logoImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoImageView.setVisibility(View.VISIBLE);
            }
        }, 2000);
        myMortgageMonitorTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                myMortgageMonitorTextView.setVisibility(View.VISIBLE);
            }
        }, 4000);
        sloganTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                sloganTextView.setVisibility(View.VISIBLE);
            }
        }, 5000);
        urlTextView.postDelayed(new Runnable() {
            @Override
            public void run() {
                urlTextView.setVisibility(View.VISIBLE);
            }
        }, 6000);
        continueWithTextview.postDelayed(new Runnable() {
            @Override
            public void run() {
                continueWithTextview.setVisibility(View.VISIBLE);
            }
        }, 8000);
        FBbutton.postDelayed(new Runnable() {
            @Override
            public void run() {
                FBbutton.setVisibility(View.VISIBLE);
            }
        }, 8000);
        googleButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                googleButton.setVisibility(View.VISIBLE);
            }
        }, 8000);
        EmailButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                EmailButton.setVisibility(View.VISIBLE);
            }
        }, 8000);
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, int buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.setTextSize(17);
                return;
            }
        }
    }

    public static HelpScreenFifthFragment newInstance(String text) {

        if (instance == null) {
            // new instance
            instance = new HelpScreenFifthFragment();

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
