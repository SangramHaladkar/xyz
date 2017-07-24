package com.app.monitormymortgage.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ResultListener;
import com.facebook.FacebookSdk;
import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.MainActivity;
import com.app.monitormymortgage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SplashScreenActivity extends BaseActivity {
    public static int splash_time_out = 3000;
    SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private boolean viaNotification = false;
    String username;
    String countryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(SplashScreenActivity.this);
        setContentView(R.layout.activity_splash_screen);
//        getMasters();
        if (getIntent().getExtras() != null) {
            viaNotification = getIntent().getExtras().getBoolean("viaNotification", false);
        }

        if (!GetCountryZipCode(this).equalsIgnoreCase("")) {
            CommonConstants.countryCode = "+" + GetCountryZipCode(this);
        } else {
            CommonConstants.countryCode = "+91";
        }


        Log.v("TAG", "countryCode" + CommonConstants.countryCode);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
        ErisCollectionManager.getInstance().setPrintMeteorLog(false);
        ErisCollectionManager.getInstance().setSSL(true);  /* Change to true if url is https. */
        ErisCollectionManager.getInstance().createInstance(this, CommonConstants.stagingTekyzURLNew);
        ErisCollectionManager.getInstance().connect();
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString("CountryCode", countryCode);

        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
        username = mPrefs.getString("username", "");
        Log.v("TAG username", username);
        if (!welcomeScreenShown) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, HelpScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, splash_time_out);
        } else if (username.equals("") && welcomeScreenShown) {
            // here you can launch another activity if you like
            // the code below will display a popup

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, splash_time_out);
        } else {
            //    progressBar.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }, splash_time_out);
        }
        editor.putBoolean(welcomeScreenShownPref, true);
        editor.apply();
    }

    public static String GetCountryZipCode(Context context) {
        String CountryID = "";
        String CountryZipCode = "";

        TelephonyManager manager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        //getNetworkCountryIso
        CountryID = manager.getSimCountryIso().toUpperCase();
        String[] rl = context.getResources().getStringArray(R.array.CountryCodes);
        for (String aRl : rl) {
            String[] g = aRl.split(",");
            if (g[1].trim().equals(CountryID.trim())) {
                CountryZipCode = g[0];
                break;
            }
        }
        return "" + CountryZipCode;
    }

    public void getMasters() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("getAllMasters", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    JSONArray jsonArrayMasterDefault = jsonObjectResult.getJSONArray("master_defaults");

                    for (int i = 0; i < jsonArrayMasterDefault.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayMasterDefault.getJSONObject(i);
                        Log.v("TAG", jsonObject1.getString("key"));
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

}
