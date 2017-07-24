package com.app.monitormymortgage.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.app.monitormymortgage.Activity.SplashScreenActivity;
import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.DataHolderClasses.UserObject;
import com.app.monitormymortgage.MainActivity;
import com.app.monitormymortgage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.UserAttributes;


public class MyM3AccountFragment extends BaseFragment {
    private static final String TAG = MyM3AccountFragment.class.getSimpleName();
    private TextView opportunityNotificationPreferncesTextView;
    private TextView actualContactPreferencesTextView;
    private EditText actualFirstNameEditText;
    private EditText actualLastNameEditText;
    private EditText actualEmailEditText;
    private EditText actualPhoneNumberEditText;
    private Button btn_edit;
    private Button btn_save;
    private Button btn_cancel;
    String phone;
    String email;
    ToastMessage toastMessage;
    String statusCode;
    String verificationCodeJSON;
    EditText verificationCodeRegistration;
    Button btn_verify;
    TextView closeButtonRegistration;
    String verificationCode;
    TextView errorMessageRegistrartionPopup;
    TextView mobileNumberTextViewRegistration;
    TextView codeDidntReceive;
    ProgressDialog csprogress;
    String facebookUserLoginId;
    boolean facebookUserLoginFlag;
    String googleUserLoginId;
    boolean googleUserLoginFlag;
    RadioGroup ContactPreferncesRadioGroup;
    AlertDialog alertDialog;
    SplashScreenActivity splashScreenActivity;
    List<CharSequence> list = new ArrayList<CharSequence>();
    String username;
    RelativeLayout opprNotificationPrefRelativeLayout;
    RelativeLayout cntPrefRelativeLayout;
    TextView socialUserErrorMessage;
    View view;
    String cntPrefernce;
    String communication_pref;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialogRegistrationPhoneNumber;

    public MyM3AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitle(R.string.my_account_fragment);
        splashScreenActivity = new SplashScreenActivity();
        toastMessage = new ToastMessage(this.getActivity());
        list.add("Email");
        list.add("App Notification");
        list.add("SMS Notification");
        view = getView();
        ViewDialog.showProgress(R.string.help_screen_one, MyM3AccountFragment.this.getActivity(), getResources().getString(R.string.please_wait));

        alertDialogBuilder = new AlertDialog.Builder(this.getContext());

        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("GoogleUserLoginFlag", false);
        username = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("username", "");
        if (CommonConstants.mDebug) Log.v(TAG + "username", username);
        if (CommonConstants.mDebug) Log.v(TAG, "GoogleUserLoginId :" + googleUserLoginId);
        if (CommonConstants.mDebug) Log.v(TAG, "GoogleUserLoginFlag :" + googleUserLoginFlag);
        if (CommonConstants.mDebug) Log.v(TAG, "FacebookUserLoginId :" + facebookUserLoginId);
        if (CommonConstants.mDebug) Log.v(TAG, "FacebookUserLoginFlag :" + facebookUserLoginFlag);
        csprogress = new ProgressDialog(MyM3AccountFragment.this.getActivity());
        if (facebookUserLoginFlag) {
            if (CommonConstants.mDebug) Log.v(TAG, "true");
        } else {
            if (CommonConstants.mDebug) Log.v(TAG, "false");
        }
        System.out.println("Get Country Code :" + UserObject.getContactPreferences());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_my_m3_account, container, false);
        initialiseUIComponent(rootview);
        setUserData();
        setVisibility();
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserData();
                btn_edit.setVisibility(View.GONE);
                btn_save.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
                opprNotificationPrefRelativeLayout.setEnabled(true);
                cntPrefRelativeLayout.setEnabled(true);
                if (googleUserLoginFlag) {
                    socialUserErrorMessage.setVisibility(View.VISIBLE);
                    socialUserErrorMessage.setText(R.string.social_user_google_update_profile);
                    actualFirstNameEditText.setEnabled(false);
                    actualLastNameEditText.setEnabled(false);
                    actualEmailEditText.setEnabled(false);
                } else if (facebookUserLoginFlag) {
                    socialUserErrorMessage.setVisibility(View.VISIBLE);
                    socialUserErrorMessage.setText(R.string.social_user_facebook_update_profile);
                    actualFirstNameEditText.setEnabled(false);
                    actualLastNameEditText.setEnabled(false);
                    actualEmailEditText.setEnabled(false);
                } else {
                    actualFirstNameEditText.setEnabled(true);
                    actualFirstNameEditText.setTypeface(actualFirstNameEditText.getTypeface(), Typeface.ITALIC);
                    actualLastNameEditText.setEnabled(true);
                    actualLastNameEditText.setTypeface(actualLastNameEditText.getTypeface(), Typeface.ITALIC);
                    actualEmailEditText.setEnabled(true);
                    actualEmailEditText.setTypeface(actualEmailEditText.getTypeface(), Typeface.ITALIC);
                }
                actualPhoneNumberEditText.setEnabled(true);
                actualPhoneNumberEditText.setTypeface(actualPhoneNumberEditText.getTypeface(), Typeface.ITALIC);
                opprNotificationPrefRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDialog();
                    }
                });
                cntPrefRelativeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ContactPreferncesPopUp(MyM3AccountFragment.this.getActivity(), "Contact Preference");
                    }
                });
                /* Hide keyboard */
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUserData();
                btn_edit.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                socialUserErrorMessage.setVisibility(View.INVISIBLE);
                opprNotificationPrefRelativeLayout.setEnabled(false);
                cntPrefRelativeLayout.setEnabled(false);
                actualFirstNameEditText.setTypeface(actualFirstNameEditText.getTypeface(), Typeface.NORMAL);
                actualFirstNameEditText.setEnabled(false);
                actualLastNameEditText.setTypeface(actualLastNameEditText.getTypeface(), Typeface.NORMAL);
                actualLastNameEditText.setEnabled(false);
                actualEmailEditText.setTypeface(actualEmailEditText.getTypeface(), Typeface.NORMAL);
                actualEmailEditText.setEnabled(false);
                actualPhoneNumberEditText.setTypeface(actualPhoneNumberEditText.getTypeface(), Typeface.NORMAL);
                actualPhoneNumberEditText.setEnabled(false);

                //  Hide Keyboard.
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });

        btnSaveClick();
        return rootview;
    }

    public void btnSaveClick() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalMethods.haveNetworkConnection(MyM3AccountFragment.this.getActivity())) {
                    if (validation(v)) {
                        if (!phone.equals(actualPhoneNumberEditText.getText().toString())) {
                            sendSMS();
                        } else {
                            HashMap<String, Object> values2 = new HashMap<String, Object>();
                            values2.put("email", actualEmailEditText.getText().toString().trim());
                            values2.put("firstname", actualFirstNameEditText.getText().toString().trim());
                            values2.put("lastname", actualLastNameEditText.getText().toString().trim());
                            values2.put("phone", actualPhoneNumberEditText.getText().toString().trim());
                            values2.put("phone_country_code", CommonConstants.countryCode);
                            values2.put("oppr_notification_pref", opportunityNotificationPreferncesTextView.getText().toString().trim());
                            values2.put("contact_pref", actualContactPreferencesTextView.getText().toString().trim());

                            HashMap<String, Object> values1 = new HashMap<>();
                            values1.put("updateType", "profile");
                            if (facebookUserLoginFlag) {
                                values1.put("_id", facebookUserLoginId);
                            } else if (googleUserLoginFlag) {
                                values1.put("_id", googleUserLoginId);
                            } else {
                                values1.put("_id", username);
                            }
                            values1.put("email", actualEmailEditText.getText().toString().trim());
                            values1.put("profile", values2);


                            // Intercom
//                            Map<String, Object> userMap = new HashMap<>();
//                            userMap.put("email", actualEmailEditText.getText().toString().trim());
//                            Intercom.client().updateUser(userMap);

                            UserAttributes userAttributes = new UserAttributes.Builder()
                                    .withName(actualFirstNameEditText.getText().toString().trim())
                                    .withEmail(actualEmailEditText.getText().toString().trim())
                                    .build();
                            Intercom.client().updateUser(userAttributes);


                            ErisCollectionManager.getInstance().callMethod("updateUser", new Object[]{values1}, new ResultListener() {
                                @Override
                                public void onSuccess(String s) {
                                    if (CommonConstants.mDebug) Log.v(TAG, "onSuccess " + s);
                                    try {
                                        JSONObject jsonRootObject = new JSONObject(s);
                                        if (jsonRootObject.getString("status").equalsIgnoreCase("false")) {
                                            JSONObject jsonObject = jsonRootObject.getJSONObject("error");
                                            if (jsonObject.getString("code").equals("11003") || jsonObject.getString("code").equals("11023")) {
                                                ViewDialog.showAlertPopUp(MyM3AccountFragment.this.getActivity(), getResources().getString(R.string.email_already_exists), getResources().getString(R.string.error));
                                            } else if (jsonObject.getString("code").equals("10005")) {

                                            }
                                        } else {
                                            actualFirstNameEditText.setText(actualFirstNameEditText.getText().toString());
                                            setVisibility();
                                            btn_edit.setVisibility(View.VISIBLE);
                                            toastMessage.showToastMsg(R.string.profile_updated_successfully, Toast.LENGTH_SHORT);
                                            CommonConstants.contactPreference=actualContactPreferencesTextView.getText().toString().trim();
                                            ViewDialog.hideProgress();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(String s, final String s1, String s2) {
                                    if (CommonConstants.mDebug)
                                        Log.v(TAG, "On Error Update :" + s1);
                                }
                            });
                        }
                    }
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                } else {
                    if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MyM3AccountFragment.this.getActivity())) {
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    }
                }
            }
        });
    }

    public void setUserData() {

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();

        if (facebookUserLoginFlag) {
            stringObjectHashMap.put("userId", facebookUserLoginId);
        } else if (googleUserLoginFlag) {
            stringObjectHashMap.put("userId", googleUserLoginId);
        } else if (!facebookUserLoginFlag && !googleUserLoginFlag) {
            stringObjectHashMap.put("userId", username);
        }

        ErisCollectionManager.getInstance().callMethod("getUser", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String s) {
                ViewDialog.hideProgress();
                if (CommonConstants.mDebug) Log.v(TAG, s);
                try {
                    JSONObject jsonRootObject = new JSONObject(s);
                    if (jsonRootObject.getString("status").equals("true")) {
                        JSONObject jsonObjectData = jsonRootObject.getJSONObject("data");
                        JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                        actualEmailEditText.setText(jsonObjectResult.getString("email"));
                        actualFirstNameEditText.setText(jsonObjectResult.getString("firstname"));
                        actualLastNameEditText.setText(jsonObjectResult.getString("lastname"));
                        actualPhoneNumberEditText.setText(jsonObjectResult.getString("phone"));
                        phone = jsonObjectResult.getString("phone");
                        email = jsonObjectResult.getString("email");
                        if (CommonConstants.mDebug) Log.v(TAG, phone);
                        if (CommonConstants.mDebug) Log.v(TAG, email);
                        setOpportunityNotificationPref(jsonObjectResult.getString("oppr_notification_pref"), opportunityNotificationPreferncesTextView);
//                            opportunityNotificationPreferncesTextView.setText(jsonObjectResult.getString("oppr_notification_pref"));
                        actualContactPreferencesTextView.setText(jsonObjectResult.getString("contact_pref"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String s, final String s1, String s2) {

                if (CommonConstants.mDebug) Log.v(TAG, s1);
                ViewDialog.hideProgress();
            }
        });
    }


    public void setVisibility() {
        actualFirstNameEditText.setEnabled(false);
        actualLastNameEditText.setEnabled(false);
        actualEmailEditText.setEnabled(false);
        opprNotificationPrefRelativeLayout.setEnabled(false);
        cntPrefRelativeLayout.setEnabled(false);
        actualPhoneNumberEditText.setEnabled(false);
        btn_save.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        socialUserErrorMessage.setVisibility(View.INVISIBLE);

    }

    public boolean validation(View view) {
        boolean bln = true;

        if (actualFirstNameEditText.getText().toString().equals("") && actualLastNameEditText.getText().toString().equals("") &&
                actualEmailEditText.getText().toString().equals("") && actualPhoneNumberEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_information_in_all_fields, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualFirstNameEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_first_name, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualLastNameEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_last_name, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualEmailEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_email, Toast.LENGTH_SHORT);
            bln = false;
        } else if (!GlobalMethods.isValidEmailId(actualEmailEditText.getText().toString())) {
            toastMessage.showToastMsg(R.string.enter_valid_email, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualPhoneNumberEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_phone, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualPhoneNumberEditText.getText().toString().length() < 10) {
            toastMessage.showToastMsg(R.string.enter_valid_phone_number, Toast.LENGTH_SHORT);
            bln = false;
        }

        return bln;
    }

    public void sendSMS() {
        HashMap<String, Object> listOfObject = new HashMap<String, Object>();
        listOfObject.put("lang", "en");
        listOfObject.put("req_from", CommonConstants.requestFrom);
        listOfObject.put("phone_number", CommonConstants.countryCode + actualPhoneNumberEditText.getText().toString().trim());
        listOfObject.put("verification_type", "update_phone");
        ErisCollectionManager.getInstance().callMethod("send_verification_sms", new Object[]{listOfObject}, new ResultListener() {
            @Override
            public void onSuccess(String jsonResponse) {
                Log.i("TAG", jsonResponse);
                try {
                    JSONObject jsonRootObject = new JSONObject(jsonResponse);
                    statusCode = jsonRootObject.getString("status");
                    if (statusCode.equalsIgnoreCase("true")) {
                        alertPopupRegistration(MyM3AccountFragment.this.getContext(), R.string.title_phone_verification_registrartion);
                    } else if (statusCode.equalsIgnoreCase("false")) {
                        Log.i(TAG, "phone already exists");
                        ViewDialog.showAlertPopUp(MyM3AccountFragment.this.getActivity(), getResources().getString(R.string.phone_already_exist), getResources().getString(R.string.error));
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

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.custom_dialog_verify_registration, null);
            alertDialogBuilder.setView(myDialog);
            alertDialogRegistrationPhoneNumber = alertDialogBuilder.create();
            alertDialogRegistrationPhoneNumber.show();
            alertDialogRegistrationPhoneNumber.setCancelable(false);
            btn_verify = (Button) myDialog.findViewById(R.id.btn_verify);
            verificationCodeRegistration = (EditText) myDialog.findViewById(R.id.verificationCodeRegistration);
            closeButtonRegistration = (TextView) myDialog.findViewById(R.id.closeButtonRegistration);
            errorMessageRegistrartionPopup = (TextView) myDialog.findViewById(R.id.errorMessageTextViewRegistration);
            mobileNumberTextViewRegistration = (TextView) myDialog.findViewById(R.id.mobileNumberTextViewRegistration);
            mobileNumberTextViewRegistration.setText(actualPhoneNumberEditText.getText().toString());
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
                public void onClick(final View v) {
                    verificationCode = verificationCodeRegistration.getText().toString().trim();
                    if (verificationCode.isEmpty()) {
                        errorMessageRegistrartionPopup.setVisibility(View.VISIBLE);
                        errorMessageRegistrartionPopup.setText(R.string.enter_verification_code);
                    } else {
                        ViewDialog.showProgress(R.string.help_screen_one, MyM3AccountFragment.this.getActivity(), getResources().getString(R.string.please_wait));

                        HashMap<String, Object> values2 = new HashMap<String, Object>();
                        values2.put("firstname", actualFirstNameEditText.getText().toString().trim());
                        values2.put("lastname", actualLastNameEditText.getText().toString().trim());
                        values2.put("phone", actualPhoneNumberEditText.getText().toString().trim());
                        values2.put("phone_country_code", CommonConstants.countryCode);
                        values2.put("oppr_notification_pref", opportunityNotificationPreferncesTextView.getText().toString().trim());
                        values2.put("contact_pref", actualContactPreferencesTextView.getText().toString().trim());

                        HashMap<String, Object> values1 = new HashMap<>();
                        values1.put("updateType", "profile");
                        if (facebookUserLoginFlag) {
                            values1.put("_id", facebookUserLoginId);
                        } else if (googleUserLoginFlag) {
                            values1.put("_id", googleUserLoginId);
                        } else {
                            values1.put("_id", username);
                        }

                        values1.put("email", actualEmailEditText.getText().toString().trim());
                        values1.put("verification_code", verificationCodeRegistration.getText().toString().trim());
                        values1.put("profile", values2);

                        // Intercom
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put("email", actualEmailEditText.getText().toString().trim());
                        Intercom.client().updateUser(userMap);

                        ErisCollectionManager.getInstance().callMethod("updateUser", new Object[]{values1}, new ResultListener() {
                            @Override
                            public void onSuccess(String result) {
                                if (ViewDialog.kProgressHUD.isShowing()) {
                                    ViewDialog.kProgressHUD.dismiss();
                                }

                                Log.v(TAG, "onSuccess " + result);
                                String status = "";
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    status = jsonObject.getString("status");
                                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                    JSONObject jsonObjectError = jsonObject.getJSONObject("error");

                                    if (status.equalsIgnoreCase("true")) {
                                        alertDialogRegistrationPhoneNumber.dismiss();
                                        actualFirstNameEditText.setText(actualFirstNameEditText.getText().toString());
                                        setVisibility();
                                        btn_edit.setVisibility(View.VISIBLE);
                                        toastMessage.showToastMsg(R.string.profile_updated_successfully, Toast.LENGTH_SHORT);
                                    } else if (status.equalsIgnoreCase("false")) {
                                        String errorMessage = jsonObjectError.getString("message");
                                        errorMessageRegistrartionPopup.setVisibility(View.VISIBLE);
                                        errorMessageRegistrartionPopup.setText(errorMessage);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String s, String s1, String s2) {
                                Log.v(TAG, "onError " + s1);
                                if (ViewDialog.kProgressHUD.isShowing())
                                    ViewDialog.kProgressHUD.dismiss();
                            }
                        });

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        if (alertDialogRegistrationPhoneNumber != null && alertDialogRegistrationPhoneNumber.isShowing()) {
            alertDialogRegistrationPhoneNumber.dismiss();
        }
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        super.onDestroyView();
    }

    public void setOpportunityNotificationPref(String opportunityNotificationPref, TextView textView) {
        if (opportunityNotificationPref.equalsIgnoreCase("Email, App Notification, SMS Notification")) {
            textView.setText("Email, App Notification, SMS Notification");
        } else if (opportunityNotificationPref.equalsIgnoreCase("Email, SMS Notification, App Notification")) {
            textView.setText("Email, App Notification, SMS Notification");
        } else if (opportunityNotificationPref.equalsIgnoreCase("App Notification, SMS Notification, Email")) {
            textView.setText("Email, App Notification, SMS Notification");
        } else if (opportunityNotificationPref.equalsIgnoreCase("App Notification, Email, SMS Notification")) {
            textView.setText("Email, App Notification, SMS Notification");
        } else if (opportunityNotificationPref.equalsIgnoreCase("SMS Notification, App Notification, Email")) {
            textView.setText("Email, App Notification, SMS Notification");
        } else if (opportunityNotificationPref.equalsIgnoreCase("SMS Notification, Email, App Notification")) {
            textView.setText("Email, App Notification, SMS Notification");
        } else if (opportunityNotificationPref.equalsIgnoreCase("Email, App Notification") || opportunityNotificationPref.equalsIgnoreCase("App Notification, Email")) {
            textView.setText("Email, App Notification");
        } else if (opportunityNotificationPref.equalsIgnoreCase("App Notification, SMS Notification") || opportunityNotificationPref.equalsIgnoreCase("SMS Notification, App Notification")) {
            textView.setText("App Notification, SMS Notification");
        } else if (opportunityNotificationPref.equalsIgnoreCase("Email, SMS Notification") || opportunityNotificationPref.equalsIgnoreCase("SMS Notification, Email")) {
            textView.setText("Email, SMS Notification");
        } else if (opportunityNotificationPref.equalsIgnoreCase("Email")) {
            textView.setText("Email");
        } else if (opportunityNotificationPref.equalsIgnoreCase("App Notification")) {
            textView.setText("App Notification");
        } else if (opportunityNotificationPref.equalsIgnoreCase("SMS Notification")) {
            textView.setText("SMS Notification");
        }
    }

    public void resendSMS() {
        HashMap<String, Object> listOfObject = new HashMap<String, Object>();
        listOfObject.put("lang", "en");
        listOfObject.put("req_from", CommonConstants.requestFrom);
        listOfObject.put("phone_number", CommonConstants.countryCode + actualPhoneNumberEditText.getText().toString().trim());
        listOfObject.put("verification_type", "update_phone");
        ErisCollectionManager.getInstance().callMethod("send_verification_sms", new Object[]{listOfObject}, new ResultListener() {
            @Override
            public void onSuccess(String jsonResponse) {
                Log.i(TAG, "Code Resend Successfully");
            }

            @Override
            public void onError(String s, String s1, String s2) {
                Log.v(TAG, "onError" + s1);
            }
        });
    }

    public void initialiseUIComponent(View view) {

        opportunityNotificationPreferncesTextView = (TextView) view.findViewById(R.id.opportunityNotificationPreferncesTextView);
        actualContactPreferencesTextView = (TextView) view.findViewById(R.id.actualContactPreferencesTextView);
        actualFirstNameEditText = (EditText) view.findViewById(R.id.actualFirstNameEditText);
        actualLastNameEditText = (EditText) view.findViewById(R.id.actualLastNameEditText);
        actualEmailEditText = (EditText) view.findViewById(R.id.actualEmailEditText);
        actualPhoneNumberEditText = (EditText) view.findViewById(R.id.actualPhoneNumberEditText);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        opprNotificationPrefRelativeLayout = (RelativeLayout) view.findViewById(R.id.opprNotificationPrefRelativeLayout);
        cntPrefRelativeLayout = (RelativeLayout) view.findViewById(R.id.cntPrefRelativeLayout);
        socialUserErrorMessage = (TextView) view.findViewById(R.id.socialUserErrorMessage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showDialog() {
        LayoutInflater inflater = ((Activity) MyM3AccountFragment.this.getActivity()).getLayoutInflater();
        final View myDialog = inflater.inflate(R.layout.custom_opprt_notification_pref, null);
        alertDialogBuilder.setView(myDialog);
        alertDialogBuilder.setCancelable(false);
        TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
        TextView close = (TextView) myDialog.findViewById(R.id.closeButton);
        final CheckBox email = (CheckBox) myDialog.findViewById(R.id.email);
        final CheckBox appNotifications = (CheckBox) myDialog.findViewById(R.id.appNotifications);
        final CheckBox smsNotifications = (CheckBox) myDialog.findViewById(R.id.smsNotifications);
        final TextView errorTextView = (TextView) myDialog.findViewById(R.id.errorTextView);


        titleTextView.setText(R.string.opportunity_notification_prefernces);
        ContactPreferncesRadioGroup = (RadioGroup) myDialog.findViewById(R.id.ContactPreferncesRadioGroup);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });
        // communication_pref = "Phone";
        cntPrefernce = opportunityNotificationPreferncesTextView.getText().toString();
        if (CommonConstants.mDebug) Log.v("TAG", cntPrefernce);
        Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communication_pref = "Email";
                errorTextView.setVisibility(View.INVISIBLE);
            }
        });
        appNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communication_pref = "App Notification";
                errorTextView.setVisibility(View.INVISIBLE);
            }
        });
        smsNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communication_pref = "SMS Notification";
                errorTextView.setVisibility(View.INVISIBLE);
            }
        });


        if (cntPrefernce.equalsIgnoreCase("Email, App Notification, SMS Notification")) {
            email.setChecked(true);
            appNotifications.setChecked(true);
            smsNotifications.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("Email, SMS Notification, App Notification")) {
            email.setChecked(true);
            appNotifications.setChecked(true);
            smsNotifications.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("App Notification, SMS Notification, Email")) {
            email.setChecked(true);
            appNotifications.setChecked(true);
            smsNotifications.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("App Notification, Email, SMS Notification")) {
            email.setChecked(true);
            appNotifications.setChecked(true);
            smsNotifications.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("SMS Notification, App Notification, Email")) {
            email.setChecked(true);
            appNotifications.setChecked(true);
            smsNotifications.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("SMS Notification, Email, App Notification")) {
            email.setChecked(true);
            appNotifications.setChecked(true);
            smsNotifications.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("Email, App Notification") || cntPrefernce.equalsIgnoreCase("App Notification, Email")) {
            email.setChecked(true);
            appNotifications.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("App Notification, SMS Notification") || cntPrefernce.equalsIgnoreCase("SMS Notification, App Notification")) {
            appNotifications.setChecked(true);
            smsNotifications.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("Email, SMS Notification") || cntPrefernce.equalsIgnoreCase("SMS Notification, Email")) {
            email.setChecked(true);
            smsNotifications.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("Email")) {
            email.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("App Notification")) {
            appNotifications.setChecked(true);
        } else if (cntPrefernce.equalsIgnoreCase("SMS Notification")) {
            smsNotifications.setChecked(true);
        }


        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!email.isChecked() && !appNotifications.isChecked() && !smsNotifications.isChecked()) {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText(R.string.select_opp_pref_error_msg);
                } else {
                    if (email.isChecked() && appNotifications.isChecked() && smsNotifications.isChecked()) {
                        opportunityNotificationPreferncesTextView.setText(R.string.email_app_sms);
                        alertDialog.dismiss();
                    } else if (email.isChecked() && appNotifications.isChecked()) {
                        opportunityNotificationPreferncesTextView.setText(R.string.email_app);
                        alertDialog.dismiss();
                    } else if (appNotifications.isChecked() && smsNotifications.isChecked()) {
                        opportunityNotificationPreferncesTextView.setText(R.string.app_sms);
                        alertDialog.dismiss();
                    } else if (email.isChecked() && smsNotifications.isChecked()) {
                        opportunityNotificationPreferncesTextView.setText(R.string.email_sms);
                        alertDialog.dismiss();
                    } else if (email.isChecked()) {
                        opportunityNotificationPreferncesTextView.setText(R.string.email);
                        alertDialog.dismiss();
                    } else if (appNotifications.isChecked()) {
                        opportunityNotificationPreferncesTextView.setText(R.string.app_notification);
                        alertDialog.dismiss();
                    } else if (smsNotifications.isChecked()) {
                        opportunityNotificationPreferncesTextView.setText(R.string.sms_notification);
                        alertDialog.dismiss();
                    }
                }
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void ContactPreferncesPopUp(Activity activity, String title) {
        LayoutInflater inflater = ((Activity) MyM3AccountFragment.this.getActivity()).getLayoutInflater();
        final View myDialog = inflater.inflate(R.layout.custom_dialog_cnt_prefernces, null);
        alertDialogBuilder.setView(myDialog);
        alertDialogBuilder.setCancelable(false);
        TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
        TextView close = (TextView) myDialog.findViewById(R.id.closeButton);
        RadioButton radioButtonPhn = (RadioButton) myDialog.findViewById(R.id.radioButtonPhn);
        RadioButton radioButtonEmail = (RadioButton) myDialog.findViewById(R.id.radioButtonEmail);
        RadioButton radioButtonBoth = (RadioButton) myDialog.findViewById(R.id.radioButtonBoth);
        titleTextView.setText(title);
        ContactPreferncesRadioGroup = (RadioGroup) myDialog.findViewById(R.id.ContactPreferncesRadioGroup);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);
        dialogButton.setVisibility(View.GONE);

        if (actualContactPreferencesTextView.getText().toString().equalsIgnoreCase("Phone")) {
            radioButtonPhn.setChecked(true);
        } else if (actualContactPreferencesTextView.getText().toString().equalsIgnoreCase("Email")) {
            radioButtonEmail.setChecked(true);
        } else {
            radioButtonBoth.setChecked(true);
        }

        ContactPreferncesRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonPhn:
                        actualContactPreferencesTextView.setText(R.string.phone);
                        break;
                    case R.id.radioButtonEmail:
                        actualContactPreferencesTextView.setText(R.string.hint_email);
                        break;
                    case R.id.radioButtonBoth:
                        actualContactPreferencesTextView.setText(R.string.both);
                        break;
                }
                alertDialog.dismiss();
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onDisconnect() {
        ViewDialog.hideProgress();
        if (isAdded()) {
            if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MyM3AccountFragment.this.getActivity())) {
                toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
            }
        }
    }
}

