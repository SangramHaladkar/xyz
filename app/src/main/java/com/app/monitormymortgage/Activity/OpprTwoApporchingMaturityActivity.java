package com.app.monitormymortgage.Activity;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class OpprTwoApporchingMaturityActivity extends BaseActivity implements NumberPicker.OnValueChangeListener {
    TextView disclaimerTextView;
    Button connectToBrokerButton;
    Button remindMeInWeekButton;
    TextView amortizedBalanceTextView;
    TextView currentPaymentTextview;
    TextView paymentFrequencyTextView;
    TextView termTextView;
    TextView interestRateTextView;
    TextView bestFixedTermTextView;
    TextView bestFixedInterestRateTextView;
    TextView bestVariableRatetermTextView;
    TextView bestVariableInterestRateTextView;
    static Dialog d;
    String opportunityIdAppochingMaturity;
    String mortgage_id;
    String title;
    String status_code;
    RadioGroup ContactPreferncesRadioGroup;
    String communication_pref;
    AlertDialog alertDialog;
    TextView maturityTextView;
    CharSequence[] bestFixedRate;
    CharSequence[] bestFixedInterestRate;
    CharSequence[] bestVariableRate;
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
    private boolean is_reminder_set = false;
    String reminderDate;
    ToastMessage toastMessage;

    //CR 4 change
    TextView informationTextView;
    String current_term_rate_type;
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
        ViewDialog.showProgress(R.string.help_screen_one, OpprTwoApporchingMaturityActivity.this, getResources().getString(R.string.please_wait));
        setContentView(R.layout.activity_oppr_two_apporching_maturity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        opportunityIdAppochingMaturity = getIntent().getExtras().getString("opportunity_id");
        if (CommonConstants.mDebug) Log.v("TAG", opportunityIdAppochingMaturity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.previous, getApplicationContext().getTheme()));
        } else {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.previous));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Redirect to activity.
                finish();
                //              ShowImagesActivity.flagChanged=true;
            }
        });

        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(this).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(this).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("GoogleUserLoginFlag", false);
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");

        initializeUIComponent();
        setEvantListener();
        getUser(username);
        setOpportunityDetails(opportunityIdAppochingMaturity);


        fixedTermDropdownClick();
        variableTermDropdownClick();

        nf = NumberFormat.getInstance();
        df = (DecimalFormat) nf;
        df.applyPattern("#,###.00");
        f = new DecimalFormat("0.00");
    }

    public void setEvantListener() {
        try {

            remindMeInWeekButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!GlobalMethods.haveNetworkConnection(OpprTwoApporchingMaturityActivity.this) && !MeteorSingleton.getInstance().isConnected()) {
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);

                    } else {
                        callRemindMeInWeek();

                        if (!is_reminder_set) {
                            ViewDialog.showAlertPopUp(OpprTwoApporchingMaturityActivity.this, getResources().getString(R.string.reminded_about_this), getResources().getString(R.string.reminder));
                        } else {
                            ViewDialog.showAlertPopUp(OpprTwoApporchingMaturityActivity.this, getResources().getString(R.string.remindar_already_set) + GlobalMethods.convertMilliSecondsToDate(reminderDate), getResources().getString(R.string.reminder));
                        }
                    }

                }
            });

            disclaimerTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewDialog.showAlertPopUp(OpprTwoApporchingMaturityActivity.this, "Penalty Calculation Disclaimer\n\nPlease note the calculated penalty shown is an estimate based on the most recent information shared by the lenders’ publicly stated calculation methodology. The results do not include any discharge fees, registration or transfer fees. This information is subject to change. Exact penalty calculations will be determined over the mortgage renewal process as provided by your lender directly.", getResources().getString(R.string.disclaimer));
                }
            });

            connectToBrokerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!GlobalMethods.haveNetworkConnection(OpprTwoApporchingMaturityActivity.this) && !MeteorSingleton.getInstance().isConnected()) {
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    } else {
                        if (status_code != null && !status_code.equals("10000")) {
                            showAlertPopUp(OpprTwoApporchingMaturityActivity.this, getResources().getString(R.string.deatils_shared_with_broker), getResources().getString(R.string.broker_already_contacted));
//                    connectToBrokerButton.setVisibility(View.GONE);
                        } else {
                            showDialog();
                        }
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void callRemindMeInWeek() {
        HashMap<String, Object> remindMeWeekHashMap = new HashMap<>();
        remindMeWeekHashMap.put("opportunity_id", opportunityIdAppochingMaturity);
        remindMeWeekHashMap.put("lang", "en");
        remindMeWeekHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("setReminderInAWeek", new Object[]{remindMeWeekHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");

                    if (jsonObjectResult.has("reminder_is_set")) {
                        if (jsonObjectResult.getString("reminder_is_set").equalsIgnoreCase("Y")) {
                            is_reminder_set = true;
                        } else {
                            is_reminder_set = false;
                        }
                    }

                    if (jsonObjectResult.has("reminder_notify_date_utc")) {
                        reminderDate = jsonObjectResult.getString("reminder_notify_date_utc");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.v("TAG", "" + reason);
            }
        });

    }

    public void getUser(final String userId) {
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
            public void onError(String error, final String reason, String details) {
                Log.v("TAG", "" + reason);
//                ViewDialog.showAlertPopUp(OpprTwoApporchingMaturityActivity.this, reason, "Error");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
//        ShowImagesActivity.flagChanged=true;
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


    public void initializeUIComponent() {
        disclaimerTextView = (TextView) findViewById(R.id.disclaimerTextView);
        String htmlString = "<font color='#3F51B5'><u>" + getResources().getString(R.string.disclaimer) + "</u></font>";
        disclaimerTextView.setText(Html.fromHtml(htmlString));
//        variableTermDropdownTextView = (TextView) findViewById(R.id.variableTermDropdownTextView);
        //      fixedTermDropdownTextView = (TextView) findViewById(R.id.fixedTermDropdownTextView);
        remindMeInWeekButton = (Button) findViewById(R.id.remindMeInWeekButton);
        connectToBrokerButton = (Button) findViewById(R.id.connectToBrokerButton);
        disclaimerTextView = (TextView) findViewById(R.id.disclaimerTextView);
        amortizedBalanceTextView = (TextView) findViewById(R.id.amortizedBalanceTextView);
        currentPaymentTextview = (TextView) findViewById(R.id.currentPaymentTextview);
        paymentFrequencyTextView = (TextView) findViewById(R.id.paymentFrequencyTextView);
        termTextView = (TextView) findViewById(R.id.termTextView);
        interestRateTextView = (TextView) findViewById(R.id.interestRateTextView);
        bestFixedTermTextView = (TextView) findViewById(R.id.bestFixedTermTextView);
        bestFixedInterestRateTextView = (TextView) findViewById(R.id.bestFixedInterestRateTextView);
        bestVariableRatetermTextView = (TextView) findViewById(R.id.bestVariableRatetermTextView);
        bestVariableInterestRateTextView = (TextView) findViewById(R.id.bestVariableInterestRateTextView);
        maturityTextView = (TextView) findViewById(R.id.maturityTextView);
        bestFixedRateRelativeLayout = (RelativeLayout) findViewById(R.id.bestFixedRateRelativeLayout);
        bestVariableRateRelativeLayout = (RelativeLayout) findViewById(R.id.bestVariableRateRelativeLayout);

        //CR4 change
        informationTextView = (TextView) findViewById(R.id.informationTextView);
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    public void setOpportunityDetails(final String opportunityId) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("opportunity_id", opportunityId);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getOpportunityDetails", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v("TAG onSuccess Appr", result);
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    JSONObject jsonObjectOpportunity = jsonObjectResult.getJSONObject("opportunity");
                    status_code = jsonObjectOpportunity.getString("status_code");
                    int statusCode = Integer.parseInt(status_code);
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
                    Log.v("TAG", mortgage_id);
                    int status_Code= Integer.parseInt(status_code);
                    if (status_Code >= 50000) {
                        connectToBrokerButton.setVisibility(View.INVISIBLE);
                    }
                    Log.v("TAG", "int status code" + statusCode);
                    if (statusCode > 10000) {
                        remindMeInWeekButton.setVisibility(View.GONE);
                    } else {
                        remindMeInWeekButton.setVisibility(View.VISIBLE);
                    }
                    // change text style to bold of Monitor My Mortgage.
                    String htmlString = "<font color='#339966'><b>" + getResources().getString(R.string.help_screen_one) + "</b></font>" + getResources().getString(R.string.mortgage_idetified) + jsonObjectResult.getString("notify_until_maturity_days") + getResources().getString(R.string.days_of_maturiity);
                    maturityTextView.setText(Html.fromHtml(htmlString));
                    if (jsonObjectResult.getString("amortized_amount").equalsIgnoreCase("0.00")) {
                        amortizedBalanceTextView.setText(" - -");
                    } else {
                        amortizedBalanceTextView.setText("$ " + df.format(Double.valueOf(jsonObjectResult.getString("amortized_amount")).doubleValue()));
                    }
                    currentPaymentTextview.setText(df.format(Double.valueOf(jsonObjectResult.getString("current_payment_amount")).doubleValue()));
                    paymentFrequencyTextView.setText(jsonObjectResult.getString("current_payment_frequency"));

                    /* Check for current term in moths.*/
                    if (jsonObjectResult.getString("current_term_in_months").equalsIgnoreCase("6")) {
                        termTextView.setText(6 + getResources().getString(R.string.months));
                    } else {
                        int years = Integer.parseInt(jsonObjectResult.getString("current_term_in_months")) / 12;
                        if (years <= 1) {
                            termTextView.setText(years + getResources().getString(R.string.year));
                        } else {
                            termTextView.setText(years + getResources().getString(R.string.years));
                        }
                    }
                    /* Check for interest rate.*/
                    if (!jsonObjectResult.getString("interest_rate_percentage").contains(".")) {
                        interestRateTextView.setText(jsonObjectResult.getString("interest_rate_percentage") + ".00" + "%");
                    } else {
                        interestRateTextView.setText(jsonObjectResult.getString("interest_rate_percentage") + "%");
                    }

                    /* Check for best fixed rate term & best fixed rate percentage.*/
                    if (jsonObjectOpportunity.getString("best_fixed_rate_term").equalsIgnoreCase("6")) {
                        bestFixedTermTextView.setText(6 + getResources().getString(R.string.mth));
                    } else {
                        int yearsBestFixedTerm = Integer.parseInt(jsonObjectOpportunity.getString("best_fixed_rate_term")) / 12;
                        bestFixedTermTextView.setText(yearsBestFixedTerm + getResources().getString(R.string.yr));
                    }
                    if (jsonObjectOpportunity.getString("best_fixed_rate_percent").length() <= 0) {
                        bestFixedInterestRateTextView.setText("0.00" + "%");
                    } else {
                        double bestFixedRate = Double.parseDouble(jsonObjectOpportunity.getString("best_fixed_rate_percent"));
                        bestFixedInterestRateTextView.setText(String.valueOf(f.format(bestFixedRate)) + "%");
                    }

                    /* Check for best variable rate term & best variable rate percentage.*/
                    if (jsonObjectOpportunity.getString("best_variable_rate_term").equalsIgnoreCase("6")) {
                        bestVariableRatetermTextView.setText(6 + getResources().getString(R.string.mth));
                    } else {
                        int yearsBestVariableTerm = Integer.parseInt(jsonObjectOpportunity.getString("best_variable_rate_term")) / 12;
                        bestVariableRatetermTextView.setText(yearsBestVariableTerm + getResources().getString(R.string.yr));
                    }
                    if (jsonObjectOpportunity.getString("best_variable_rate_percent").length() <= 0) {
                        bestVariableInterestRateTextView.setText("0.00" + "%");
                    } else {
                        double bestVariableRate = Double.parseDouble(jsonObjectOpportunity.getString("best_variable_rate_percent"));
                        bestVariableInterestRateTextView.setText(String.valueOf(f.format(bestVariableRate)) + "%");
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
                            calculatedValue = calculatedValue * 100;
                            calculatedValue = Math.round(calculatedValue);
                            calculatedValue = calculatedValue / 100;
                            interestRateTextView.setText(String.valueOf(f.format(calculatedValue)) + "%");
                        } else {
                            calculatedValue = Dprime_rate_current_percent + (-Dprime_rate_adjustment_percentage);
                            calculatedValue = calculatedValue * 100;
                            calculatedValue = Math.round(calculatedValue);
                            calculatedValue = calculatedValue / 100;
                            interestRateTextView.setText(String.valueOf(f.format(calculatedValue)) + "%");
                        }

                        showPrimeInformationPopup(String.valueOf(f.format(calculatedValue)) + "%", String.valueOf(f.format(Dprime_rate_current_percent)) + "%", String.valueOf(f.format(Dprime_rate_adjustment_percentage)) + "%", prime_rate_plus_minus);
                    }

                    if (jsonObjectOpportunity.has("reminder_is_set")) {
                        if (jsonObjectOpportunity.getString("reminder_is_set").equalsIgnoreCase("Y")) {
                            is_reminder_set = true;
                        } else {
                            is_reminder_set = false;
                        }
                    }

                    if (jsonObjectOpportunity.has("reminder_notify_date_utc")) {
                        reminderDate = jsonObjectOpportunity.getString("reminder_notify_date_utc");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ViewDialog.hideProgress();
            }

            @Override
            public void onError(String error, final String reason, String details) {
                if (CommonConstants.mDebug) Log.v("TAG onError Appr", reason);
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        if (value) {
//                            ViewDialog.showProgress(R.string.help_screen_one, OpprTwoApporchingMaturityActivity.this, getResources().getString(R.string.please_wait));
                            setOpportunityDetails(opportunityIdAppochingMaturity);
                            callRemindMeInWeek();
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
                        Log.v("TAG", "" + reason);
//                        ViewDialog.showAlertPopUp(OpprTwoApporchingMaturityActivity.this, reason, "Error");
//                        connectToBrokerButton.setEnabled(false);
                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        if (status) {
                            setOpportunityDetails(opportunityIdAppochingMaturity);
                            callRemindMeInWeek();
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
                ViewDialog.showPopUpPrimeRate(OpprTwoApporchingMaturityActivity.this, param1, param2, param3, param4);
            }
        });
    }

//    public String convertMilliSecondsToDate(String milliseconds) {
//        long millisecond = Long.parseLong(milliseconds);
//        String dateString = DateFormat.format("MMM d,yyyy", new Date(millisecond)).toString();
//        if (CommonConstants.mDebug) Log.v("TAG", dateString);
//        return dateString;
//    }

    public void showInterestRate() {
        if (current_term_rate_type.equalsIgnoreCase("Fixed")) {
            informationTextView.setVisibility(View.GONE);
        } else if (current_term_rate_type.equalsIgnoreCase("Variable")) {
            informationTextView.setVisibility(View.VISIBLE);
        }
    }


    public void showDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OpprTwoApporchingMaturityActivity.this);
        LayoutInflater inflater = ((Activity) OpprTwoApporchingMaturityActivity.this).getLayoutInflater();
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
                    stringObjectHashMap.put("opportunity_id", opportunityIdAppochingMaturity);
                    stringObjectHashMap.put("communication_pref", communication_pref);
                    stringObjectHashMap.put("lang", "en");
                    stringObjectHashMap.put("req_from", "android");
                    ErisCollectionManager.getInstance().callMethod("connectToBroker", new Object[]{stringObjectHashMap}, new ResultListener() {
                        @Override
                        public void onSuccess(String result) {
                            if (CommonConstants.mDebug) Log.v("TAG", result);
                            alertDialog.dismiss();
                            ViewDialog.showProgress(R.string.help_screen_one, OpprTwoApporchingMaturityActivity.this, getResources().getString(R.string.please_wait));
                            showAlertPopUp(OpprTwoApporchingMaturityActivity.this, getResources().getString(R.string.broker_will_contact_shortly), getResources().getString(R.string.request_sent));
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
                    if (!MeteorSingleton.getInstance().isConnected()) {
                        MeteorSingleton.getInstance().reconnect();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fixedTermDropdownClick() {
        bestFixedRateRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OpprTwoApporchingMaturityActivity.this);
                builder.setTitle(getResources().getString(R.string.select_best_fixed_rate));
                builder.setItems(bestFixedRate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int item) {
                        bestFixedTermTextView.setText(bestFixedRate[item].toString());
                        if (bestFixedInterestRateList.get(item).length() <= 0) {
                            bestFixedInterestRateTextView.setText("0.00" + "%");
                        } else {
                            double value = Double.parseDouble(bestFixedInterestRateList.get(item));
                            bestFixedInterestRateTextView.setText(String.valueOf(f.format(value)) + "%");
                        }

//                        bestFixedInterestRateTextView.setText(bestFixedInterestRateList.get(item));

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void getBestFixedRate(final String mortgage_id) {
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
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        getBestFixedRate(mortgage_id);
                    }

                    @Override
                    public void onDisconnect() {

                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        getBestFixedRate(mortgage_id);
                    }
                });

            }
        });

    }

    public void getBestVariableRate(final String mortgage_id) {
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
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        getBestFixedRate(mortgage_id);
                    }

                    @Override
                    public void onDisconnect() {

                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        getBestFixedRate(mortgage_id);
                    }
                });
            }
        });
    }


    public void variableTermDropdownClick() {
        bestVariableRateRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OpprTwoApporchingMaturityActivity.this);
                builder.setTitle(getResources().getString(R.string.select_best_variable_rate));
                builder.setItems(bestVariableRate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int item) {
                        bestVariableRatetermTextView.setText(bestVariableRate[item].toString());
                        if (bestVariableInterestRateList.get(item).length() <= 0) {
                            bestVariableInterestRateTextView.setText("0.00" + "%");
                        } else {
                            double value = Double.parseDouble(bestVariableInterestRateList.get(item));
                            bestVariableInterestRateTextView.setText(String.valueOf(f.format(value)) + "%");
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

}
