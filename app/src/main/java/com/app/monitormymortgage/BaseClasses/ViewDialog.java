package com.app.monitormymortgage.BaseClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.app.monitormymortgage.Activity.AddMortgageActivity;
import com.app.monitormymortgage.Activity.EditMortgageActivity;
import com.app.monitormymortgage.Activity.NotificationListActivity;
import com.app.monitormymortgage.Activity.SplashScreenActivity;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Sangram on 16/02/16.
 */
public class ViewDialog {
    String newPassword;
    String repeatPassword;
    String phoneNumber;
    String verification_Code;
    TextView errorMessageTextView;
    Button btn_ok_phoneNumber;
    TextView mobileNumberTextView;
    EditText newPasswordEditText;
    EditText repeatPasswordEditText;
    EditText phoneNumberEditText;
    EditText verificationCode;
    public String getVerification_Code;
    String statusCode = "";
    ProgressDialog csprogress;
    TextView errorTextView;
    Typeface font_helvetica;
    static ProgressDialog progressDialog;
    public static KProgressHUD kProgressHUD;
    String countryCode;
    SplashScreenActivity splashScreenActivity;
    //    String pattern;
    Context context;
    ToastMessage toastMessage;

    public ViewDialog(Context context) {
        this.context = context;
        this.toastMessage = new ToastMessage(context);
    }


    public static void showProgress(int title, Activity activity, String message) {
        try {
            kProgressHUD = new KProgressHUD(activity);
            kProgressHUD.create(activity);
            kProgressHUD.setCancellable(false);
            kProgressHUD.setLabel(message);
            kProgressHUD.setAnimationSpeed(2);
            kProgressHUD.setDimAmount(0.5f);
            kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
            kProgressHUD.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgress() {
        if (kProgressHUD.isShowing())
            kProgressHUD.dismiss();
    }

    public static void showNotifications(final Activity activity, String msg) {
        AlertDialog.Builder Builder = new AlertDialog.Builder(activity)
                .setTitle(R.string.my_notifications)
                .setMessage(msg)
                .setIcon(R.drawable.applogo)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(activity, NotificationListActivity.class);
                        activity.startActivity(intent);
                    }
                });
        AlertDialog alertDialog = Builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    public static void showAlertPopUp(Activity activity, final String msg, String title) {
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
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlertPopUpAndFinish(final Activity activity, final String msg, String title) {
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
                    activity.finish();
                }
            });

            text.setText(msg);
            titleTextView.setText(title);
            Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    activity.finish();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showPopUpPrimeRate(Activity activity, String currentInterestRate, String currentPrimeRate, String primeRateAdjustment, String sign) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.custome_prime_rate_layout, null);
            alertDialogBuilder.setView(myDialog);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            TextView title = (TextView) myDialog.findViewById(R.id.titleTextView);
            title.setText(R.string.current_interest_rate_calculation);
            TextView ActualCurrentInterestTextView = (TextView) myDialog.findViewById(R.id.ActualCurrentInterestTextView);
            TextView ActualPrimeRateTextView = (TextView) myDialog.findViewById(R.id.ActualPrimeRateTextView);
            TextView ActualPrimeRateAdjstumentTextView = (TextView) myDialog.findViewById(R.id.ActualPrimeRateAdjstumentTextView);
            TextView signOneTextView = (TextView) myDialog.findViewById(R.id.signOneTextView);
            TextView signTwoTextView = (TextView) myDialog.findViewById(R.id.signTwoTextView);
            ActualCurrentInterestTextView.setText(currentInterestRate);
            ActualPrimeRateTextView.setText(currentPrimeRate);
            ActualPrimeRateAdjstumentTextView.setText(primeRateAdjustment);
            signOneTextView.setText(sign);
            signTwoTextView.setText(sign);

            TextView close = (TextView) myDialog.findViewById(R.id.closeButton);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            Button btn_okay = (Button) myDialog.findViewById(R.id.btn_okay);
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPopupAddMortgage(final Activity activity, int title) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.custom_dialog_add_mortgage, null);
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

            titleTextView.setText(title);
            Button completeLaterButton = (Button) myDialog.findViewById(R.id.btn_complete_later);
            Button completeNowButton = (Button) myDialog.findViewById(R.id.btn_complete_now);

            completeLaterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
//                    ShowImagesActivity.flagChanged=true;
                    activity.finish();
                    AddMortgageActivity.flag = false;
                    EditMortgageActivity.flagError = false;
                }
            });

            completeNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showPopupAddMortgage(final Activity activity, String message, int title) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.custom_dialog_add_mortgage, null);
            alertDialogBuilder.setView(myDialog);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
            TextView text = (TextView) myDialog.findViewById(R.id.text_dialog);
            text.setText(message);
            TextView text2 = (TextView) myDialog.findViewById(R.id.text_dialog2);
            text2.setVisibility(View.INVISIBLE);
            TextView close = (TextView) myDialog.findViewById(R.id.closeButton);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            titleTextView.setText(title);
            Button completeLaterButton = (Button) myDialog.findViewById(R.id.btn_complete_later);
            Button completeNowButton = (Button) myDialog.findViewById(R.id.btn_complete_now);

            completeLaterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
//                    ShowImagesActivity.flagChanged=true;
                    activity.finish();
                    AddMortgageActivity.flag = false;
                    EditMortgageActivity.flagError = false;
                }
            });

            completeNowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showNetworkPopup(final Activity activity, final String msg, String title) {
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
                    activity.onBackPressed();
                }
            });

            text.setText(msg);
            titleTextView.setText(title);
            Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    activity.onBackPressed();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showAlertPopUpForgotPassword(final Context context, int title) {

        try {
            kProgressHUD.dismiss();
            csprogress.dismiss();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.custom_dialog_forgot_password, null);
            alertDialogBuilder.setView(myDialog);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            toastMessage = new ToastMessage(context);
            alertDialog.setCancelable(false);
            errorMessageTextView = (TextView) myDialog.findViewById(R.id.errorMessageTextView);
            newPasswordEditText = (EditText) myDialog.findViewById(R.id.newPasswordEditText);
            newPasswordEditText.setTypeface(Typeface.DEFAULT);
            newPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
            repeatPasswordEditText = (EditText) myDialog.findViewById(R.id.repeatPasswordEditText);
            repeatPasswordEditText.setTypeface(Typeface.DEFAULT);
            repeatPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
            errorTextView = (TextView) myDialog.findViewById(R.id.errorTextView);
            newPasswordEditText.addTextChangedListener(mTextEditorWatcher);
            font_helvetica = Typeface.createFromAsset(context.getAssets(), "HelveticaNeue-Regular.ttf");
            errorTextView.setTypeface(font_helvetica);


            TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
            verificationCode = (EditText) myDialog.findViewById(R.id.verificationCode);
            TextView close = (TextView) myDialog.findViewById(R.id.closeButtonForgetPassword);
            TextView codeDidntReceive = (TextView) myDialog.findViewById(R.id.codeDidntReceive);
            String htmlString = context.getString(R.string.didnot_receive) + " <font color='#337ab7'><u> " + context.getString(R.string.resend_code) + "</u></font>";
            codeDidntReceive.setText(Html.fromHtml(htmlString));

            codeDidntReceive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GlobalMethods.haveNetworkConnection((Activity) context)) {
                        kProgressHUD = new KProgressHUD(context);
                        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
                        kProgressHUD.setCancellable(false);
                        kProgressHUD.setAnimationSpeed(2);
                        kProgressHUD.setDimAmount(0.5f);
                        kProgressHUD.setLabel(context.getString(R.string.please_wait));
                        kProgressHUD.show();
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                if (!MeteorSingleton.getInstance().isConnected()) {
                                    MeteorSingleton.getInstance().reconnect();
                                    resendSMS();
                                } else {
                                    resendSMS();
                                }
                                kProgressHUD.dismiss();
                            }
                        }, 3000);
                    } else {
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                        ViewDialog.showAlertPopUp((Activity) context, context.getString(R.string.no_network_found), context.getString(R.string.error));
                    }
                }
            });
            Typeface font1 = Typeface.createFromAsset(context.getAssets(), "fontawesome-webfont.ttf");
            close.setTypeface(font1);
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            textWatcher(newPasswordEditText, errorMessageTextView);
            textWatcher(repeatPasswordEditText, errorMessageTextView);
            titleTextView.setText(title);
            Button dialogButton = (Button) alertDialog.findViewById(R.id.btn_dialog);

            textWatcher(verificationCode, errorMessageTextView);
            if (dialogButton != null) {
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        if (GlobalMethods.haveNetworkConnection((Activity) context)) {
                            newPassword = newPasswordEditText.getText().toString();
                            repeatPassword = repeatPasswordEditText.getText().toString();
                            verification_Code = verificationCode.getText().toString();
                            if (newPassword.isEmpty()) {
                                errorMessageTextView.setVisibility(View.VISIBLE);
                                errorMessageTextView.setText(R.string.please_enter_password);

                            } else if (repeatPassword.isEmpty()) {
                                errorMessageTextView.setVisibility(View.VISIBLE);
                                errorMessageTextView.setText(R.string.please_enter_confirm_password);
                            } else if (newPassword.length() < 8) {
                                errorMessageTextView.setVisibility(View.VISIBLE);
                                errorMessageTextView.setText(R.string.password_eight_char_long);
                            } else if (!newPassword.matches(CommonConstants.passwordSimplificationRegex)) {
                                errorMessageTextView.setVisibility(View.VISIBLE);
                                errorMessageTextView.setText(R.string.alphanumeric_password);
                            } else if (!newPassword.equals(repeatPassword)) {
                                errorMessageTextView.setVisibility(View.VISIBLE);
                                errorMessageTextView.setText(R.string.password_do_not_match);
                            } else if (verification_Code.isEmpty()) {
                                errorMessageTextView.setVisibility(View.VISIBLE);
                                errorMessageTextView.setText(R.string.enter_verification_code);
                            }

//                            else if (!getVerification_Code.equals(verification_Code.toString())) {
//                                errorMessageTextView.setVisibility(View.VISIBLE);
//                                errorMessageTextView.setText(R.string.enter_valid_verification_code);
//                            }

                            else {

                                kProgressHUD = new KProgressHUD(context);
                                kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
                                kProgressHUD.setCancellable(false);
                                kProgressHUD.setAnimationSpeed(2);
                                kProgressHUD.setDimAmount(0.5f);
                                kProgressHUD.setLabel(context.getString(R.string.please_wait));
                                kProgressHUD.show();

                                if (!MeteorSingleton.getInstance().isConnected()) {
                                    MeteorSingleton.getInstance().reconnect();
                                }
                                HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
                                stringObjectHashMap.put("lang", "en");
                                stringObjectHashMap.put("req_from", CommonConstants.requestFrom);
                                stringObjectHashMap.put("forgot_data", phoneNumber);
                                stringObjectHashMap.put("forgot_value", newPassword);
                                stringObjectHashMap.put("verification_code", verification_Code);
                                MeteorSingleton.getInstance().call("setUserPassword", new Object[]{stringObjectHashMap}, new ResultListener() {
                                    @Override
                                    public void onSuccess(String result) {

                                        if (kProgressHUD.isShowing()) {
                                            kProgressHUD.dismiss();
                                        }

                                        Log.i("TAG password reset", result);
                                        String status = "";
                                        try {

                                            JSONObject jsonObject = new JSONObject(result);
                                            JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                            JSONObject jsonObjectError = jsonObject.getJSONObject("error");
                                            if (jsonObject.has("status")) {
                                                status = jsonObject.getString("status");
                                            }

                                            if (status.equalsIgnoreCase("true")) {
                                                String successMessage = jsonObjectData.getString("message");
                                                Log.v("TAG", "Password Updated Successfully");
                                                alertDialog.dismiss();
                                                toastMessage.showToastMsg(successMessage, Toast.LENGTH_SHORT);
                                                hideKeyboard(v, context);

                                            } else if (status.equalsIgnoreCase("false")) {
                                                String errorMessage = jsonObjectError.getString("message");
                                                errorMessageTextView.setVisibility(View.VISIBLE);
                                                errorMessageTextView.setText(errorMessage);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onError(String s, String s1, String s2) {
                                        Log.v("TAG", s1);
                                        if (kProgressHUD.isShowing()) {
                                            kProgressHUD.dismiss();
                                        }

                                    }
                                });
                            }
                        } else {
                            toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                        }

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {

//            pattern = "^.*(?=.{8,25})(?=.*\\d)(?=.*[A-Z])(?=.*[a-zA-Z]).*$";
            if (newPasswordEditText.getText().toString().matches(CommonConstants.passwordSimplificationRegex)) {
                errorTextView.setText(R.string.fa_info_circle_strong);
                errorTextView.setTextColor(ContextCompat.getColor(context, R.color.color_standard_green));
            } else {
                errorTextView.setText(R.string.fa_info_circle_week);
                errorTextView.setTextColor(Color.RED);
            }
        }
    };

    public void showAlertPopupPhoneNumber(final Context context, int title) {
        try {
            csprogress = new ProgressDialog(context);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.custom_dialog_phone_number, null);
            alertDialogBuilder.setView(myDialog);
            final AlertDialog alertDialogPhoneNumber = alertDialogBuilder.create();
            alertDialogPhoneNumber.show();
            alertDialogPhoneNumber.setCancelable(false);
            phoneNumberEditText = (EditText) myDialog.findViewById(R.id.phoneNumberEditText);
            btn_ok_phoneNumber = (Button) myDialog.findViewById(R.id.btn_ok_phoneNumber);
            errorMessageTextView = (TextView) myDialog.findViewById(R.id.errorMessageTextView);
            TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
            titleTextView.setText(title);
            TextView close = (TextView) myDialog.findViewById(R.id.closeButtonForgetPassword);


            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogPhoneNumber.dismiss();
                }
            });
            textWatcher(phoneNumberEditText, errorMessageTextView);
            btn_ok_phoneNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GlobalMethods.haveNetworkConnection((Activity) context)) {
                        if (!MeteorSingleton.getInstance().isConnected()) {
                            MeteorSingleton.getInstance().reconnect();
                        }
                        phoneNumber = phoneNumberEditText.getText().toString();
                        if (phoneNumber.isEmpty()) {
                            errorMessageTextView.setVisibility(View.VISIBLE);
                            errorMessageTextView.setText(R.string.enter_email_phone);
                        } else {
                            kProgressHUD = new KProgressHUD(context);
                            kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
                            kProgressHUD.setCancellable(false);
                            kProgressHUD.setAnimationSpeed(2);
                            kProgressHUD.setDimAmount(0.5f);
                            kProgressHUD.setLabel(context.getString(R.string.please_wait));
                            kProgressHUD.show();

                            HashMap<String, Object> listOfObject = new HashMap<String, Object>();
                            listOfObject.put("lang", "en");
                            listOfObject.put("req_from", CommonConstants.requestFrom);
                            if (phoneNumber.contains("@")) {
                                listOfObject.put("phone_number", phoneNumber);
                            } else {
                                listOfObject.put("phone_number", CommonConstants.countryCode + phoneNumber);
                            }
                            listOfObject.put("verification_type", "forgot_password");

                            MeteorSingleton.getInstance().call("send_verification_sms", new Object[]{listOfObject}, new ResultListener() {
                                @Override
                                public void onSuccess(final String jsonResponse) {
                                    Log.i("TAG", jsonResponse);
                                    try {
                                        JSONObject jsonRootObject = new JSONObject(jsonResponse);
                                        statusCode = jsonRootObject.getString("status");
                                        if (statusCode.equalsIgnoreCase("true")) {

//                                            JSONObject jsonObject = jsonRootObject.getJSONObject("data");
//                                            jsonObject.getString("code");
//                                            jsonObject.getString("message");
//                                            JSONObject jsonObjectResult = jsonObject.getJSONObject("result");
//                                            getVerification_Code = jsonObjectResult.getString("verification_code");

                                            showAlertPopUpForgotPassword(context, R.string.forgot_reset_password_step_two_title);
                                            alertDialogPhoneNumber.dismiss();

                                        } else if (statusCode.equalsIgnoreCase("false")) {
                                            JSONObject jsonObjectError = jsonRootObject.getJSONObject("error");
                                            String message = jsonObjectError.getString("message");
                                            errorMessageTextView.setVisibility(View.VISIBLE);
                                            errorMessageTextView.setText(message);
                                            kProgressHUD.dismiss();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(String errorCode, String error, String reason) {
                                    Log.v("TAG", error);
                                    errorMessageTextView.setVisibility(View.VISIBLE);
                                    errorMessageTextView.setText(error);
                                    kProgressHUD.dismiss();
                                }
                            });
                        }
                    } else {
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
        InputFilter filter = new InputFilter() {
            boolean canEnterSpace = false;

            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                if (phoneNumberEditText.getText().toString().equals("")) {
                    canEnterSpace = false;
                }

                StringBuilder builder = new StringBuilder();

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);

                    if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                        builder.append(currentChar);
                        canEnterSpace = true;
                    }

                    if (Character.isWhitespace(currentChar) && canEnterSpace) {
                        builder.append(currentChar);
                    }
                }
                return builder.toString();
            }

        };

    }

    public void sendSMS() {

    }


    public void resendSMS() {
        HashMap<String, Object> listOfObject = new HashMap<String, Object>();
        listOfObject.put("lang", "en");
        listOfObject.put("req_from", CommonConstants.requestFrom);
        if (phoneNumber.contains("@")) {
            listOfObject.put("phone_number", phoneNumber);
        } else {
            listOfObject.put("phone_number", CommonConstants.countryCode + phoneNumber);
        }
        listOfObject.put("verification_type", "forgot_password");

        MeteorSingleton.getInstance().call("send_verification_sms", new Object[]{listOfObject}, new ResultListener() {
            @Override
            public void onSuccess(final String jsonResponse) {
                Log.i("TAG", jsonResponse);
                try {
                    JSONObject jsonRootObject = new JSONObject(jsonResponse);
                    statusCode = jsonRootObject.getString("status");
                    if (statusCode.equalsIgnoreCase("true")) {
                        Log.i("TAG", "Code Resend Successfully");
                    } else if (statusCode.equalsIgnoreCase("false")) {
                        errorMessageTextView.setVisibility(View.VISIBLE);
                        errorMessageTextView.setText(R.string.user_not_found_with_email_or_phone);
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


    /*
    * Hide Textview after change Edittext Content.
    * */
    public void textWatcher(EditText editText, final TextView textView) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textView.setVisibility(View.GONE);

            }
        });
    }

    public void hideKeyboard(View view, Context context) {
        //  Hide Keyboard.
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}
