package com.app.monitormymortgage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.monitormymortgage.Fragments.DashboardFragment;
import com.app.monitormymortgage.Fragments.PastOpportunityFragment;
import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.facebook.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.app.monitormymortgage.Activity.LoginActivity;
import com.app.monitormymortgage.Activity.NotificationListActivity;
import com.app.monitormymortgage.Activity.ShowImagesActivity;
import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.DataHolderClasses.UserObject;
import com.app.monitormymortgage.Fragments.FAQFragment;
import com.app.monitormymortgage.Fragments.MyDashboardFragment;
import com.app.monitormymortgage.Fragments.MyM3AccountFragment;
import com.app.monitormymortgage.Fragments.NotificationSettingFragment;
import com.app.monitormymortgage.Fragments.PrivacyPolicyFragment;
import com.app.monitormymortgage.Fragments.TermsAndConditionFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.UnreadConversationCountListener;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {
    FragmentManager fragmentManager;
    Profile profile;
    private GoogleApiClient mGoogleApiClient;
    UserObject userObject;

    private static final String TAG = "MainActivity";
    String username;
    String facebookUserLoginId;
    boolean facebookUserLoginFlag;
    String googleUserLoginId;
    boolean googleUserLoginFlag;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    GlobalMethods globalMethods;
    Context mContext;
    ToastMessage toastMessage;
    private int unreadIntercomMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fragmentManager = getSupportFragmentManager();
        globalMethods = new GlobalMethods(this);
        mContext = this;
        toastMessage = new ToastMessage(mContext);
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");
        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(this).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(this).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("GoogleUserLoginFlag", false);

        // Intercom - Listen for new conversation activity
        unreadIntercomMessages = 0;
        Intercom.client().addUnreadConversationCountListener(unreadConversationCountListener);

        /*
        * Google configuration
        * */

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

                if (navigationView != null) {
                    MenuItem helpMenuItem = navigationView.getMenu().findItem(R.id.nav_help);
                    if (helpMenuItem != null) {
                        // No messages
                        if (unreadIntercomMessages == 0) {
                            helpMenuItem.setTitle("Help");
                        }

                        // At least one message
                        else {
                            Spannable multicolorText = new SpannableString("Help - new message!");
                            multicolorText.setSpan(new ForegroundColorSpan(Color.YELLOW), 7, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            helpMenuItem.setTitle(multicolorText);
                        }
                    }
                }
            }
        };

        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();
        userObject = new UserObject();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
        }

        globalMethods.replaceFragment(new MyDashboardFragment(), this, null);
    }

    private final UnreadConversationCountListener unreadConversationCountListener = new UnreadConversationCountListener() {
        @Override
        public void onCountUpdate(int unreadCount) {
            Log.i("Intercom", "UnreadConversationCountListener - Count is: " + unreadCount);

            unreadIntercomMessages = unreadCount;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    //signIn();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onResume() {
        invalidateOptionsMenu();

        if (!ShowImagesActivity.flagChanged) {
            globalMethods.replaceFragment(new MyDashboardFragment(), this, null);
//            MyDashboardFragment myDashboardFragment = new MyDashboardFragment();
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.container, myDashboardFragment).commit();
        }
        ShowImagesActivity.flagChanged = false;

        super.onResume();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (CommonConstants.mDebug) Log.v("TAG", fragments.toString());
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment instanceof MyDashboardFragment) {
                super.onBackPressed();
            } else {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new MyDashboardFragment()).commit();
                assert drawer != null;
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try {
            getMenuInflater().inflate(R.menu.main, menu);
            menu.findItem(R.id.closeButton).setVisible(false);
            getNotifications(menu);
            return super.onCreateOptionsMenu(menu);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getNotifications(final Menu menu) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        if (facebookUserLoginFlag) {
            stringObjectHashMap.put("user_id", facebookUserLoginId);
        } else if (googleUserLoginFlag) {
            stringObjectHashMap.put("user_id", googleUserLoginId);
        } else {
            stringObjectHashMap.put("user_id", username);
        }
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("getUserUnreadNotificationCount", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    String notificationCnt = jsonObjectResult.getString("notification_count");

                    // Add together the notifications and unread Help messages
                    int totalNotifications = Integer.parseInt(notificationCnt) + unreadIntercomMessages;
                    notificationCnt = String.valueOf(totalNotifications);

                    if (notificationCnt.equals("0")) {
                        menu.findItem(R.id.icon).setVisible(true);
                        menu.findItem(R.id.closeButton).setVisible(false);
                        menu.findItem(R.id.badge).setVisible(false);
                        menu.findItem(R.id.icon).setEnabled(false);
                    } else if (!notificationCnt.equals("0")) {
                        MenuItem menuItem = menu.findItem(R.id.badge);
                        menu.findItem(R.id.icon).setVisible(false);
                        menu.findItem(R.id.closeButton).setVisible(false);
                        MenuItemCompat.setActionView(menuItem, R.layout.badge_menu);
                        FrameLayout relativeLayout = (FrameLayout) MenuItemCompat.getActionView(menuItem);
                        TextView bellTextView = (TextView) relativeLayout.findViewById(R.id.bellTextView);
                        TextView textView = (TextView) relativeLayout.findViewById(R.id.actionbar_notifcation_textview);
                        textView.setText(notificationCnt);
                        bellTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, NotificationListActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        getNotifications(menu);
                    }

                    @Override
                    public void onDisconnect() {

                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        getNotifications(menu);
                    }
                });
            }
        });
    }


    /*
    * Navigation view
    * */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MainActivity.this)) {
            toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
        } else {
            if (id == R.id.nav_dashboard) {
                globalMethods.replaceFragment(new MyDashboardFragment(), this, null);
            } else if (id == R.id.nav_lapsed_opportunity) {
                globalMethods.replaceFragment(new PastOpportunityFragment(), this, null);
            } else if (id == R.id.nav_my_m3_account) {
                globalMethods.replaceFragment(new MyM3AccountFragment(), this, null);
            } else if (id == R.id.nav_notification_settings) {
                globalMethods.replaceFragment(new NotificationSettingFragment(), this, null);
            } else if (id == R.id.nav_faq) {
                globalMethods.replaceFragment(new FAQFragment(), this, null);
            }

            // Help
            else if (id == R.id.nav_help) {
                // Intercom
                Intercom.client().displayMessenger();
            } else if (id == R.id.nav_terms_condition) {
                globalMethods.replaceFragment(new TermsAndConditionFragment(), this, null);
            } else if (id == R.id.nav_privacy_policy) {
                globalMethods.replaceFragment(new PrivacyPolicyFragment(), this, null);
            } else if (id == R.id.nav_logout) {
                logOutMeteor();
            } else {
                globalMethods.replaceFragment(new MyDashboardFragment(), this, null);
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            assert drawer != null;
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    /*
    * Logout from Meteor
    *
    * */

    public void logOutMeteor() {
        // Intercom
        Intercom.client().reset();

        try {
            if (MeteorSingleton.getInstance().isConnected()) {
                MeteorSingleton.getInstance().logout(new ResultListener() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            if (CommonConstants.mDebug)
                                Log.v("MainActivity", "Logout onSuccess" + result);
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove("username").apply();
                            finish();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            profile = Profile.getCurrentProfile();

                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove("FacebookUserLoginId").apply();
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove("FacebookUserLoginFlag").apply();

                            signOut();

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(String error, String reason, String details) {
                        if (CommonConstants.mDebug) Log.v("onError", reason);
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "Error : Unable to logout,Server connection is lost.", Toast.LENGTH_SHORT).show();
                MeteorSingleton.getInstance().reconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (CommonConstants.mDebug) Log.v("TAG", e.toString());
        }
    }

    /*
    * Signout from Google.
    * */
    public void signOut() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {

                            // [START_EXCLUDE]
                            Log.v("MainActivity ", "Successfully logout from google" + status);
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove("GoogleUserLoginId").apply();
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().remove("GoogleUserLoginFlag").apply();

                            // [END_EXCLUDE]
                        }
                    });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

}
