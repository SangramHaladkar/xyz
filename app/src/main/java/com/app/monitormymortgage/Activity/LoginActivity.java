package com.app.monitormymortgage.Activity;

import android.Manifest;
import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.Fragments.HelpScreenFifthFragment;
import com.app.monitormymortgage.MainActivity;
import com.app.monitormymortgage.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;


public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button facebookButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button createNewAccountButton;
    private Button signInButton;
    TextView forgotResetPasswordTextView;
    String email;
    String password;
    String statusCode;
    String id;
    LoginButton loginButton;
    Profile profile;

    private static final int SIGN_IN_CODE = 0;
    KProgressHUD kProgressHUD;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private GoogleApiClient mGoogleApiClient;
    public static boolean googleFlag = false;
    public static String googleUserLoginId;

    ImageView imageView;
    static final String PREF_USER_NAME = "username";
    ViewDialog viewDialog;
    ToastMessage toastMessage;

    public static String googleId;
    public static String googleAccessToken;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (CommonConstants.mDebug) Log.v(TAG, "onCreate");
        viewDialog = new ViewDialog(this);
        toastMessage = new ToastMessage(this);
        profile = Profile.getCurrentProfile();
        if (profile != null) {
            LoginManager.getInstance().logOut();
        }

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton googlePlusButton = (SignInButton) findViewById(R.id.googlePlusButton);
        imageView = (ImageView) findViewById(R.id.imageView);
        assert googlePlusButton != null;
        googlePlusButton.setSize(SignInButton.SIZE_STANDARD);
        googlePlusButton.setScopes(gso.getScopeArray());
        googlePlusButton.setOnClickListener(this);
        kProgressHUD = new KProgressHUD(this);
        kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        kProgressHUD.setCancellable(false);
        kProgressHUD.setAnimationSpeed(2);
        kProgressHUD.setDimAmount(0.5f);
        kProgressHUD.setLabel("Please wait...");
        viewDialog = new ViewDialog(this);
        initializeUIComponant();
        setGooglePlusButtonText(googlePlusButton, R.string.google);
        createNewAccount();
        signInButtonClick();
        forgotResetPasswordTextViewClick();

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalMethods.haveNetworkConnection(LoginActivity.this)) {
                    ViewDialog.showProgress(R.string.help_screen_one, LoginActivity.this, getResources().getString(R.string.please_wait));
                    if (!MeteorSingleton.getInstance().isConnected()) {
                        MeteorSingleton.getInstance().reconnect();
                        loginButton.performClick();
                    } else {
                        loginButton.performClick();
                    }
                } else {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                    ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.no_network_found), getResources().getString(R.string.error));

                }
            }
        });


        if (HelpScreenFifthFragment.googleSilentLoginFlag) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                }
            } else {
                signIn();
            }
        }

        BroadcastReceiver receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("TAG","Activity finish BR");
                String action=intent.getAction();
                if (action.equalsIgnoreCase("finish_acivity")){
                    finish();
                }
            }
        };
        registerReceiver(receiver,new IntentFilter("finish_acivity"));
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, int buttonText) {
        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                tv.getCompoundPaddingLeft();
                tv.setTextSize(17);
                return;
            }
        }
    }


    public void forgotResetPasswordTextViewClick() {

        forgotResetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalMethods.haveNetworkConnection(LoginActivity.this)) {
                    viewDialog.showAlertPopupPhoneNumber(LoginActivity.this, R.string.forgot_reset_password_step_one_title);
                } else {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                    ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.no_network_found), getResources().getString(R.string.error));
                }
            }
        });
    }


    public void initializeUIComponant() {
        try {
            facebookButton = (Button) findViewById(R.id.facebookButton);
            createNewAccountButton = (Button) findViewById(R.id.createAccountButton);
            signInButton = (Button) findViewById(R.id.signInButton);
            emailEditText = (EditText) findViewById(R.id.userNameEditText);
            passwordEditText = (EditText) findViewById(R.id.passwordEditText);
            passwordEditText.setTypeface(Typeface.DEFAULT);
            passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            loginButton = (LoginButton) findViewById(R.id.login_button);
            forgotResetPasswordTextView = (TextView) findViewById(R.id.forgotResetPasswordTextView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createNewAccount() {
        createNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalMethods.haveNetworkConnection(LoginActivity.this)) {
                    Intent intent = new Intent(LoginActivity.this, CreateNewAccountActivity.class);
                    startActivity(intent);
                } else {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                    ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.no_network_found), getResources().getString(R.string.error));
                }
            }
        });
    }

    public void signInButtonClick() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GlobalMethods.haveNetworkConnection(LoginActivity.this)) {
                    email = emailEditText.getText().toString();
                    password = passwordEditText.getText().toString();
                    if (email.isEmpty()) {
                        ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.alert_username_empty), getResources().getString(R.string.error));
                    } else if (password.isEmpty()) {
                        ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.alert_password_empty), getResources().getString(R.string.error));
                    } else {
                        kProgressHUD.show();
//                    progress_dialog.show();
                        if (MeteorSingleton.getInstance().isConnected()) {
                            // do login
                            ErisCollectionManager.getInstance().loginWithPassword(email, password, new ResultListener() {

                                @Override
                                public void onSuccess(String result) {

                                    passwordEditText.setText("");
                                    if (CommonConstants.mDebug)
                                        Log.v(TAG, "Login onSuccess :" + result);
                                    if (CommonConstants.mDebug)
                                        Log.v(TAG, "Is logged in: " + MeteorSingleton.getInstance().isLoggedIn());
                                    if (CommonConstants.mDebug)
                                        Log.v(TAG, "User ID: " + MeteorSingleton.getInstance().getUserId());
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                    sharedPreferences.edit().putString("userid", MeteorSingleton.getInstance().getUserId()).apply();
                                    googleFlag = false;
                                    facebookFlag = false;

                                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("FacebookUserLoginFlag", facebookFlag).apply();
                                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("GoogleUserLoginFlag", googleFlag).apply();
                                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(PREF_USER_NAME, MeteorSingleton.getInstance().getUserId()).apply();
                                    checkUserRole(MeteorSingleton.getInstance().getUserId());
                                }

                                @Override
                                public void onError(String error, String reason, String details) {
                                    if (reason.equalsIgnoreCase("incorrect password")) {
                                        ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.alert_invalid_email), getResources().getString(R.string.error));
                                        kProgressHUD.dismiss();
                                    } else if (reason.equalsIgnoreCase("User not found")) {
                                        ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.alert_invalid_email), getResources().getString(R.string.error));
                                        kProgressHUD.dismiss();
                                    } else {
                                        ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.alert_invalid_email), getResources().getString(R.string.error));
                                        kProgressHUD.dismiss();
                                    }
                                }
                            });

                        } else {
                            MeteorSingleton.getInstance().reconnect();
                            ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.error_connecting_server), getResources().getString(R.string.error));
                            kProgressHUD.dismiss();

                        }
                    }
                } else {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                    ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.no_network_found), getResources().getString(R.string.error));
                }
            }
        });
    }


    public void checkUserRole(final String userId) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("userId", userId);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("checkUserRole", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    if (jsonObjectMain.getString("status").equalsIgnoreCase("true")) {
                        //Intercom
                        CommonConstants.registerIntercom(userId);

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        kProgressHUD.dismiss();
                        startActivity(intent);
                        finish();
                    } else if (jsonObjectMain.getString("status").equalsIgnoreCase("false")) {
                        JSONObject jsonObjectError = jsonObjectMain.getJSONObject("error");
                        ViewDialog.showAlertPopUp(LoginActivity.this, jsonObjectError.getString("message"), getResources().getString(R.string.error));
                        kProgressHUD.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, String reason, String details) {
                if (CommonConstants.mDebug) Log.v(TAG, reason);
                kProgressHUD.dismiss();

            }
        });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.googlePlusButton:
                if (CommonConstants.mDebug) Log.v(TAG, "start sign process");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
                    if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_ASK_PERMISSIONS);
                    }
                } else {
                    signIn();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    signIn();
                } else {
                    // Permission Denied
                    Toast.makeText(LoginActivity.this, "GET_Accounts Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    // [START signIn]
    private void signIn() {
        if (GlobalMethods.haveNetworkConnection(LoginActivity.this)) {
            if (!MeteorSingleton.getInstance().isConnected()) {
                MeteorSingleton.getInstance().reconnect();
            }
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, SIGN_IN_CODE);
            HelpScreenFifthFragment.googleSilentLoginFlag = false;
        } else {
            toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//            ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.no_network_found), getResources().getString(R.string.error));
        }
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {

                        // [START_EXCLUDE]
                        if (CommonConstants.mDebug)
                            Log.v("MainActivity ", "Successfully logout from google" + status);
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove("GoogleUserLoginId").apply();
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove("GoogleUserLoginFlag").apply();
                        // [END_EXCLUDE]
                    }
                });
    }

    /*
         Will receive the activity result and check which request we are responding to

        */
    @Override
    public void onActivityResult(int requestCode, int responseCode,
                                 Intent intent) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, responseCode, intent);
        if (requestCode == SIGN_IN_CODE) {
            if (responseCode != RESULT_OK) {
                kProgressHUD.dismiss();
            }

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        }
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        if (CommonConstants.mDebug) Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            final GoogleSignInAccount acct = result.getSignInAccount();

            try {
                AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        String token = "";
                        try {
                            assert acct != null;
                            final Account account = new Account(acct.getEmail(), GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                            final String scope = "oauth2:" + Scopes.PROFILE;

                            token = GoogleAuthUtil.getToken(LoginActivity.this, account, scope);
                        } catch (IOException | GoogleAuthException e) {
                            e.printStackTrace();
                        }
                        return token;
                    }

                    @Override
                    protected void onPostExecute(String token) {
                        getData(acct, token);
                    }
                };
                task.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Signed out, show unauthenticated UI.
        }
    }
    // [END handleSignInResult]

    public void getData(final GoogleSignInAccount acct, final String accessTokanGoogle) {
        try {
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("GoogleToken", acct.getId()).apply();
            String s = acct.getDisplayName();
            googleId = acct.getId();
            googleAccessToken = accessTokanGoogle;
            String[] splitString;
            splitString = s != null ? s.split("\\s+") : new String[0];
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("GoogleFirstName", splitString[0]).apply();
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("GoogleLastName", splitString[1]).apply();
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("GoogleEmail", acct.getEmail()).apply();
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("GoogleIDToken", accessTokanGoogle).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
        kProgressHUD.dismiss();
//        progress_dialog.dismiss();

        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("type", "google");
        stringObjectHashMap.put("value", acct.getId());
        stringObjectHashMap.put("email", acct.getEmail());

        ErisCollectionManager.getInstance().callMethod("isUserExist", new Object[]{stringObjectHashMap}, new ResultListener() {
                    @Override
                    public void onSuccess(String s) {
                        Log.v(TAG, "onSuccess" + s);
                        try {
                            JSONObject jsonRootObject = new JSONObject(s);
                            statusCode = jsonRootObject.getString("status");
                            if (CommonConstants.mDebug) Log.v(TAG, statusCode);
                            if (statusCode.equalsIgnoreCase("false")) {
                                JSONObject jsonObject = jsonRootObject.getJSONObject("error");
                                jsonObject.getString("code");
                                jsonObject.getString("message");

                                Intent intent = new Intent(LoginActivity.this, GoogleRegisterActivity.class);
                                startActivity(intent);

                            } else {
                                JSONObject jsonRootObjectTrue = new JSONObject(s);
                                statusCode = jsonRootObjectTrue.getString("status");
                                if (statusCode.equalsIgnoreCase("true")) {
                                    JSONObject jsonObject1 = jsonRootObject.getJSONObject("data");
                                    String message = jsonObject1.getString("message");
                                    if (message.equalsIgnoreCase("User email already exists.")) {
                                        ViewDialog.showAlertPopUp(LoginActivity.this, getResources().getString(R.string.user_already_exist_with_email), getResources().getString(R.string.error));
                                        /* Change if not needed.*/
                                        signOut();
                                    } else {
                                        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
                                        stringObjectHashMap.put("lang", "en");
                                        stringObjectHashMap.put("req_from", CommonConstants.requestFrom);
                                        stringObjectHashMap.put("id", acct.getId());
                                        stringObjectHashMap.put("socialType", "google");
                                        stringObjectHashMap.put("accessToken", accessTokanGoogle);
                                        stringObjectHashMap.put("profile", "{}");
                                        ErisCollectionManager.getInstance().callMethod("socialLoginWithAccessToken", new Object[]{stringObjectHashMap}, new ResultListener() {
                                            @Override
                                            public void onSuccess(String result) {
                                                Log.v("TAG Google Login", result);
                                                try {
                                                    JSONObject jsonRootObject = new JSONObject(result);
                                                    jsonRootObject.getString("status");
                                                    if (jsonRootObject.getString("status").equals("true")) {
                                                        JSONObject jsonObject = jsonRootObject.getJSONObject("data");
                                                        jsonObject.getString("code");
                                                        jsonObject.getString("message");
                                                        JSONObject jsonObject1 = jsonObject.getJSONObject("result");
                                                        jsonObject1.getString("type");
                                                        jsonObject1.getString("userId");
                                                        if (CommonConstants.mDebug)
                                                            Log.v(TAG, jsonObject1.getString("userId"));
                                                        googleUserLoginId = jsonObject1.getString("userId");
                                                        googleFlag = true;
                                                        facebookFlag = false;
//                                                        final SharedPreferences prefs = getSharedPreferences();
//                                                        final SharedPreferences.Editor editor = prefs.edit();
//                                                        editor.putString(Preferences.Keys.LOGIN_TOKEN, result);
//                                                        editor.apply();
                                                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("FacebookUserLoginFlag", facebookFlag).apply();
                                                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean("GoogleUserLoginFlag", googleFlag).apply();
                                                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("GoogleUserLoginId", googleUserLoginId).apply();
                                                        checkUserRole(googleUserLoginId);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onError(String s, String s1, String s2) {
                                                Log.v("TAG G+ loginError", s1);
                                            }
                                        });
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String s, String s1, String s2) {
                        if (CommonConstants.mDebug) Log.v(TAG, s1);
                    }
                }

        );

    }
}