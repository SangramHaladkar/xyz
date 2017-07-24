package com.app.monitormymortgage.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.app.monitormymortgage.Activity.PostalCodeActivity;
import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.MainActivity;
import com.app.monitormymortgage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class CreateNewAccountFragment extends BaseFragment {
    private static final String TAG = CreateNewAccountFragment.class.getSimpleName();
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText confirmEmail;
    EditText phoneNumber;
    EditText confirmPassword;
    EditText postalCode;
    CheckBox termsAndConditionCheckBox;
    TextView errorMessageTextView;
    Button submitButton;
    TextView errorHintTextView;
    TextView passwordStrengthErrorTextView;
    EditText passwordEditText;
    TextView termsAndConditionTextView;
    TextView privacyPolicyTextView;
    int color;
    ListView listView;
    String facebookId;
    private boolean flag;
    Context context = this.getActivity();
    ViewDialog viewDialog;
    Typeface font_helvetica;
    ToastMessage toastMessage;
    ArrayAdapter<String> dataAdapter = null;
    EditText verificationCodeRegistration;
    Button btn_verify;
    TextView closeButtonRegistration;
    String verificationCode;
    TextView errorMessageRegistrartionPopup;
    TextView mobileNumberTextViewRegistration;
    String statusCode;
    String verificationCodeJSON;
    TextView codeDidntReceive;
    ProgressDialog csprogress;
    public static boolean googleFlag = false;
    public static boolean facebookFlag = false;
    static final String PREF_USER_NAME = "username";
    String pattern;
    private String M3BaseSystemSuperBrokerAccountId = "";


    public CreateNewAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDialog = new ViewDialog(this.getActivity());
        toastMessage = new ToastMessage(CreateNewAccountFragment.this.getActivity());
        csprogress = new ProgressDialog(CreateNewAccountFragment.this.getActivity());
        String passwd = "aaZZa44@";
        pattern = "^.*(?=.{8,25})(?=.*\\d)(?=.*[A-Z])(?=.*[a-zA-Z]).*$";
        boolean b = passwd.matches(pattern);
        if (CommonConstants.mDebug) Log.v("TAG", String.valueOf(b));
        superBrokerId();

        //Todo:Add if necessary.
        if (M3BaseSystemSuperBrokerAccountId.equalsIgnoreCase("")) {
            superBrokerId();
        }
    }

    public void superBrokerId() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("getM3BaseSystemSuperBrokerAccountId", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v(TAG + "superBrokerId", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    if (CommonConstants.mDebug) Log.v(TAG, jsonObjectData.getString("message"));
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    M3BaseSystemSuperBrokerAccountId = jsonObjectResult.getString("superbroker_id");
                    Log.v(TAG, "SuperBrokerId - " + M3BaseSystemSuperBrokerAccountId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.v(TAG + "superBrokerId", reason);
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        Log.v(TAG, "OnReconnectMeteor onConnect");
                        superBrokerId();
                    }

                    @Override
                    public void onDisconnect() {
                        Log.v(TAG, "OnReconnectMeteor onDisconnect");
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.v(TAG, "OnReconnectMeteor onException");
                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        Log.v(TAG, "OnReconnectMeteor onInternetStatusChanged");
                        superBrokerId();
                    }
                });
            }

        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        (getActivity()).setTitle(R.string.create_my_account);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_new_account, container, false);
        initializeUIComponent(v);
        submitButtonClick();
        passwordEditText.addTextChangedListener(mTextEditorWatcher);
        flag = true;
        errorHintTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordStrengthErrorTextView.setVisibility(View.VISIBLE);
                if (flag) {
                    passwordStrengthErrorTextView.setVisibility(View.GONE);
                    flag = false;
                } else {
                    passwordStrengthErrorTextView.setVisibility(View.VISIBLE);
                    flag = true;
                }
            }
        });
        termsAndConditionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TermsAndConditionFragment termsAndConditionFragment = new TermsAndConditionFragment();
                FragmentTransaction fragmentManager = getActivity().getSupportFragmentManager().beginTransaction();

                fragmentManager.replace(R.id.container_create_new_account, termsAndConditionFragment).addToBackStack(termsAndConditionFragment.getClass().getName()).commit();
            }
        });
        privacyPolicyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrivacyPolicyFragment privacyPolicyFragment = new PrivacyPolicyFragment();
                FragmentTransaction fragmentManager = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.container_create_new_account, privacyPolicyFragment).addToBackStack(privacyPolicyFragment.getClass().getName()).commit();
            }
        });

        postalCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonConstants.mDebug) Log.v(TAG, "on postal code click");
                Intent intent = new Intent(getActivity(), PostalCodeActivity.class);
                intent.putExtra("USDollars", "");
                startActivityForResult(intent, 1);
            }
        });
        termsAndConditionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (termsAndConditionCheckBox.isChecked()) {
                    errorMessageTextView.setVisibility(View.INVISIBLE);
                } else
                    errorMessageTextView.setVisibility(View.VISIBLE);
            }
        });


        return v;
    }

    public void initializeUIComponent(View view) {
        context = this.getActivity();
        color = context.getResources().getColor(R.color.color_very_strong_error_message);
        firstName = (EditText) view.findViewById(R.id.firstNameEditText);
        lastName = (EditText) view.findViewById(R.id.lastNameEditText);
        email = (EditText) view.findViewById(R.id.emailEditText);
        confirmEmail = (EditText) view.findViewById(R.id.confirmEmailEditText);
        phoneNumber = (EditText) view.findViewById(R.id.phoneNumberEditText);
        passwordEditText = (EditText) view.findViewById(R.id.passwordEditText);
        passwordEditText.setTypeface(Typeface.DEFAULT);
        passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
        confirmPassword = (EditText) view.findViewById(R.id.confirmPasswordEditText);
        confirmPassword.setTypeface(Typeface.DEFAULT);
        confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
        postalCode = (EditText) view.findViewById(R.id.postalCodeEditText);
        termsAndConditionCheckBox = (CheckBox) view.findViewById(R.id.checkBox);
        termsAndConditionTextView = (TextView) view.findViewById(R.id.termsAndConditionTextView);
        privacyPolicyTextView = (TextView) view.findViewById(R.id.privacyPolicyTextView);
        listView = (ListView) view.findViewById(R.id.listView1);
        errorMessageTextView = (TextView) view.findViewById(R.id.errorMessage);
        errorMessageTextView.setVisibility(View.INVISIBLE);
        submitButton = (Button) view.findViewById(R.id.btn_submit);
        errorHintTextView = (TextView) view.findViewById(R.id.errorHintTextView);
        passwordStrengthErrorTextView = (TextView) view.findViewById(R.id.passwordErrorMessage);
        passwordStrengthErrorTextView.setVisibility(View.GONE);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        errorHintTextView.setTypeface(font);
    }


    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            if (passwordEditText.getText().toString().matches(CommonConstants.passwordSimplificationRegex)) {
                errorHintTextView.setText(R.string.fa_info_circle_strong);
                errorHintTextView.setTextColor(getResources().getColor(R.color.color_standard_green));
            } else {
                errorHintTextView.setText(R.string.fa_info_circle_week);
                errorHintTextView.setTextColor(getResources().getColor(R.color.color_broker_responce_pending));
            }
        }
    };


    public void submitButtonClick() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalMethods.haveNetworkConnection(CreateNewAccountFragment.this.getActivity())) {
                    if (validation(v)) {
                        if (!termsAndConditionCheckBox.isChecked()) {
                            errorMessageTextView.setVisibility(View.VISIBLE);
                        } else {
                            ViewDialog.showProgress(R.string.help_screen_one, CreateNewAccountFragment.this.getActivity(), getResources().getString(R.string.please_wait));
                            HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
                            stringObjectHashMap.put("type", "email");
                            stringObjectHashMap.put("value", email.getText().toString().trim());
                            stringObjectHashMap.put("email", email.getText().toString().trim());
                            stringObjectHashMap.put("phone", phoneNumber.getText().toString().trim());
                            ErisCollectionManager.getInstance().callMethod("isUserExist", new Object[]{stringObjectHashMap}, new ResultListener() {
                                @Override
                                public void onSuccess(String s) {
                                    if (CommonConstants.mDebug) Log.v(TAG, s);
                                    try {
                                        JSONObject jsonRootObject = new JSONObject(s);
                                        if (jsonRootObject.getString("status").equals("true")) {
                                            JSONObject jsonObject = jsonRootObject.getJSONObject("data");
                                            ViewDialog.hideProgress();
                                            ViewDialog.showAlertPopUp(CreateNewAccountFragment.this.getActivity(), jsonObject.getString("message"), getResources().getString(R.string.error));
                                        } else if (jsonRootObject.getString("status").equals("false")) {
                                            ViewDialog.hideProgress();
                                            sendSMS();
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
                    }
                } else {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                    ViewDialog.showAlertPopUp((Activity) context, context.getString(R.string.no_network_found), context.getString(R.string.error));
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public boolean validation(View view) {
        boolean bln = true;
        try {
            if (firstName.getText().toString().equals("") && lastName.getText().toString().equals("") && email.getText().toString().equals("") && confirmEmail.getText().toString().equals("")
                    && phoneNumber.getText().toString().equals("") && passwordEditText.getText().toString().equals("") && confirmPassword.getText().toString().equals("")
                    && postalCode.getText().toString().equals("")) {
                toastMessage.showToastMsg(R.string.enter_information_in_all_fields, Toast.LENGTH_SHORT);
                bln = false;
            } else if (firstName.getText().toString().equals("")) {
                firstName.requestFocus();
                toastMessage.showToastMsg(R.string.enter_first_name, Toast.LENGTH_SHORT);
                bln = false;
            } else if (lastName.getText().toString().equals("")) {
                lastName.requestFocus();
                toastMessage.showToastMsg(R.string.enter_last_name, Toast.LENGTH_SHORT);
                bln = false;
            } else if (email.getText().toString().equals("")) {
                email.requestFocus();
                toastMessage.showToastMsg(R.string.enter_email, Toast.LENGTH_SHORT);
                bln = false;
            } else if (!GlobalMethods.isValidEmailId(email.getText().toString())) {
                email.requestFocus();
                toastMessage.showToastMsg(R.string.enter_valid_email, Toast.LENGTH_SHORT);
                bln = false;
            } else if (confirmEmail.getText().toString().equals("")) {
                confirmEmail.requestFocus();
                toastMessage.showToastMsg(R.string.entered_email_address_do_not_match, Toast.LENGTH_SHORT);
                bln = false;
            } else if (!GlobalMethods.isValidEmailId(confirmEmail.getText().toString())) {
                confirmEmail.requestFocus();
                toastMessage.showToastMsg(R.string.entered_email_address_do_not_match, Toast.LENGTH_SHORT);
                bln = false;
            } else if (!email.getText().toString().equals(confirmEmail.getText().toString())) {
                confirmEmail.requestFocus();
                toastMessage.showToastMsg(R.string.entered_email_address_do_not_match, Toast.LENGTH_SHORT);
                bln = false;
            } else if (phoneNumber.getText().toString().equals("")) {
                phoneNumber.requestFocus();
                toastMessage.showToastMsg(R.string.enter_phone, Toast.LENGTH_SHORT);
                bln = false;
            } else if (phoneNumber.getText().toString().length() < 10) {
                phoneNumber.requestFocus();
                toastMessage.showToastMsg(R.string.enter_valid_phone_number, Toast.LENGTH_SHORT);
                bln = false;
            } else if (passwordEditText.getText().toString().equals("")) {
                passwordEditText.requestFocus();
                toastMessage.showToastMsg(R.string.please_enter_password, Toast.LENGTH_SHORT);
                bln = false;
            } else if (!passwordEditText.getText().toString().matches(CommonConstants.passwordSimplificationRegex)) {
                passwordEditText.requestFocus();
                toastMessage.showToastMsg(R.string.password_hint_error_message, Toast.LENGTH_SHORT);
                bln = false;
            } else if (passwordEditText.getText().toString().length() < 8) {
                passwordEditText.requestFocus();
                toastMessage.showToastMsg(R.string.password_eight_char_long, Toast.LENGTH_SHORT);
                bln = false;
            } else if (confirmPassword.getText().toString().equals("")) {
                confirmPassword.requestFocus();
                toastMessage.showToastMsg(R.string.password_do_not_match, Toast.LENGTH_SHORT);
                bln = false;
            } else if (!passwordEditText.getText().toString().equals(confirmPassword.getText().toString())) {
                confirmPassword.requestFocus();
                toastMessage.showToastMsg(R.string.password_do_not_match, Toast.LENGTH_SHORT);
                bln = false;
            } else if (postalCode.getText().toString().equals("")) {
                postalCode.requestFocus();
                toastMessage.showToastMsg(R.string.enter_postal_code, Toast.LENGTH_SHORT);
                bln = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bln;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Get selected postal code data. */
        if (requestCode == 1) {
            if (data != null) {
                String strPostalCode = data.getStringExtra("postal_code");
                postalCode.setText(strPostalCode);
            }
        }
    }


    public void alertPopupRegistration(Context context, int title) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.custom_dialog_verify_registration, null);
            alertDialogBuilder.setView(myDialog);
            final AlertDialog alertDialogRegistrationPhoneNumber = alertDialogBuilder.create();
            alertDialogRegistrationPhoneNumber.show();
            alertDialogRegistrationPhoneNumber.setCancelable(false);
            btn_verify = (Button) myDialog.findViewById(R.id.btn_verify);
            verificationCodeRegistration = (EditText) myDialog.findViewById(R.id.verificationCodeRegistration);
            closeButtonRegistration = (TextView) myDialog.findViewById(R.id.closeButtonRegistration);
            errorMessageRegistrartionPopup = (TextView) myDialog.findViewById(R.id.errorMessageTextViewRegistration);
            mobileNumberTextViewRegistration = (TextView) myDialog.findViewById(R.id.mobileNumberTextViewRegistration);
            mobileNumberTextViewRegistration.setText(phoneNumber.getText().toString());
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
            closeButtonRegistration.setTypeface(font);
            codeDidntReceive = (TextView) myDialog.findViewById(R.id.codeDidntReceive);
            String htmlString = getResources().getString(R.string.didnot_receive) + " <font color='#337ab7'><u> " + getResources().getString(R.string.resend_code) + "</u></font>";
            codeDidntReceive.setText(Html.fromHtml(htmlString));
            TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
            titleTextView.setText(title);
            closeButtonRegistration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogRegistrationPhoneNumber.dismiss();
                }
            });
            codeDidntReceive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GlobalMethods.haveNetworkConnection(CreateNewAccountFragment.this.getActivity())) {
                        csprogress.setMessage("Sending SMS");
                        csprogress.show();
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                if (!MeteorSingleton.getInstance().isConnected()) {
                                    MeteorSingleton.getInstance().reconnect();
                                } else {
                                    resendSMS();
                                }
                                csprogress.dismiss();
                            }
                        }, 3000);
                    } else {
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                        ViewDialog.showAlertPopUp(CreateNewAccountFragment.this.getActivity(), getResources().getString(R.string.no_network_found), getResources().getString(R.string.error));
                    }
                }
            });
            verificationCodeRegistration.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    errorMessageRegistrartionPopup.setVisibility(View.INVISIBLE);
                }
            });
            btn_verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewDialog.showProgress(R.string.help_screen_one, CreateNewAccountFragment.this.getActivity(), getResources().getString(R.string.please_wait));
                    verificationCode = verificationCodeRegistration.getText().toString().trim();
                    if (verificationCode.isEmpty()) {
                        errorMessageRegistrartionPopup.setVisibility(View.VISIBLE);
                        errorMessageRegistrartionPopup.setText(R.string.enter_verification_code);
                        if (ViewDialog.kProgressHUD.isShowing()) {
                            ViewDialog.hideProgress();
                        }
                    }
//                    else if (!verificationCodeJSON.equals(verificationCode)) {
//                        errorMessageRegistrartionPopup.setVisibility(View.VISIBLE);
//                        errorMessageRegistrartionPopup.setText(R.string.enter_valid_verification_code);
//                    }
                    else {
                        Boolean meteorConnectionStatus = MeteorSingleton.getInstance().isConnected();
                        if (CommonConstants.mDebug) Log.v(TAG, meteorConnectionStatus.toString());
                        Boolean strLoggedIn = MeteorSingleton.getInstance().isLoggedIn();
                        if (CommonConstants.mDebug) Log.v("Login", strLoggedIn.toString());
                        if (MeteorSingleton.getInstance().isConnected()) {
//                            HashMap<String, Object> values = new HashMap<>();
//                            values.put("firstname", firstName.getText().toString().trim());
//                            values.put("lastname", lastName.getText().toString().trim());
//                            values.put("phone", phoneNumber.getText().toString().trim());
//                            values.put("postal_code", postalCode.getText().toString().trim());
//                            values.put("linked_with_superbroker_id", M3BaseSystemSuperBrokerAccountId);
//                            values.put("is_active", "1");
//                            values.put("profile_type", "email");
//                            values.put("role", 2);
//                            values.put("oppr_notification_pref", CommonConstants.opprtuNotificationPref);
//                            values.put("contact_pref", CommonConstants.contactPref);
//                            values.put("notify_saving_per_month", CommonConstants.notifySavingPerMonth);
//                            values.put("notify_saving_over_term", CommonConstants.notifySavingOverTerm);
//                            values.put("notify_rate_increase", CommonConstants.notifyRateIncrease);
//                            values.put("notify_until_maturity", CommonConstants.notifyUntilMaturity);

                            HashMap<String, Object> userDataHashMap = new HashMap<>();
                            userDataHashMap.put("email", email.getText().toString().trim());
                            userDataHashMap.put("password", passwordEditText.getText().toString().trim());
                            userDataHashMap.put("firstname", firstName.getText().toString().trim());
                            userDataHashMap.put("lastname", lastName.getText().toString().trim());
                            userDataHashMap.put("phone", phoneNumber.getText().toString().trim());
                            userDataHashMap.put("phone_country_code", CommonConstants.countryCode);
                            userDataHashMap.put("postal_code", postalCode.getText().toString().trim());

                            HashMap<String, Object> mainUserDataHashMap = new HashMap<>();
                            mainUserDataHashMap.put("lang", "en");
                            mainUserDataHashMap.put("req_from", CommonConstants.requestFrom);
                            mainUserDataHashMap.put("verification_code", verificationCodeRegistration.getText().toString().trim());
                            mainUserDataHashMap.put("user_data", userDataHashMap);

                            ErisCollectionManager.getInstance().callMethod("verifyAndCreateConsumerAccount", new Object[]{mainUserDataHashMap}, new ResultListener() {
                                @Override
                                public void onSuccess(String result) {
                                    if (ViewDialog.kProgressHUD.isShowing()) {
                                        ViewDialog.hideProgress();
                                    }
                                    Log.i("TAG Reg success", result);
                                    String status = "";
                                    try {
                                        JSONObject jsonObject = new JSONObject(result);
                                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                        JSONObject jsonObjectError = jsonObject.getJSONObject("error");
                                        if (jsonObject.has("status")) {
                                            status = jsonObject.getString("status");
                                        }

                                        if (status.equalsIgnoreCase("true")) {
                                            loginWIthPassword(email.getText().toString().trim(), passwordEditText.getText().toString().trim());
                                            alertDialogRegistrationPhoneNumber.dismiss();
                                        } else if (status.equalsIgnoreCase("false")) {
                                            String errorMessage = jsonObjectError.getString("message");
                                            errorMessageRegistrartionPopup.setVisibility(View.VISIBLE);
                                            errorMessageRegistrartionPopup.setText(errorMessage);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(String error, String reason, String details) {
                                    Log.i("TAG Reg error", reason);
                                    if (ViewDialog.kProgressHUD.isShowing()) {
                                        ViewDialog.hideProgress();
                                    }
                                }
                            });

//                    if(LoginActivity.facebookFlag==true) {
//                            MeteorSingleton.getInstance().registerAndLogin(phoneNumber.getText().toString().trim(), email.getText().toString().trim(), passwordEditText.getText().toString().trim(), values, new ResultListener() {
//                                @Override
//                                public void onSuccess(String s) {
//                                    toastMessage.showToastMsg(R.string.user_created_successfully, Toast.LENGTH_SHORT);
//                                    googleFlag = false;
//                                    facebookFlag = false;
//                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("FacebookUserLoginFlag", facebookFlag).apply();
//                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("GoogleUserLoginFlag", googleFlag).apply();
//                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(PREF_USER_NAME, MeteorSingleton.getInstance().getUserId()).apply();
//                                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                                    startActivity(intent);
//                                    getActivity().finish();
//                                    alertDialogRegistrationPhoneNumber.dismiss();
//
//                                }
//
//                                @Override
//                                public void onError(String s, String s1, String s2) {
//
//                                    if (s1.equalsIgnoreCase("username already exists")) {
//                                        toastMessage.showToastMsg(R.string.email_phone_already_exist, Toast.LENGTH_SHORT);
//                                    }
//                                }
//                            });

                        } else {
                            viewDialog.showAlertPopUp(getActivity(), getResources().getString(R.string.error_connecting_server), getResources().getString(R.string.error));
                        }
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendSMS() {
        HashMap<String, Object> listOfObject = new HashMap<String, Object>();
        listOfObject.put("phone_number", CommonConstants.countryCode + phoneNumber.getText().toString().trim());
        listOfObject.put("verification_type", "registration");
        listOfObject.put("lang", "en");
        listOfObject.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("send_verification_sms", new Object[]{listOfObject}, new ResultListener() {
            @Override
            public void onSuccess(String jsonResponse) {
                if (CommonConstants.mDebug) Log.v("TAG", jsonResponse);
                try {
                    JSONObject jsonRootObject = new JSONObject(jsonResponse);
                    statusCode = jsonRootObject.getString("status");
                    if (statusCode.equalsIgnoreCase("true")) {
                        alertPopupRegistration(context, R.string.title_phone_verification_registrartion);
                    } else if (statusCode.equalsIgnoreCase("false")) {
                        if (CommonConstants.mDebug) Log.v(TAG, "phone already exists");
                        ViewDialog.showAlertPopUp(CreateNewAccountFragment.this.getActivity(), getResources().getString(R.string.email_phone_exist), getResources().getString(R.string.error));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String errorCode, String error, String reason) {
                if (CommonConstants.mDebug) Log.v("TAG", error);
            }
        });

    }

    public void resendSMS() {
        HashMap<String, Object> listOfObject = new HashMap<String, Object>();
        listOfObject.put("phone_number", CommonConstants.countryCode + phoneNumber.getText().toString().trim());
        listOfObject.put("verification_type", "registration");
        listOfObject.put("lang", "en");
        listOfObject.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("send_verification_sms", new Object[]{listOfObject}, new ResultListener() {
            @Override
            public void onSuccess(String jsonResponse) {
                Log.i(TAG, "Code Resend Successfully");
            }

            @Override
            public void onError(String s, String s1, String s2) {
                Log.i(TAG, "onError" + s1);
            }
        });
    }

    public void loginWIthPassword(final String email, String password) {
        try {
            ErisCollectionManager.getInstance().loginWithPassword(email, password, new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    // Intercom
                    CommonConstants.registerIntercom(MeteorSingleton.getInstance().getUserId());

                    toastMessage.showToastMsg(R.string.user_created_successfully, Toast.LENGTH_SHORT);
                    googleFlag = false;
                    facebookFlag = false;
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("FacebookUserLoginFlag", facebookFlag).apply();
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("GoogleUserLoginFlag", googleFlag).apply();
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(PREF_USER_NAME, MeteorSingleton.getInstance().getUserId()).apply();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();

                    Intent i=new Intent("finish_acivity");
                    getActivity().sendBroadcast(i);

                }

                @Override
                public void onError(String error, String reason, String details) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
