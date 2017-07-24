package com.app.monitormymortgage.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.app.monitormymortgage.Adapters.NotificationAdapter;
import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.DataHolderClasses.NotificationHolder;
import com.app.monitormymortgage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import io.intercom.android.sdk.Intercom;


public class NotificationListActivity extends BaseActivity {
    public ArrayList<NotificationHolder> notificationHoldersArrayList;
    public static final String LOGTAG = "NotificationListActivity";
    NotificationAdapter notificationAdapter;
    ListView notificationListView;
    TextView noNotificationsFound;
    String username;
    String facebookUserLoginId;
    boolean facebookUserLoginFlag;
    String googleUserLoginId;
    boolean googleUserLoginFlag;
    ToastMessage toastMessage;
    Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);
        this.setTitle(R.string.notification_list);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        menu = this.menu;
        toolbar.hideOverflowMenu();
        toastMessage = new ToastMessage(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.previous, getApplicationContext().getTheme()));
        } else {
            toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.previous));
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (GlobalMethods.haveNetworkConnection(NotificationListActivity.this)) {
            ViewDialog.showProgress(R.string.help_screen_one, NotificationListActivity.this, getResources().getString(R.string.please_wait));
        }
        username = PreferenceManager.getDefaultSharedPreferences(this).getString("username", "");
        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(this).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(this).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("GoogleUserLoginFlag", false);

        if (CommonConstants.mDebug) Log.v("TAG username", username);
        notificationHoldersArrayList = new ArrayList<NotificationHolder>();
        notificationListView = (ListView) findViewById(R.id.notificationListView);
        noNotificationsFound = (TextView) findViewById(R.id.noNotificationsFound);
        getNotificationList();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.closeButton).setVisible(false);
        getNotifications(menu);
        return super.onCreateOptionsMenu(menu);
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
                    int totalNotifications = Integer.parseInt(notificationCnt) + Intercom.client().getUnreadConversationCount();
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
//                                Intent intent = new Intent(NotificationListActivity.this, NotificationListActivity.class);
//                                startActivity(intent);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.icon:
//                Intent intent = new Intent(MainActivity.this, NotificationListActivity.class);
//                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void getNotificationList() {
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

        ErisCollectionManager.getInstance().callMethod("getUserUnreadNotificationList", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v("TAG", result);
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONArray jsonArrayResult = jsonObjectData.getJSONArray("result");

                    final int intercomNotifications = Intercom.client().getUnreadConversationCount();
                    final int m3Notifications = jsonArrayResult.length();

                    for (int i = 0; i < jsonArrayResult.length(); i++) {
                        JSONObject jsonObjectCell = jsonArrayResult.getJSONObject(i);
                        NotificationHolder notificationHolder = new NotificationHolder();
                        notificationHolder.setMortgageTitle(jsonObjectCell.getString("mortgage_title"));
                        notificationHolder.setOpportunityTitle(jsonObjectCell.getString("opportunity_type"));
                        notificationHolder.setOpporCreatedDate(jsonObjectCell.getString("opportunity_created_at"));
                        notificationHolder.setNotificationId(jsonObjectCell.getString("_id"));
                        notificationHolder.setIsRead(jsonObjectCell.getString("is_read"));
                        notificationHolder.setOpportunityId(jsonObjectCell.getString("opportunity_id"));
                        notificationHolder.setIsExpired(jsonObjectCell.getString("is_expired"));
                        if (jsonObjectCell.has("notification_type_code")) {
                            notificationHolder.setNotification_type_code(jsonObjectCell.getString("notification_type_code"));
                        }
                        if (jsonObjectCell.has("notification_title")) {
                            notificationHolder.setNotification_title(jsonObjectCell.getString("notification_title"));
                        }
                        if (jsonObjectCell.has("notification_msg")) {
                            notificationHolder.setNotification_msg(jsonObjectCell.getString("notification_msg"));
                        }

                        if (jsonObjectCell.has("opportunity_type_code")) {
                            notificationHolder.setType(jsonObjectCell.getString("notification_type_code"));
                        }
                        notificationHoldersArrayList.add(notificationHolder);
                    }

                    if (intercomNotifications > 0) {
                        NotificationHolder notificationHolder = new NotificationHolder();
                        notificationHolder.setMortgageTitle("Help");
                        notificationHolder.setOpportunityTitle("There is a new help message!");
                        notificationHolder.setNotification_title("There is a new help message!");
                        notificationHolder.setOpporCreatedDate("");
                        notificationHolder.setNotificationId("");
                        notificationHolder.setIsRead("");
                        notificationHolder.setOpportunityId("");
                        notificationHolder.setIsExpired("");
                        notificationHolder.setNotification_type_code("3");
                        notificationHolder.setType("3");
                        notificationHoldersArrayList.add(notificationHolder);
                    }

                    if (intercomNotifications + m3Notifications == 0) {
                        noNotificationsFound.setVisibility(View.VISIBLE);
                    }

                    ViewDialog.hideProgress();

                    notificationAdapter = new NotificationAdapter(NotificationListActivity.this, notificationHoldersArrayList);
                    notificationListView.setAdapter(notificationAdapter);
                    notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(NotificationListActivity.this)) {
//                                toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                                ViewDialog.showNetworkPopup(NotificationListActivity.this, getResources().getString(R.string.app_cant_perform_operation), getResources().getString(R.string.no_network_connection));
                            } else {

                                // Help Message
                                if (intercomNotifications > 0 && position == (intercomNotifications + m3Notifications) - 1) {
                                    finish();

                                    // Intercom
                                    Intercom.client().displayMessenger();
                                }

                                // M3 Notification
                                else {
                                    String notificationId = notificationHoldersArrayList.get(position).getNotificationId();
                                    HashMap<String, Object> stringObjectHashMap1 = new HashMap<String, Object>();
                                    stringObjectHashMap1.put("notification_id", notificationId);
                                    if (facebookUserLoginFlag) {
                                        stringObjectHashMap1.put("user_id", facebookUserLoginId);
                                    } else if (googleUserLoginFlag) {
                                        stringObjectHashMap1.put("user_id", googleUserLoginId);
                                    } else {
                                        stringObjectHashMap1.put("user_id", username);
                                    }
                                    stringObjectHashMap1.put("lang", "en");
                                    stringObjectHashMap1.put("req_from", "android");

                                    ErisCollectionManager.getInstance().callMethod("readNotification", new Object[]{stringObjectHashMap1}, new ResultListener() {
                                        @Override
                                        public void onSuccess(String result) {
                                            if (CommonConstants.mDebug) Log.v("TAG", result);
                                            if (notificationHoldersArrayList.get(position).getIsExpired().equalsIgnoreCase("Y")) {
                                                showAlertPopUp(NotificationListActivity.this, "Sorry! This opportunity no longer exists for your mortgage.", "Monitor My Mortgage");
                                            } else if (notificationHoldersArrayList.get(position).getNotification_type_code().equalsIgnoreCase("3")) {
                                                showAlertPopUpRequestQuote(NotificationListActivity.this, notificationHoldersArrayList.get(position).getNotification_msg(), notificationHoldersArrayList.get(position).getNotification_title());
                                            } else {
                                                if (notificationHoldersArrayList.get(position).getOpportunityTitle().equalsIgnoreCase("Saving")) {
                                                    if (CommonConstants.mDebug)
                                                        Log.v("TAG", "saving opportunity");
                                                    Intent intent = new Intent(NotificationListActivity.this, OpportunityOneSavingsActivity.class);
                                                    intent.putExtra("opportunity_id", notificationHoldersArrayList.get(position).getOpportunityId());
                                                    intent.putExtra("activity_title", notificationHoldersArrayList.get(position).getMortgageTitle());
                                                    finish();
                                                    startActivity(intent);
                                                } else if (notificationHoldersArrayList.get(position).getOpportunityTitle().equalsIgnoreCase("Approaching Maturity")) {
                                                    if (CommonConstants.mDebug)
                                                        Log.v("TAG", "apporching maturity opportunity");
                                                    Intent intent = new Intent(NotificationListActivity.this, OpprTwoApporchingMaturityActivity.class);
                                                    intent.putExtra("opportunity_id", notificationHoldersArrayList.get(position).getOpportunityId());
                                                    intent.putExtra("activity_title", notificationHoldersArrayList.get(position).getMortgageTitle());
                                                    finish();
                                                    startActivity(intent);
                                                } else if (notificationHoldersArrayList.get(position).getOpportunityTitle().equalsIgnoreCase("Variable Rate")) {
                                                    if (CommonConstants.mDebug)
                                                        Log.v("TAG", "variable rate opportunity");
                                                    Intent intent = new Intent(NotificationListActivity.this, OpprThreeVariableRateNotificationActivity.class);
                                                    intent.putExtra("opportunity_id", notificationHoldersArrayList.get(position).getOpportunityId());
                                                    intent.putExtra("activity_title", notificationHoldersArrayList.get(position).getMortgageTitle());
                                                    finish();
                                                    startActivity(intent);
                                                } else if (notificationHoldersArrayList.get(position).getOpportunityTitle().equalsIgnoreCase("Prime Rate Change")) {
                                                    if (CommonConstants.mDebug)
                                                        Log.v("TAG", "Prime Rate Change opportunity");
                                                    Intent intent = new Intent(NotificationListActivity.this, OpprFourPrimeRateChangeActivity.class);
                                                    intent.putExtra("opportunity_id", notificationHoldersArrayList.get(position).getOpportunityId());
                                                    intent.putExtra("activity_title", notificationHoldersArrayList.get(position).getMortgageTitle());
                                                    finish();
                                                    startActivity(intent);
                                                }

                                            }
                                        }

                                        @Override
                                        public void onError(String error, String reason, String details) {

                                        }
                                    });
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, String reason, String details) {
                if (CommonConstants.mDebug) Log.v("TAG", reason);
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        if (value)
                            if (notificationHoldersArrayList.size() <= 0) {
                                getNotificationList();

                            }

                    }

                    @Override
                    public void onDisconnect() {
                        if (ViewDialog.kProgressHUD.isShowing())
                            ViewDialog.hideProgress();

                        if (!GlobalMethods.haveNetworkConnection(NotificationListActivity.this) && !MeteorSingleton.getInstance().isConnected()) {
                            toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        if (status)
                            if (notificationHoldersArrayList.size() <= 0) {
                                getNotificationList();
                            }
                    }
                });
            }
        });
    }

    public void showAlertPopUp(Activity activity, String msg, String title) {
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
                    ViewDialog.showProgress(R.string.help_screen_one, NotificationListActivity.this, getResources().getString(R.string.please_wait));
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    alertDialog.dismiss();
                    ViewDialog.hideProgress();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAlertPopUpRequestQuote(Activity activity, String msg, String title) {
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
                    ViewDialog.showProgress(R.string.help_screen_one, NotificationListActivity.this, getResources().getString(R.string.please_wait));
//                    Intent intent = getIntent();
                    finish();
//                    startActivity(intent);
                    alertDialog.dismiss();
                    ViewDialog.hideProgress();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
