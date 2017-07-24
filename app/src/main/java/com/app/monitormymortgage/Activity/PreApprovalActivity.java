package com.app.monitormymortgage.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.EditTextThousand;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.Fragments.MyDashboardFragment;
import com.app.monitormymortgage.R;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class PreApprovalActivity extends BaseActivity implements View.OnClickListener {

    public static String TAG = PreApprovalActivity.class.getName();

    private EditText actualTitleEditText, purchasePriceEditText, downpaymentAmtEditText, MLSEditText;
    private TextView actualStartDateTextView, paymentFrequencyTwoTextview;
    private RelativeLayout selectFrequencyRelativeLayout;
    private Button submitQuoteButton;
    private DatePickerDialog fromDatePickerDialog;
    private DateFormat dateFormatter;
    private long startTime;
    private CharSequence[] paymentFrequencyItems;
    ToastMessage toastMessage;
    String mortgageTypeOne, mortgageTypeTwo;
    private RadioGroup toggleOne, toggleTwo;
    String userId;

    boolean facebookUserLoginFlag;
    boolean googleUserLoginFlag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_approval);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        (this).setTitle(getResources().getString(R.string.pre_approval_request));
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        userId = getIntent().getExtras().getString("UserId");
        Log.v(TAG, userId);
//        ViewDialog.showProgress(R.string.help_screen_one, PreApprovalActivity.this, getResources().getString(R.string.please_wait));
        toastMessage = new ToastMessage(this);
        dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.previous, getApplicationContext().getTheme()));
        } else {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.previous, getApplicationContext().getTheme()));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ViewDialog(PreApprovalActivity.this).showPopupAddMortgage(PreApprovalActivity.this, getResources().getString(R.string.request_quote_incomplete_message), R.string.request_quote_title);
                hideKeyboard(v);
            }
        });

        initializeUIComponent();
        setDateTimeField();
//        getAllMasters();
        setEventListener();
        getMortgageTypeRadioButtonClick();
        paymentFrequencyItems = MyDashboardFragment.mastersArrayList.toArray(new CharSequence[MyDashboardFragment.mastersArrayList.size()]);
        purchasePriceEditText.addTextChangedListener(new EditTextThousand(purchasePriceEditText));
        downpaymentAmtEditText.addTextChangedListener(new EditTextThousand(downpaymentAmtEditText));
        if (!MeteorSingleton.getInstance().isConnected()) {
            MeteorSingleton.getInstance().reconnect();
        }

        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("GoogleUserLoginFlag", false);

    }


    /*
    * Hide Keyboard
    * */
    public void hideKeyboard(View view) {
        //  Hide Keyboard.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        new ViewDialog(PreApprovalActivity.this).showPopupAddMortgage(PreApprovalActivity.this, R.string.mortgage_incomplete_title);
    }

    /*
        * Initialize all UI component
        * */
    public void initializeUIComponent() {
        actualTitleEditText = (EditText) findViewById(R.id.actualTitleEditText);
        actualStartDateTextView = (TextView) findViewById(R.id.actualStartDateEditText);
        paymentFrequencyTwoTextview = (TextView) findViewById(R.id.paymentFrequencyTwoTextview);
        purchasePriceEditText = (EditText) findViewById(R.id.purchasePriceEditText);
        downpaymentAmtEditText = (EditText) findViewById(R.id.downpaymentAmtEditText);
        MLSEditText = (EditText) findViewById(R.id.MLSEditText);
        selectFrequencyRelativeLayout = (RelativeLayout) findViewById(R.id.selectFrequencyRelativeLayout);
        submitQuoteButton = (Button) findViewById(R.id.submitQuoteButton);
        toggleOne = (RadioGroup) findViewById(R.id.toggle);
        toggleTwo = (RadioGroup) findViewById(R.id.toggle2);

    }

    /*
    * Get Radio Button values.
    * */
    public void getMortgageTypeRadioButtonClick() {
        mortgageTypeOne = ((RadioButton) findViewById(toggleOne.getCheckedRadioButtonId())).getText().toString();
        mortgageTypeTwo = ((RadioButton) findViewById(toggleTwo.getCheckedRadioButtonId())).getText().toString();

        toggleOne.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.residential:
                        mortgageTypeOne = "Residential";
                        Log.v(TAG, mortgageTypeOne);
                        break;
                    case R.id.commercial:
                        mortgageTypeOne = "Commercial";
                        Log.v(TAG, mortgageTypeOne);
                        break;
                }
            }
        });

        toggleTwo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.primaryRadioBtn:
                        mortgageTypeTwo = "Primary";
                        Log.v(TAG, mortgageTypeTwo);
                        break;
                    case R.id.secondryRadioBtn:
                        mortgageTypeTwo = "Secondary";
                        Log.v(TAG, mortgageTypeTwo);
                        break;
                }
            }
        });
    }


    /*
    * Handle clicks on button
    *
    * */
    public void setEventListener() {
        selectFrequencyRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PreApprovalActivity.this);
                builder.setTitle("Select Frequency");
                builder.setItems(paymentFrequencyItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        paymentFrequencyTwoTextview.setText(paymentFrequencyItems[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        submitQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {
                    if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(PreApprovalActivity.this)) {
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    } else {
                        ViewDialog.showProgress(R.string.help_screen_one, PreApprovalActivity.this, getResources().getString(R.string.please_wait));
                        submitQuoteAPI(userId);
                    }
                }
            }
        });
    }

    /*
    * Show calendar
    * get Date and set to textview.
    * convert date to milliseconds and pass to API.
    * */
    public void setDateTimeField() {
        actualStartDateTextView.setOnClickListener(this);
        final Calendar newCalendar = Calendar.getInstance();
        Date date = new Date();
        final long datenew = date.getTime();

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int yearOne, int monthOfYearOne, int dayOfMonthOne) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(yearOne, monthOfYearOne, dayOfMonthOne);
                startTime = calendar.getTimeInMillis();

                actualStartDateTextView.setText(dateFormatter.format(calendar.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMinDate(datenew);
    }

    /*
    * Submit quote
    * */
    public void submitQuoteAPI(final String userName) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("title", actualTitleEditText.getText().toString());
        stringObjectHashMap.put("type", mortgageTypeOne);
        stringObjectHashMap.put("type2", mortgageTypeTwo);
        stringObjectHashMap.put("quote_type", "Pre-Approval");
        stringObjectHashMap.put("quote_type_code", 1);
        stringObjectHashMap.put("original_amount", purchasePriceEditText.getText().toString().replace(",", ""));
        stringObjectHashMap.put("down_payment_amount", downpaymentAmtEditText.getText().toString().replace(",", ""));
        stringObjectHashMap.put("current_payment_frequency", paymentFrequencyTwoTextview.getText().toString());
        stringObjectHashMap.put("start_date_utc", startTime);
        stringObjectHashMap.put("mls_number", MLSEditText.getText().toString());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("user_id", userName);
        hashMap.put("lang", "en");
        hashMap.put("req_from", "android");
        hashMap.put("request_input", stringObjectHashMap);

        ErisCollectionManager.getInstance().callMethod("saveRequestQuote", new Object[]{hashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v("TAG onSuccess", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        ViewDialog.hideProgress();
                        showAlertPopUp(jsonObjectData.getString("message"), getResources().getString(R.string.request_sent));
                    } else if (status.equalsIgnoreCase("false")) {
                        JSONObject jsonObjectError = jsonObject.getJSONObject("error");

                        String code = jsonObjectError.getString("code");
                        if (code.equals("10005")) {
                        } else {
                            ViewDialog.hideProgress();
                            showAlertPopUp(jsonObjectError.getString("message"), getResources().getString(R.string.request_sent));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.v("TAG onError", reason);
                ViewDialog.hideProgress();
                toastMessage.showToastMsg(reason, Toast.LENGTH_SHORT);

            }
        });

    }

    public void showAlertPopUp(final String msg, String title) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (this).getLayoutInflater();
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
                    finish();
                }
            });

            text.setText(msg);
            titleTextView.setText(title);
            Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    * Onclick event on actualStartDateEditText.
    * */
    @Override
    public void onClick(View v) {
        if (v == actualStartDateTextView) {
            fromDatePickerDialog.show();
        }
    }


    /*
    * validate all feilds.
    * */
    public boolean validation() {
        boolean bln = true;
        if (actualTitleEditText.getText().toString().equals("") && purchasePriceEditText.getText().toString().equals("")
                && downpaymentAmtEditText.getText().toString().equals("") && MLSEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_information_in_all_fields, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualTitleEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.please_enter_property_name, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualStartDateTextView.getText().toString().equalsIgnoreCase("Select Date")) {
            toastMessage.showToastMsg(R.string.error_empty_date, Toast.LENGTH_SHORT);
            bln = false;
        } else if (paymentFrequencyTwoTextview.getText().toString().equals("Select Frequency")) {
            toastMessage.showToastMsg(R.string.please_select_frequency, Toast.LENGTH_SHORT);
            bln = false;
        } else if (purchasePriceEditText.getText().toString().equals("")) {
            purchasePriceEditText.requestFocus();
            toastMessage.showToastMsg(R.string.please_enter_purchase_price, Toast.LENGTH_SHORT);
            bln = false;
        } else if (downpaymentAmtEditText.getText().toString().equals("")) {
            downpaymentAmtEditText.requestFocus();
            toastMessage.showToastMsg(R.string.please_enter_down_payment_amt, Toast.LENGTH_SHORT);
            bln = false;
        }
        return bln;
    }

    /*
    * M3 menu on top
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_mortgage, menu);
        menu.findItem(R.id.icon).setVisible(true);
        menu.findItem(R.id.icon).setEnabled(false);
        return true;
    }


//    @Override
//    public void onConnect(boolean value) {
//        submitQuoteAPI(userId);
//    }
//
//    @Override
//    public void onDisconnect() {
//
//    }
//
//    @Override
//    public void onException(Exception e) {
//
//    }
//
//    @Override
//    public void onInternetStatusChanged(boolean status) {
//
//    }
}
