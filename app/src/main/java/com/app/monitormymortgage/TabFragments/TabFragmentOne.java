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



public class TabFragmentOne extends BaseFragment {
    TextView disclaimerTextView;
    Button connectToBrokerButtonTabOne;
    TextView savedOverTermTextView;
    TextView currentNewPaymentAmtTextview;
    TextView currentNewPaymentFrequencyTextview;
    TextView currentTermTextView;
    TextView currentStatusTextView;
    TextView currentTypeTextView;
    TextView newTermTextView;
    TextView newStatusTextView;
    TextView newTypeTextView;
    TextView currentRateTextView;
    TextView newRateTextView;
    TextView terminationCostTextView;
    String opportunityIdSavings;
    RadioGroup ContactPreferncesRadioGroup;
    AlertDialog alertDialog;
    String communication_pref;
    String status_code;
    String title;
    NumberFormat nf;
    DecimalFormat df;
    String username;
    String cntPrefernce;
    String facebookUserLoginId;
    boolean facebookUserLoginFlag;
    String googleUserLoginId;
    boolean googleUserLoginFlag;
    private ToastMessage toastMessage;

    public TabFragmentOne() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewDialog.showProgress(R.string.help_screen_one, TabFragmentOne.this.getActivity(), getResources().getString(R.string.please_wait));
        title = getActivity().getIntent().getExtras().getString("activity_title");
        opportunityIdSavings = getActivity().getIntent().getExtras().getString("opportunity_id");
        toastMessage = new ToastMessage(this.getContext());
        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(TabFragmentOne.this.getActivity()).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(TabFragmentOne.this.getActivity()).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(TabFragmentOne.this.getActivity()).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(TabFragmentOne.this.getActivity()).getBoolean("GoogleUserLoginFlag", false);
        username = PreferenceManager.getDefaultSharedPreferences(TabFragmentOne.this.getActivity()).getString("username", "");
        Log.v("TAG", opportunityIdSavings);
        nf = NumberFormat.getInstance();
        df = (DecimalFormat) nf;
        df.applyPattern("#,###.00");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tab_fragment_one, container, false);
        initializeUIComponent(v);
        getUser(username);
        setOpportunityDetails(opportunityIdSavings);
        connectToBroker();
        disclaimerTextViewClick();
        return v;
    }

    public void getUser(String userId) {
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


    public void initializeUIComponent(View view) {
        disclaimerTextView = (TextView) view.findViewById(R.id.disclaimerTextView);
        connectToBrokerButtonTabOne = (Button) view.findViewById(R.id.connectToBrokerButtonTabOne);
        String htmlString = "<font color='#3F51B5'><u>Disclaimer</u></font>";
        disclaimerTextView.setText(Html.fromHtml(htmlString));
        savedOverTermTextView = (TextView) view.findViewById(R.id.savedOverTermTextView);
        currentNewPaymentAmtTextview = (TextView) view.findViewById(R.id.currentNewPaymentAmtTextview);
        currentNewPaymentFrequencyTextview = (TextView) view.findViewById(R.id.currentNewPaymentFrequencyTextview);
        currentTermTextView = (TextView) view.findViewById(R.id.currentTermTextView);
        currentStatusTextView = (TextView) view.findViewById(R.id.currentStatusTextView);
        currentTypeTextView = (TextView) view.findViewById(R.id.currentTypeTextView);
        newTermTextView = (TextView) view.findViewById(R.id.newTermTextView);
        newStatusTextView = (TextView) view.findViewById(R.id.newStatusTextView);
        newTypeTextView = (TextView) view.findViewById(R.id.newTypeTextView);
        currentRateTextView = (TextView) view.findViewById(R.id.currentRateTextView);
        newRateTextView = (TextView) view.findViewById(R.id.newRateTextView);
        terminationCostTextView = (TextView) view.findViewById(R.id.terminationCostTextView);
    }

    public void disclaimerTextViewClick() {
        disclaimerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog.showAlertPopUp(TabFragmentOne.this.getActivity(), "Penalty Calculation Disclaimer\n\nPlease note the calculated penalty shown is an estimate based on the most recent information shared by the lenders’ publicly stated calculation methodology. The results do not include any discharge fees, registration or transfer fees. This information is subject to change. Exact penalty calculations will be determined over the mortgage renewal process as provided by your lender directly.", getResources().getString(R.string.disclaimer));
            }
        });
    }

    public void setOpportunityDetails(String opportunityId) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("opportunity_id", opportunityId);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getOpportunityDetails", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v("TAG Saving", result);

                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    JSONObject jsonObjectOpportunity = jsonObjectResult.getJSONObject("opportunity");
                    status_code = jsonObjectOpportunity.getString("status_code");
                    int statusCode= Integer.parseInt(status_code);
                    if (statusCode >= 50000) {
                        connectToBrokerButtonTabOne.setVisibility(View.INVISIBLE);
                    }
                    currentNewPaymentAmtTextview.setText(df.format(Double.valueOf(jsonObjectResult.getString("current_payment_amount")).doubleValue()));
                    currentNewPaymentFrequencyTextview.setText(jsonObjectResult.getString("current_payment_frequency"));

                    /*Check for 6 month term and year*/
                    if (jsonObjectResult.getString("current_term_in_months").equalsIgnoreCase("6")) {
                        currentTermTextView.setText(6 + " Mth");
                    } else {
                        int years = Integer.parseInt(jsonObjectResult.getString("current_term_in_months")) / 12;
                        if (years <= 1) {
                            currentTermTextView.setText(years + " Yr");
                        } else {
                            currentTermTextView.setText(years + " Yrs");
                        }
                    }
                    currentStatusTextView.setText(jsonObjectResult.getString("current_term_rate_type"));
                    currentTypeTextView.setText(jsonObjectResult.getString("current_term_type"));
                    if (!jsonObjectResult.getString("interest_rate_percentage").contains(".")) {
                        currentRateTextView.setText(jsonObjectResult.getString("interest_rate_percentage") + ".00" + "%");
                    } else {
                        currentRateTextView.setText(jsonObjectResult.getString("interest_rate_percentage") + "%");
                    }
                    String str = jsonObjectOpportunity.getString("total_saving_over_term");
                    int index = str.indexOf(".");
                    savedOverTermTextView.setText(df.format(Double.valueOf(str).doubleValue()));
                    if (jsonObjectOpportunity.getString("penalty").contains(".")) {
                        String penalty = jsonObjectOpportunity.getString("penalty");
                        int penaltyIndex = penalty.indexOf(".");
                        terminationCostTextView.setText("Early termination costs ($" + df.format(Double.valueOf(penalty).doubleValue()) + ") have been calculated within the savings. See disclaimer note below.");
                    } else {
                        terminationCostTextView.setText("Early termination costs ($" + jsonObjectOpportunity.getString("penalty") + ".00" + ") have been calculated within the savings. See disclaimer note below.");
                    }
                    /*Check for 6 month term and year*/
                    if (jsonObjectOpportunity.getString("proposed_term_in_months").equalsIgnoreCase("6")) {
                        newTermTextView.setText(6 + " Mth");
                    } else {
                        int newYrs = Integer.parseInt(jsonObjectOpportunity.getString("proposed_term_in_months")) / 12;
                        if (newYrs <= 1) {
                            newTermTextView.setText(newYrs + " Yr");
                        } else {
                            newTermTextView.setText(newYrs + " Yrs");
                        }
                    }
                    newStatusTextView.setText(jsonObjectOpportunity.getString("proposed_term_rate_type"));
                    newTypeTextView.setText(jsonObjectOpportunity.getString("proposed_term_type"));
                    if (jsonObjectOpportunity.getString("proposed_interest_rate_percentage").length() <= 0) {
                        newRateTextView.setText("0.00" + "%");
                    } else if (!jsonObjectOpportunity.getString("proposed_interest_rate_percentage").contains(".")) {
                        newRateTextView.setText(jsonObjectOpportunity.getString("proposed_interest_rate_percentage") + ".00" + "%");
                    } else {
                        newRateTextView.setText(jsonObjectOpportunity.getString("proposed_interest_rate_percentage") + "%");
                    }

                    status_code = jsonObjectOpportunity.getString("status_code");
                    if (status_code.equalsIgnoreCase("50000") || status_code.equalsIgnoreCase("60000") || status_code.equalsIgnoreCase("70000")) {
                        TabFragmentTwo.connectToBrokerButton.setVisibility(View.INVISIBLE);
                    }
                    Log.v("TAG", status_code);
                    String str1 = jsonObjectOpportunity.getString("total_saving_over_frequency");
                    int index1 = str1.indexOf(".");
                    TabFragmentTwo.byFrequencyAmtTextView.setText("$" + df.format(Double.valueOf(str1).doubleValue()));
                    Log.v("TAG", jsonObjectResult.getString("current_payment_frequency"));
                    if (jsonObjectResult.getString("current_payment_frequency").equalsIgnoreCase("Accelerated Bi-Weekly")) {
                        TabFragmentTwo.frequencyTextView.setText("Acc Bi-Weekly");
                    } else {
                        TabFragmentTwo.frequencyTextView.setText(jsonObjectResult.getString("current_payment_frequency"));
                    }
                    String str2 = jsonObjectOpportunity.getString("total_saving_annually");
                    int index2 = str2.indexOf(".");
                    TabFragmentTwo.byAnnualyTextView.setText("$" + df.format(Double.valueOf(str2).doubleValue()));
                    String strOverRemainingTerm = jsonObjectOpportunity.getString("total_saving_over_term");
                    int indexOverRemainingTerm = strOverRemainingTerm.indexOf(".");
                    TabFragmentTwo.overRemainingTermAmtTextView.setText("$" + df.format(Double.valueOf(strOverRemainingTerm).doubleValue()));
                    String stringAmtBal = new BigDecimal(jsonObjectOpportunity.getString("proposed_balance_amount")).toPlainString();
                    int indexAmtBal = stringAmtBal.indexOf(".");
                    TabFragmentTwo.amortizesBalanceTextView.setText(df.format(Double.valueOf(stringAmtBal).doubleValue()));
                    TabFragmentTwo.paymentAmtTextView.setText(df.format(Double.valueOf(jsonObjectOpportunity.getString("proposed_periodic_payment")).doubleValue()));
                    TabFragmentTwo.paymentFrequencyTextView.setText(jsonObjectOpportunity.getString("proposed_payment_frequency"));
                    /*Check for term in months in months or years.*/
                    if (jsonObjectOpportunity.getString("proposed_term_in_months").equalsIgnoreCase("6")) {
                        TabFragmentTwo.termTextView.setText(6 + " Months");
                    } else {
                        int years = Integer.parseInt(jsonObjectOpportunity.getString("proposed_term_in_months")) / 12;
                        if (years <= 1) {
                            TabFragmentTwo.termTextView.setText(years + " Year");
                        } else {
                            TabFragmentTwo.termTextView.setText(years + " Years");
                        }
                    }

                    /*Check for interest rate.*/
                    if (jsonObjectOpportunity.getString("proposed_interest_rate_percentage").length() <= 0) {
                        TabFragmentTwo.interestRateTextView.setText("0.00" + "%");
                    } else if (!jsonObjectOpportunity.getString("proposed_interest_rate_percentage").contains(".")) {
                        TabFragmentTwo.interestRateTextView.setText(jsonObjectOpportunity.getString("proposed_interest_rate_percentage") + ".00" + "%");
                    } else {
                        TabFragmentTwo.interestRateTextView.setText(jsonObjectOpportunity.getString("proposed_interest_rate_percentage") + "%");
                    }

                    TabFragmentTwo.rateTypeTextView.setText(jsonObjectOpportunity.getString("proposed_term_rate_type"));
                    TabFragmentTwo.amortizationTermTextView.setText(jsonObjectOpportunity.getString("proposed_amortization_term_in_months") + " Months");
                    TabFragmentTwo.getUser(username);
                    connectToBroker();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ViewDialog.hideProgress();
            }

            public void connectToBroker() {
                TabFragmentTwo.connectToBrokerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!GlobalMethods.haveNetworkConnection(TabFragmentOne.this.getActivity()) && !MeteorSingleton.getInstance().isConnected()) {
                            toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                        } else {
                            if (status_code != null && !status_code.equals("10000")) {
                                showAlertPopUp(TabFragmentOne.this.getActivity(), "The Broker has viewed your opportunity details and will contact you shortly.", "Broker Already Contacted");
                            } else {
                                showDialog();
                            }
                        }
                    }
                });
            }

            @Override
            public void onError(String error, String reason, String details) {

                Log.v("TAG Saving", reason);
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
                        if (ViewDialog.kProgressHUD.isShowing())
                            ViewDialog.hideProgress();
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
        connectToBrokerButtonTabOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalMethods.haveNetworkConnection(TabFragmentOne.this.getActivity()) && !MeteorSingleton.getInstance().isConnected()) {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                } else {
                    if (status_code != null && !status_code.equals("10000")) {
                        showAlertPopUp(TabFragmentOne.this.getActivity(), "The Broker has viewed your opportunity details and will contact you shortly.", "Broker Already Contacted");
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
            if (ViewDialog.kProgressHUD.isShowing()) {
                ViewDialog.hideProgress();
            }
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
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TabFragmentOne.this.getActivity());
        LayoutInflater inflater = ((Activity) TabFragmentOne.this.getActivity()).getLayoutInflater();
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
        // communication_pref = "Phone";

        Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);
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

        if (cntPrefernce != null && cntPrefernce.equalsIgnoreCase("Email")) {
            emailCheckBox.setChecked(true);
        } else if (cntPrefernce != null && cntPrefernce.equalsIgnoreCase("Phone")) {
            phnCheckBox.setChecked(true);
        } else if (cntPrefernce != null && cntPrefernce.equalsIgnoreCase("Both")) {
            emailCheckBox.setChecked(true);
            phnCheckBox.setChecked(true);
        }

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
                            ViewDialog.showProgress(R.string.help_screen_one, TabFragmentOne.this.getActivity(), getResources().getString(R.string.please_wait));
                            showAlertPopUp(TabFragmentOne.this.getActivity(), "Our Mortgage Broker will" + "\n" + "contact you shortly.", "Request Sent");
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
