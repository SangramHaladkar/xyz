package com.app.monitormymortgage.BaseClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.app.monitormymortgage.Activity.FacebookRegisterActivity;
import com.app.monitormymortgage.Activity.LoginActivity;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.GCM.RegistrationIntentService;
import com.app.monitormymortgage.MainActivity;
import com.app.monitormymortgage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BaseActivity extends AppCompatActivity implements ErisConnectionListener {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private BroadcastReceiver pushNotificationMsgBroadcastReceiver;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private boolean isReceiverRegistered;
    String email;
    String statusCode;
    private CallbackManager callbackManager;
    public static String id;
    public static String accessToken;
    Profile profile;
    public static boolean facebookFlag = false;
    public static String facebookUserLoginId;
    HashMap<String, Object> stringObjectHashMap;
//    public static boolean isInBackground = false;

    boolean facebookUserLoginFlag;
    boolean googleUserLoginFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(BaseActivity.this);
        setContentView(R.layout.activity_base);
        pushNotificationMsgBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("GCM", "Intent Received");
                if (intent.getExtras() != null) {
                    String msg = intent.getExtras().getString("message");
                    String title = intent.getExtras().getString("title");
                    ViewDialog.showNotifications(BaseActivity.this, msg);
                }
            }
        };
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
            Log.i("GCM", "Registering intent");
        }

        ErisCollectionManager.getInstance().setConnectionListener(this);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (CommonConstants.mDebug) Log.v(TAG, "onSuccess");

                accessToken = loginResult.getAccessToken().getToken();
                if (CommonConstants.mDebug) Log.v(TAG, accessToken);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {

                            Log.v("JsonObject", object.toString());
                            Log.v("Grapf response", response.toString());
//                            profile = Profile.getCurrentProfile();
//                            Log.v("Profile", profile.toString());
//                            if (profile != null) {
                            Log.i(TAG, "Facebook Object :" + object);
                            id = object.getString("id");
                            stringObjectHashMap = new HashMap<String, Object>();
                            if (object.has("email")) {
                                String email = object.getString("email");
                                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("FacebookEmail", email).apply();
                                stringObjectHashMap.put("email", email);
                            } else {
                                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove("FacebookEmail").apply();
                                if (CommonConstants.mDebug)
                                    Log.v(TAG, "facebook email not found");
                            }
                            String firstName = object.getString("first_name");
                            String lastName = object.getString("last_name");
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("FacebookId", id).apply();
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("FacebookFirstName", firstName).apply();
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("FacebookLastName", lastName).apply();
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("AccessTokan", accessToken).apply();

                            stringObjectHashMap.put("type", "facebook");
                            stringObjectHashMap.put("value", id);
                            stringObjectHashMap.put("email", email);

                            ErisCollectionManager.getInstance().callMethod("isUserExist", new Object[]{stringObjectHashMap}, new ResultListener() {
                                @Override
                                public void onSuccess(String jsonResponse) {
                                    try {
                                        JSONObject jsonRootObject = new JSONObject(jsonResponse);
                                        statusCode = jsonRootObject.getString("status");

                                        if (statusCode.equalsIgnoreCase("false")) {
                                            JSONObject jsonObject = jsonRootObject.getJSONObject("error");
                                            jsonObject.getString("code");
                                            jsonObject.getString("message");

//                                                if (profile != null) {
                                            Intent intent = new Intent(BaseActivity.this, FacebookRegisterActivity.class);
                                            startActivity(intent);
                                            finish();
//                                                }

                                        } else if (statusCode.equalsIgnoreCase("true")) {
                                            JSONObject jsonRootObjectTrue = new JSONObject(jsonResponse);
                                            statusCode = jsonRootObjectTrue.getString("status");
                                            if (statusCode.equalsIgnoreCase("true")) {
                                                HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
                                                stringObjectHashMap.put("lang", "en");
                                                stringObjectHashMap.put("req_from", CommonConstants.requestFrom);
                                                stringObjectHashMap.put("id", id);
                                                stringObjectHashMap.put("socialType", "facebook");
                                                stringObjectHashMap.put("accessToken", accessToken);
                                                stringObjectHashMap.put("profile", "{}");
                                                ErisCollectionManager.getInstance().callMethod("socialLoginWithAccessToken", new Object[]{stringObjectHashMap}, new ResultListener() {
                                                    @Override
                                                    public void onSuccess(String s) {
                                                        Log.i("TAG FB Login", s);
                                                        try {
                                                            JSONObject jsonRootObject = new JSONObject(s);
                                                            jsonRootObject.getString("status");
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
                                                                // Intercom
                                                                CommonConstants.registerIntercom(facebookUserLoginId);

                                                                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("FacebookUserLoginFlag", facebookFlag).apply();
                                                                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("FacebookUserLoginId", facebookUserLoginId).apply();
                                                                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(String s, String s1, String s2) {
                                                        Log.v("TAG FB Error", s1);
                                                    }
                                                });
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(String s, String s1, String s2) {
                                    Log.v(TAG, "onError" + s1);
                                }
                            });
//                            }else{
//                                Log.v("TAG","Profile is null");
//                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                if (CommonConstants.mDebug) Log.v(TAG, "onCancel");
                if (ViewDialog.kProgressHUD.isShowing()) {
                    ViewDialog.hideProgress();
                }
            }

            @Override
            public void onError(FacebookException error) {
                Log.v("FB error", error.toString());
            }
        });


        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("GoogleUserLoginFlag", false);
    }

    @Override
    protected void onResume() {
//        isInBackground = false;
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
//        isInBackground = true;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(pushNotificationMsgBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(pushNotificationMsgBroadcastReceiver, new IntentFilter("pushNotification"));
            isReceiverRegistered = true;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void recreateSession() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", CommonConstants.requestFrom);

        if (facebookUserLoginFlag) {
            stringObjectHashMap.put("id", BaseActivity.id);
            stringObjectHashMap.put("socialType", "facebook");
            stringObjectHashMap.put("accessToken", BaseActivity.accessToken);
        } else if (googleUserLoginFlag) {
            stringObjectHashMap.put("id", LoginActivity.googleId);
            stringObjectHashMap.put("socialType", "google");
            stringObjectHashMap.put("accessToken", LoginActivity.googleAccessToken);
        }
        stringObjectHashMap.put("profile", "{}");
        ErisCollectionManager.getInstance().callMethod("socialLoginWithAccessToken", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v("sessionRecreate_success", result);
            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.v("TAG session recreate", reason);

            }
        });
    }


    @Override
    public void onConnect(boolean value) {
        recreateSession();
    }

    @Override
    public void onDisconnect() {

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onInternetStatusChanged(boolean status) {
        if (status) {
            Log.v(TAG, "Connection Status : " + status);
            if (ErisCollectionManager.getInstance().isConnectedMeteor()) {
                Log.v(TAG, "Meteor Connection Status : " + MeteorSingleton.getInstance().isConnected());
            } else {
                ErisCollectionManager.getInstance().reconnectMeteor(this);
            }
        }
    }
}
