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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
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


public class FacebookRegisterFragment extends BaseFragment {
    private static final String TAG = FacebookRegisterFragment.class.getSimpleName();
    TextView termsAndConditionTextView;
    TextView privacyPolicyTextView;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText phoneNumberEditText;
    EditText postalCodeEditText;
    CheckBox checkBox;
    TextView errorMessage;
    String facebookId;
    String accessTokan;
    Button btn_submit;
    Context context;
    ViewDialog viewDialog;
    ToastMessage toastMessage;
    String statusCode;
    String verificationCodeJSON;
    Button btn_verify;
    EditText verificationCodeRegistration;
    TextView closeButtonRegistration;
    String verificationCode;
    TextView errorMessageRegistrartionPopup;
    TextView mobileNumberTextViewRegistration;
    TextView codeDidntReceive;
    ProgressDialog csprogress;
    public static String userid;
    public static boolean facebookFlag = false;
    public static String facebookUserLoginId;
    ArrayAdapter<String> dataAdapter = null;
    ListView listView;
    private String M3BaseSystemSuperBrokerAccountId = "";


    public FacebookRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = FacebookRegisterFragment.this.getActivity();
        viewDialog = new ViewDialog(this.getActivity());
        toastMessage = new ToastMessage(FacebookRegisterFragment.this.getActivity());
        csprogress = new ProgressDialog(FacebookRegisterFragment.this.getActivity());
        getSuperBrokerId();
        //Todo:Add if necessary.
        if (M3BaseSystemSuperBrokerAccountId.equalsIgnoreCase("")) {
            getSuperBrokerId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        (getActivity()).setTitle(R.string.register_with_facebook);
        View view = inflater.inflate(R.layout.fragment_facebook_register, container, false);
        initializeUIComponent(view);

        submitButtonClick();
        selectPostalCode();
        facebookId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("FacebookId", "");
        if (CommonConstants.mDebug) Log.v(TAG, "Facebook Id" + facebookId);
        accessTokan = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("AccessTokan", "");
        if (CommonConstants.mDebug) Log.v(TAG, "Facebook Access Tokan" + accessTokan);

        /*Check facebook user first name*/
        if (!PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("FacebookFirstName", "").equalsIgnoreCase("")) {
            firstNameEditText.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("FacebookFirstName", ""));
            firstNameEditText.setEnabled(false);
            firstNameEditText.setFocusable(false);
            firstNameEditText.setTextColor(ContextCompat.getColor(context, R.color.colorFaint));
        } else {
            firstNameEditText.setText("");
            firstNameEditText.setFocusable(true);
            this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        /*Check facebook user last name*/
        if (!PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("FacebookLastName", "").equalsIgnoreCase("")) {
            lastNameEditText.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("FacebookLastName", ""));
            lastNameEditText.setEnabled(false);
            lastNameEditText.setFocusable(false);
            lastNameEditText.setTextColor(ContextCompat.getColor(context, R.color.colorFaint));
        } else {
            lastNameEditText.setText("");
            lastNameEditText.setFocusable(true);
            this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        /*Check Facebook user Email*/
        if (!PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("FacebookEmail", "").equalsIgnoreCase("")) {
            emailEditText.setText(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("FacebookEmail", ""));
            emailEditText.setEnabled(false);
            emailEditText.setFocusable(false);

            emailEditText.setTextColor(ContextCompat.getColor(context, R.color.colorFaint));
        } else {
            emailEditText.setText("");
            emailEditText.setFocusable(true);
            this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }


        termsAndConditionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermsAndConditionFragment termsAndConditionFragment = new TermsAndConditionFragment();
                FragmentTransaction fragmentManager = getActivity().getSupportFragmentManager().beginTransaction();

                fragmentManager.replace(R.id.container_facebook, termsAndConditionFragment).addToBackStack(termsAndConditionFragment.getClass().getName()).commit();

//                Intent intent = new Intent(getActivity(), TermsAndConditionFragment.class);
//                getActivity().startActivity(intent);
            }
        });

        privacyPolicyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrivacyPolicyFragment privacyPolicyFragment = new PrivacyPolicyFragment();
                FragmentTransaction fragmentManager = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentManager.replace(R.id.container_facebook, privacyPolicyFragment).addToBackStack(privacyPolicyFragment.getClass().getName()).commit();
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    errorMessage.setVisibility(View.INVISIBLE);
                } else
                    errorMessage.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    public void getSuperBrokerId() {
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
                    if (CommonConstants.mDebug) Log.v(TAG, M3BaseSystemSuperBrokerAccountId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                if (CommonConstants.mDebug) Log.v(TAG + "superBrokerId", reason);
            }

        });
    }

    public void submitButtonClick() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation(v)) {
                    if (!checkBox.isChecked()) {
                        errorMessage.setVisibility(View.VISIBLE);
                    } else {
                        ViewDialog.showProgress(R.string.help_screen_one, FacebookRegisterFragment.this.getActivity(), getResources().getString(R.string.please_wait));
                        if (CommonConstants.mDebug) Log.v("TAG", "on success");
                        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
                        stringObjectHashMap.put("type", "facebook");
                        stringObjectHashMap.put("value", facebookId);
                        stringObjectHashMap.put("email", emailEditText.getText().toString().trim());
                        stringObjectHashMap.put("phone", phoneNumberEditText.getText().toString().trim());
                        ErisCollectionManager.getInstance().callMethod("isUserExist", new Object[]{stringObjectHashMap}, new ResultListener() {
                            @Override
                            public void onSuccess(String s) {
                                if (CommonConstants.mDebug) Log.v(TAG, s);
                                try {
                                    JSONObject jsonRootObject = new JSONObject(s);
                                    if (jsonRootObject.getString("status").equals("true")) {
                                        JSONObject jsonObject = jsonRootObject.getJSONObject("data");
                                        ViewDialog.showAlertPopUp(FacebookRegisterFragment.this.getActivity(), jsonObject.getString("message"), getResources().getString(R.string.error));
                                        ViewDialog.hideProgress();
//                                        if (jsonObject.getString("message").equals("User phone already exists")) {
//                                            ViewDialog.showAlertPopUp(FacebookRegisterFragment.this.getActivity(), "Entered Phone Number already exists.", "Error");
//                                        } else if (jsonObject.getString("message").equals("User email already exists")) {
//                                            ViewDialog.showAlertPopUp(FacebookRegisterFragment.this.getActivity(), "Entered Email already exists.", "Error");
//                                        }
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
                                if (CommonConstants.mDebug) Log.v(TAG, "onError" + s1);
                            }
                        });
                    }
                }
            }
        });
    }

    public void initializeUIComponent(View view) {
        termsAndConditionTextView = (TextView) view.findViewById(R.id.termsAndConditionTextView);
        privacyPolicyTextView = (TextView) view.findViewById(R.id.privacyPolicyTextView);
        firstNameEditText = (EditText) view.findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) view.findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) view.findViewById(R.id.emailEditText);
        phoneNumberEditText = (EditText) view.findViewById(R.id.phoneNumberEditText);
        postalCodeEditText = (EditText) view.findViewById(R.id.postalCodeEditText);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        errorMessage = (TextView) view.findViewById(R.id.errorMessage);
        errorMessage.setVisibility(View.INVISIBLE);
        btn_submit = (Button) view.findViewById(R.id.btn_submit);
        listView = (ListView) view.findViewById(R.id.listView1);
    }

    public void selectPostalCode() {
        postalCodeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonConstants.mDebug) Log.v(TAG, "on postal code click");
                Intent intent = new Intent(getActivity(), PostalCodeActivity.class);
                intent.putExtra("USDollars", "");
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* Get selected postal code data. */
        if (requestCode == 1) {
            if (data != null) {
                String strPostalCode = data.getStringExtra("postal_code");
                postalCodeEditText.setText(strPostalCode);
            }
        }
    }


    public boolean validation(View view) {
        boolean bln = true;
        try {
            if (firstNameEditText.getText().toString().equals("") && lastNameEditText.getText().toString().equals("") && emailEditText.getText().toString().equals("")
                    && phoneNumberEditText.getText().toString().equals("") && postalCodeEditText.getText().toString().equals("")) {
                toastMessage.showToastMsg(R.string.enter_information_in_all_fields, Toast.LENGTH_SHORT);
                bln = false;
            } else if (firstNameEditText.getText().toString().equals("")) {
                firstNameEditText.requestFocus();
                toastMessage.showToastMsg(R.string.enter_first_name, Toast.LENGTH_SHORT);
                bln = false;
            } else if (lastNameEditText.getText().toString().equals("")) {
                lastNameEditText.requestFocus();
                toastMessage.showToastMsg(R.string.enter_last_name, Toast.LENGTH_SHORT);
                bln = false;
            } else if (emailEditText.getText().toString().equals("")) {
                emailEditText.requestFocus();
                toastMessage.showToastMsg(R.string.enter_email, Toast.LENGTH_SHORT);
                bln = false;
            } else if (!GlobalMethods.isValidEmailId(emailEditText.getText().toString())) {
                emailEditText.requestFocus();
                toastMessage.showToastMsg(R.string.enter_valid_email, Toast.LENGTH_SHORT);
                bln = false;
            } else if (phoneNumberEditText.getText().toString().equals("")) {
                phoneNumberEditText.requestFocus();
                toastMessage.showToastMsg(R.string.enter_phone, Toast.LENGTH_SHORT);
                bln = false;
            } else if (phoneNumberEditText.getText().toString().length() < 10) {
                phoneNumberEditText.requestFocus();
                toastMessage.showToastMsg(R.string.enter_valid_phone_number, Toast.LENGTH_SHORT);
                bln = false;
            } else if (postalCodeEditText.getText().toString().equals("")) {
                postalCodeEditText.requestFocus();
                toastMessage.showToastMsg(R.string.enter_postal_code, Toast.LENGTH_SHORT);
                bln = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bln;
    }

    public void sendSMS() {
        HashMap<String, Object> listOfObject = new HashMap<String, Object>();
        listOfObject.put("lang", "en");
        listOfObject.put("req_from", CommonConstants.requestFrom);
        listOfObject.put("phone_number", CommonConstants.countryCode + phoneNumberEditText.getText().toString().trim());
        listOfObject.put("verification_type", "registration");
        ErisCollectionManager.getInstance().callMethod("send_verification_sms", new Object[]{listOfObject}, new ResultListener() {
            @Override
            public void onSuccess(String jsonResponse) {
                Log.v("TAG", jsonResponse);
                try {
                    JSONObject jsonRootObject = new JSONObject(jsonResponse);
                    statusCode = jsonRootObject.getString("status");
                    if (statusCode.equalsIgnoreCase("true")) {
                        alertPopupRegistration(context, R.string.title_phone_verification_registrartion);
                    } else if (statusCode.equalsIgnoreCase("false")) {
                        ViewDialog.showAlertPopUp(FacebookRegisterFragment.this.getActivity(), getResources().getString(R.string.email_phone_exist), getResources().getString(R.string.error));
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
            mobileNumberTextViewRegistration.setText(phoneNumberEditText.getText().toString());
            Typeface font = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
            closeButtonRegistration.setTypeface(font);
            codeDidntReceive = (TextView) myDialog.findViewById(R.id.codeDidntReceive);
            String htmlString = "Didn't receive? <font color='#337ab7'><u> Resend Code</u></font>";
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
                    csprogress.setMessage("Sending SMS");
                    csprogress.show();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            resendSMS();
                            csprogress.dismiss();
                        }
                    }, 3000);
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
                    verificationCode = verificationCodeRegistration.getText().toString().trim();
                    if (verificationCode.isEmpty()) {
                        errorMessageRegistrartionPopup.setVisibility(View.VISIBLE);
                        errorMessageRegistrartionPopup.setText(R.string.enter_verification_code);
                    }
//                    else if (!verificationCodeJSON.equals(verificationCode)) {
//                        errorMessageRegistrartionPopup.setVisibility(View.VISIBLE);
//                        errorMessageRegistrartionPopup.setText(R.string.enter_valid_verification_code);
//                    }
                    else {
                        ViewDialog.showProgress(R.string.help_screen_one, FacebookRegisterFragment.this.getActivity(), getResources().getString(R.string.please_wait));

                        Boolean meteorConnectionStatus = MeteorSingleton.getInstance().isConnected();
                        if (CommonConstants.mDebug) Log.v(TAG, meteorConnectionStatus.toString());
                        Boolean strLoggedIn = MeteorSingleton.getInstance().isLoggedIn();
                        if (CommonConstants.mDebug) Log.v("Login", strLoggedIn.toString());
                        if (MeteorSingleton.getInstance().isConnected()) {

                            HashMap<String, Object> values = new HashMap<>();
                            values.put("firstname", firstNameEditText.getText().toString().trim());
                            values.put("lastname", lastNameEditText.getText().toString().trim());
                            values.put("phone", phoneNumberEditText.getText().toString().trim());
                            values.put("phone_country_code", CommonConstants.countryCode);
                            values.put("postal_code", postalCodeEditText.getText().toString().trim());
                            values.put("email", emailEditText.getText().toString().trim());

//                            values.put("linked_with_superbroker_id", M3BaseSystemSuperBrokerAccountId);
//                            values.put("is_active", "1");
//                            values.put("profile_type", "facebook");
//                            values.put("role", 2);
//                            values.put("oppr_notification_pref", CommonConstants.opprtuNotificationPref);
//                            values.put("contact_pref", CommonConstants.contactPref);
//                            values.put("notify_saving_per_month", CommonConstants.notifySavingPerMonth);
//                            values.put("notify_saving_over_term", CommonConstants.notifySavingOverTerm);
//                            values.put("notify_rate_increase", CommonConstants.notifyRateIncrease);
//                            values.put("notify_until_maturity", CommonConstants.notifyUntilMaturity);

                            HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
                            stringObjectHashMap.put("lang", "en");
                            stringObjectHashMap.put("req_from", CommonConstants.requestFrom);
                            stringObjectHashMap.put("id", facebookId);
                            stringObjectHashMap.put("socialType", "facebook");
                            stringObjectHashMap.put("accessToken", accessTokan);
                            stringObjectHashMap.put("verification_code", verificationCodeRegistration.getText().toString().trim());
                            stringObjectHashMap.put("profile", values);

                            ErisCollectionManager.getInstance().callMethod("socialLoginWithAccessToken", new Object[]{stringObjectHashMap}, new ResultListener() {
                                @Override
                                public void onSuccess(String s) {
                                    if (ViewDialog.kProgressHUD.isShowing()) {
                                        ViewDialog.kProgressHUD.dismiss();
                                    }
                                    Log.v(TAG, "onSuccess FB login" + s);
                                    try {
                                        JSONObject jsonRootObject = new JSONObject(s);
                                        if (jsonRootObject.getString("status").equals("true")) {
                                            JSONObject jsonObject = jsonRootObject.getJSONObject("data");
                                            jsonObject.getString("code");
                                            jsonObject.getString("message");
                                            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                            jsonObject1.getString("type");
                                            jsonObject1.getString("userId");
                                            Log.v(TAG, jsonObject1.getString("userId"));
                                            facebookUserLoginId = jsonObject1.getString("userId");
                                            facebookFlag = true;
                                            PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit().putBoolean("FacebookUserLoginFlag", facebookFlag).commit();
                                            PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()).edit().putString("FacebookUserLoginId", facebookUserLoginId).commit();
                                            // Intercom
                                            CommonConstants.registerIntercom(facebookUserLoginId);

                                            Intent intent = new Intent(getActivity(), MainActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        } else if (jsonRootObject.getString("status").equals("false")) {
                                            JSONObject jsonObjectError = jsonRootObject.getJSONObject("error");
                                            String errorMessage = jsonObjectError.getString("message");
                                            errorMessageRegistrartionPopup.setVisibility(View.VISIBLE);
                                            errorMessageRegistrartionPopup.setText(errorMessage);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(String s, String s1, String s2) {
                                    if (ViewDialog.kProgressHUD.isShowing()) {
                                        ViewDialog.kProgressHUD.dismiss();
                                    }
                                    Log.v(TAG, "onError social login " + s1);

                                }
                            });
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

    public void resendSMS() {
        HashMap<String, Object> listOfObject = new HashMap<String, Object>();
        listOfObject.put("lang", "en");
        listOfObject.put("req_from", CommonConstants.requestFrom);
        listOfObject.put("phone_number", CommonConstants.countryCode + phoneNumberEditText.getText().toString().trim());
        listOfObject.put("verification_type", "registration");
        ErisCollectionManager.getInstance().callMethod("send_verification_sms", new Object[]{listOfObject}, new ResultListener() {
            @Override
            public void onSuccess(String jsonResponse) {
                Log.v(TAG, "Code Resend Successfully");
                try {
                    JSONObject jsonRootObject = new JSONObject(jsonResponse);
                    statusCode = jsonRootObject.getString("status");
                    if (statusCode.equalsIgnoreCase("true")) {
                        Log.v(TAG, "Code resend successfully");
                    } else if (statusCode.equalsIgnoreCase("false")) {
                        Log.v(TAG, "phone already exists");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String s, String s1, String s2) {
                if (CommonConstants.mDebug) Log.v(TAG, "onError" + s1);
            }
        });
    }


}
