package com.app.monitormymortgage.TabFragments;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;



public class TabFragmentTwo extends BaseFragment {
    TextView disclaimerTextView;
    public static Button connectToBrokerButton;
    public static TextView byFrequencyAmtTextView;
    public static TextView frequencyTextView;
    public static TextView byAnnualyTextView;
    public static TextView overRemainingTermAmtTextView;
    public static TextView amortizesBalanceTextView;
    public static TextView paymentAmtTextView;
    public static TextView paymentFrequencyTextView;
    public static TextView termTextView;
    public static TextView interestRateTextView;
    public static TextView rateTypeTextView;
    public static TextView amortizationTermTextView;
    public static String opportunityIdSavings;
    public static RadioGroup ContactPreferncesRadioGroup;
    public static AlertDialog alertDialog;
    public static String communication_pref;
    public static String status_code;
    String title;
    public static NumberFormat nf;
    public static DecimalFormat df;
    public static String username;
    public static String cntPrefernce;
    public static String facebookUserLoginId;
    public static boolean facebookUserLoginFlag;
    public static String googleUserLoginId;
    public static boolean googleUserLoginFlag;
    public static ToastMessage toastMessage;

    public TabFragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        opportunityIdSavings = getActivity().getIntent().getExtras().getString("opportunity_id");
        title = getActivity().getIntent().getExtras().getString("activity_title");
        toastMessage = new ToastMessage(this.getContext());
        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(TabFragmentTwo.this.getActivity()).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(TabFragmentTwo.this.getActivity()).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(TabFragmentTwo.this.getActivity()).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(TabFragmentTwo.this.getActivity()).getBoolean("GoogleUserLoginFlag", false);
        username = PreferenceManager.getDefaultSharedPreferences(TabFragmentTwo.this.getActivity()).getString("username", "");
        Log.v("TAG two", opportunityIdSavings);
        nf = NumberFormat.getInstance();
        df = (DecimalFormat) nf;
        df.applyPattern("#,###.00");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_fragment_two, container, false);
        initializeUIComponent(v);
        getUser(username);
        setOpportunityDetails(opportunityIdSavings);
        connectToBroker();
        disclaimerTextViewClick();
        return v;
    }


    public void initializeUIComponent(View view) {
        disclaimerTextView = (TextView) view.findViewById(R.id.disclaimerTextView);
        connectToBrokerButton = (Button) view.findViewById(R.id.connectToBrokerButton);
        String htmlString = "<font color='#3F51B5'><u>Disclaimer</u></font>";
        disclaimerTextView.setText(Html.fromHtml(htmlString));
        byFrequencyAmtTextView = (TextView) view.findViewById(R.id.byFrequencyAmtTextView);
        frequencyTextView = (TextView) view.findViewById(R.id.frequencyTextView);
        byAnnualyTextView = (TextView) view.findViewById(R.id.byAnnualyTextView);
        overRemainingTermAmtTextView = (TextView) view.findViewById(R.id.overRemainingTermAmtTextView);
        amortizesBalanceTextView = (TextView) view.findViewById(R.id.amortizesBalanceTextView);
        paymentAmtTextView = (TextView) view.findViewById(R.id.paymentAmtTextView);
        paymentFrequencyTextView = (TextView) view.findViewById(R.id.paymentFrequencyTextView);
        termTextView = (TextView) view.findViewById(R.id.termTextView);
        interestRateTextView = (TextView) view.findViewById(R.id.interestRateTextView);
        rateTypeTextView = (TextView) view.findViewById(R.id.rateTypeTextView);
        amortizationTermTextView = (TextView) view.findViewById(R.id.amortizationTermTextView);
    }

    public static  void getUser(String userId) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        if (facebookUserLoginFlag) {
            stringObjectHashMap.put("userId", facebookUserLoginId);
        } else if (googleUserLoginFlag) {
            stringObjectHashMap.put("userId", googleUserLoginId);
        } else {
            stringObjectHashMap.put("userId", userId);
        }
        ErisCollectionManager.getInstance().callMethod("getUser", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v("TAG", result);
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    cntPrefernce = jsonObjectResult.getString("contact_pref");
                    Log.v("TAG", cntPrefernce);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {

            }
        });
    }

    public void disclaimerTextViewClick() {
        disclaimerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog.showAlertPopUp(TabFragmentTwo.this.getActivity(), "Penalty Calculation Disclaimer\n\nPlease note the calculated penalty shown is an estimate based on the most recent information shared by the lenders’ publicly stated calculation methodology. The results do not include any discharge fees, registration or transfer fees. This information is subject to change. Exact penalty calculations will be determined over the mortgage renewal process as provided by your lender directly.", getResources().getString(R.string.disclaimer));
            }
        });
    }

    public static void setOpportunityDetails(final String opportunityId) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("opportunity_id", opportunityId);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getOpportunityDetails", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v("Fragment two", result);
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    JSONObject jsonObjectOpportunity = jsonObjectResult.getJSONObject("opportunity");
                    status_code = jsonObjectOpportunity.getString("status_code");
                    int statusCode= Integer.parseInt(status_code);
                    if (statusCode >= 50000) {
                        connectToBrokerButton.setVisibility(View.INVISIBLE);
                    }
                    Log.v("TAG",status_code);
                    String str = jsonObjectOpportunity.getString("total_saving_over_frequency");
                    int index = str.indexOf(".");
                    byFrequencyAmtTextView.setText("$" + df.format(Double.valueOf(str).doubleValue()));
                    Log.v("TAG", jsonObjectResult.getString("current_payment_frequency"));
                    if (jsonObjectResult.getString("current_payment_frequency").equalsIgnoreCase("Accelerated Bi-Weekly")) {
                        frequencyTextView.setText("Acc Bi-Weekly");
                    } else {
                        frequencyTextView.setText(jsonObjectResult.getString("current_payment_frequency"));
                    }
                    String str2 = jsonObjectOpportunity.getString("total_saving_annually");
                    int index2 = str2.indexOf(".");
                    byAnnualyTextView.setText("$" + df.format(Double.valueOf(str2).doubleValue()));
                    String strOverRemainingTerm = jsonObjectOpportunity.getString("total_saving_over_term");
                    int indexOverRemainingTerm = strOverRemainingTerm.indexOf(".");
                    overRemainingTermAmtTextView.setText("$" + df.format(Double.valueOf(strOverRemainingTerm).doubleValue()));
                    String stringAmtBal = new BigDecimal(jsonObjectOpportunity.getString("proposed_balance_amount")).toPlainString();
                    int indexAmtBal = stringAmtBal.indexOf(".");
                    amortizesBalanceTextView.setText(df.format(Double.valueOf(stringAmtBal).doubleValue()));
                    paymentAmtTextView.setText(df.format(Double.valueOf(jsonObjectOpportunity.getString("proposed_periodic_payment")).doubleValue()));
                    paymentFrequencyTextView.setText(jsonObjectOpportunity.getString("proposed_payment_frequency"));
                    /*Check for term in months in months or years.*/
                    if (jsonObjectOpportunity.getString("proposed_term_in_months").equalsIgnoreCase("6")) {
                        termTextView.setText(6 + " Months");
                    } else {
                        int years = Integer.parseInt(jsonObjectOpportunity.getString("proposed_term_in_months")) / 12;
                        if (years <= 1) {
                            termTextView.setText(years + " Year");
                        } else {
                            termTextView.setText(years + " Years");
                        }
                    }

                    /*Check for interest rate.*/
                    if (jsonObjectOpportunity.getString("proposed_interest_rate_percentage").length() <= 0) {
                        interestRateTextView.setText("0.00" + "%");
                    } else if (!jsonObjectOpportunity.getString("proposed_interest_rate_percentage").contains(".")) {
                        interestRateTextView.setText(jsonObjectOpportunity.getString("proposed_interest_rate_percentage") + ".00" + "%");
                    } else {
                        interestRateTextView.setText(jsonObjectOpportunity.getString("proposed_interest_rate_percentage") + "%");
                    }

                    rateTypeTextView.setText(jsonObjectOpportunity.getString("proposed_term_rate_type"));
                    amortizationTermTextView.setText(jsonObjectOpportunity.getString("proposed_amortization_term_in_months") + " Months");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String error, String reason, String details) {
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        setOpportunityDetails(opportunityIdSavings);
                        if (facebookUserLoginFlag) {
                            getUser(facebookUserLoginId);
                        } else if (googleUserLoginFlag) {
                            getUser(googleUserLoginId);
                        } else {
                            getUser(username);
                        }
                    }

                    @Override
                    public void onDisconnect() {
                        ViewDialog.hideProgress();
                        Log.v("TAG", "onDisconnect");

                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        setOpportunityDetails(opportunityIdSavings);
                        if (facebookUserLoginFlag) {
                            getUser(facebookUserLoginId);
                        } else if (googleUserLoginFlag) {
                            getUser(googleUserLoginId);
                        } else {
                            getUser(username);
                        }
                    }
                });

            }
        });
    }

    public void connectToBroker() {
        connectToBrokerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalMethods.haveNetworkConnection(TabFragmentTwo.this.getActivity()) && !MeteorSingleton.getInstance().isConnected()) {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                } else {
                    if (status_code != null && !status_code.equals("10000")) {
                        showAlertPopUp(TabFragmentTwo.this.getActivity(), "The Broker has viewed your opportunity details and will contact you shortly.", "Broker Already Contacted");
                    } else {
                        showDialog();
                    }
                }
            }
        });
    }

    public void showAlertPopUp(Activity activity, String msg, String title) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.custom_dailog, null);
            alertDialogBuilder.setView(myDialog);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
            TextView text = (TextView) myDialog.findViewById(R.id.text_dialog);
            TextView close = (TextView) myDialog.findViewById(R.id.closeButton);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            text.setText(msg);
            titleTextView.setText(title);
            Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                    CommonConstants.dashboardReplaceFlag = true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabFragmentTwo.this.getActivity());
        LayoutInflater inflater = ((Activity) TabFragmentTwo.this.getActivity()).getLayoutInflater();
        final View myDialog = inflater.inflate(R.layout.custom_dialog_broker_cnt, null);
        alertDialogBuilder.setView(myDialog);
        alertDialogBuilder.setCancelable(false);
        TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
        TextView close = (TextView) myDialog.findViewById(R.id.closeButton);
        final CheckBox phnCheckBox = (CheckBox) myDialog.findViewById(R.id.radioButtonPhn);
        final CheckBox emailCheckBox = (CheckBox) myDialog.findViewById(R.id.radioButtonEmail);
        final TextView errorTextView = (TextView) myDialog.findViewById(R.id.errorTextView);

        titleTextView.setText(R.string.how_broker_connect);
        ContactPreferncesRadioGroup = (RadioGroup) myDialog.findViewById(R.id.ContactPreferncesRadioGroup);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //   communication_pref = "Phone";

        Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);

        if (cntPrefernce != null && cntPrefernce.equalsIgnoreCase("Email")) {
            emailCheckBox.setChecked(true);
        } else if (cntPrefernce != null && cntPrefernce.equalsIgnoreCase("Phone")) {
            phnCheckBox.setChecked(true);
        } else if (cntPrefernce != null && cntPrefernce.equalsIgnoreCase("Both")) {
            emailCheckBox.setChecked(true);
            phnCheckBox.setChecked(true);
        }


        phnCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communication_pref = "Phone";
                errorTextView.setVisibility(View.INVISIBLE);
            }
        });
        emailCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communication_pref = "Email";
                errorTextView.setVisibility(View.INVISIBLE);
            }
        });


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phnCheckBox.isChecked() && !emailCheckBox.isChecked()) {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText("Please select Contact Preference.");
                } else {
                    if (phnCheckBox.isChecked() && emailCheckBox.isChecked()) {
                        communication_pref = "Phone,Email";
                    } else if (phnCheckBox.isChecked()) {
                        communication_pref = "Phone";
                    } else if (emailCheckBox.isChecked()) {
                        communication_pref = "Email";
                    }

                    HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
                    stringObjectHashMap.put("opportunity_id", opportunityIdSavings);
                    stringObjectHashMap.put("communication_pref", communication_pref);
                    stringObjectHashMap.put("lang", "en");
                    stringObjectHashMap.put("req_from", "android");
                    ErisCollectionManager.getInstance().callMethod("connectToBroker", new Object[]{stringObjectHashMap}, new ResultListener() {
                        @Override
                        public void onSuccess(String result) {
                            Log.v("TAG", result);
                            alertDialog.dismiss();
                            showAlertPopUp(TabFragmentTwo.this.getActivity(), "Our Mortgage Broker will" + "\n" + "contact you shortly.", "Request Sent");
                        }

                        @Override
                        public void onError(String error, String reason, String details) {
                            Log.v("TAG", reason);
                        }
                    });
                }
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
