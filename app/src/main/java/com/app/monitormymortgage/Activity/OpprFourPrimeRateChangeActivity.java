package com.app.monitormymortgage.Activity;

import android.app.Activity;
import android.content.Context;
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

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.List;


public class OpprFourPrimeRateChangeActivity extends BaseActivity {
    TextView currentPrimeRateTextView, newPrimeRateTextView, disclaimerTextView, amortizedBalanceTextView, currentPaymentTextView;
    TextView informationTextView, paymentFrequencyTextView, termTextView, interestRateTextView;
    RelativeLayout bestFixedRateRelativeLayout, bestVariableRateRelativeLayout;
    TextView bestFixedInterestRateTextview, bestVariableRateInterestTextView;
    TextView bestFixedTermTextView, bestVariableRatetermTextView;
    Button connectToBrokerButton;
    private String title;
    String opportunityId;
    RadioGroup ContactPreferncesRadioGroup;
    AlertDialog alertDialog;
    String communication_pref;
    String status_code;
    CharSequence[] bestFixedRate;
    CharSequence[] bestVariableRate;
    List<String> stringList = new ArrayList<>();
    NumberFormat nf;
    DecimalFormat df;
    String username;
    String cntPrefernce;
    String facebookUserLoginId;
    boolean facebookUserLoginFlag;
    String googleUserLoginId;
    boolean googleUserLoginFlag;
    private GoogleApiClient client;
    private String mortgage_id;
    DecimalFormat f;
    ToastMessage toastMessage;

    public ArrayList<String> bestFixedRateList;
    public ArrayList<String> bestFixedInterestRateList;
    public ArrayList<String> bestVariableRateList;
    public ArrayList<String> bestVariableInterestRateList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prime_rate_change_opportunity);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        title = getIntent().getExtras().getString("activity_title");
        (this).setTitle(title);
        Context context = this;
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
        ViewDialog.showProgress(R.string.help_screen_one, OpprFourPrimeRateChangeActivity.this, getResources().getString(R.string.please_wait));
        opportunityId = getIntent().getExtras().getString("opportunity_id");
        if (CommonConstants.mDebug) Log.v("TAG", opportunityId);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
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
        initializeUIComponent(context);
        disclaimerTextViewClick();
        setOpportunityDetails(opportunityId);
        getUser(username);
        connectToBroker();
        fixedTermDropdownClick();
        variableTermDropdownClick();
        nf = NumberFormat.getInstance();
        df = (DecimalFormat) nf;
        df.applyPattern("#,###.00");
        f = new DecimalFormat("0.00");

    }

    private void initializeUIComponent(Context context) {
        currentPrimeRateTextView = (TextView) findViewById(R.id.currentPrimeRateTextView);
        newPrimeRateTextView = (TextView) findViewById(R.id.newPrimeRateTextView);
        disclaimerTextView = (TextView) findViewById(R.id.disclaimerTextView);
        amortizedBalanceTextView = (TextView) findViewById(R.id.amortizedBalanceTextView);
        currentPaymentTextView = (TextView) findViewById(R.id.currentPaymentTextView);
        String htmlString = "<font color='#3F51B5'><u>Disclaimer</u></font>";
        disclaimerTextView.setText(Html.fromHtml(htmlString));
        informationTextView = (TextView) findViewById(R.id.informationTextView);
        paymentFrequencyTextView = (TextView) findViewById(R.id.paymentFrequencyTextView);
        termTextView = (TextView) findViewById(R.id.termTextView);
        interestRateTextView = (TextView) findViewById(R.id.interestRateTextView);
        bestFixedInterestRateTextview = (TextView) findViewById(R.id.bestFixedInterestRateTextview);
        bestVariableRateInterestTextView = (TextView) findViewById(R.id.bestVariableRateInterestTextView);
        bestFixedRateRelativeLayout = (RelativeLayout) findViewById(R.id.bestFixedRateRelativeLayout);
        bestVariableRateRelativeLayout = (RelativeLayout) findViewById(R.id.bestVariableRateRelativeLayout);
        bestFixedTermTextView = (TextView) findViewById(R.id.bestFixedTermTextView);
        bestVariableRatetermTextView = (TextView) findViewById(R.id.bestVariableRatetermTextView);
        connectToBrokerButton = (Button) findViewById(R.id.connectToBrokerButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_mortgage, menu);
        menu.findItem(R.id.icon).setVisible(true);
        menu.findItem(R.id.icon).setEnabled(false);
//        menu.findItem(R.id.closeButton).setVisible(false);
        return true;
    }

    private void showPrimeInformationPopup(final String param1, final String param2, final String param3, final String param4) {
        informationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog.showPopUpPrimeRate(OpprFourPrimeRateChangeActivity.this, param1, param2, param3, param4);
            }
        });
    }

    public void disclaimerTextViewClick() {
        disclaimerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewDialog.showAlertPopUp(OpprFourPrimeRateChangeActivity.this, "Penalty Calculation Disclaimer\n\nPlease note the calculated penalty shown is an estimate based on the most recent information shared by the lenders’ publicly stated calculation methodology. The results do not include any discharge fees, registration or transfer fees. This information is subject to change. Exact penalty calculations will be determined over the mortgage renewal process as provided by your lender directly.", getResources().getString(R.string.disclaimer));
            }
        });
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

    public void setOpportunityDetails(String opportunityId1) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("opportunity_id", opportunityId1);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("getOpportunityDetails", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v("TAG", result);
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    JSONObject jsonObjectOpportunity = jsonObjectResult.getJSONObject("opportunity");
                    status_code = jsonObjectOpportunity.getString("status_code");
                    mortgage_id = jsonObjectOpportunity.getString("mortgage_id");
                    if (CommonConstants.mDebug) Log.v("TAG", mortgage_id);
                    if (mortgage_id != null && !mortgage_id.equalsIgnoreCase("")) {
                        if (bestFixedRateList.size() <= 0 && bestVariableRateList.size() <= 0) {
                            getBestFixedRate(mortgage_id);
                            getBestVariableRate(mortgage_id);
                        }
                    }
                    int statusCode= Integer.parseInt(status_code);
                    if (statusCode >= 50000) {
                        connectToBrokerButton.setVisibility(View.INVISIBLE);
                    }

                    if (jsonObjectOpportunity.has("prime_rate_previous_percent"))
                        currentPrimeRateTextView.setText(jsonObjectOpportunity.getString("prime_rate_previous_percent") + "%");

                    if (jsonObjectOpportunity.has("prime_rate_current_percent"))
                        newPrimeRateTextView.setText(jsonObjectOpportunity.getString("prime_rate_current_percent") + "%");

                    if (jsonObjectOpportunity.has("amortized_balance_amount")) {
                        if (jsonObjectOpportunity.getString("amortized_balance_amount").equalsIgnoreCase("0.00")) {
                            amortizedBalanceTextView.setText(" - -");
                        } else {
                            amortizedBalanceTextView.setText("$" + df.format(Double.valueOf(jsonObjectOpportunity.getString("amortized_balance_amount")).doubleValue()));
                        }
                    }

                    if (jsonObjectResult.has("current_payment_amount"))
                        currentPaymentTextView.setText(jsonObjectResult.getString("current_payment_amount"));

                    if (jsonObjectResult.has("current_payment_frequency"))
                        paymentFrequencyTextView.setText(jsonObjectResult.getString("current_payment_frequency"));

                    if (jsonObjectResult.has("current_term_in_months")) {
                        int years = Integer.parseInt(jsonObjectResult.getString("current_term_in_months")) / 12;
                        if (years <= 1) {
                            termTextView.setText(years + " Year");
                        } else {
                            termTextView.setText(years + " Years");
                        }
                    }

                    if (jsonObjectOpportunity.has("prime_rate_current_percent")) {
                        String prime_rate_current_percent = jsonObjectOpportunity.getString("prime_rate_current_percent");  //2.90
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

                    if (jsonObjectOpportunity.has("best_fixed_rate_term")) {
                        int yearsBestFixedTerm = Integer.parseInt(jsonObjectOpportunity.getString("best_fixed_rate_term")) / 12;
                        bestFixedTermTextView.setText(yearsBestFixedTerm + " Yr");
                    /* Check for best fixed rate percentage*/
                        if (jsonObjectOpportunity.getString("best_fixed_rate_percent").length() <= 0) {
                            bestFixedInterestRateTextview.setText("0.00" + "%");
                        } else {
                            double bestFixedRate = Double.parseDouble(jsonObjectOpportunity.getString("best_fixed_rate_percent"));
                            bestFixedInterestRateTextview.setText(String.valueOf(f.format(bestFixedRate)) + "%");
                        }
                    }

                    if (jsonObjectOpportunity.has("best_variable_rate_term")) {
                        int yearsBestVariableTerm = Integer.parseInt(jsonObjectOpportunity.getString("best_variable_rate_term")) / 12;
                        bestVariableRatetermTextView.setText(yearsBestVariableTerm + " Yr");
                    /* Check for best variable rate percentage*/
                        if (jsonObjectOpportunity.getString("best_variable_rate_percent").length() <= 0) {
                            bestVariableRateInterestTextView.setText("0.00" + "%");
                        } else {
                            double bestvariableRate = Double.parseDouble(jsonObjectOpportunity.getString("best_variable_rate_percent"));
                            bestVariableRateInterestTextView.setText(String.valueOf(f.format(bestvariableRate)) + "%");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ViewDialog.hideProgress();
            }

            @Override
            public void onError(String error, String reason, String details) {

                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
//                        ViewDialog.showProgress(R.string.help_screen_one, OpprFourPrimeRateChangeActivity.this, getResources().getString(R.string.please_wait));
                        setOpportunityDetails(opportunityId);
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
                        Log.v("TAG", "No network found");
                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        setOpportunityDetails(opportunityId);
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
                if (!GlobalMethods.haveNetworkConnection(OpprFourPrimeRateChangeActivity.this) && !MeteorSingleton.getInstance().isConnected()) {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                } else {
                    if (status_code != null && !status_code.equals("10000")) {
                        showAlertPopUp(OpprFourPrimeRateChangeActivity.this, "The Broker has viewed your opportunity details and will contact you shortly.", "Broker Already Contacted");
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

    public void showDialog() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OpprFourPrimeRateChangeActivity.this);
        LayoutInflater inflater = ((Activity) OpprFourPrimeRateChangeActivity.this).getLayoutInflater();
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
                    stringObjectHashMap.put("opportunity_id", opportunityId);
                    stringObjectHashMap.put("communication_pref", communication_pref);
                    stringObjectHashMap.put("lang", "en");
                    stringObjectHashMap.put("req_from", "android");
                    ErisCollectionManager.getInstance().callMethod("connectToBroker", new Object[]{stringObjectHashMap}, new ResultListener() {
                        @Override
                        public void onSuccess(String result) {
                            if (CommonConstants.mDebug) Log.v("TAG", result);
                            alertDialog.dismiss();
                            ViewDialog.showProgress(R.string.help_screen_one, OpprFourPrimeRateChangeActivity.this, getResources().getString(R.string.please_wait));
                            showAlertPopUp(OpprFourPrimeRateChangeActivity.this, "Our Mortgage Broker will" + "\n" + "contact you shortly.", "Request Sent");
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

    public void fixedTermDropdownClick() {
        bestFixedRateRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OpprFourPrimeRateChangeActivity.this);
                builder.setTitle("Select Best Fixed Rate");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(OpprFourPrimeRateChangeActivity.this);
                builder.setTitle("Select Best Variable Rate");
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

}
