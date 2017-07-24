package com.app.monitormymortgage.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.R;
import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OpprThreeVariableRateNotificationActivity extends BaseActivity {
    Button connectToBrokerButton;
    TextView disclaimerTextView;
    TextView variableTermDropdownTextView;
    TextView fixedTermDropdownTextView;
    TextView currentRateTextView;
    TextView changeAmtTextView;
    TextView updateDateTextView;
    TextView amortizedBalanceTextView;
    TextView currentPaymentTextView;
    TextView paymentFrequencyTextView;
    TextView termTextView;
    TextView interestRateTextView;
    TextView bestFixedTermTextView;
    TextView bestFixedInterestRateTextview;
    TextView bestVariableRatetermTextView;
    TextView bestVariableRateInterestTextView;
    String opportunityId;
    String title;
    RadioGroup ContactPreferncesRadioGroup;
    AlertDialog alertDialog;
    String communication_pref;
    String status_code;
    CharSequence[] bestFixedRate;
    CharSequence[] bestVariableRate;
    List<String> stringList = new ArrayList<>();
    RelativeLayout bestFixedRateRelativeLayout;
    RelativeLayout bestVariableRateRelativeLayout;
    NumberFormat nf;
    DecimalFormat df;
    String username;
    String cntPrefernce;
    String facebookUserLoginId;
    boolean facebookUserLoginFlag;
    String googleUserLoginId;
    boolean googleUserLoginFlag;
    //    private GoogleApiClient client;
    private String mortgage_id;
    ToastMessage toastMessage;

    //CR 4 change
    String current_term_rate_type;
    TextView informationTextView;
    DecimalFormat f;

    public ArrayList<String> bestFixedRateList;
    public ArrayList<String> bestFixedInterestRateList;
    public ArrayList<String> bestVariableRateList;
    public ArrayList<String> bestVariableInterestRateList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        title = getIntent().getExtras().getString("activity_title");
        (this).setTitle(title);
        toastMessage = new ToastMessage(this);

        bestFixedRateList = new ArrayList<>();
        bestFixedInterestRateList = new ArrayList<>();
        bestVariableRateList = new ArrayList<>();
        bestVariableInterestRateList = new ArrayList<>();

        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(this).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(this).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("GoogleUserLoginFlag", false);
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");
        ViewDialog.showProgress(R.string.help_screen_one, OpprThreeVariableRateNotificationActivity.this, getResources().getString(R.string.please_wait));
        opportunityId = getIntent().getExtras().getString("opportunity_id");
        if (CommonConstants.mDebug) Log.v("TAG", opportunityId);

        setContentView(R.layout.activity_oppr_three_variable_rate_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.previous, getApplicationContext().getTheme()));
        } else {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.previous));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initializeUIComponent();
        getUser(username);
        setOpportunityDetails(opportunityId);
        connectToBroker();
        fixedTermDropdownClick();
        variableTermDropdownClick();
        disclaimerTextViewClick();
        nf = NumberFormat.getInstance();
        df = (DecimalFormat) nf;
        df.applyPattern("#,###.00");
        f = new DecimalFormat("0.00");

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).build();
    }

    public void disclaimerTextViewClick() {
        disclaimerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog.showAlertPopUp(OpprThreeVariableRateNotificationActivity.this, "Penalty Calculation Disclaimer\n\nPlease note the calculated penalty shown is an estimate based on the most recent information shared by the lenders’ publicly stated calculation methodology. The results do not include any discharge fees, registration or transfer fees. This information is subject to change. Exact penalty calculations will be determined over the mortgage renewal process as provided by your lender directly.", getResources().getString(R.string.disclaimer));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_mortgage, menu);
        menu.findItem(R.id.icon).setVisible(true);
        menu.findItem(R.id.icon).setEnabled(false);
//        menu.findItem(R.id.closeButton).setVisible(false);
        return true;
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
                if (CommonConstants.mDebug) Log.v("TAG", result);
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    cntPrefernce = jsonObjectResult.getString("contact_pref");
                    if (CommonConstants.mDebug) Log.v("TAG", cntPrefernce);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {

            }
        });
    }

    public void initializeUIComponent() {
        disclaimerTextView = (TextView) findViewById(R.id.disclaimerTextView);
        String htmlString = "<font color='#3F51B5'><u>" + getResources().getString(R.string.disclaimer) + "</u></font>";
        disclaimerTextView.setText(Html.fromHtml(htmlString));
        connectToBrokerButton = (Button) findViewById(R.id.connectToBrokerButton);
        currentRateTextView = (TextView) findViewById(R.id.currentRateTextView);
        changeAmtTextView = (TextView) findViewById(R.id.changeAmtTextView);
        updateDateTextView = (TextView) findViewById(R.id.updateDateTextView);
        amortizedBalanceTextView = (TextView) findViewById(R.id.amortizedBalanceTextView);
        currentPaymentTextView = (TextView) findViewById(R.id.currentPaymentTextView);
        paymentFrequencyTextView = (TextView) findViewById(R.id.paymentFrequencyTextView);
        termTextView = (TextView) findViewById(R.id.termTextView);
        interestRateTextView = (TextView) findViewById(R.id.interestRateTextView);
        bestFixedTermTextView = (TextView) findViewById(R.id.bestFixedTermTextView);
        bestFixedInterestRateTextview = (TextView) findViewById(R.id.bestFixedInterestRateTextview);
        bestVariableRatetermTextView = (TextView) findViewById(R.id.bestVariableRatetermTextView);
        bestVariableRateInterestTextView = (TextView) findViewById(R.id.bestVariableRateInterestTextView);
        bestFixedRateRelativeLayout = (RelativeLayout) findViewById(R.id.bestFixedRateRelativeLayout);
        bestVariableRateRelativeLayout = (RelativeLayout) findViewById(R.id.bestVariableRateRelativeLayout);

        //CR 4 change
        informationTextView = (TextView) findViewById(R.id.informationTextView);
    }

    public void fixedTermDropdownClick() {
        bestFixedRateRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OpprThreeVariableRateNotificationActivity.this);
                builder.setTitle(getResources().getString(R.string.select_best_fixed_rate));
                builder.setItems(bestFixedRate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int item) {

                        bestFixedTermTextView.setText(bestFixedRate[item].toString());
                        if (bestFixedInterestRateList.get(item).length() <= 0) {
                            bestFixedInterestRateTextview.setText("0.00" + "%");
                        } else {
                            double value = Double.parseDouble(bestFixedInterestRateList.get(item));
                            bestFixedInterestRateTextview.setText(String.valueOf(f.format(value)) + "%");
                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void variableTermDropdownClick() {
        bestVariableRateRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OpprThreeVariableRateNotificationActivity.this);
                builder.setTitle(getResources().getString(R.string.select_best_variable_rate));
                builder.setItems(bestFixedRate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int item) {

                        bestVariableRatetermTextView.setText(bestVariableRate[item].toString());
                        if (bestVariableInterestRateList.get(item).length() <= 0) {
                            bestVariableRateInterestTextView.setText("0.00" + "%");
                        } else {
                            double value = Double.parseDouble(bestVariableInterestRateList.get(item));
                            bestVariableRateInterestTextView.setText(String.valueOf(f.format(value)) + "%");
                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


    public void setOpportunityDetails(String opportunityId1) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("opportunity_id", opportunityId1);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getOpportunityDetails", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v("TAG opportunity_id", result);
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    JSONObject jsonObjectOpportunity = jsonObjectResult.getJSONObject("opportunity");
                    status_code = jsonObjectOpportunity.getString("status_code");
                    mortgage_id = jsonObjectOpportunity.getString("mortgage_id");

                    if (mortgage_id != null && !mortgage_id.equalsIgnoreCase("")) {
                        if (bestFixedRateList.size() <= 0 && bestVariableRateList.size() <= 0) {
                            getBestFixedRate(mortgage_id);
                            getBestVariableRate(mortgage_id);
                        }
                    }
                    //CR 4 change
                    if (jsonObjectResult.has("current_term_rate_type"))
                        current_term_rate_type = jsonObjectResult.getString("current_term_rate_type");
                    Log.v("TAG current term", current_term_rate_type);
                    showInterestRate();

                    if (CommonConstants.mDebug) Log.v("TAG", mortgage_id);
                    int statusCode= Integer.parseInt(status_code);
                    if (statusCode >= 50000) {
                        connectToBrokerButton.setVisibility(View.INVISIBLE);
                    }
                    currentRateTextView.setText(jsonObjectOpportunity.getString("boc_current_rate_percent") + "%");
                    changeAmtTextView.setText(jsonObjectOpportunity.getString("boc_change_rate_percent") + "%");
                    if (jsonObjectOpportunity.has("boc_rate_update_date")) {
                        updateDateTextView.setText(GlobalMethods.convertMilliSecondsToDate(jsonObjectOpportunity.getString("boc_rate_update_date")));
                    } else {
                        if (jsonObjectOpportunity.has("status_updated_at")) {
                            updateDateTextView.setText(GlobalMethods.convertMilliSecondsToDate(jsonObjectOpportunity.getString("status_updated_at")));
                        }
                    }

                    if (jsonObjectResult.getString("amortized_amount").equalsIgnoreCase("0.00")) {
                        amortizedBalanceTextView.setText(" - -");
                    } else {
                        amortizedBalanceTextView.setText("$" + df.format(Double.valueOf(jsonObjectResult.getString("amortized_amount")).doubleValue()));
                    }
                    currentPaymentTextView.setText(df.format(Double.valueOf(jsonObjectResult.getString("current_payment_amount")).doubleValue()));
                    paymentFrequencyTextView.setText(jsonObjectResult.getString("current_payment_frequency"));

                    int years = Integer.parseInt(jsonObjectResult.getString("current_term_in_months")) / 12;
                    if (years <= 1) {
                        termTextView.setText(years + getResources().getString(R.string.year));
                    } else {
                        termTextView.setText(years + getResources().getString(R.string.years));
                    }

                    if (!jsonObjectResult.getString("interest_rate_percentage").contains(".")) {
                        interestRateTextView.setText(jsonObjectResult.getString("interest_rate_percentage") + ".00 " + "%");
                    } else {
                        interestRateTextView.setText(jsonObjectResult.getString("interest_rate_percentage") + "%");
                    }


                    int yearsBestFixedTerm = Integer.parseInt(jsonObjectOpportunity.getString("best_fixed_rate_term")) / 12;
                    bestFixedTermTextView.setText(yearsBestFixedTerm + getResources().getString(R.string.yr));
                    /* Check for best fixed rate percentage*/
                    if (jsonObjectOpportunity.getString("best_fixed_rate_percent").length() <= 0) {
                        bestFixedInterestRateTextview.setText("0.00" + "%");
                    } else {
                        double bestFixedRate = Double.parseDouble(jsonObjectOpportunity.getString("best_fixed_rate_percent"));
                        bestFixedInterestRateTextview.setText(String.valueOf(f.format(bestFixedRate)) + "%");
                    }

                    int yearsBestVariableTerm = Integer.parseInt(jsonObjectOpportunity.getString("best_variable_rate_term")) / 12;
                    bestVariableRatetermTextView.setText(yearsBestVariableTerm + getResources().getString(R.string.yr));
                    /* Check for best variable rate percentage*/
                    if (jsonObjectOpportunity.getString("best_variable_rate_percent").length() <= 0) {
                        bestVariableRateInterestTextView.setText("0.00" + "%");
                    } else {
                        double bestVariableRate = Double.parseDouble(jsonObjectOpportunity.getString("best_variable_rate_percent"));
                        bestVariableRateInterestTextView.setText(String.valueOf(f.format(bestVariableRate)) + "%");
                    }

                    //CR 4 change
                    if (jsonObjectResult.has("prime_rate")) {
                        String prime_rate_current_percent = jsonObjectResult.getString("prime_rate");  //2.90
                        String prime_rate_adjustment_percentage = jsonObjectResult.getString("prime_rate_adjustment_percentage");  //0.25
                        String prime_rate_plus_minus = jsonObjectResult.getString("prime_rate_plus_minus");  //-
                        double Dprime_rate_current_percent = Double.parseDouble(prime_rate_current_percent);
                        double Dprime_rate_adjustment_percentage = Double.parseDouble(prime_rate_adjustment_percentage);

                        double calculatedValue;
                        if (prime_rate_plus_minus.equalsIgnoreCase("+")) {
                            calculatedValue = Dprime_rate_current_percent + Dprime_rate_adjustment_percentage;
                            interestRateTextView.setText(String.valueOf(f.format(calculatedValue)) + "%");
                        } else {
                            calculatedValue = Dprime_rate_current_percent + (-Dprime_rate_adjustment_percentage);
                            interestRateTextView.setText(String.valueOf(f.format(calculatedValue)) + "%");
                        }
                        showPrimeInformationPopup(String.valueOf(f.format(calculatedValue)) + "%", String.valueOf(f.format(Dprime_rate_current_percent)) + "%", String.valueOf(f.format(Dprime_rate_adjustment_percentage)) + "%", prime_rate_plus_minus);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                ViewDialog.hideProgress();
            }

            @Override
            public void onError(String error, final String reason, String details) {
                if (CommonConstants.mDebug) Log.v("TAG On Error", reason);
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        if (value) {
                            setOpportunityDetails(opportunityId);
                            if (facebookUserLoginFlag) {
                                getUser(facebookUserLoginId);
                            } else if (googleUserLoginFlag) {
                                getUser(googleUserLoginId);
                            } else {
                                getUser(username);
                            }
                        }
                    }

                    @Override
                    public void onDisconnect() {
                        ViewDialog.hideProgress();
//                        ToastMessage toastMessage=new ToastMessage(OpprThreeVariableRateNotificationActivity.this);
//                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                        ViewDialog.showAlertPopUp(OpprThreeVariableRateNotificationActivity.this, reason, getResources().getString(R.string.error));
//                        connectToBrokerButton.setEnabled(false);
                    }

                    @Override
                    public void onException(Exception e) {
                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        if (status) {
                            setOpportunityDetails(opportunityId);
                            if (facebookUserLoginFlag) {
                                getUser(facebookUserLoginId);
                            } else if (googleUserLoginFlag) {
                                getUser(googleUserLoginId);
                            } else {
                                getUser(username);
                            }
                            connectToBrokerButton.setEnabled(true);
                        }

                    }
                });
            }
        });
    }

    private void showPrimeInformationPopup(final String param1, final String param2, final String param3, final String param4) {
        informationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog.showPopUpPrimeRate(OpprThreeVariableRateNotificationActivity.this, param1, param2, param3, param4);
            }
        });
    }

    public void connectToBroker() {
        connectToBrokerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalMethods.haveNetworkConnection(OpprThreeVariableRateNotificationActivity.this) && !MeteorSingleton.getInstance().isConnected()) {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                } else {
                    if (status_code != null && !status_code.equals("10000")) {
                        showAlertPopUp(OpprThreeVariableRateNotificationActivity.this, getResources().getString(R.string.deatils_shared_with_broker), getResources().getString(R.string.broker_already_contacted));
                    } else {
                        showDialog();
                    }
                }
            }
        });
    }

    public void showInterestRate() {
        if (current_term_rate_type.equalsIgnoreCase("Fixed")) {
            informationTextView.setVisibility(View.GONE);
        } else if (current_term_rate_type.equalsIgnoreCase("Variable")) {
            informationTextView.setVisibility(View.VISIBLE);
        }
    }

//    public String convertMilliSecondsToDate(String milliseconds) {
//        long millisecond = Long.parseLong(milliseconds);
//        String dateString = DateFormat.format("MMM d,yyyy", new Date(millisecond)).toString();
//        if (CommonConstants.mDebug) Log.v("TAG", dateString);
//        return dateString;
//    }


    public void showDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OpprThreeVariableRateNotificationActivity.this);
        LayoutInflater inflater = ((Activity) OpprThreeVariableRateNotificationActivity.this).getLayoutInflater();
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
//        communication_pref = "Phone";

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
                    errorTextView.setText(getResources().getString(R.string.select_contact_prefernce));
                } else {
                    if (phnCheckBox.isChecked() && emailCheckBox.isChecked()) {
                        communication_pref = "Phone,Email";
                    } else if (phnCheckBox.isChecked()) {
                        communication_pref = "Phone";
                    } else if (emailCheckBox.isChecked()) {
                        communication_pref = "Email";
                    }


                    HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
                    stringObjectHashMap.put("opportunity_id", opportunityId);
                    stringObjectHashMap.put("communication_pref", communication_pref);
                    stringObjectHashMap.put("lang", "en");
                    stringObjectHashMap.put("req_from", "android");
                    ErisCollectionManager.getInstance().callMethod("connectToBroker", new Object[]{stringObjectHashMap}, new ResultListener() {
                        @Override
                        public void onSuccess(String result) {
                            if (CommonConstants.mDebug) Log.v("TAG", result);
                            alertDialog.dismiss();
                            ViewDialog.showProgress(R.string.help_screen_one, OpprThreeVariableRateNotificationActivity.this, getResources().getString(R.string.please_wait));
                            showAlertPopUp(OpprThreeVariableRateNotificationActivity.this, getResources().getString(R.string.broker_will_contact_shortly), getResources().getString(R.string.request_sent));
                        }

                        @Override
                        public void onError(String error, String reason, String details) {
                            if (CommonConstants.mDebug) Log.v("TAG", reason);
                        }
                    });
                }
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
                    finish();
                    CommonConstants.dashboardReplaceFlag = true;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getBestFixedRate(String mortgage_id) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        stringObjectHashMap.put("mortgage_id", mortgage_id);


        ErisCollectionManager.getInstance().callMethod("getBestFixedVariableRatesList", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    JSONArray jsonArrayBestFixedRate = jsonObjectResult.getJSONArray("best_fixed_rate_list");
                    for (int i = 0; i < jsonArrayBestFixedRate.length(); i++) {
                        JSONObject row = jsonArrayBestFixedRate.getJSONObject(i);
                        bestFixedRateList.add(row.getString("val"));
                        bestFixedInterestRateList.add(row.getString("rate"));
                        bestFixedRate = bestFixedRateList.toArray(new CharSequence[bestFixedRateList.size()]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {

            }
        });

    }

    public void getBestVariableRate(String mortgage_id) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        stringObjectHashMap.put("mortgage_id", mortgage_id);

        ErisCollectionManager.getInstance().callMethod("getBestFixedVariableRatesList", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    JSONArray jsonArrayBestFixedRate = jsonObjectResult.getJSONArray("best_variable_rate_list");
                    for (int i = 0; i < jsonArrayBestFixedRate.length(); i++) {
                        JSONObject row = jsonArrayBestFixedRate.getJSONObject(i);
                        bestVariableRateList.add(row.getString("val"));
                        bestVariableInterestRateList.add(row.getString("rate"));
                        bestVariableRate = bestVariableRateList.toArray(new CharSequence[bestVariableRateList.size()]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, String reason, String details) {

            }
        });
    }


//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "OpprThreeVariableRateNotification Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.mobifilia.monitormymortgage.Activity/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "OpprThreeVariableRateNotification Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.mobifilia.monitormymortgage.Activity/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
}
