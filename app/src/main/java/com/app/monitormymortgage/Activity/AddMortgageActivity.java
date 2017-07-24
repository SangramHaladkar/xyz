package com.app.monitormymortgage.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.monitormymortgage.Adapters.ArrayAdapterItem;
import com.app.monitormymortgage.Adapters.PreviewAdapter;
import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ShowLenderListPopup;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.CustomTextWatcherInterestRate;
import com.app.monitormymortgage.Common.EditTextThousand;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.Common.InputFilterMinMax;
import com.app.monitormymortgage.DataHolderClasses.AttachmentFileData;
import com.app.monitormymortgage.DataHolderClasses.ObjectItem;
import com.app.monitormymortgage.R;
import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.readystatesoftware.viewbadger.BadgeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.UnreadConversationCountListener;


public class AddMortgageActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = AddMortgageActivity.class.getSimpleName();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 456;
    private TextView paymentFrequencyTwoTextview;
    private TextView actualMaturityDateEditText;
    private TextView actualStartDateEditText;
    private Button stepOneTextView;
    private Button stepTwoTextView;
    private Button stepThreeTextView;
    private LinearLayout stepOneLinearLayout;
    private LinearLayout stepTwoLinearLayout;
    private LinearLayout stepThreeLinearLayout;
    private TextView actualRateIncreaseTextView;
    private TextView actualNotifyMeTextView;
    private TextView actaulCurrencyTextview;
    private Button step_one_btn_next;
    private Button step_one_btn_help;
    private Button step_two_btn_next;
    private Button step_two_btn_help;
    private Button step_two_btn_previous;
    private Button step_three_btn_previous;
    private Button step_three_btn_help;
    private BadgeView step_one_btn_help_badge;
    private BadgeView step_two_btn_help_badge;
    private BadgeView step_three_btn_help_badge;
    CharSequence[] rateIncreaseItems;
    CharSequence[] noifyMeItems;
    CharSequence[] currencyItems;
    CharSequence[] paymentFrequencyItems;

    CharSequence[] MortgageTypeItems;
    CharSequence[] MortgageStatus;
    CharSequence[] CurrentTerm;
    CharSequence[] operator = {"+", "-"};
    CharSequence[] periods;
    Calendar myCalendar;
    ToastMessage toastMessage;
    EditText actualTitleEditText;
    EditText postalCodeEditText;
    EditText totalMortgageEditText;
    EditText currentAmortizedMortgageValueEditText;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    EditText AmortizationTermEditText;
    EditText currentPaymentAmtEditText;
    EditText actualInterestRateEditText;
    public static EditText discountRateEditText;
    EditText currentPaymentEditText;
    EditText creditLineAmtEditText;
    EditText savingAmtPerMonthEditText;
    EditText actualremainingTermEditText;
    EditText actualMpacEditText;
    Button btn_save_monitor;
    TextView cameraTextView;
    public ImageView lenderLogoImageview;
    public AlertDialog alertDialogStores;
    RadioGroup toggle;
    List<Bitmap> lenderString;
    ShowLenderListPopup showLenderListPopup;
    TextView selectLenderTextview;
    String lenderId;
    ArrayList<ObjectItem> objectItemArrayList;
    String lenderName;
    String mortgageType;
    String opportunityIdAlreadyAvailable;
    String opportunityNameAlreadyAvialble;
    TextView takePictureTextView;
    TextView currentTermNumberTextView, mortgageTypeTextView, mortgageStatusTextView;
    String termsInMonth;
    int termInYear;
    String notifyMe;
    String rateIncrease;
    String mortgageID;
    int arivalYear, arivalMonth, arivalDay;
    boolean stepOneShow, stepTwoShow, stepThreeShow;
    String otherLenderName;
    TextView otherLenderErrorTextView;
    EditText lenderEditText;
    long startTime;
    long maturityTime;
    Date nextYear;
    String facebookUserLoginId;
    boolean facebookUserLoginFlag;
    String googleUserLoginId;
    boolean googleUserLoginFlag;
    String username;
    RecyclerView img_recycler_view;
    ImageView btnCamera;
    private Uri fileUri;
    final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 2989;
    private static final int PHOTO_PREVIEW_REQUEST_CODE = 6384;
    private PreviewAdapter fileAdapter;
    private ArrayList<AttachmentFileData> fileDataList;
    int thumbFactor = 4;
    public static final String ATTACHMENTLISTKEY = "attachmentsList";
    RelativeLayout selectCurrencyRelativeLayout;
    RelativeLayout selectLenderRelativeLayout;
    RelativeLayout rateIncreaseRelativeLayout;
    RelativeLayout notifyMeRelativeLayout;
    RelativeLayout selectFrequencyRelativeLayout;
    ArrayList<String> arrayList;
    RelativeLayout maturityDateLayout;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    // CR 4
    TextView interesrRateTextView;
    TextView discountRateTextView;
    TextView optionalRateDiscountTextView;
    TextView informationTextView;
    RelativeLayout spinnerLayout;
    public static TextView operatorTextView;
    public static TextView variableInterestRateTextView;
    public static double primeRate;
    public static double interestOne;
    DecimalFormat f;
    public static boolean flag = false;


    // Change v1.0.43
    TextView originalTextView;
    RelativeLayout selectPeriodRelativeLayout;
    TextView periodTextView;


    RelativeLayout selectTermRelativeLayout, selectTypeRelativeLayout, selectStatusRelativeLayout;


    @Override
    public void onBackPressed() {
        new ViewDialog(this).showPopupAddMortgage(AddMortgageActivity.this, R.string.mortgage_incomplete_title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_add_mortgage);
        fileDataList = new ArrayList<>();
        arrayList = new ArrayList<>();
        f = new DecimalFormat("0.00");
        selectCurrencyRelativeLayout = (RelativeLayout) findViewById(R.id.selectCurrencyRelativeLayout);
        selectLenderRelativeLayout = (RelativeLayout) findViewById(R.id.selectLenderRelativeLayout);
        rateIncreaseRelativeLayout = (RelativeLayout) findViewById(R.id.rateIncreaseRelativeLayout);
        notifyMeRelativeLayout = (RelativeLayout) findViewById(R.id.notifyMeRelativeLayout);
        selectFrequencyRelativeLayout = (RelativeLayout) findViewById(R.id.selectFrequencyRelativeLayout);

        // Change v1.0.43
        selectPeriodRelativeLayout = (RelativeLayout) findViewById(R.id.selectPeriodRelativeLayout);
        periodTextView = (TextView) findViewById(R.id.periodTextView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.hideOverflowMenu();
        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(this).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(this).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("GoogleUserLoginFlag", false);
        alertDialogBuilder = new AlertDialog.Builder(AddMortgageActivity.this);
        alertDialog = alertDialogBuilder.create();
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");
        if (CommonConstants.mDebug) Log.v(TAG, username);
        ViewDialog.showProgress(R.string.help_screen_one, AddMortgageActivity.this, getResources().getString(R.string.please_wait));
        Date date = new Date();
        rateIncrease = "10";
        notifyMe = "120";
        opportunityIdAlreadyAvailable = "";
        opportunityNameAlreadyAvialble = "";
        showLenderListPopup = new ShowLenderListPopup();
        objectItemArrayList = new ArrayList<>();
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.previous));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ViewDialog(AddMortgageActivity.this).showPopupAddMortgage(AddMortgageActivity.this, R.string.mortgage_incomplete_title);
                hideKeyboard(v);
            }
        });
        myCalendar = Calendar.getInstance();
        toastMessage = new ToastMessage(this);
        dateFormatter = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        initialiseUIComponent();
        stepOneShow = true;
        stepTwoShow = false;
        stepThreeShow = false;
        stepOneLinearLayout.setVisibility(View.VISIBLE);
        stepTwoLinearLayout.setVisibility(View.GONE);
        stepThreeLinearLayout.setVisibility(View.GONE);

        stepOneTextView.setBackgroundResource(R.drawable.steponesel);
        stepTwoTextView.setBackgroundResource(R.drawable.steptwounselectedstepthreeunselected);
        stepThreeTextView.setBackgroundResource(R.drawable.stepthreeunselected);

        //      Step One UI Handling                     //
        stepOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepOneDetails();
            }
        });
        step_one_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonConstants.mDebug) Log.v(TAG, "On next button click");
                if (validationStepOne()) {
                    stepTwoDetails();
                }
            }
        });

        step_one_btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonConstants.mDebug) Log.v(TAG, "On help button click");

                // Show the help interface
                Intercom.client().displayMessenger();
            }
        });

        //      Step One UI Handling                     //
        stepOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepOneDetails();
            }
        });
        step_one_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonConstants.mDebug) Log.v(TAG, "On next button click");

                if (validationStepOne()) {
                    stepTwoDetails();
                    hideKeyboard(v);
                }
            }
        });
        stepThreeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationStepOne()) {
                    stepThreeDetails();
                }
            }
        });

        //      Step Two UI Handling      //
        stepTwoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepOneLinearLayout.isShown() && validationStepOne()) {
                    stepTwoDetails();
                    hideKeyboard(v);
                }
                if (stepThreeLinearLayout.isShown()) {
                    stepTwoDetails();
                    hideKeyboard(v);
                }
            }
        });
        step_two_btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepOneDetails();
            }
        });
        step_two_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationStepTwo()) {
                    stepThreeDetails();
                    hideKeyboard(v);

                }
            }
        });

        step_two_btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonConstants.mDebug) Log.v(TAG, "On help button click");

                // Show the help interface
                Intercom.client().displayMessenger();
            }
        });

        stepOneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepOneDetails();
            }
        });
        stepThreeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationStepTwo()) {
                    stepThreeDetails();
                    hideKeyboard(v);
                }
            }
        });
        //      Step Three UI Handling                     //
        stepThreeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validationStepOne() && validationStepTwo()) {
                    stepThreeDetails();
                    hideKeyboard(v);
                }
            }
        });
        step_three_btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stepTwoDetails();
            }
        });

        step_three_btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonConstants.mDebug) Log.v(TAG, "On help button click");

                // Show the help interface
                Intercom.client().displayMessenger();
            }
        });

        btn_save_monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalMethods.haveNetworkConnection(AddMortgageActivity.this) && !MeteorSingleton.getInstance().isConnected()) {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                } else {
                    if (valiadationStepThree()) {
                        flag = true;
                        ViewDialog.showProgress(R.string.help_screen_one, AddMortgageActivity.this, getResources().getString(R.string.progress_save_mortgage));
                        addMortgage(AddMortgageActivity.this);
                    }
                }
            }
        });

        // Intercom - Listen for new conversation activity
        Intercom.client().addUnreadConversationCountListener(unreadConversationCountListener);

        rateIncreaseSpinnerSelect();
        notifyMeSpinnerSelect();
        currencySpineerTextViewSelect();
        paymentFrequencySpinnerTextviewSelect();
        setDateTimeField();
        showLenderList();
        getLenderList();
        getUser();
        getMortgageTypeRadioButtonClick();
        currentTermDropdownClick();
        mortgageTypeDropdownClick();
        mortgageStatusDropdownClick();
        //   Change v1.0.42
        selectPeriod();
        cameraBtnClick();
        selectPostalCode();
        getAllMasters();
        totalMortgageEditText.addTextChangedListener(new EditTextThousand(totalMortgageEditText));
        currentAmortizedMortgageValueEditText.addTextChangedListener(new EditTextThousand(currentAmortizedMortgageValueEditText));
        currentPaymentAmtEditText.addTextChangedListener(new EditTextThousand(currentPaymentAmtEditText));
        actualInterestRateEditText.addTextChangedListener(new CustomTextWatcherInterestRate(actualInterestRateEditText));
        discountRateEditText.addTextChangedListener(new CustomTextWatcherInterestRate(discountRateEditText));
        creditLineAmtEditText.addTextChangedListener(new EditTextThousand(creditLineAmtEditText));
        savingAmtPerMonthEditText.addTextChangedListener(new EditTextThousand(savingAmtPerMonthEditText));
        actualremainingTermEditText.addTextChangedListener(new EditTextThousand(actualremainingTermEditText));

        // Intercom
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("step_number", "0");
        Intercom.client().logEvent("add_mortgage", eventData);

        //CR 4 change
        getCurrentPrimeRate();
        onDisconnect();
    }

    private final UnreadConversationCountListener unreadConversationCountListener = new UnreadConversationCountListener() {
        @Override
        public void onCountUpdate(int unreadCount) {
            Log.i("Intercom", "UnreadConversationCountListener - Count is: " + unreadCount);

            updateHelpButtonBages();
        }
    };

    private void updateHelpButtonBages() {
        int unreadHelpCount = Intercom.client().getUnreadConversationCount();
        Log.i("Intercom", "Unread Help Count: " + unreadHelpCount);

        if (unreadHelpCount == 0) {
            step_one_btn_help_badge.hide();
            step_two_btn_help_badge.hide();
            step_three_btn_help_badge.hide();
        } else {
            step_one_btn_help_badge.setText(String.valueOf(unreadHelpCount));
            step_one_btn_help_badge.show();

            step_two_btn_help_badge.setText(String.valueOf(unreadHelpCount));
            step_two_btn_help_badge.show();

            step_three_btn_help_badge.setText(String.valueOf(unreadHelpCount));
            step_three_btn_help_badge.show();
        }
    }

    public void getCurrentPrimeRate() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getCurrentPrimeRate", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String resultMain) {
                Log.v("TAG", resultMain);
                try {
                    JSONObject jsonObject = new JSONObject(resultMain);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject result = data.getJSONObject("result");
                        String primerate = result.getString("prime_rate");
                        primeRate = Double.parseDouble(primerate);
                    } else if (status.equalsIgnoreCase("false")) {
                        primeRate = 0.00;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.v("TAG", reason);
            }
        });
    }

    @Override
    protected void onDestroy() {
        flag = false;

        // Intercom
        Intercom.client().removeUnreadConversationCountListener(unreadConversationCountListener);

        super.onDestroy();
    }

    public void getAllMasters() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("getAllMasters", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v("TAG", result);
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    JSONArray jsonArrayPaymentFrequencyMaster = jsonObjectResult.getJSONArray("mortgage_payment_frequency");
                    JSONArray jsonArrayRateIncreaseMaster = jsonObjectResult.getJSONArray("user_notify_rate_increase_bps");
                    JSONArray jsonArrayCurrencyMaster = jsonObjectResult.getJSONArray("mortgage_currency");
                    JSONArray jsonArrayMaturityDaysMaster = jsonObjectResult.getJSONArray("user_notify_until_maturity_days");
                    JSONArray jsonArrayCurrentTermType = jsonObjectResult.getJSONArray("mortgage_current_term_type");
                    JSONArray jsonArrayCurrentTermRateType = jsonObjectResult.getJSONArray("mortgage_current_term_rate_type");
                    JSONArray jsonArrayCurrentTerm = jsonObjectResult.getJSONArray("mortgage_current_term");
                    JSONArray jsonArrayAmortizationTerm=jsonObjectResult.getJSONArray("m3_mortgage_amortization_period_unit");

                    /* Get Payment Frequency Master*/
                    for (int i = 0; i < jsonArrayPaymentFrequencyMaster.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayPaymentFrequencyMaster.getJSONObject(i);
                        arrayList.add(jsonObject1.getString("key"));
                        paymentFrequencyItems = arrayList.toArray(new CharSequence[arrayList.size()]);
                    }
                    arrayList.clear();

                    /* Get Rate Increase Master*/
                    for (int i = 0; i < jsonArrayRateIncreaseMaster.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayRateIncreaseMaster.getJSONObject(i);
                        arrayList.add(jsonObject1.getString("val"));
                        rateIncreaseItems = arrayList.toArray(new CharSequence[arrayList.size()]);
                    }

                    arrayList.clear();

                    /*Get Mortgage currency Masters*/
                    for (int i = 0; i < jsonArrayCurrencyMaster.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayCurrencyMaster.getJSONObject(i);
                        arrayList.add(jsonObject1.getString("val"));
                        currencyItems = arrayList.toArray(new CharSequence[arrayList.size()]);
                    }

                    arrayList.clear();
                    /* Get Current Term Type Masters*/
                    for (int i = 0; i < jsonArrayCurrentTermType.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayCurrentTermType.getJSONObject(i);
                        arrayList.add(jsonObject1.getString("val"));
                        MortgageTypeItems = arrayList.toArray(new CharSequence[arrayList.size()]);

                    }
                    arrayList.clear();
                    /* Get Current Term Rate Type*/
                    for (int i = 0; i < jsonArrayCurrentTermRateType.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayCurrentTermRateType.getJSONObject(i);
                        arrayList.add(jsonObject1.getString("val"));
                        MortgageStatus = arrayList.toArray(new CharSequence[arrayList.size()]);

                    }
                    arrayList.clear();
                    /* Get Notify Until Maturity Days Masters*/
                    for (int i = 0; i < jsonArrayMaturityDaysMaster.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayMaturityDaysMaster.getJSONObject(i);
                        arrayList.add(jsonObject1.getString("val"));
                        noifyMeItems = arrayList.toArray(new CharSequence[arrayList.size()]);

                    }
                    arrayList.clear();
                    /* Get Term in Year Masters*/
                    for (int i = 0; i < jsonArrayCurrentTerm.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayCurrentTerm.getJSONObject(i);
                        arrayList.add(jsonObject1.getString("val"));
                        CurrentTerm = arrayList.toArray(new CharSequence[arrayList.size()]);
                    }
                    arrayList.clear();

                    /* Get Amortization Terms Periods */
                    for (int i = 0; i < jsonArrayAmortizationTerm.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayAmortizationTerm.getJSONObject(i);
                        arrayList.add(jsonObject1.getString("val"));
                        periods = arrayList.toArray(new CharSequence[arrayList.size()]);
                    }
                    arrayList.clear();

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String error, String reason, String details) {


            }
        });
    }

    public void cameraBtnClick() {
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonConstants.mDebug) Log.v(TAG, "Select Photo");
                int hasWriteExternalStorage = ActivityCompat.checkSelfPermission(AddMortgageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && hasWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.GET_ACCOUNTS},
                            REQUEST_CODE_ASK_PERMISSIONS);
                } else if (hasWriteExternalStorage == PackageManager.PERMISSION_GRANTED) {
                    openImageFileChooser("Upload Image", "image");
                }

            }
        });
    }

    public void hideKeyboard(View view) {
        //  Hide Keyboard.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void getMortgageTypeRadioButtonClick() {
        mortgageType = ((RadioButton) findViewById(toggle.getCheckedRadioButtonId())).getText().toString();
        if (CommonConstants.mDebug) Log.v(TAG, mortgageType);

        toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.residential:
                        mortgageType = "Residential";
                        if (CommonConstants.mDebug) Log.v(TAG, mortgageType);
                        break;
                    case R.id.commercial:
                        mortgageType = "Commercial";
                        if (CommonConstants.mDebug) Log.v(TAG, mortgageType);
                        break;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    openImageFileChooser("Upload Image", "image");
                } else {
                    // Permission Denied
                    Toast.makeText(AddMortgageActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void initialiseUIComponent() {
        selectLenderTextview = (TextView) findViewById(R.id.selectLenderTextview);
        stepOneTextView = (Button) findViewById(R.id.stepOneTextView);
        stepTwoTextView = (Button) findViewById(R.id.stepTwoTextView);
        stepThreeTextView = (Button) findViewById(R.id.stepThreeTextView);
        stepOneLinearLayout = (LinearLayout) findViewById(R.id.stepOneLinearLayout);
        stepTwoLinearLayout = (LinearLayout) findViewById(R.id.stepTwoLinearLayout);
        stepThreeLinearLayout = (LinearLayout) findViewById(R.id.stepThreeLinearLayout);
        actualRateIncreaseTextView = (TextView) findViewById(R.id.actualRateIncreaseTextView);
        actualNotifyMeTextView = (TextView) findViewById(R.id.actualNotifyMeTextView);
        actaulCurrencyTextview = (TextView) findViewById(R.id.actaulCurrencyTextview);
        paymentFrequencyTwoTextview = (TextView) findViewById(R.id.paymentFrequencyTwoTextview);
        actualMaturityDateEditText = (TextView) findViewById(R.id.actualMaturityDateEditText);
        actualStartDateEditText = (TextView) findViewById(R.id.actualStartDateEditText);
        originalTextView = (TextView) findViewById(R.id.originalTextView);
        // Help Button
        Typeface font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        step_one_btn_help = (Button) findViewById(R.id.step_one_btn_help);
        step_one_btn_help.setTypeface(font);

        step_two_btn_help = (Button) findViewById(R.id.step_two_btn_help);
        step_two_btn_help.setTypeface(font);

        step_three_btn_help = (Button) findViewById(R.id.step_three_btn_help);
        step_three_btn_help.setTypeface(font);

        // Help Button Badge
        step_one_btn_help_badge = new BadgeView(this, step_one_btn_help);
        step_one_btn_help_badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);

        step_two_btn_help_badge = new BadgeView(this, step_two_btn_help);
        step_two_btn_help_badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);

        step_three_btn_help_badge = new BadgeView(this, step_three_btn_help);
        step_three_btn_help_badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);

        updateHelpButtonBages();

        step_one_btn_next = (Button) findViewById(R.id.step_one_btn_next);
        step_two_btn_next = (Button) findViewById(R.id.step_two_btn_next);
        step_two_btn_previous = (Button) findViewById(R.id.step_two_btn_previous);
        step_three_btn_previous = (Button) findViewById(R.id.step_three_btn_previous);
        actualTitleEditText = (EditText) findViewById(R.id.actualTitleEditText);
        postalCodeEditText = (EditText) findViewById(R.id.postalCodeEditText);
        totalMortgageEditText = (EditText) findViewById(R.id.totalMortgageEditText);
        currentAmortizedMortgageValueEditText = (EditText) findViewById(R.id.currentAmortizedMortgageValueEditText);
        AmortizationTermEditText = (EditText) findViewById(R.id.AmortizationTermEditText);
        AmortizationTermEditText.setFilters(new InputFilter[]{new InputFilterMinMax("1", "10000")});
        currentPaymentAmtEditText = (EditText) findViewById(R.id.currentPaymentAmtEditText);
        actualInterestRateEditText = (EditText) findViewById(R.id.actualInterestRateEditText);
        discountRateEditText = (EditText) findViewById(R.id.discountRateEditText);
        creditLineAmtEditText = (EditText) findViewById(R.id.creditLineAmtEditText);
        savingAmtPerMonthEditText = (EditText) findViewById(R.id.savingAmtPerMonthEditText);
        actualremainingTermEditText = (EditText) findViewById(R.id.actualremainingTermEditText);
        actualMpacEditText = (EditText) findViewById(R.id.actualMpacEditText);
        btn_save_monitor = (Button) findViewById(R.id.btn_save_monitor);
        lenderLogoImageview = (ImageView) findViewById(R.id.lenderLogoImageview);
        selectLenderTextview = (TextView) findViewById(R.id.selectLenderTextview);
        toggle = (RadioGroup) findViewById(R.id.toggle);
        currentTermNumberTextView = (TextView) findViewById(R.id.currentTermNumberTextView);
        mortgageTypeTextView = (TextView) findViewById(R.id.mortgageTypeTextView);
        mortgageStatusTextView = (TextView) findViewById(R.id.mortgageStatusTextView);
        btnCamera = (ImageView) findViewById(R.id.btnCamera);
        img_recycler_view = (RecyclerView) findViewById(R.id.img_recycler_view);
        maturityDateLayout = (RelativeLayout) findViewById(R.id.maturityDateLayout);
        takePictureTextView = (TextView) findViewById(R.id.takePictureTextView);

        //CR 4
        interesrRateTextView = (TextView) findViewById(R.id.interesrRateTextView);
        discountRateTextView = (TextView) findViewById(R.id.discountRateTextView);
        optionalRateDiscountTextView = (TextView) findViewById(R.id.optionalRateDiscountTextView);
        informationTextView = (TextView) findViewById(R.id.informationTextView);
        spinnerLayout = (RelativeLayout) findViewById(R.id.spinnerLayout);
        operatorTextView = (TextView) findViewById(R.id.operatorTextView);
        variableInterestRateTextView = (TextView) findViewById(R.id.variableInterestRateTextView);

        selectTermRelativeLayout = (RelativeLayout) findViewById(R.id.selectTermRelativeLayout);
        selectTypeRelativeLayout = (RelativeLayout) findViewById(R.id.selectTypeRelativeLayout);
        selectStatusRelativeLayout = (RelativeLayout) findViewById(R.id.selectStatusRelativeLayout);
    }


    public void selectPostalCode() {
        postalCodeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMortgageActivity.this, PostalCodeActivity.class);
                intent.putExtra("USDollars", actaulCurrencyTextview.getText().toString());
                startActivityForResult(intent, 1);
            }
        });
    }

    public void currentTermDropdownClick() {
        selectTermRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!actualStartDateEditText.getText().toString().equals("Select Date")) {
                    actualMaturityDateEditText.setText("Select Date");
                    actualStartDateEditText.setText("Select Date");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMortgageActivity.this);
                builder.setTitle("Select Term");
                builder.setItems(CurrentTerm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if (CommonConstants.mDebug) Log.v(TAG, CurrentTerm[item].toString());
                        currentTermNumberTextView.setText(CurrentTerm[item].toString());
                        if (CurrentTerm[item].toString().equalsIgnoreCase("6 Mth")) {
                            termsInMonth = "6";
                        }
                        if (CurrentTerm[item].toString().equalsIgnoreCase("1 Yr")) {
                            termsInMonth = "12";
                            termInYear = 1;
                        }
                        if (CurrentTerm[item].toString().equalsIgnoreCase("2 Yr")) {
                            termsInMonth = "24";
                            termInYear = 2;
                        }
                        if (CurrentTerm[item].toString().equalsIgnoreCase("3 Yr")) {
                            termsInMonth = "36";
                            termInYear = 3;
                        }
                        if (CurrentTerm[item].toString().equalsIgnoreCase("4 Yr")) {
                            termsInMonth = "48";
                            termInYear = 4;
                        }
                        if (CurrentTerm[item].toString().equalsIgnoreCase("5 Yr")) {
                            termsInMonth = "60";
                            termInYear = 5;
                        }
                        if (CurrentTerm[item].toString().equalsIgnoreCase("6 Yr")) {
                            termsInMonth = "72";
                            termInYear = 6;
                        }
                        if (CurrentTerm[item].toString().equalsIgnoreCase("7 Yr")) {
                            termsInMonth = "84";
                            termInYear = 7;
                        }
                        if (CurrentTerm[item].toString().equalsIgnoreCase("8 Yr")) {
                            termsInMonth = "96";
                            termInYear = 8;
                        }
                        if (CurrentTerm[item].toString().equalsIgnoreCase("9 Yr")) {
                            termsInMonth = "108";
                            termInYear = 9;
                        }
                        if (CurrentTerm[item].toString().equalsIgnoreCase("10 Yr")) {
                            termsInMonth = "120";
                            termInYear = 10;
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    public void stepOneDetails() {
        stepOneLinearLayout.setVisibility(View.VISIBLE);
        stepTwoLinearLayout.setVisibility(View.GONE);
        stepThreeLinearLayout.setVisibility(View.GONE);
        stepOneTextView.setBackgroundResource(R.drawable.steponesel);
        stepTwoTextView.setBackgroundResource(R.drawable.steptwounselectedstepthreeunselected);
        stepThreeTextView.setBackgroundResource(R.drawable.stepthreeunselected);
    }

    public void stepTwoDetails() {
        stepOneLinearLayout.setVisibility(View.GONE);
        stepTwoLinearLayout.setVisibility(View.VISIBLE);
        stepThreeLinearLayout.setVisibility(View.GONE);
        stepOneTextView.setBackgroundResource(R.drawable.oneunselectedtwoselected);
        stepTwoTextView.setBackgroundResource(R.drawable.steptwoselected);
        stepThreeTextView.setBackgroundResource(R.drawable.stepthreeunselected);
        stepTwoShow = true;
    }

    public void stepThreeDetails() {
        stepOneLinearLayout.setVisibility(View.GONE);
        stepTwoLinearLayout.setVisibility(View.GONE);
        stepThreeLinearLayout.setVisibility(View.VISIBLE);
        stepOneTextView.setBackgroundResource(R.drawable.oneunselectedthreeselected);
        stepTwoTextView.setBackgroundResource(R.drawable.steptwounselectedstepthreeselected);
        stepThreeTextView.setBackgroundResource(R.drawable.stepthreeselected);
        stepThreeShow = true;
    }

    public void rateIncreaseSpinnerSelect() {
        rateIncreaseRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMortgageActivity.this);
                builder.setTitle("Rate Increase");
                builder.setItems(rateIncreaseItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        actualRateIncreaseTextView.setText(rateIncreaseItems[item]);
                        if (rateIncreaseItems[item].toString().equalsIgnoreCase("0.10%")) {
                            rateIncrease = "10";
                            if (CommonConstants.mDebug) {
                                Log.v(TAG, rateIncrease);
                            }
                        }
                        if (rateIncreaseItems[item].toString().equalsIgnoreCase("0.15%")) {
                            rateIncrease = "15";
                            if (CommonConstants.mDebug) {
                                Log.v(TAG, rateIncrease);
                            }
                        }
                        if (rateIncreaseItems[item].toString().equalsIgnoreCase("0.20%")) {
                            rateIncrease = "20";
                            if (CommonConstants.mDebug) {
                                Log.v(TAG, rateIncrease);
                            }
                        }
                        if (rateIncreaseItems[item].toString().equalsIgnoreCase("0.25%")) {
                            rateIncrease = "25";
                            if (CommonConstants.mDebug) {
                                Log.v(TAG, rateIncrease);
                            }
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });
    }

    public void notifyMeSpinnerSelect() {
        notifyMeRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMortgageActivity.this);
                builder.setTitle("Notify Me");
                builder.setItems(noifyMeItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        actualNotifyMeTextView.setText(noifyMeItems[item]);
                        if (noifyMeItems[item].toString().equalsIgnoreCase("90 Days Until Maturity")) {
                            notifyMe = "90";
                            if (CommonConstants.mDebug) {
                                Log.v(TAG, notifyMe);
                            }
                        }
                        if (noifyMeItems[item].toString().equalsIgnoreCase("120 Days Until Maturity")) {
                            notifyMe = "120";
                            if (CommonConstants.mDebug) {
                                Log.v(TAG, notifyMe);
                            }
                        }
                        if (noifyMeItems[item].toString().equalsIgnoreCase("180 Days Until Maturity")) {
                            notifyMe = "180";
                            if (CommonConstants.mDebug) {
                                Log.v(TAG, notifyMe);
                            }
                        }

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void currencySpineerTextViewSelect() {
        selectCurrencyRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMortgageActivity.this);
                builder.setTitle("Select Currency");
                builder.setItems(currencyItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        actaulCurrencyTextview.setText(currencyItems[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void paymentFrequencySpinnerTextviewSelect() {
        selectFrequencyRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMortgageActivity.this);
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
    }

    private void selectPeriod() {
        selectPeriodRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMortgageActivity.this);
                builder.setTitle("Select Period");
                builder.setItems(periods, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        periodTextView.setText(periods[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void mortgageTypeDropdownClick() {
        selectTypeRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMortgageActivity.this);
                builder.setTitle("Select Type");
                builder.setItems(MortgageTypeItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        mortgageTypeTextView.setText(MortgageTypeItems[item]);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void mortgageStatusDropdownClick() {
        selectStatusRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMortgageActivity.this);
                builder.setTitle("Select Rate Type");
                builder.setItems(MortgageStatus, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        mortgageStatusTextView.setText(MortgageStatus[item]);
                        if (MortgageStatus[item].toString().equalsIgnoreCase("Variable")) {
                            actualInterestRateEditText.setText("");
                            operatorTextView.setText("");
                            variableInterestRateTextView.setText("");
                            changeUIForVariableRate();
                        } else if (MortgageStatus[item].toString().equalsIgnoreCase("Fixed")) {
                            actualInterestRateEditText.setText("");
                            discountRateEditText.setText("");
                            changeUIForFixedRate();
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    //CR4
    public void changeUIForVariableRate() {
        interesrRateTextView.setText("Prime Rate Adjustment");
        originalTextView.setVisibility(View.GONE);
        interesrRateTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        discountRateTextView.setText("Current Interest Rate");
        optionalRateDiscountTextView.setVisibility(View.GONE);
        informationTextView.setVisibility(View.VISIBLE);
        spinnerLayout.setVisibility(View.VISIBLE);
        discountRateEditText.setVisibility(View.GONE);
        variableInterestRateTextView.setVisibility(View.VISIBLE);
        informationPopUpClick();
        selectOperator();
    }

    public void changeUIForFixedRate() {
        interesrRateTextView.setText("Interest Rate");
        discountRateTextView.setText("Rate Discount");
        originalTextView.setVisibility(View.VISIBLE);
        optionalRateDiscountTextView.setVisibility(View.VISIBLE);
        informationTextView.setVisibility(View.GONE);
        spinnerLayout.setVisibility(View.GONE);
        variableInterestRateTextView.setVisibility(View.GONE);
        discountRateEditText.setVisibility(View.VISIBLE);
    }

    public void selectOperator() {
        spinnerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddMortgageActivity.this);
                builder.setTitle("Select Prime Rate Adjustment");
                builder.setItems(operator, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        operatorTextView.setText(operator[which].toString());
                        if (operator[which].toString().equals("+")) {

                            if (!actualInterestRateEditText.getText().toString().equals("")) {
                                interestOne = Double.parseDouble(actualInterestRateEditText.getText().toString());
                                double total = interestOne + primeRate;
                                // Round up to nearest 2 decimal digit.
                                total = total * 100;
                                total = Math.round(total);
                                total = total / 100;
                                String totalStr = String.valueOf(total);
                                variableInterestRateTextView.setText(String.valueOf(f.format(total)));

                            } else {
                                Log.v("TAG", "enter interest rate");
                            }
                        } else if (operator[which].toString().equals("-")) {

                            if (!actualInterestRateEditText.getText().toString().equals("")) {
                                interestOne = Double.parseDouble(actualInterestRateEditText.getText().toString());
                                double total = primeRate + (-interestOne);
                                // Round up to nearest 2 decimal digit.
                                total = total * 100;
                                total = Math.round(total);
                                total = total / 100;
                                String totalStr = String.valueOf(total);
                                variableInterestRateTextView.setText(String.valueOf(f.format(total)));
                            }
                        }
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void informationPopUpClick() {
        if (informationTextView.isShown()) {
            informationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (check()) {
                        Log.v("TAG", "");
                    }
                    actualInterestRateEditText.clearFocus();
                    double calculatedValue;
                    if (operatorTextView.getText().toString().equalsIgnoreCase("+")) {
                        calculatedValue = interestOne + primeRate;
                    } else {
                        calculatedValue = primeRate + (-interestOne);
                    }
                    // Round up to nearest 2 decimal digit.
                    calculatedValue = calculatedValue * 100;
                    calculatedValue = Math.round(calculatedValue);
                    calculatedValue = calculatedValue / 100;
                    if (operatorTextView.getText().toString().equalsIgnoreCase("") || actualInterestRateEditText.getText().toString().equalsIgnoreCase("")) {
                        ViewDialog.showPopUpPrimeRate(AddMortgageActivity.this, "-", String.valueOf(f.format(primeRate)) + " %", "-", "+/-");
                    } else {
                        double rate = Double.parseDouble(actualInterestRateEditText.getText().toString());
                        ViewDialog.showPopUpPrimeRate(AddMortgageActivity.this, String.valueOf(f.format(calculatedValue)) + "%", String.valueOf(f.format(primeRate)) + " %", String.valueOf(f.format(rate)) + " %", operatorTextView.getText().toString());
                    }
                }
            });
        }
    }


    private boolean check() {
        boolean bln;
        bln = !operatorTextView.getText().equals("") && !actualInterestRateEditText.getText().toString().equals("") && !variableInterestRateTextView.getText().equals("");
        return bln;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_mortgage, menu);
        menu.findItem(R.id.icon).setVisible(true);
        menu.findItem(R.id.icon).setEnabled(false);
        return true;
    }

    public boolean validationStepOne() {
        boolean bln = true;
        if (actualTitleEditText.getText().toString().equals("") && postalCodeEditText.getText().toString().equals("")
                && totalMortgageEditText.getText().toString().equals("") && currentAmortizedMortgageValueEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_information_in_all_fields, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualTitleEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.please_enter_property_name, Toast.LENGTH_SHORT);
            actualTitleEditText.requestFocus();
            bln = false;
        } else if (selectLenderTextview.getText().toString().equalsIgnoreCase("Select Lender") && selectLenderTextview.isShown()) {
            toastMessage.showToastMsg(R.string.please_select_lender, Toast.LENGTH_SHORT);
            bln = false;
        } else if (postalCodeEditText.getText().toString().equals("")) {
            postalCodeEditText.requestFocus();
            toastMessage.showToastMsg(R.string.please_enter_postal_code, Toast.LENGTH_SHORT);
            bln = false;
        } else if (totalMortgageEditText.getText().toString().equals("")) {
            totalMortgageEditText.requestFocus();
            toastMessage.showToastMsg(R.string.please_enter_total_mortgage, Toast.LENGTH_SHORT);
            bln = false;
        }
        return bln;
    }

    public boolean validationStepTwo() {
        boolean bln = true;
        Date date = new Date();
        int period=0;
        if (!AmortizationTermEditText.getText().toString().equalsIgnoreCase("")) {
           period = Integer.parseInt(AmortizationTermEditText.getText().toString());
        }
        if (AmortizationTermEditText.getText().toString().equals("") && currentPaymentAmtEditText.getText().toString().equals("") && actualInterestRateEditText.getText().toString().equals("")
                && discountRateEditText.getText().toString().equals("") && creditLineAmtEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_information_in_all_fields, Toast.LENGTH_SHORT);
            bln = false;
        } else if (AmortizationTermEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.please_enter_original_amortization, Toast.LENGTH_SHORT);
            AmortizationTermEditText.requestFocus();
            bln = false;
        } else if (periodTextView.getText().toString().equalsIgnoreCase("Years") && period > 50) {
            toastMessage.showToastMsg(R.string.please_enter_valid_amortization_value, Toast.LENGTH_SHORT);
            bln = false;
        } else if (periodTextView.getText().toString().equalsIgnoreCase("Months") && period > 600) {
            toastMessage.showToastMsg(R.string.please_enter_valid_amortization_value, Toast.LENGTH_SHORT);
            bln = false;
        } else if (currentPaymentAmtEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.please_enter_current_payment_amt, Toast.LENGTH_SHORT);
            currentPaymentAmtEditText.requestFocus();
            bln = false;
        } else if (paymentFrequencyTwoTextview.getText().toString().contains("Select Frequency")) {
            toastMessage.showToastMsg(R.string.please_select_frequency, Toast.LENGTH_SHORT);
            bln = false;
        } else if (currentTermNumberTextView.getText().toString().contains("Select Term") && mortgageTypeTextView.getText().toString().contains("Select Type") &&
                mortgageStatusTextView.getText().toString().contains("Select Type")) {
            toastMessage.showToastMsg(R.string.please_select_current_term_mortgage_details, Toast.LENGTH_SHORT);
            bln = false;
        } else if (currentTermNumberTextView.getText().toString().contains("Select Term")) {
            toastMessage.showToastMsg(R.string.please_select_current_term_mortgage_details, Toast.LENGTH_SHORT);
            bln = false;
        } else if (mortgageTypeTextView.getText().toString().contains("Select Type")) {
            toastMessage.showToastMsg(R.string.please_select_current_term_mortgage_details, Toast.LENGTH_SHORT);
            bln = false;
        } else if (mortgageStatusTextView.getText().toString().contains("Select Type")) {
            toastMessage.showToastMsg(R.string.please_select_current_term_mortgage_details, Toast.LENGTH_SHORT);
            bln = false;
        } else if (operatorTextView.isShown() && operatorTextView.getText().toString().equalsIgnoreCase("")) {
            toastMessage.showToastMsg(R.string.positive_negative_message, Toast.LENGTH_SHORT);
            bln = false;
        } else if (operatorTextView.isShown() && actualInterestRateEditText.getText().toString().equalsIgnoreCase("")) {
            toastMessage.showToastMsg(R.string.prime_rate_adjustment_error_message, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualInterestRateEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.please_enter_interest_rate, Toast.LENGTH_SHORT);
            actualInterestRateEditText.requestFocus();
            bln = false;
        } else if (actualStartDateEditText.getText().toString().contains("Select Date")) {
            toastMessage.showToastMsg(R.string.please_select_date, Toast.LENGTH_SHORT);
            bln = false;
        } else if (nextYear.before(date)) {
            toastMessage.showToastMsg(R.string.maturity_date_before, Toast.LENGTH_SHORT);
            actualMaturityDateEditText.setText("");
            actualStartDateEditText.setText("Select Date");
            bln = false;
        }

        return bln;
    }


    public boolean valiadationStepThree() {
        boolean bln = true;
        if (savingAmtPerMonthEditText.getText().toString().equals("") && actualremainingTermEditText.getText().toString().equals("")) {
            savingAmtPerMonthEditText.requestFocus();
            toastMessage.showToastMsg(R.string.enter_information_in_all_fields, Toast.LENGTH_SHORT);
            bln = false;
        } else if (savingAmtPerMonthEditText.getText().toString().equals("")) {
            savingAmtPerMonthEditText.requestFocus();
            toastMessage.showToastMsg(R.string.enter_saving_amt_per_month, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualremainingTermEditText.getText().toString().equals("")) {
            actualremainingTermEditText.requestFocus();
            toastMessage.showToastMsg(R.string.enter_saving_amt_over_remaining_term, Toast.LENGTH_SHORT);
            bln = false;
        }
        return bln;
    }

    public void setDateTimeField() {
        actualStartDateEditText.setOnClickListener(this);
        final Calendar newCalendar = Calendar.getInstance();
        Date date = new Date();
        final long datenew = date.getTime();
        arivalYear = newCalendar.get(Calendar.YEAR);
        arivalMonth = newCalendar.get(Calendar.MONTH);
        arivalDay = newCalendar.get(Calendar.DAY_OF_MONTH);

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int yearOne, int monthOfYearOne, int dayOfMonthOne) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(yearOne, monthOfYearOne, dayOfMonthOne);
                startTime = calendar.getTimeInMillis();
                if (CommonConstants.mDebug) {
                    Log.v(TAG, "" + startTime);
                }
                actualStartDateEditText.setText(dateFormatter.format(calendar.getTime()));
                if (termsInMonth.equalsIgnoreCase("6")) {
                    calendar.add(Calendar.MONTH, 6);
                } else {
                    calendar.add(Calendar.YEAR, termInYear);
                }
                nextYear = calendar.getTime();
                actualMaturityDateEditText.setText(dateFormatter.format(nextYear));
                maturityTime = calendar.getTimeInMillis();
                if (CommonConstants.mDebug) {
                    Log.v(TAG, "" + maturityTime);
                }
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMaxDate(datenew);
    }

    @Override
    public void onClick(View v) {
        if (v == actualStartDateEditText) {
            fromDatePickerDialog.show();
            actualInterestRateEditText.clearFocus();
        } else if (v == actualMaturityDateEditText) {
            toDatePickerDialog.show();
        }
    }

    public void openImageFileChooser(String msg, String fileType) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "IMG_" + sdf.format(new Date()) + ".jpg";
        File myDirectory = new File(Environment.getExternalStorageDirectory() + "/M3/");
        if (!myDirectory.exists())
            myDirectory.mkdirs();
        File file = new File(myDirectory, fileName);
        fileUri = Uri.fromFile(file);

        Intent imageIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType(fileType + "/*");

        Intent chooserIntent = Intent.createChooser(pickIntent, msg);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{imageIntent});

        try {
            startActivityForResult(chooserIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } catch (Exception e) {
            // The reason for the existence of aFileChooser
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                try {
                    Uri imageUri = fileUri;
                    Bitmap scaledOriginalImage;
                    if (imageUri != null && (data == null || data.getData() == null)) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = 4;

                        AssetFileDescriptor fileDescriptor;

                        fileDescriptor = this.getContentResolver().openAssetFileDescriptor(imageUri, "r");

                        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                        int bitmapWidth = bitmap.getWidth();
                        if (CommonConstants.mDebug) Log.v(TAG, "bitmap width" + bitmapWidth);
                        if (bitmapWidth > 1024) {
                            //create scaled image from original image.Resize width to 1024.
                            scaledOriginalImage = Bitmap.createScaledBitmap(bitmap, 1024, bitmap.getHeight(), false);
                        } else {
                            scaledOriginalImage = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                        }
                        // create thumbnail of image/bitmap captured from camera.
                        Bitmap thumb = Bitmap.createScaledBitmap(scaledOriginalImage, 100, 100, false);
                        if (CommonConstants.mDebug) Log.v(TAG, "thumbWidth" + thumb.getWidth());
                        if (CommonConstants.mDebug) Log.v(TAG, "thumbHeight" + thumb.getHeight());
                        Bitmap rotatedImage = roatedBitmap(fileUri.getPath(), scaledOriginalImage);
                        final String path = FileUtils.getPath(this, imageUri);
                        String fileName = getFileNameByUri(this, imageUri);
                        File file = new File(path);
                        double fileLength = file.length();
                        double fileSizeKb = (fileLength / 1024);
                        double fileSizeMb = (fileSizeKb / 1024);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        scaledOriginalImage.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                        byte[] bitmapData = stream.toByteArray();


                        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                        thumb.compress(Bitmap.CompressFormat.JPEG, 50, stream1);
                        byte[] thumbData = stream1.toByteArray();

                        if (fileSizeMb <= 5) {
                            AttachmentFileData attachmentFileData = new AttachmentFileData();
                            attachmentFileData.setFileName(fileName);
                            attachmentFileData.setFileSize("" + Math.round(fileSizeKb));
                            attachmentFileData.setFileBase64String(Base64.encodeToString(bitmapData, Base64.DEFAULT));
                            attachmentFileData.setFileType(getMimeType(fileName));
                            attachmentFileData.setThumbNailBase64String(Base64.encodeToString(thumbData, Base64.DEFAULT));
                            fileDataList.add(attachmentFileData);
                            setupRecyclerView(img_recycler_view, fileDataList);

                        } else {
                            ViewDialog.showAlertPopUp(AddMortgageActivity.this, getResources().getString(R.string.file_upload_limit), getResources().getString(R.string.error));
                        }

                    } else if (data != null && data.getData() != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        if (CommonConstants.mDebug)
                            Log.i("MainActivityResult", "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            final String path = FileUtils.getPath(this, uri);
                            Bitmap rotatedImage;
                            Bitmap scaledOriginalImageLarge;
                            File file = new File(path);
                            String fileName = file.getName().replaceAll("\\s", "%20");
                            double fileLength = file.length();
                            double fileSizeKb = (fileLength / 1024);
                            double fileSizeMb = (fileSizeKb / 1024);
                            Bitmap bitmap = (BitmapFactory.decodeFile(path));
                            if (CommonConstants.mDebug)
                                Log.v("TAG", "bitmap width" + bitmap.getWidth());
                            if (bitmap.getWidth() > 1024) {
                                scaledOriginalImageLarge = Bitmap.createScaledBitmap(bitmap, 1024, bitmap.getHeight() / 3, false);
                                rotatedImage = roatedBitmap(path, scaledOriginalImageLarge);
                                if (CommonConstants.mDebug)
                                    Log.v("TAG", "rotatedImageWidth" + rotatedImage.getWidth());
                                if (CommonConstants.mDebug)
                                    Log.v("TAG", "rotatedImageHeight" + rotatedImage.getHeight());
                            } else {
                                scaledOriginalImageLarge = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false);
                                rotatedImage = roatedBitmap(path, scaledOriginalImageLarge);
                                if (CommonConstants.mDebug)
                                    Log.v("TAG", "rotatedImageWidth" + rotatedImage.getWidth());
                                if (CommonConstants.mDebug)
                                    Log.v("TAG", "rotatedImageHeight" + rotatedImage.getHeight());

                            }
                            Bitmap thumb = Bitmap.createScaledBitmap(scaledOriginalImageLarge, 100, 100, false);

                            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                            thumb.compress(Bitmap.CompressFormat.JPEG, 50, stream1);
                            byte[] thumbData = stream1.toByteArray();

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            if (getMimeType(fileName).contains("png") || getMimeType(fileName).contains("jpg") || getMimeType(fileName).contains("jpeg")) {
                                if (getMimeType(fileName).contains("png")) {
                                    rotatedImage.compress(Bitmap.CompressFormat.PNG, 50, stream);
                                } else {
                                    rotatedImage.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                                }
                                byte[] bitmapData2 = stream.toByteArray();

                                if (fileSizeMb <= 5) {
                                    AttachmentFileData attachmentFileData = new AttachmentFileData();
                                    attachmentFileData.setFileName(fileName);
                                    attachmentFileData.setFileSize("" + Math.round(fileSizeKb));
                                    attachmentFileData.setFileBase64String(Base64.encodeToString(bitmapData2, Base64.DEFAULT));
                                    attachmentFileData.setThumbNailBase64String(Base64.encodeToString(thumbData, Base64.DEFAULT));
                                    attachmentFileData.setFileType(getMimeType(fileName));
                                    fileDataList.add(attachmentFileData);
                                    setupRecyclerView(img_recycler_view, fileDataList);
                                } else {
                                    ViewDialog.showAlertPopUp(AddMortgageActivity.this, getResources().getString(R.string.file_upload_limit), getResources().getString(R.string.error));
                                }
                            } else {
                                ViewDialog.showAlertPopUp(AddMortgageActivity.this, getResources().getString(R.string.supported_file_formats), getResources().getString(R.string.error));
                            }
                        } catch (Exception e) {
                            if (CommonConstants.mDebug)
                                Log.e(TAG, "File select error" + e.getMessage());
                        }
                    } else {
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == PHOTO_PREVIEW_REQUEST_CODE) {
                if (data != null) {

                    Bundle bdata = data.getExtras();
                    fileDataList = (ArrayList<AttachmentFileData>) bdata.getSerializable(ATTACHMENTLISTKEY);
                    setupRecyclerView(img_recycler_view, fileDataList);
                }
            } else if (requestCode == 1) {
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String postalCode = data.getStringExtra("postal_code");
                        postalCodeEditText.setText(postalCode);
                    }
                }
            }
        }

    }

    //setup recycler view

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView, final ArrayList<AttachmentFileData> fileDataArrayList) {
        recyclerView.setVisibility(View.VISIBLE);
        fileAdapter = new PreviewAdapter(this, fileDataList);
        recyclerView.setAdapter(fileAdapter);
        fileAdapter.setCallBack(new PreviewAdapter.RecyclerViewListeners() {
            @Override
            public void onRecycleItemClick(ArrayList<AttachmentFileData> filesDataList, int position) {
                Intent previewIntent = new Intent(AddMortgageActivity.this, PhotoPreviewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putSerializable(ATTACHMENTLISTKEY, filesDataList);
                previewIntent.putExtras(bundle);
                startActivityForResult(previewIntent, PHOTO_PREVIEW_REQUEST_CODE);
            }

            @Override
            public void onCloseIconClick(ArrayList<AttachmentFileData> filesDataList, int position) {

                filesDataList.remove(position);
                fileAdapter.notifyDataSetChanged();
                if (filesDataList.size() >= 5) {
                    btnCamera.setVisibility(View.GONE);
                    takePictureTextView.setVisibility(View.GONE);
                } else if (filesDataList.size() == 0) {
                    btnCamera.setVisibility(View.VISIBLE);
                    takePictureTextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    btnCamera.setVisibility(View.VISIBLE);
                    takePictureTextView.setVisibility(View.VISIBLE);
                }
            }
        });
        if (fileDataArrayList.size() >= 5) {
            btnCamera.setVisibility(View.GONE);
            takePictureTextView.setVisibility(View.GONE);
        } else if (fileDataList.size() == 0) {
            btnCamera.setVisibility(View.VISIBLE);
            takePictureTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            btnCamera.setVisibility(View.VISIBLE);
            takePictureTextView.setVisibility(View.VISIBLE);
        }
    }

    public void showLenderList() {
        if (lenderLogoImageview.isEnabled()) {
            selectLenderRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp(lenderString, AddMortgageActivity.this);
                }
            });
        }
        if (selectLenderTextview.isEnabled()) {
            selectLenderRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopUp(lenderString, AddMortgageActivity.this);
                }
            });
        }
    }

    public void getLenderList() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getLenderList", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v(TAG, result);
                try {
                    lenderString = new ArrayList<Bitmap>();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            if (!jsonObject2.getString("lender_logo").equalsIgnoreCase("other")) {
                                ObjectItem objectItem = new ObjectItem();
                                objectItem.setLenderId(jsonObject2.getString("lender_id"));
                                objectItem.setLenderName(jsonObject2.getString("lender_name"));
                                String lenderStr = jsonObject2.getString("lender_logo");
                                String[] arrayString = lenderStr.split("\\,");
                                if (arrayString.length > 0) {
                                    lenderStr = arrayString[1];
                                }
                                lenderString.add(base64(lenderStr));
                                objectItem.setBase46String(lenderStr);
                                objectItemArrayList.add(objectItem);
                                ViewDialog.hideProgress();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                if (CommonConstants.mDebug) {
                    Log.v(TAG, reason);
                }

            }
        });
    }

    public Bitmap base64(String base64String) {

        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


    public void showPopUp(List<Bitmap> list, final Activity activity) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddMortgageActivity.this);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View myDialog = inflater.inflate(R.layout.lender_list, null);
        alertDialogBuilder.setView(myDialog);
        final ArrayAdapterItem adapter = new ArrayAdapterItem(activity, R.layout.list_view_row_item, list, objectItemArrayList);
        final ListView listView = (ListView) myDialog.findViewById(R.id.lenderListView);
        TextView closeButtonTextView = (TextView) myDialog.findViewById(R.id.closeButtonTextView);
        TextView otherTextView = (TextView) myDialog.findViewById(R.id.otherTextView);

        listView.setAdapter(adapter);
        final AlertDialog alertDialogShow = alertDialogBuilder.create();
        alertDialogShow.show();
        closeButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogShow.dismiss();
            }
        });

        otherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonConstants.mDebug) {
                    Log.v(TAG, "on click other");
                }
                alertDialogShow.dismiss();
                showOtherLenderPopup(activity, "Other Lender");
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectLenderTextview.setVisibility(View.GONE);
                lenderLogoImageview.setVisibility(View.VISIBLE);
                ObjectItem listItem = (ObjectItem) parent.getAdapter().getItem(position);
                lenderId = listItem.getLenderId();
                lenderName = listItem.getLenderName();
                if (CommonConstants.mDebug) {
                    Log.v(TAG, lenderId);
                    Log.v(TAG, lenderName);
                }
                lenderLogoImageview.setImageBitmap(listItem.getImageBitmap());
                postalCodeEditText.requestFocus();
                alertDialogShow.dismiss();
            }
        });
    }


    public void showOtherLenderPopup(Activity activity, String title) {
        try {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddMortgageActivity.this);
            LayoutInflater inflater = activity.getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.other_lender_popup, null);
            alertDialogBuilder.setView(myDialog);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            TextView closeButtonTextView = (TextView) myDialog.findViewById(R.id.closeButton);
            Button btn_dialog = (Button) myDialog.findViewById(R.id.btn_dialog);
            TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
            titleTextView.setText(title);
            otherLenderErrorTextView = (TextView) myDialog.findViewById(R.id.otherLenderErrorTextView);
            lenderEditText = (EditText) myDialog.findViewById(R.id.lenderEditText);

            btn_dialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    otherLenderName = lenderEditText.getText().toString();
                    if (otherLenderName.isEmpty()) {
                        otherLenderErrorTextView.setVisibility(View.VISIBLE);
                        otherLenderErrorTextView.setText("Please enter name of lender.");
                    } else {
                        lenderLogoImageview.setVisibility(View.GONE);
                        selectLenderTextview.setVisibility(View.VISIBLE);
                        selectLenderTextview.setText("Other");
                        lenderId = "1000";
                        if (CommonConstants.mDebug) {
                            Log.v(TAG, otherLenderName);
                        }
                        alertDialog.dismiss();
                    }
                }
            });
            new ViewDialog(this).textWatcher(lenderEditText, otherLenderErrorTextView);
            closeButtonTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addMortgage(Activity activity) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        if (lenderId == "1000") {
            stringObjectHashMap.put("lender_id", "1000");
            stringObjectHashMap.put("other_lender", otherLenderName);
        } else {
            stringObjectHashMap.put("lender_id", lenderId);
            stringObjectHashMap.put("other_lender", "");
        }
        stringObjectHashMap.put("title", actualTitleEditText.getText().toString());
        stringObjectHashMap.put("type", mortgageType);
        stringObjectHashMap.put("currency", actaulCurrencyTextview.getText().toString());
        stringObjectHashMap.put("property_postal_code", postalCodeEditText.getText().toString());
        stringObjectHashMap.put("original_amount", totalMortgageEditText.getText().toString().replace(",", ""));
        stringObjectHashMap.put("amortized_amount", currentAmortizedMortgageValueEditText.getText().toString().replace(",", ""));
//        stringObjectHashMap.put("amortization_term_in_months", AmortizationTermEditText.getText().toString());
        stringObjectHashMap.put("amortization_period",AmortizationTermEditText.getText().toString());
        stringObjectHashMap.put("amortization_period_unit",periodTextView.getText().toString());
        stringObjectHashMap.put("current_payment_amount", currentPaymentAmtEditText.getText().toString().replace(",", ""));
        stringObjectHashMap.put("current_payment_frequency", paymentFrequencyTwoTextview.getText().toString());
        stringObjectHashMap.put("interest_rate_percentage", actualInterestRateEditText.getText().toString());

        //CR 4 change
        if (mortgageStatusTextView.getText().toString().equalsIgnoreCase("fixed")) {
            stringObjectHashMap.put("prime_rate_plus_minus", "");
            stringObjectHashMap.put("prime_rate_adjustment_percentage", "");
        } else {
            stringObjectHashMap.put("prime_rate_plus_minus", operatorTextView.getText().toString());
            stringObjectHashMap.put("prime_rate_adjustment_percentage", actualInterestRateEditText.getText().toString());
        }
        //
        if (discountRateEditText.getText().toString().equalsIgnoreCase("")) {
            stringObjectHashMap.put("discount_rate_percentage", "0.00");
        } else {
            stringObjectHashMap.put("discount_rate_percentage", discountRateEditText.getText().toString());
        }
        stringObjectHashMap.put("start_date_utc", startTime);
        stringObjectHashMap.put("maturity_date_utc", maturityTime);
        stringObjectHashMap.put("current_term_in_months", termsInMonth);
        stringObjectHashMap.put("current_term_type", mortgageTypeTextView.getText().toString());
        stringObjectHashMap.put("current_term_rate_type", mortgageStatusTextView.getText().toString());
        stringObjectHashMap.put("credit_line_amount", creditLineAmtEditText.getText().toString().replace(",", ""));
        stringObjectHashMap.put("notify_saving_amount_per_month", savingAmtPerMonthEditText.getText().toString().replace(",", ""));
        stringObjectHashMap.put("notify_saving_amount_over_remaining_term", actualremainingTermEditText.getText().toString().replace(",", ""));

        stringObjectHashMap.put("notify_rate_increase_bps", rateIncrease);
        stringObjectHashMap.put("notify_until_maturity_days", notifyMe);
        stringObjectHashMap.put("mpac_number", actualMpacEditText.getText().toString());
        stringObjectHashMap.put("attached_documents", "");
        HashMap<String, Object> hashMap = new HashMap<>();
        if (facebookUserLoginFlag) {
            hashMap.put("user_id", facebookUserLoginId);
        } else if (googleUserLoginFlag) {
            hashMap.put("user_id", googleUserLoginId);
        } else {
            hashMap.put("user_id", username);
        }
        hashMap.put("lang", "en");
        hashMap.put("req_from", "android");
        hashMap.put("mortgage_input", stringObjectHashMap);

        ErisCollectionManager.getInstance().callMethod("saveMortgage", new Object[]{hashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v(TAG, result);

                try {
                    JSONObject jsonObjectResult = new JSONObject(result);
                    if (jsonObjectResult.has("status")) {

                        // Failed to add mortgage
                        if (jsonObjectResult.getString("status").equalsIgnoreCase("false")) {
                            JSONObject jsonObjectError = jsonObjectResult.getJSONObject("error");
                            String code = jsonObjectError.getString("code");
                            if (code.equals("10005")) {
                            } else {
                                toastMessage.showToastMsg(jsonObjectError.getString("message"), Toast.LENGTH_SHORT);
                                actualTitleEditText.requestFocus();
                                ViewDialog.hideProgress();
                            }
                        }

                        // Successfully added mortgage
                        else {
                            JSONObject jsonObject1 = jsonObjectResult.getJSONObject("data");
                            if (jsonObject1.getString("code").equalsIgnoreCase("15100")) {
                                JSONObject jsonObject2 = jsonObject1.getJSONObject("result");
                                mortgageID = jsonObject2.getString("mortgage_id");
                            }

                            // No files to upload
                            if (fileDataList.size() <= 0) {
                                if (CommonConstants.mDebug) Log.v(TAG, "No Attchment");
                                //finish();
                                ViewDialog.hideProgress();
                                ViewDialog.showProgress(R.string.help_screen_one, AddMortgageActivity.this, getResources().getString(R.string.progress_analyze_mortgage));

                                analyzeNowAPI();
                            }

                            // Upload files
                            else {
                                for (int i = 0; i < fileDataList.size(); i++) {
                                    ViewDialog.hideProgress();
                                    ViewDialog.showProgress(R.string.help_screen_one, AddMortgageActivity.this, "Uploading " + i + " of " + fileDataList.size());
                                    attachDocuments(mortgageID, i);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        // Actions to do after 2 seconds
                                        ViewDialog.hideProgress();
                                        ViewDialog.showProgress(R.string.help_screen_one, AddMortgageActivity.this, getResources().getString(R.string.progress_analyze_mortgage));
                                        analyzeNowAPI();
                                    }
                                }, 2000);
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                if (CommonConstants.mDebug) Log.v(TAG, reason);
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {

                    }

                    @Override
                    public void onDisconnect() {
                        ViewDialog.hideProgress();

                        showAlertPopUp(AddMortgageActivity.this, "App cannot perform current operation. Please retry when connected.", "No Network connection");

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


    private void analyzeNowAPI() {
        if (CommonConstants.mDebug) Log.v("TAG", "on Analyze btn click");
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("mortgage_id", mortgageID);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("analyzeMortgage", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                ViewDialog.hideProgress();
                if (CommonConstants.mDebug) Log.v("TAG", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        final JSONObject jsonObjectData = jsonObject.getJSONObject("data");

                        if (jsonObjectData.getString("code").equalsIgnoreCase("16108")) {
                            //alertDialog.dismiss();
                            ViewDialog.hideProgress();
                            showAlertPopUp(AddMortgageActivity.this, "Mortgage looks good at this time. We will keep you posted.", "Monitor My Mortgage");

                        } else {
                            Log.i("ANALYZE", jsonObjectData.toString());
                            final String title = actualTitleEditText.getText().toString();

                            LayoutInflater inflater = ((Activity) AddMortgageActivity.this).getLayoutInflater();
                            final View myDialog = inflater.inflate(R.layout.custom_dialog_oppr_available, null);
                            alertDialogBuilder.setView(myDialog);
                            alertDialogBuilder.setCancelable(false);
                            alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                            if (ViewDialog.kProgressHUD.isShowing()) {
                                ViewDialog.hideProgress();
                            }
                            TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
                            TextView text = (TextView) myDialog.findViewById(R.id.text_dialog);
                            TextView close = (TextView) myDialog.findViewById(R.id.closeButton);
                            close.setVisibility(View.INVISIBLE);

                            text.setText(getResources().getString(R.string.we_found_better_option_for_your_mortgage) + title + getResources().getString(R.string.mortgage));
                            titleTextView.setText(R.string.title_oppo_available_dialog);
                            Button viewLaterButton = (Button) myDialog.findViewById(R.id.btn_view_later);
                            Button viewNowButton = (Button) myDialog.findViewById(R.id.btn_view_now);

                            viewLaterButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });

                            viewNowButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        if (GlobalMethods.haveNetworkConnection(AddMortgageActivity.this) && MeteorSingleton.getInstance().isConnected()) {
                                            if (!MeteorSingleton.getInstance().isConnected())
                                                MeteorSingleton.getInstance().reconnect();
                                            if (jsonObjectData.getString("code").equalsIgnoreCase("16110")) {
                                                JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                                                if (jsonObjectResult.getString("opportunity_type").equalsIgnoreCase("Variable Rate")) {
                                                    String opportunityId = jsonObjectResult.getString("opportunity_id");
                                                    Intent intent = new Intent(AddMortgageActivity.this, OpprThreeVariableRateNotificationActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("opportunity_id", opportunityId);
                                                    intent.putExtra("activity_title", title);
                                                    startActivity(intent);
                                                } else if (jsonObjectResult.getString("opportunity_type").equalsIgnoreCase("Saving")) {
                                                    String opportunityId = jsonObjectResult.getString("opportunity_id");
                                                    Intent intent = new Intent(AddMortgageActivity.this, OpportunityOneSavingsActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("opportunity_id", opportunityId);
                                                    intent.putExtra("activity_title", title);
                                                    startActivity(intent);
                                                } else if (jsonObjectResult.getString("opportunity_type").equalsIgnoreCase("Approaching Maturity")) {
                                                    String opportunityId = jsonObjectResult.getString("opportunity_id");
                                                    Intent intent = new Intent(AddMortgageActivity.this, OpprTwoApporchingMaturityActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("opportunity_id", opportunityId);
                                                    intent.putExtra("activity_title", title);
                                                    startActivity(intent);
                                                } else if (jsonObjectResult.getString("opportunity_type").equalsIgnoreCase("Prime Rate Change")) {
                                                    String opportunityId = jsonObjectResult.getString("opportunity_id");
                                                    Intent intent = new Intent(AddMortgageActivity.this, OpprFourPrimeRateChangeActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("opportunity_id", opportunityId);
                                                    intent.putExtra("activity_title", title);
                                                    startActivity(intent);
                                                }
                                            } else if (jsonObjectData.getString("code").equalsIgnoreCase("16109")) {
                                                if (jsonObjectData.getString("message").equalsIgnoreCase("Opportunity already available")) {
                                                    if (opportunityNameAlreadyAvialble.equalsIgnoreCase("Variable Rate")) {
                                                        Intent intent = new Intent(AddMortgageActivity.this, OpprThreeVariableRateNotificationActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("opportunity_id", opportunityIdAlreadyAvailable);
                                                        intent.putExtra("activity_title", title);
                                                        startActivity(intent);
                                                    } else if (opportunityNameAlreadyAvialble.equalsIgnoreCase("Saving")) {
                                                        Intent intent = new Intent(AddMortgageActivity.this, OpportunityOneSavingsActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.putExtra("opportunity_id", opportunityIdAlreadyAvailable);
                                                        intent.putExtra("activity_title", title);
                                                        startActivity(intent);
                                                    } else if (opportunityNameAlreadyAvialble.equalsIgnoreCase("Approaching Maturity")) {
                                                        Intent intent = new Intent(AddMortgageActivity.this, OpprTwoApporchingMaturityActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.putExtra("opportunity_id", opportunityIdAlreadyAvailable);
                                                        intent.putExtra("activity_title", title);
                                                        startActivity(intent);
                                                    } else if (opportunityNameAlreadyAvialble.equalsIgnoreCase("Prime Rate Change")) {
                                                        Intent intent = new Intent(AddMortgageActivity.this, OpprFourPrimeRateChangeActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.putExtra("opportunity_id", opportunityIdAlreadyAvailable);
                                                        intent.putExtra("activity_title", title);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                            alertDialog.dismiss();
                                            finish();
                                        } else {
                                            alertDialog.dismiss();
                                            ViewDialog.showNetworkPopup(AddMortgageActivity.this, getResources().getString(R.string.app_cant_perform_operation), getResources().getString(R.string.no_network_connection));
                                            finish();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, final String reason, String details) {
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        if (value) {
                            analyzeNowAPI();
                        }
                    }

                    @Override
                    public void onDisconnect() {
                        ViewDialog.hideProgress();
                        Log.v("TAG", "onDisconnect");
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onException(Exception e) {
                        ViewDialog.hideProgress();
                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        if (status) {
                            analyzeNowAPI();
                        }
                    }
                });

            }
        });
    }

    public void onDisconnect() {
        ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
            @Override
            public void onConnect(boolean value) {
                getAllMasters();

                if (savingAmtPerMonthEditText.getText().toString().equalsIgnoreCase("")) {
                    getUser();
                }

                if (objectItemArrayList.size() <= 0) {
                    getLenderList();
                }

                getCurrentPrimeRate();
            }

            @Override
            public void onDisconnect() {
                ViewDialog.hideProgress();
                if (flag) {
                    if (!GlobalMethods.haveNetworkConnection(AddMortgageActivity.this) && !MeteorSingleton.getInstance().isConnected()) {
                        showAlertPopUp(AddMortgageActivity.this, "App cannot perform current operation. Please retry when connected.", "No Network connection");
                    }
                }
            }

            @Override
            public void onException(Exception e) {

            }

            @Override
            public void onInternetStatusChanged(boolean status) {
                getAllMasters();
                if (savingAmtPerMonthEditText.getText().toString().equalsIgnoreCase("")) {
                    getUser();
                }
                if (objectItemArrayList.size() <= 0) {
                    getLenderList();
                }
                getCurrentPrimeRate();
            }
        });
    }


    public void showAlertPopUp(Activity activity, final String msg, String title) {
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


    public static void showProgress(int title, Activity activity, String message) {

        KProgressHUD kProgressHUD = new KProgressHUD(activity);
        KProgressHUD.create(activity);
        kProgressHUD.setCancellable(false);
        kProgressHUD.setLabel(message);
        kProgressHUD.setAnimationSpeed(2);
        kProgressHUD.setDimAmount(0.5f);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        kProgressHUD.show();
    }

    public void attachDocuments(final String mortgageID, int counter) {
        if (CommonConstants.mDebug) Log.v(TAG, mortgageID);
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("mortgage_id", mortgageID);
        stringObjectHashMap.put("thumbnail_data", fileDataList.get(counter).getThumbNailBase64StringWithType());
        stringObjectHashMap.put("large_data", fileDataList.get(counter).getFileBase64StringWithType());
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("doc_id", "");
        stringObjectHashMap.put("doc_number", counter + 1);
        Log.v(TAG, "counter " + counter);
        stringObjectHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("saveMortgageAttachment", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v(TAG, result);
//                Intent intent = new Intent(AddMortgageActivity.this, MainActivity.class);
//                intent.putExtra("mortgageID", mortgageID);
//                startActivityForResult(intent, 1888);
                //finish();

//                // Wait 2 seconds, then analyze
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        Log.i("SERVER_RET", "Analyze2");
//                        analyzeNowAPI();
//                    }
//                }, 15000);
            }

            @Override
            public void onError(String error, String reason, String details) {
                if (CommonConstants.mDebug) Log.v(TAG, reason);
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {

                    }

                    @Override
                    public void onDisconnect() {
                        ViewDialog.hideProgress();
                        showAlertPopUp(AddMortgageActivity.this, "App cannot perform current operation. Please retry when connected.", "No Network connection");
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

    public void getUser() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        if (facebookUserLoginFlag) {
            stringObjectHashMap.put("userId", facebookUserLoginId);
        } else if (googleUserLoginFlag) {
            stringObjectHashMap.put("userId", googleUserLoginId);
        } else {
            stringObjectHashMap.put("userId", username);
        }
        ErisCollectionManager.getInstance().callMethod("getUser", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jsonRootObject = new JSONObject(s);
                    Log.v("TAG user", s);
                    if (jsonRootObject.getString("status").equals("true")) {
                        JSONObject jsonObjectData = jsonRootObject.getJSONObject("data");
                        JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                        if (!jsonObjectResult.getString("notify_saving_per_month").contains(".")) {
                            savingAmtPerMonthEditText.setText(jsonObjectResult.getString("notify_saving_per_month") + ".00");
                        } else {
                            savingAmtPerMonthEditText.setText(jsonObjectResult.getString("notify_saving_per_month"));
                        }
                        if (!jsonObjectResult.getString("notify_saving_over_term").contains(".")) {
                            actualremainingTermEditText.setText(jsonObjectResult.getString("notify_saving_over_term") + ".00");
                        } else {
                            actualremainingTermEditText.setText(jsonObjectResult.getString("notify_saving_over_term"));
                        }
                        /* Check for "bps" text*/
                        if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("10")) {
                            actualRateIncreaseTextView.setText("0.10%");
                        } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("15")) {
                            actualRateIncreaseTextView.setText("0.15%");
                        } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("20")) {
                            actualRateIncreaseTextView.setText("0.20%");
                        } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("25")) {
                            actualRateIncreaseTextView.setText("0.25%");
                        }


                        if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("0.10%")) {
                            rateIncrease = "10";
                        }
                        if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("0.15%")) {
                            rateIncrease = "15";
                        }
                        if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("0.20%")) {
                            rateIncrease = "20";
                        }
                        if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("0.25%")) {
                            rateIncrease = "25";
                        }

                        /* Check for "Days Until Maturity" text*/
                        if (!jsonObjectResult.getString("notify_until_maturity").contains("Days Until Maturity")) {
                            actualNotifyMeTextView.setText(jsonObjectResult.getString("notify_until_maturity") + " Days Until Maturity");
                        } else {
                            actualNotifyMeTextView.setText(jsonObjectResult.getString("notify_until_maturity"));
                        }


                        if (jsonObjectResult.getString("notify_until_maturity").equalsIgnoreCase("90 Days Until Maturity")) {
                            notifyMe = "90";
                        }
                        if (jsonObjectResult.getString("notify_until_maturity").equalsIgnoreCase("120 Days Until Maturity")) {
                            notifyMe = "120";
                        }
                        if (jsonObjectResult.getString("notify_until_maturity").equalsIgnoreCase("180 Days Until Maturity")) {
                            notifyMe = "180";
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String s, String s1, String s2) {

            }
        });
    }


    public static String getFileNameByUri(Context context, Uri uri) {
        String fileName = "unknown";//default fileName
        Uri filePathUri = uri;
        if (uri.getScheme().toString().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
                filePathUri = Uri.parse(cursor.getString(column_index));
                fileName = filePathUri.getLastPathSegment().toString();
            }
        } else if (uri.getScheme().compareTo("file") == 0) {
            fileName = filePathUri.getLastPathSegment().toString();
        } else {
            fileName = fileName + "_" + filePathUri.getLastPathSegment();
        }
        return fileName;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension.toLowerCase());
            if (type == null) {
                type = extension;
            }
        }
        return type;
    }

    public Bitmap roatedBitmap(String filePath, Bitmap bitmap) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filePath);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        Matrix matrix = new Matrix();

        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

}


