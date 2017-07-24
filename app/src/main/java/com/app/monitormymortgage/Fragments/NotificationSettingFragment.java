package com.app.monitormymortgage.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.app.monitormymortgage.Common.EditTextThousand;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.MainActivity;
import com.app.monitormymortgage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;




public class NotificationSettingFragment extends BaseFragment {
    private static final String TAG = NotificationSettingFragment.class.getSimpleName();
    EditText actualSavingPerMonthEditText;
    EditText actualremainingTermEditText;
    private TextView spinnerTextView;
    private TextView actualRateIncreaseTextView;
    private TextView notifyMeMaturityTextView;
    private Typeface font_helvetica;
    private Button btn_edit;
    private Button btn_save, btn_cancel;
    RadioGroup myRadioGroup;
    RadioButton sound;
    RadioButton vibration;
    RadioButton silent;
    String savingAmtPerMnth = "";
    String savingAmtOverRemTerm = "";
    String notifyRateIncrease = "";
    String notifyMe = "";
    String radioButton;
    ToastMessage toastMessage;
    int pos;
    int pos1;
    String facebookUserLoginId;
    boolean facebookUserLoginFlag;
    String googleUserLoginId;
    boolean googleUserLoginFlag;
    String username;
    RelativeLayout rateIncreaseSpinnerRelativeLayout;
    ArrayList<String> arrayList;
    CharSequence[] items;
    String key = "120";
    AlertDialog alert;

    public NotificationSettingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitle(R.string.notification_setting_fragment);
        font_helvetica = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeue-Regular.ttf");
        toastMessage = new ToastMessage(this.getActivity());
        arrayList = new ArrayList<>();
        ViewDialog.showProgress(R.string.help_screen_one, NotificationSettingFragment.this.getActivity(), getResources().getString(R.string.please_wait));
        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("GoogleUserLoginFlag", false);
        username = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("username", "");
        getAllMasters();
        if (CommonConstants.mDebug) Log.v("TAG", username);
        if (CommonConstants.mDebug) Log.v(TAG, "GoogleUserLoginId :" + googleUserLoginId);
        if (CommonConstants.mDebug) Log.v(TAG, "GoogleUserLoginFlag :" + googleUserLoginFlag);
        if (CommonConstants.mDebug) Log.v(TAG, "FacebookUserLoginId :" + facebookUserLoginId);
        if (CommonConstants.mDebug) Log.v(TAG, "FacebookUserLoginFlag :" + facebookUserLoginFlag);

    }

    private void getAllMasters() {
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
                    JSONArray jsonArrayRateIncreaseMaster = jsonObjectResult.getJSONArray("user_notify_rate_increase_bps");

                    /* Get Notify Rate Increase Master*/
                    for (int i = 0; i < jsonArrayRateIncreaseMaster.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayRateIncreaseMaster.getJSONObject(i);
                        arrayList.add(jsonObject1.getString("val"));
                        items = arrayList.toArray(new CharSequence[arrayList.size()]);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootview = inflater.inflate(R.layout.fragment_notification_setting, container, false);
        initialise(rootview);
        setUserData();
        actualSavingPerMonthEditText.addTextChangedListener(new EditTextThousand(actualSavingPerMonthEditText));
        actualremainingTermEditText.addTextChangedListener(new EditTextThousand(actualremainingTermEditText));
        actualSavingPerMonthEditText.setText(savingAmtPerMnth);
        actualremainingTermEditText.setText(savingAmtOverRemTerm);
        actualRateIncreaseTextView.setText(notifyRateIncrease);
        notifyMeMaturityTextView.setText(notifyMe);
        setVisibility();
        rateIncreaseSpinnerRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(NotificationSettingFragment.this.getActivity());
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        actualRateIncreaseTextView.setText(items[item]);
                    }
                });
                alert = builder.create();
                alert.show();

            }
        });

        myRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pos = myRadioGroup.indexOfChild(rootview.findViewById(checkedId));
                pos1 = myRadioGroup.indexOfChild(rootview.findViewById(myRadioGroup.getCheckedRadioButtonId()));
                switch (pos) {
                    case 0:
                        notifyMeMaturityTextView.setText("90 Days Until Maturity");
                        key = "90";
                        break;
                    case 1:
                        notifyMeMaturityTextView.setText("120 Days Until Maturity");
                        key = "120";
                        break;
                    case 2:
                        notifyMeMaturityTextView.setText("180 Days Until Maturity");
                        key = "180";
                        break;
                }

            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_edit.setVisibility(View.GONE);
                actualSavingPerMonthEditText.setEnabled(true);
                actualSavingPerMonthEditText.setTypeface(actualSavingPerMonthEditText.getTypeface(), Typeface.ITALIC);
                actualremainingTermEditText.setEnabled(true);
                actualremainingTermEditText.setTypeface(actualremainingTermEditText.getTypeface(), Typeface.ITALIC);
                rateIncreaseSpinnerRelativeLayout.setEnabled(true);

                notifyMeMaturityTextView.setVisibility(View.GONE);
                myRadioGroup.setVisibility(View.VISIBLE);
                btn_save.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.VISIBLE);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalMethods.haveNetworkConnection(NotificationSettingFragment.this.getActivity())) {
                    if (validation(v)) {
                    /* TODO : If necessary change */
                        ViewDialog.showProgress(R.string.help_screen_one, NotificationSettingFragment.this.getActivity(), getResources().getString(R.string.please_wait));
                        myRadioGroup.setVisibility(View.GONE);
                        actualSavingPerMonthEditText.clearFocus();
                        actualremainingTermEditText.clearFocus();

                        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
                        stringObjectHashMap.put("notify_saving_per_month", actualSavingPerMonthEditText.getText().toString().replace(",", ""));
                        stringObjectHashMap.put("notify_saving_over_term", actualremainingTermEditText.getText().toString().replace(",", ""));

                        if (actualRateIncreaseTextView.getText().toString().equalsIgnoreCase("0.10%")) {
                            stringObjectHashMap.put("notify_rate_increase", "10");
                        } else if (actualRateIncreaseTextView.getText().toString().equalsIgnoreCase("0.15%")) {
                            stringObjectHashMap.put("notify_rate_increase", "15");
                        } else if (actualRateIncreaseTextView.getText().toString().equalsIgnoreCase("0.20%")) {
                            stringObjectHashMap.put("notify_rate_increase", "20");
                        } else if (actualRateIncreaseTextView.getText().toString().equalsIgnoreCase("0.25%")) {
                            stringObjectHashMap.put("notify_rate_increase", "25");
                        }
                        stringObjectHashMap.put("notify_until_maturity", key);

                        HashMap<String, Object> values1 = new HashMap<>();
                        values1.put("updateType", "notification");
                        if (facebookUserLoginFlag) {
                            values1.put("_id", facebookUserLoginId);
                        } else if (googleUserLoginFlag) {
                            values1.put("_id", googleUserLoginId);
                        } else {
                            values1.put("_id", username);
                        }
                        values1.put("profile", stringObjectHashMap);

                        ErisCollectionManager.getInstance().callMethod("updateUser", new Object[]{values1}, new ResultListener() {
                            @Override
                            public void onSuccess(String s) {
                                myRadioGroup.setVisibility(View.GONE);
                                if (CommonConstants.mDebug) Log.v(TAG, "Update onSuccess " + s);
                                setVisibility();
                                notifyMeMaturityTextView.setVisibility(View.VISIBLE);
                                btn_edit.setVisibility(View.VISIBLE);
                                if (ViewDialog.kProgressHUD.isShowing()) {
                                    ViewDialog.hideProgress();
                                }
                            }

                            @Override
                            public void onError(String s, String s1, String s2) {
                                if (CommonConstants.mDebug) Log.v(TAG, "On Error Update :" + s1);
                            }
                        });
                    }

                } else {
                    if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(NotificationSettingFragment.this.getActivity())) {
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    }
                }

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setUserData();
                 /* If necessary change */
                myRadioGroup.setVisibility(View.GONE);
                btn_edit.setVisibility(View.VISIBLE);
                actualSavingPerMonthEditText.setEnabled(false);
                actualSavingPerMonthEditText.setTypeface(actualSavingPerMonthEditText.getTypeface(), Typeface.NORMAL);
                actualremainingTermEditText.setEnabled(false);
                actualremainingTermEditText.setTypeface(actualremainingTermEditText.getTypeface(), Typeface.NORMAL);
                rateIncreaseSpinnerRelativeLayout.setEnabled(false);
                notifyMeMaturityTextView.setVisibility(View.VISIBLE);
                myRadioGroup.setVisibility(View.GONE);
                btn_save.setVisibility(View.GONE);
                btn_cancel.setVisibility(View.GONE);
                /*Hide soft keyboard.*/
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });


        return rootview;
    }

    @Override
    public void onDestroyView() {
        if (alert != null && alert.isShowing())
            alert.dismiss();
        super.onDestroyView();
    }

    public void initialise(View view) {
        actualSavingPerMonthEditText = (EditText) view.findViewById(R.id.actualSavingPerMonthTextView);
        actualremainingTermEditText = (EditText) view.findViewById(R.id.actualremainingTermTextView);
        actualRateIncreaseTextView = (TextView) view.findViewById(R.id.actualRateIncreaseTextView);
        notifyMeMaturityTextView = (TextView) view.findViewById(R.id.notifyMeMaturityTextView);
        btn_edit = (Button) view.findViewById(R.id.btn_edit);
        btn_save = (Button) view.findViewById(R.id.btn_save);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        myRadioGroup = (RadioGroup) view.findViewById(R.id.myRadioGroup);
        sound = (RadioButton) view.findViewById(R.id.sound);
        vibration = (RadioButton) view.findViewById(R.id.vibration);
        silent = (RadioButton) view.findViewById(R.id.silent);
        rateIncreaseSpinnerRelativeLayout = (RelativeLayout) view.findViewById(R.id.rateIncreaseSpinnerRelativeLayout);
    }

    public void setUserData() {

        if (facebookUserLoginFlag) {
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("userId", facebookUserLoginId);
            ErisCollectionManager.getInstance().callMethod("getUser", new Object[]{stringObjectHashMap}, new ResultListener() {
                @Override
                public void onSuccess(String jsonResponse) {
                    if (CommonConstants.mDebug) Log.v(TAG, jsonResponse);
                    try {
                        JSONObject jsonRootObject = new JSONObject(jsonResponse);
                        if (jsonRootObject.getString("status").equals("true")) {
                            JSONObject jsonObjectData = jsonRootObject.getJSONObject("data");
                            JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                            /*Check for formatter*/
                            if (!jsonObjectResult.getString("notify_saving_per_month").contains(".")) {
                                actualSavingPerMonthEditText.setText(jsonObjectResult.getString("notify_saving_per_month") + ".00");
                            } else {
                                actualSavingPerMonthEditText.setText(jsonObjectResult.getString("notify_saving_per_month"));
                            }
                            /*Check for formatter*/
                            if (!jsonObjectResult.getString("notify_saving_over_term").contains(".")) {
                                actualremainingTermEditText.setText(jsonObjectResult.getString("notify_saving_over_term") + ".00");
                            } else {
                                actualremainingTermEditText.setText(jsonObjectResult.getString("notify_saving_over_term"));
                            }


                             /*Check for bps text */
                            if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("10")) {
                                actualRateIncreaseTextView.setText("0.10%");
                            } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("15")) {
                                actualRateIncreaseTextView.setText("0.15%");
                            } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("20")) {
                                actualRateIncreaseTextView.setText("0.20%");
                            } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("25")) {
                                actualRateIncreaseTextView.setText("0.25%");
                            }


                            /* Check for "Days Until Maturity" text */
                            if (!jsonObjectResult.getString("notify_until_maturity").contains("Days Until Maturity")) {
                                notifyMeMaturityTextView.setText(jsonObjectResult.getString("notify_until_maturity") + " Days Until Maturity");
                            } else {
                                notifyMeMaturityTextView.setText(jsonObjectResult.getString("notify_until_maturity"));
                            }
                            radioButtonCheck(jsonObjectResult.getString("notify_until_maturity"));

                            ViewDialog.hideProgress();

                        }else if (jsonRootObject.getString("status").equals("false"))
                        {
                            JSONObject jsonObjectError = jsonRootObject.getJSONObject("error");
                            String code = jsonObjectError.getString("code");
                            if (code.equals("10005")) {
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String s, String s1, String s2) {
                    Log.v(TAG + "FB error", "On Error" + s1);
                    toastMessage.showToastMsg(s1, Toast.LENGTH_SHORT);
                    if (ViewDialog.kProgressHUD.isShowing()) {
                        ViewDialog.hideProgress();
                    }

                    ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                        @Override
                        public void onConnect(boolean value) {
                            if (value) setUserData();
                        }

                        @Override
                        public void onDisconnect() {
                            ViewDialog.hideProgress();
                            if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(NotificationSettingFragment.this.getActivity())) {
                                toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onException(Exception e) {

                        }

                        @Override
                        public void onInternetStatusChanged(boolean status) {
                            if (status) setUserData();
                        }
                    });
                }
            });

        } else if (googleUserLoginFlag) {
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("userId", googleUserLoginId);
            ErisCollectionManager.getInstance().callMethod("getUser", new Object[]{stringObjectHashMap}, new ResultListener() {
                @Override
                public void onSuccess(String result) {
                    if (CommonConstants.mDebug) Log.v(TAG, result);
                    try {
                        JSONObject jsonRootObject = new JSONObject(result);
                        if (jsonRootObject.getString("status").equals("true")) {
                            JSONObject jsonObjectData = jsonRootObject.getJSONObject("data");
                            JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                            /*Check for formatter*/
                            if (!jsonObjectResult.getString("notify_saving_per_month").contains(".")) {
                                actualSavingPerMonthEditText.setText(jsonObjectResult.getString("notify_saving_per_month") + ".00");
                            } else {
                                actualSavingPerMonthEditText.setText(jsonObjectResult.getString("notify_saving_per_month"));
                            }
                            /*Check for formatter*/
                            if (!jsonObjectResult.getString("notify_saving_over_term").contains(".")) {
                                actualremainingTermEditText.setText(jsonObjectResult.getString("notify_saving_over_term") + ".00");
                            } else {
                                actualremainingTermEditText.setText(jsonObjectResult.getString("notify_saving_over_term"));
                            }

                             /*Check for bps text */
                            if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("10")) {
                                actualRateIncreaseTextView.setText("0.10%");
                            } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("15")) {
                                actualRateIncreaseTextView.setText("0.15%");
                            } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("20")) {
                                actualRateIncreaseTextView.setText("0.20%");
                            } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("25")) {
                                actualRateIncreaseTextView.setText("0.25%");
                            }

                             /*Check for "Days Until Maturity" text*/
                            if (!jsonObjectResult.getString("notify_until_maturity").contains("Days Until Maturity")) {
                                notifyMeMaturityTextView.setText(jsonObjectResult.getString("notify_until_maturity") + " Days Until Maturity");
                            } else {
                                notifyMeMaturityTextView.setText(jsonObjectResult.getString("notify_until_maturity"));
                            }

                            radioButtonCheck(jsonObjectResult.getString("notify_until_maturity"));

                            ViewDialog.hideProgress();
                        }else if (jsonRootObject.getString("status").equals("false"))
                        {
                            JSONObject jsonObjectError = jsonRootObject.getJSONObject("error");
                            String code = jsonObjectError.getString("code");
                            if (code.equals("10005")) {
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(String error, String reason, String details) {
                    if (CommonConstants.mDebug) Log.v(TAG, "On Error" + reason);
                    toastMessage.showToastMsg(reason, Toast.LENGTH_SHORT);
                    if (ViewDialog.kProgressHUD.isShowing()) {
                        ViewDialog.hideProgress();
                    }

                    ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                        @Override
                        public void onConnect(boolean value) {
                            if (value) setUserData();
                        }

                        @Override
                        public void onDisconnect() {
                            ViewDialog.hideProgress();
                            if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(NotificationSettingFragment.this.getActivity())) {
                                toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onException(Exception e) {

                        }

                        @Override
                        public void onInternetStatusChanged(boolean status) {
                            if (status) setUserData();
                        }
                    });
                }
            });

        } else if (!facebookUserLoginFlag && !googleUserLoginFlag) {
            facebookUserLoginFlag = false;
            googleUserLoginFlag = false;
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("userId", username);
            ErisCollectionManager.getInstance().callMethod("getUser", new Object[]{stringObjectHashMap}, new ResultListener() {
                @Override
                public void onSuccess(String s) {
                    if (CommonConstants.mDebug) Log.v(TAG, s);
                    try {
                        JSONObject jsonRootObject = new JSONObject(s);
                        if (jsonRootObject.getString("status").equals("true")) {
                            JSONObject jsonObjectData = jsonRootObject.getJSONObject("data");
                            JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                            /*Check for formatter*/
                            if (!jsonObjectResult.getString("notify_saving_per_month").contains(".")) {
                                actualSavingPerMonthEditText.setText(jsonObjectResult.getString("notify_saving_per_month") + ".00");
                            } else {
                                actualSavingPerMonthEditText.setText(jsonObjectResult.getString("notify_saving_per_month"));
                            }
                            /*Check for formatter*/
                            if (!jsonObjectResult.getString("notify_saving_over_term").contains(".")) {
                                actualremainingTermEditText.setText(jsonObjectResult.getString("notify_saving_over_term") + ".00");
                            } else {
                                actualremainingTermEditText.setText(jsonObjectResult.getString("notify_saving_over_term"));
                            }


                            /*Check for bps text */
                            if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("10")) {
                                actualRateIncreaseTextView.setText("0.10%");
                            } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("15")) {
                                actualRateIncreaseTextView.setText("0.15%");
                            } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("20")) {
                                actualRateIncreaseTextView.setText("0.20%");
                            } else if (jsonObjectResult.getString("notify_rate_increase").equalsIgnoreCase("25")) {
                                actualRateIncreaseTextView.setText("0.25%");
                            }

                            /*Check for "Days Until Maturity" text*/
                            if (!jsonObjectResult.getString("notify_until_maturity").contains("Days Until Maturity")) {
                                notifyMeMaturityTextView.setText(jsonObjectResult.getString("notify_until_maturity") + " Days Until Maturity");
                            } else {
                                notifyMeMaturityTextView.setText(jsonObjectResult.getString("notify_until_maturity"));
                            }

                            if (CommonConstants.mDebug)
                                Log.v(TAG, jsonObjectResult.getString("notify_until_maturity"));
                            radioButtonCheck(jsonObjectResult.getString("notify_until_maturity"));
                            ViewDialog.hideProgress();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String s, String s1, String s2) {
                    ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                        @Override
                        public void onConnect(boolean value) {
                            if (value) setUserData();
                        }

                        @Override
                        public void onDisconnect() {
                            ViewDialog.hideProgress();
                            if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(NotificationSettingFragment.this.getActivity())) {
                                toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onException(Exception e) {

                        }

                        @Override
                        public void onInternetStatusChanged(boolean status) {
                            if (status) setUserData();
                        }
                    });
                }
            });

        }
    }


    public void radioButtonCheck(String value) {
        if (value.equalsIgnoreCase("90 Days Until Maturity") || value.equalsIgnoreCase("90")) {
            sound.setChecked(true);
        } else if (value.equalsIgnoreCase("120 Days Until Maturity") || value.equalsIgnoreCase("120")) {
            vibration.setChecked(true);
        } else if (value.equalsIgnoreCase("180 Days Until Maturity") || value.equalsIgnoreCase("180")) {
            silent.setChecked(true);
        }
    }


    public void setVisibility() {
        actualSavingPerMonthEditText.setEnabled(false);
        actualremainingTermEditText.setEnabled(false);
        rateIncreaseSpinnerRelativeLayout.setEnabled(false);
        myRadioGroup.setVisibility(View.GONE);
        btn_save.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
    }

    public boolean validation(View view) {
        boolean bln = true;
        if (actualSavingPerMonthEditText.getText().toString().equals("") && actualremainingTermEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_information_in_all_fields, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualSavingPerMonthEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_saving_amt_per_month, Toast.LENGTH_SHORT);
            bln = false;
        } else if (actualremainingTermEditText.getText().toString().equals("")) {
            toastMessage.showToastMsg(R.string.enter_saving_amt_over_remaining_term, Toast.LENGTH_SHORT);
            bln = false;
        }

        return bln;
    }
}
