package com.app.monitormymortgage.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.R;

public class FAQFragment extends BaseFragment {
    WebView webView;
    ToastMessage toastMessage;
    public FAQFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        (getActivity()).setTitle(R.string.faq_fragment);
     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=inflater.inflate(R.layout.fragment_faq, container, false);
        webView = (WebView)rootview.findViewById(R.id.help_webview);
        toastMessage=new ToastMessage(this.getContext());
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        final AlertDialog alertDialog = new AlertDialog.Builder(FAQFragment.this.getActivity()).create();
        ViewDialog.showProgress(R.string.help_screen_one,FAQFragment.this.getActivity(),getResources().getString(R.string.please_wait));
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(CommonConstants.mDebug) Log.i("TAG", "Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if(CommonConstants.mDebug) Log.i("TAG", "Finished loading URL: " + url);
                ViewDialog.hideProgress();
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                if(CommonConstants.mDebug) Log.e("TAG", "Error: " + description);
                toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                Toast.makeText(FAQFragment.this.getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
//                alertDialog.setTitle(R.string.error);
//                alertDialog.setMessage(description);
//                alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        return;
//                    }
//                });
//                alertDialog.show();
            }

        });
        webView.loadUrl(CommonConstants.faqURL);
        return rootview;
    }

}
