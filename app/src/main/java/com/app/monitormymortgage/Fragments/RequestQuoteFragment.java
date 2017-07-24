package com.app.monitormymortgage.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.R;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;


public class RequestQuoteFragment extends BaseFragment {
    private TextView titleTextView, requirementdateTextView, paymentFrequencyTextView, mortgageTypeTextView, purchasePriceTextview, downpaymentamtTextView, MLSTextview, termTextview, postalCodeTextview;
    private LinearLayout termTypeLinearLayout, postalCodeLinearLayout;
    String requestId;
    String requestQuoteTypeCode;
    DecimalFormat df;
    NumberFormat nf;
    ToastMessage toastMessage;


    public RequestQuoteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDialog.showProgress(R.string.help_screen_one, RequestQuoteFragment.this.getActivity(), getResources().getString(R.string.please_wait));
        Bundle bundle = RequestQuoteFragment.this.getArguments();
        if (getArguments() != null) {
            requestId = bundle.getString("RquestId");
            requestQuoteTypeCode = bundle.getString("RequestQuoteType");
        }

        if (requestQuoteTypeCode != null && requestQuoteTypeCode.equalsIgnoreCase("1")) {
            (getActivity()).setTitle(getResources().getString(R.string.pre_approval_request));
        } else if (requestQuoteTypeCode != null && requestQuoteTypeCode.equalsIgnoreCase("2")) {
            (getActivity()).setTitle(getResources().getString(R.string.new_mortgage_quote));
        }
        toastMessage = new ToastMessage(RequestQuoteFragment.this.getContext());

        nf = NumberFormat.getInstance();
        df = (DecimalFormat) nf;
        df.applyPattern("#,###.00");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_request_quote, container, false);
        initializeUIComponent(rootview);
        getDetails(requestId);
        onDisconnect();
        return rootview;
    }

    public void onDisconnect() {
        ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
            @Override
            public void onConnect(boolean value) {
                getDetails(requestId);
            }

            @Override
            public void onDisconnect() {
                if (ViewDialog.kProgressHUD.isShowing()) {
                    ViewDialog.hideProgress();
                }
                if (isAdded()) {
                    if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(RequestQuoteFragment.this.getActivity())) {
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    }
                }
            }

            @Override
            public void onException(Exception e) {

            }

            @Override
            public void onInternetStatusChanged(boolean status) {

            }
        });
    }

    public void initializeUIComponent(View view) {
        titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        requirementdateTextView = (TextView) view.findViewById(R.id.requirementdateTextView);
        paymentFrequencyTextView = (TextView) view.findViewById(R.id.paymentFrequencyTextView);
        mortgageTypeTextView = (TextView) view.findViewById(R.id.mortgageTypeTextView);
        purchasePriceTextview = (TextView) view.findViewById(R.id.purchasePriceTextview);
        downpaymentamtTextView = (TextView) view.findViewById(R.id.downpaymentamtTextView);
        MLSTextview = (TextView) view.findViewById(R.id.MLSTextview);
        termTextview = (TextView) view.findViewById(R.id.termTextview);
        postalCodeTextview = (TextView) view.findViewById(R.id.postalCodeTextview);
        termTypeLinearLayout = (LinearLayout) view.findViewById(R.id.termTypeLinearLayout);
        postalCodeLinearLayout = (LinearLayout) view.findViewById(R.id.postalCodeLinearLayout);
    }

    public void getDetails(final String requestId) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("request_id", requestId);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getRequestQuoteDetails", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v("TAG", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                    }
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");

                    if (jsonObjectResult.has("quote_type_code"))
                        if (jsonObjectResult.getString("quote_type_code").equalsIgnoreCase("2")) {
                            postalCodeLinearLayout.setVisibility(View.VISIBLE);
                            termTypeLinearLayout.setVisibility(View.VISIBLE);
                        } else if (jsonObjectResult.getString("quote_type_code").equalsIgnoreCase("1")) {
                            postalCodeLinearLayout.setVisibility(View.INVISIBLE);
                            termTypeLinearLayout.setVisibility(View.INVISIBLE);
                        }

                    if (jsonObjectResult.has("title"))
                        titleTextView.setText(jsonObjectResult.getString("title"));

                    if (jsonObjectResult.has("start_date_utc"))
                        requirementdateTextView.setText(GlobalMethods.convertMilliSecondsToDate(jsonObjectResult.getString("start_date_utc")));


                    if (jsonObjectResult.has("current_payment_frequency"))
                        paymentFrequencyTextView.setText(jsonObjectResult.getString("current_payment_frequency"));

                    if (jsonObjectResult.has("type") && jsonObjectResult.has("type2"))
                        mortgageTypeTextView.setText(jsonObjectResult.getString("type") + " and " + jsonObjectResult.getString("type2"));

                    if (jsonObjectResult.has("current_term_rate_type") && jsonObjectResult.has("current_term_in_months"))
                        if (jsonObjectResult.getString("current_term_in_months").equalsIgnoreCase("6")) {
                            termTextview.setText(jsonObjectResult.getString("current_term_in_months") + " Months   " + jsonObjectResult.getString("current_term_rate_type"));
                        } else {
                            int years = Integer.parseInt(jsonObjectResult.getString("current_term_in_months"));
                            years = years / 12;
                            if (years == 1) {
                                termTextview.setText(years + " Year " + jsonObjectResult.getString("current_term_rate_type"));
                            } else {
                                termTextview.setText(years + " Years " + jsonObjectResult.getString("current_term_rate_type"));
                            }
                        }


                    if (jsonObjectResult.has("original_amount"))
                        purchasePriceTextview.setText("$" + df.format(Double.valueOf(jsonObjectResult.getString("original_amount")).doubleValue()));

                    if (jsonObjectResult.has("down_payment_amount"))
                        downpaymentamtTextView.setText("$" + df.format(Double.valueOf(jsonObjectResult.getString("down_payment_amount")).doubleValue()));

                    if (jsonObjectResult.has("mls_number"))
                        MLSTextview.setText(jsonObjectResult.getString("mls_number"));

                    if (jsonObjectResult.has("property_postal_code"))
                        postalCodeTextview.setText(jsonObjectResult.getString("property_postal_code"));


                    if (ViewDialog.kProgressHUD.isShowing()) {
                        ViewDialog.hideProgress();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (ViewDialog.kProgressHUD.isShowing()) {
                        ViewDialog.hideProgress();
                    }
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.v("TAG", reason);
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        getDetails(requestId);
                    }

                    @Override
                    public void onDisconnect() {

                        if (ViewDialog.kProgressHUD.isShowing()) {
                            ViewDialog.hideProgress();
                        }

                        if (isAdded()) {
                            if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(RequestQuoteFragment.this.getActivity())) {
                                toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                            }
                        }
                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {

                    }
                });
            }
        });


    }

}
