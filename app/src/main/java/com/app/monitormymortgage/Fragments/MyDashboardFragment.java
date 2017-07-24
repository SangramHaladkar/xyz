package com.app.monitormymortgage.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.monitormymortgage.Activity.ShowImagesActivity;
import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.app.monitormymortgage.Activity.AddMortgageActivity;
import com.app.monitormymortgage.Activity.PreApprovalActivity;
import com.app.monitormymortgage.Activity.PurchaseActivity;
import com.app.monitormymortgage.Adapters.MortgageAdapter;
import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.DataHolderClasses.MortgageHolder;
import com.app.monitormymortgage.MainActivity;
import com.app.monitormymortgage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class MyDashboardFragment extends BaseFragment implements ErisConnectionListener {
    private Button btn_add_mortgage, btn_notification_settings, btn_request_quote;
    RelativeLayout relativeLayout;
    ListView mortgageList;
    public ArrayList<MortgageHolder> mortgageHolderArrayList;
    public ArrayList<MortgageHolder> requestQuoteHolderArrayList;

    public static final String LOGTAG = "MyDashboardFragment";
    MortgageAdapter mortgageAdapter;
    String facebookUserLoginId;
    boolean facebookUserLoginFlag;
    String googleUserLoginId;
    boolean googleUserLoginFlag;
    String username;
    KProgressHUD kProgressHUD;
    ImageView calloutImageView;
    TextView startTextView;
    FrameLayout frameLayout;
    LinearLayout calloutLinearLayout;
    GlobalMethods globalMethods;
    ToastMessage toastMessage;
    public static ArrayList<String> mastersArrayList;
    public static ArrayList<String> termTypeArrayList;
    public static ArrayList<String> termRateTypeArrayList;

    public MyDashboardFragment() {
        // Required empty public constructor
    }

    /*
    * initialized all variables.
    * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setTitle(R.string.my_dashboard_fragment);
        showProgressDialog(getResources().getString(R.string.loading_mortgage_list));
        mortgageHolderArrayList = new ArrayList<>();
        requestQuoteHolderArrayList = new ArrayList<>();

        globalMethods = new GlobalMethods(this.getContext());
        toastMessage = new ToastMessage(MyDashboardFragment.this.getContext());
        mastersArrayList = new ArrayList<>();
        termTypeArrayList = new ArrayList<>();
        termRateTypeArrayList = new ArrayList<>();

        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("GoogleUserLoginFlag", false);
        username = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("username", "");

        ErisCollectionManager.getInstance().setConnectionListener(this);
        getUser(username);

    }


    /*
    * called when fragment view created.
    * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_my_dashboard_new_design, container, false);
        initialiseUIComponent(rootview);
        mortgageHolderArrayList.clear();
        requestQuoteHolderArrayList.clear();

        if (mortgageHolderArrayList.size() <= 0) {

            new getList().execute();
        }
        btn_notification_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MyDashboardFragment.this.getActivity())) {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                } else {
                    globalMethods.replaceFragment(new NotificationSettingFragment(), MyDashboardFragment.this.getActivity(), null);
                }
            }
        });
        btn_add_mortgage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MyDashboardFragment.this.getActivity())) {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                } else {
//                    if (mortgageHolderArrayList != null && mortgageHolderArrayList.size() >= 5) {
//                        ViewDialog.showAlertPopUp(MyDashboardFragment.this.getActivity(), getResources().getString(R.string.mortgage_limit_message), getResources().getString(R.string.mortgage_limit_reached));
//                    } else {
                    Intent intent = new Intent(MyDashboardFragment.this.getActivity(), AddMortgageActivity.class);
                    startActivity(intent);
//                    }
                }
            }
        });

        btn_request_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MyDashboardFragment.this.getActivity())) {
                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                } else {
                    showPopupRequestQuote(MyDashboardFragment.this.getActivity());
                }
            }
        });

        if (!GlobalMethods.haveNetworkConnection(MyDashboardFragment.this.getActivity()) && !MeteorSingleton.getInstance().isConnected()) {
            if (kProgressHUD.isShowing())
                kProgressHUD.dismiss();
            toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
        }

        if (mortgageList != null) {
            mortgageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                PrimaryResidenceFragment primaryResidenceFragment = new PrimaryResidenceFragment();
                    if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MyDashboardFragment.this.getActivity())) {
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    } else {
                        if (mortgageHolderArrayList.get(position).getMortgageType().equalsIgnoreCase("Mortgage")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("mortgageName", mortgageHolderArrayList.get(position).getMortgageTitle());
                            bundle.putString("mortgageID", mortgageHolderArrayList.get(position).getMortgageId());
                            String str = mortgageHolderArrayList.get(position).getLenderLogo();
                            if (str.length() > 10) {
                                bundle.putString("lenderString", mortgageHolderArrayList.get(position).getLenderLogo());
                                bundle.putString("lenderid", mortgageHolderArrayList.get(position).getLenderId());
                            } else {
                                bundle.putString("lenderString", "others");
                                bundle.putString("lenderid", "1000");
                            }
                            globalMethods.replaceFragment(new PrimaryResidenceFragment(), MyDashboardFragment.this.getActivity(), bundle);
                        } else if (mortgageHolderArrayList.get(position).getMortgageType().equalsIgnoreCase("RequestQuote")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("RequestQuoteType", mortgageHolderArrayList.get(position).getQuote_type_code());
                            bundle.putString("RquestId", mortgageHolderArrayList.get(position).getRequest_id());
                            globalMethods.replaceFragment(new RequestQuoteFragment(), MyDashboardFragment.this.getActivity(), bundle);
                        }
                    }
                }
            });
        }


        getAllMasters();
        return rootview;
    }

//    @Override
//    public void onConnect(boolean value) {
//        super.onConnect(value);
//        Log.v("TAG", "onConnect");
//        if (mortgageHolderArrayList.size() <= 0) {
//            new getList().execute();
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        if (ShowImagesActivity.flagChanged)
            ShowImagesActivity.flagChanged = false;
    }

    /*
        * Progress dialog
        * */
    public void showProgressDialog(String message) {
        kProgressHUD = new KProgressHUD(this.getActivity());
        final KProgressHUD kProgressHUD = this.kProgressHUD.create(this.getActivity());
        this.kProgressHUD.setCancellable(false);
        this.kProgressHUD.setLabel(message);
        this.kProgressHUD.setAnimationSpeed(2);
        this.kProgressHUD.setDimAmount(0.5f);
        this.kProgressHUD.setStyle(KProgressHUD.Style.SPIN_INDETERMINATE);
        this.kProgressHUD.show();
    }

    /*
    * Initialize all UI elements.
    * */
    public void initialiseUIComponent(View view) {
        btn_add_mortgage = (Button) view.findViewById(R.id.btn_add_mortgage);
        btn_notification_settings = (Button) view.findViewById(R.id.btn_notification_settings);
        btn_request_quote = (Button) view.findViewById(R.id.btn_request_quote);
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayoutListAvailable);
        calloutImageView = (ImageView) view.findViewById(R.id.calloutImageView);
        startTextView = (TextView) view.findViewById(R.id.startTextView);
        mortgageList = (ListView) view.findViewById(R.id.mortgageList);
        frameLayout = (FrameLayout) view.findViewById(R.id.frameLayout);
        calloutLinearLayout = (LinearLayout) view.findViewById(R.id.calloutLinearLayout);
    }


    /*
    * Get mortgage response from web.
    * Bind to list.
    * On Error method check size and reload list.
    * */
    public void getMortgageList() {
        final HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        if (facebookUserLoginFlag) {
            stringObjectHashMap.put("user_id", facebookUserLoginId);
        } else if (googleUserLoginFlag) {
            stringObjectHashMap.put("user_id", googleUserLoginId);
        } else {
            stringObjectHashMap.put("user_id", username);
        }
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("getUserMortgages", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    mortgageHolderArrayList.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObjectData.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectCell = jsonArray.getJSONObject(i);
                            MortgageHolder mortgageHolder = new MortgageHolder();
                            mortgageHolder.setMortgageType("Mortgage");
                            mortgageHolder.setMortgageId(jsonObjectCell.getString("mortgage_id"));
                            mortgageHolder.setMortgageTitle(jsonObjectCell.getString("title"));
                            mortgageHolder.setMortgageOppStatus(jsonObjectCell.getString("mortgage_opportunity_status"));
                            mortgageHolder.setOwner(jsonObjectCell.getString("owner"));
                            mortgageHolder.setLenderId(jsonObjectCell.getString("lender_id"));
                            if (jsonObjectCell.getString("lender_id").equalsIgnoreCase("1000")) {
                                mortgageHolder.setLenderLogo("");
                            } else {
                                String lenderLogo = jsonObjectCell.getString("lender_logo");
                                String newConvertedLenderString = "";
                                String[] parts = lenderLogo.split(",");
                                if (parts.length > 0) {
                                    String part1 = parts[0];
                                    newConvertedLenderString = parts[1];
                                }
                                mortgageHolder.setLenderLogo(newConvertedLenderString);
                            }
                            mortgageHolder.setLenderName(jsonObjectCell.getString("lender_name"));
                            mortgageHolder.setOppCreatedDate(jsonObjectCell.getString("opportunity_created_date"));
                            mortgageHolderArrayList.add(mortgageHolder);
//                            relativeLayout.setBackgroundResource(R.drawable.dashboardbg);
                            if (mortgageHolderArrayList.size() >= 5) {
                                btn_add_mortgage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MyDashboardFragment.this.getActivity())) {
                                            toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                                        } else {
                                            ViewDialog.showAlertPopUp(MyDashboardFragment.this.getActivity(), getResources().getString(R.string.mortgage_limit_message), getResources().getString(R.string.mortgage_limit_reached));
                                        }
                                    }
                                });
                            }
                        }

                        if (requestQuoteHolderArrayList.size() <= 0) {
                            if (facebookUserLoginFlag) {
                                getUserRequestedQuotes(facebookUserLoginId);
                            } else if (googleUserLoginFlag) {
                                getUserRequestedQuotes(googleUserLoginId);
                            } else {
                                getUserRequestedQuotes(username);
                            }
                        }

                    } else if (jsonObject.getString("status").equalsIgnoreCase("false")) {
                        JSONObject jsonError = jsonObject.getJSONObject("error");
                        String code = jsonError.getString("code");
                        if (code.equalsIgnoreCase("10005")) {
                            recreateSession();
                        }


                        if (mortgageHolderArrayList.size() <= 0) {
                            if (requestQuoteHolderArrayList.size() <= 0) {
                                if (facebookUserLoginFlag) {
                                    getUserRequestedQuotes(facebookUserLoginId);
                                } else if (googleUserLoginFlag) {
                                    getUserRequestedQuotes(googleUserLoginId);
                                } else {
                                    getUserRequestedQuotes(username);
                                }
                            }
                        } else {
                            kProgressHUD.dismiss();
//                            mortgageList.setVisibility(View.GONE);
                            frameLayout.setVisibility(View.VISIBLE);
                            calloutLinearLayout.setVisibility(View.VISIBLE);
                            startTextView.setVisibility(View.VISIBLE);
                            relativeLayout.setBackgroundResource(R.drawable.dashboard);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onError(String error, final String reason, String details) {
                Log.v(LOGTAG, error + reason);
//                toastMessage.showToastMsg(reason, Toast.LENGTH_SHORT);
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
        });
    }

    /*
    * Request quote functionility
    * */
    public void showPopupRequestQuote(final Activity activity) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.custom_dialog_request_quote, null);
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

            titleTextView.setText(getResources().getString(R.string.requestquote));
            Button preApprovalButton = (Button) myDialog.findViewById(R.id.btn_pre_approval);
            Button purchaseButton = (Button) myDialog.findViewById(R.id.btn_purchase);

            preApprovalButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MyDashboardFragment.this.getActivity())) {
                        alertDialog.dismiss();
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    } else {
                        alertDialog.dismiss();
                        Intent preApprovalIntent = new Intent(MyDashboardFragment.this.getActivity(), PreApprovalActivity.class);
                        if (facebookUserLoginFlag) {
                            preApprovalIntent.putExtra("UserId", facebookUserLoginId);
                        } else if (googleUserLoginFlag) {
                            preApprovalIntent.putExtra("UserId", googleUserLoginId);
                        } else {
                            preApprovalIntent.putExtra("UserId", username);
                        }
                        startActivity(preApprovalIntent);
                    }
                }
            });

            purchaseButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MyDashboardFragment.this.getActivity())) {
                        alertDialog.dismiss();
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    } else {
                        alertDialog.dismiss();
                        Intent purchaseIntent = new Intent(MyDashboardFragment.this.getActivity(), PurchaseActivity.class);
                        if (facebookUserLoginFlag) {
                            purchaseIntent.putExtra("UserId", facebookUserLoginId);
                        } else if (googleUserLoginFlag) {
                            purchaseIntent.putExtra("UserId", googleUserLoginId);
                        } else {
                            purchaseIntent.putExtra("UserId", username);
                        }
                        startActivity(purchaseIntent);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
    * onActivity destroy disable progress dialog.
    * */

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (kProgressHUD.isShowing()) {
            kProgressHUD.dismiss();
        }
    }


    /*
    * Get Request Quote List.
    * */
    public void getUserRequestedQuotes(final String userId) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("user_id", userId);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getUserRequestedQuotes", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                Log.v(LOGTAG, result);
                try {
                    requestQuoteHolderArrayList.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObjectData.getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectCell = jsonArray.getJSONObject(i);
                            MortgageHolder requestQuoteData = new MortgageHolder();
                            requestQuoteData.setMortgageType("RequestQuote");
                            requestQuoteData.setRequest_id(jsonObjectCell.getString("request_id"));
                            requestQuoteData.setMortgageTitle(jsonObjectCell.getString("title"));
                            requestQuoteData.setQuote_type(jsonObjectCell.getString("quote_type"));
                            requestQuoteData.setQuote_type_code(jsonObjectCell.getString("quote_type_code"));
                            requestQuoteData.setMortgageOppStatus(jsonObjectCell.getString("mortgage_opportunity_status"));
                            requestQuoteData.setOwner(jsonObjectCell.getString("owner"));
                            requestQuoteData.setOpportunity_created_at(jsonObjectCell.getString("opportunity_created_at"));
                            requestQuoteData.setOpportunity_created_date(jsonObjectCell.getString("opportunity_created_date"));

                            requestQuoteHolderArrayList.add(requestQuoteData);
                            relativeLayout.setBackgroundResource(R.drawable.dashboardbg);
                        }

                        for (int i = 0; i < requestQuoteHolderArrayList.size(); i++) {
                            mortgageHolderArrayList.add(requestQuoteHolderArrayList.get(i));
                        }

                        /*set adapter after for loop*/
                        if (mortgageHolderArrayList.size() > 0) {
                            mortgageAdapter = new MortgageAdapter(MyDashboardFragment.this.getActivity(), mortgageHolderArrayList);

                            mortgageList.setAdapter(mortgageAdapter);
                            relativeLayout.setBackgroundResource(R.drawable.dashboardbg);
                        }
                        if (kProgressHUD.isShowing()) {
                            kProgressHUD.dismiss();
                        }
                    } else if (jsonObject.getString("status").equalsIgnoreCase("false")) {
                        if (mortgageHolderArrayList.size() > 0) {
                            mortgageAdapter = new MortgageAdapter(MyDashboardFragment.this.getActivity(), mortgageHolderArrayList);
                            mortgageList.setAdapter(mortgageAdapter);
                            relativeLayout.setBackgroundResource(R.drawable.dashboardbg);
                        } else {
//                            mortgageList.setVisibility(View.GONE);
                            frameLayout.setVisibility(View.VISIBLE);
                            calloutLinearLayout.setVisibility(View.VISIBLE);
                            startTextView.setVisibility(View.VISIBLE);
                            relativeLayout.setBackgroundResource(R.drawable.dashboard);
                        }

                        if (kProgressHUD.isShowing())
                            kProgressHUD.dismiss();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.v(LOGTAG, reason);
            }
        });
    }


    public static void getAllMasters() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");
        ErisCollectionManager.getInstance().callMethod("getAllMasters", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v("TAG", result);
                try {
                    if (mastersArrayList != null)
                        mastersArrayList.clear();

                    if (termTypeArrayList != null)
                        termTypeArrayList.clear();

                    if (termRateTypeArrayList != null)
                        termRateTypeArrayList.clear();
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    JSONArray jsonArrayPaymentFrequencyMaster = jsonObjectResult.getJSONArray("mortgage_payment_frequency");
                    JSONArray jsonArrayCurrentTermType = jsonObjectResult.getJSONArray("mortgage_current_term");
                    JSONArray jsonArrayCurrentTermRateType = jsonObjectResult.getJSONArray("mortgage_current_term_rate_type");

                    /* Get Payment Frequency Master*/
                    for (int i = 0; i < jsonArrayPaymentFrequencyMaster.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayPaymentFrequencyMaster.getJSONObject(i);
                        mastersArrayList.add(jsonObject1.getString("key"));
                    }

                     /* Get Current Term Type Masters*/
                    for (int i = 0; i < jsonArrayCurrentTermType.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayCurrentTermType.getJSONObject(i);
                        termTypeArrayList.add(jsonObject1.getString("val"));

                    }
                    /* Get Current Term Rate Type*/
                    for (int i = 0; i < jsonArrayCurrentTermRateType.length(); i++) {
                        JSONObject jsonObject1 = jsonArrayCurrentTermRateType.getJSONObject(i);
                        termRateTypeArrayList.add(jsonObject1.getString("val"));

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
    public void onDisconnect() {
        Log.v("TAG", "onDisconnect");
        if (isAdded()) {
            if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(MyDashboardFragment.this.getActivity())) {
                toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                if (kProgressHUD.isShowing()) {
                    kProgressHUD.dismiss();
                }
            }
        }

    }

    @Override
    public void onException(Exception e) {

    }

    @Override
    public void onInternetStatusChanged(boolean status) {
        if (status) {
            Log.v(LOGTAG, "Connection Status : " + status);
            if (MeteorSingleton.getInstance().isConnected()) {
                Log.v(LOGTAG, "Meteor Connection Status : " + MeteorSingleton.getInstance().isConnected());
            } else {
                ErisCollectionManager.getInstance().reconnectMeteor(this);
            }
        }
    }

    public class getList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getMortgageList();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    public void getUser(String userId) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        if (facebookUserLoginFlag) {
            stringObjectHashMap.put("userId", facebookUserLoginId);
        } else if (googleUserLoginFlag) {
            stringObjectHashMap.put("userId", googleUserLoginId);
        } else {
            stringObjectHashMap.put("userId", userId);
        }
        ErisCollectionManager.getInstance().callMethod("getUser", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v("TAG", result);
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                    CommonConstants.contactPreference = jsonObjectResult.getString("contact_pref");
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
