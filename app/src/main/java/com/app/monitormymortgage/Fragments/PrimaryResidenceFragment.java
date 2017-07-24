package com.app.monitormymortgage.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;
import com.app.monitormymortgage.Activity.EditMortgageActivity;
import com.app.monitormymortgage.Activity.OpportunityOneSavingsActivity;
import com.app.monitormymortgage.Activity.OpprFourPrimeRateChangeActivity;
import com.app.monitormymortgage.Activity.OpprThreeVariableRateNotificationActivity;
import com.app.monitormymortgage.Activity.OpprTwoApporchingMaturityActivity;
import com.app.monitormymortgage.Activity.ShowImagesActivity;
import com.app.monitormymortgage.BaseClasses.BaseFragment;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.DataHolderClasses.AttachmentFileData;
import com.app.monitormymortgage.DataHolderClasses.MortgageDetailsHolder;
import com.app.monitormymortgage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class PrimaryResidenceFragment extends BaseFragment {

    ProgressDialog progress;
    ImageView mortgageImageTextView;
    TextView paperClipTextView;
    TextView mortgageDocumentsTextView;
    TextView faAngleRightTextView, faAngleRightCaseTwoTextView, faAngleRightCaseOneTextView;
    Typeface typeface;
    String mortgageID;
    TextView amortizedBalanceTextView;
    TextView interestRateTextView;
    TextView paymentTextView;
    TextView paymentFrequencyTextView;
    TextView maturityDateTextview;
    TextView amortizationTextView;
    TextView termTextview;
    TextView statusTextView;
    TextView typeTextView;
    TextView postalCodeTextview;
    TextView creditLineAmtTextView;
    TextView lastUpdateTextView;
    TextView dollarTextView;
    MortgageDetailsHolder mortgageDetailsHolder;
    RelativeLayout relativeLayoutCaseOne;
    RelativeLayout relativeLayoutCaseTwo;
    RelativeLayout relativeLayoutCaseThree;
    TextView textViewCaseFour;
    String title;
    String lenderLogo;
    Button btn_analyze_now;
    Button btn_edit_mortgage;
    TextView opprNotAvailableTextView;
    Button btn_backToDashboard;
    String opportunityId;
    String opportunityName;
    TextView otherTextView;
    String lenderid;
    String opportunityIdAlreadyAvailable;
    String opportunityNameAlreadyAvialble;
    int term;
    int attachmentCount;
    ArrayList<AttachmentFileData> attachmentFileDatas;
    private String doc_id;
    boolean hasOpportunityFlag = false;
    NumberFormat nf;
    DecimalFormat df;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    boolean flag;
    int t;
    boolean editActivityOpened;

    // CR 4
    TextView interestRateTV;
    TextView informationTextView;
    String current_term_rate_type;
    double primeRate;
    String prime_rate_plus_minus;
    String prime_rate_adjustment_percentage;
    double calculatedValue;
    double Dprime_rate_adjustment_percentage = 0.0;
    DecimalFormat f;
    String mortgage_opportunity_status;
    GlobalMethods globalMethods;
    Context mContext;
    ToastMessage toastMessage;

    String period="";

    private TextView originalTextView;


    boolean facebookUserLoginFlag;
    boolean googleUserLoginFlag;

    public PrimaryResidenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editActivityOpened = false;
        flag = true;
        ShowImagesActivity.flagChanged = false;
        mContext = this.getActivity();
        alertDialogBuilder = new AlertDialog.Builder(PrimaryResidenceFragment.this.getActivity());
        alertDialog = alertDialogBuilder.create();
        toastMessage = new ToastMessage(PrimaryResidenceFragment.this.getActivity());
        mortgageDetailsHolder = new MortgageDetailsHolder();
        globalMethods = new GlobalMethods(this.getContext());
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");
        ViewDialog.showProgress(R.string.help_screen_one, PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.please_wait));
        attachmentCount = 0;
        attachmentFileDatas = new ArrayList<>();
        nf = NumberFormat.getInstance();
        df = (DecimalFormat) nf;
        df.applyPattern("#,###.00");
        f = new DecimalFormat("0.00");

        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("GoogleUserLoginFlag", false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("INFO", "OnCreateView");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_primary_residence, container, false);
        initializeUIComponent(view);
        paperClipTextView.setVisibility(View.INVISIBLE);
        mortgageDocumentsTextView.setVisibility(View.INVISIBLE);
        opportunityIdAlreadyAvailable = "";
        opportunityNameAlreadyAvialble = "";

        Bundle bundle = PrimaryResidenceFragment.this.getArguments();
        if (getArguments() != null) {
            mortgageID = bundle.getString("mortgageID");
            title = bundle.getString("mortgageName");
            lenderLogo = bundle.getString("lenderString");
            lenderid = bundle.getString("lenderid");
        }
        (getActivity()).setTitle(title);
        mortgageDocumentsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(PrimaryResidenceFragment.this.getActivity())) {
//                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    ViewDialog.showNetworkPopup(PrimaryResidenceFragment.this.getActivity(), "App cannot perform current operation. Please retry when connected.", "No Network connection");
                } else {
                    Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), ShowImagesActivity.class);
                    intent.putExtra("mortgageID", mortgageID);
                    intent.putExtra("doc_id", doc_id);
                    startActivity(intent);
                }
            }
        });
        getCurrentPrimeRate();
        getMortgageDetails();

        if (lenderLogo.length() > 10) {
            try {
                base64(mortgageImageTextView, lenderLogo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            otherTextView.setVisibility(View.GONE);
        } else {
            mortgageImageTextView.setVisibility(View.GONE);
            otherTextView.setVisibility(View.VISIBLE);
        }
        editMortgageButtonClick(btn_edit_mortgage);
        analyzeNowBtnClick();
        onDisconnect();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (editActivityOpened) {
            editActivityOpened = false;

            if (GlobalMethods.currentMortgageID != null) {
                Log.i("INFO", "Refreshing mortgage details");

                mortgageID = GlobalMethods.currentMortgageID;
                ViewDialog.showProgress(R.string.help_screen_one, PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.please_wait));
                getMortgageDetails();
            }
        }
    }

    public void getCurrentPrimeRate() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getCurrentPrimeRate", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String resultMain) {
                Log.v("TAG", resultMain);
                try {
                    JSONObject jsonObject = new JSONObject(resultMain);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject result = data.getJSONObject("result");
                        String primerate = result.getString("prime_rate");
                        primeRate = Double.parseDouble(primerate);
                        Log.v("TAG", "" + primeRate);
                    } else if (status.equalsIgnoreCase("false")) {
                        primeRate = 0.00;
                        Log.v("TAG", "" + primeRate);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, String reason, String details) {
                Log.v("TAG", reason);

            }
        });
    }

    public void initializeUIComponent(View view) {
        mortgageImageTextView = (ImageView) view.findViewById(R.id.mortgageImageTextView);
        paperClipTextView = (TextView) view.findViewById(R.id.paperClipTextView);
        mortgageDocumentsTextView = (TextView) view.findViewById(R.id.mortgageDocumentsTextView);
        faAngleRightTextView = (TextView) view.findViewById(R.id.faAngleRightTextView);
        faAngleRightCaseTwoTextView = (TextView) view.findViewById(R.id.faAngleRightCaseTwoTextView);
        faAngleRightCaseOneTextView = (TextView) view.findViewById(R.id.faAngleRightCaseOneTextView);
        faAngleRightCaseOneTextView.setTypeface(typeface);
        faAngleRightCaseTwoTextView.setTypeface(typeface);
        faAngleRightTextView.setTypeface(typeface);
        paperClipTextView.setTypeface(typeface);
        String htmlString = "<font color='#6E99CC'><i><u>" + getResources().getString(R.string.mortgage_documents) + "</u></i></font>";
//        String htmlString = "<font color='#6E99CC'><i><u> Mortgage Documents</u></i></font>";
        mortgageDocumentsTextView.setText(Html.fromHtml(htmlString));
        mortgageDocumentsTextView.setVisibility(View.VISIBLE);
        amortizedBalanceTextView = (TextView) view.findViewById(R.id.amortizedBalanceTextView);
        interestRateTextView = (TextView) view.findViewById(R.id.interestRateTextView);
        paymentTextView = (TextView) view.findViewById(R.id.paymentTextView);
        paymentFrequencyTextView = (TextView) view.findViewById(R.id.paymentFrequencyTextView);
        maturityDateTextview = (TextView) view.findViewById(R.id.maturityDateTextview);
        amortizationTextView = (TextView) view.findViewById(R.id.amortizationTextView);
        termTextview = (TextView) view.findViewById(R.id.termTextview);
        statusTextView = (TextView) view.findViewById(R.id.statusTextView);
        typeTextView = (TextView) view.findViewById(R.id.typeTextView);
        postalCodeTextview = (TextView) view.findViewById(R.id.postalCodeTextview);
        creditLineAmtTextView = (TextView) view.findViewById(R.id.creditLineAmtTextView);
        lastUpdateTextView = (TextView) view.findViewById(R.id.lastUpdateTextView);
        relativeLayoutCaseOne = (RelativeLayout) view.findViewById(R.id.relativeLayoutCaseOne);
        relativeLayoutCaseTwo = (RelativeLayout) view.findViewById(R.id.relativeLayoutCaseTwo);
        relativeLayoutCaseThree = (RelativeLayout) view.findViewById(R.id.relativeLayoutCaseThree);
        textViewCaseFour = (TextView) view.findViewById(R.id.textViewCaseFour);
        opprNotAvailableTextView = (TextView) view.findViewById(R.id.opprNotAvailableTextView);
        btn_analyze_now = (Button) view.findViewById(R.id.btn_analyze_now);
        btn_edit_mortgage = (Button) view.findViewById(R.id.btn_edit_mortgage);
        btn_backToDashboard = (Button) view.findViewById(R.id.btn_backToDashboard);
        otherTextView = (TextView) view.findViewById(R.id.otherTextView);
        dollarTextView = (TextView) view.findViewById(R.id.dollarTextView);

        // CR 4
        interestRateTV = (TextView) view.findViewById(R.id.interestRateTV);
        informationTextView = (TextView) view.findViewById(R.id.informationTextView);
        originalTextView=(TextView)view.findViewById(R.id.originalTextView);
    }

    public void getMortgageDetails() {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("mortgage_id", mortgageID);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getMortgageDetails", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v("TAG getMortgageDetails", result);
                try {
                    JSONObject jsonObjectResult = new JSONObject(result);
                    if (jsonObjectResult.getString("status").equalsIgnoreCase("true")) {
                        JSONObject jsonObjectData = jsonObjectResult.getJSONObject("data");
                        JSONObject jsonObjectResultMain = jsonObjectData.getJSONObject("result");
                        if (jsonObjectResultMain.has("amortized_amount")) {
                            mortgageDetailsHolder.setAmortizedBalance(new BigDecimal(jsonObjectResultMain.getString("amortized_amount")).toPlainString());
                        } else {
                            mortgageDetailsHolder.setAmortizedBalance("0");
                        }
                        mortgageDetailsHolder.setInterestRate(new BigDecimal(jsonObjectResultMain.getString("interest_rate_percentage")).toPlainString());
                        mortgageDetailsHolder.setPayment(new BigDecimal(jsonObjectResultMain.getString("current_payment_amount")).toPlainString());
                        mortgageDetailsHolder.setPaymentFrequency(jsonObjectResultMain.getString("current_payment_frequency"));
                        mortgageDetailsHolder.setMaturityDate(jsonObjectResultMain.getString("maturity_date_utc"));
                        mortgageDetailsHolder.setAmortization(jsonObjectResultMain.getString("amortization_period"));
                        mortgageDetailsHolder.setTerm(jsonObjectResultMain.getString("current_term_in_months"));
                        mortgageDetailsHolder.setStatus(jsonObjectResultMain.getString("current_term_rate_type"));
                        current_term_rate_type = jsonObjectResultMain.getString("current_term_rate_type");


                        if (jsonObjectResultMain.has("amortization_period_unit"))
                        period=jsonObjectResultMain.getString("amortization_period_unit");

                        //CR-4
                        if (jsonObjectResultMain.has("prime_rate_plus_minus")) {
                            prime_rate_plus_minus = jsonObjectResultMain.getString("prime_rate_plus_minus");
                        } else {
                            prime_rate_plus_minus = "-";
                        }
                        if (jsonObjectResultMain.has("prime_rate_adjustment_percentage")) {
                            prime_rate_adjustment_percentage = jsonObjectResultMain.getString("prime_rate_adjustment_percentage");
                        } else {
                            prime_rate_adjustment_percentage = "0.0";
                        }

                        mortgageDetailsHolder.setType(jsonObjectResultMain.getString("current_term_type"));
                        mortgageDetailsHolder.setPostalCode(jsonObjectResultMain.getString("property_postal_code"));
                        mortgageDetailsHolder.setOpprCreatedDate(jsonObjectResultMain.getString("opportunity_created_date"));
                        mortgageDetailsHolder.setAttachmentCount(jsonObjectResultMain.getString("attachments_count"));


                        attachmentCount = Integer.parseInt(jsonObjectResultMain.getString("attachments_count"));
                        if (jsonObjectResultMain.has("mortgage_opportunity_type")) {
                            opportunityName = jsonObjectResultMain.getString("mortgage_opportunity_type");
                        }
                        if (jsonObjectResultMain.has("mortgage_opportunity_id")) {
                            opportunityId = jsonObjectResultMain.getString("mortgage_opportunity_id");
                        }

                        if (CommonConstants.mDebug) Log.v("AttachmnetCount", "" + attachmentCount);
                        if (attachmentCount > 0) {
                            paperClipTextView.setVisibility(View.VISIBLE);
                            mortgageDocumentsTextView.setVisibility(View.VISIBLE);
                        }
                        if (CommonConstants.mDebug)
                            Log.v("TAG", jsonObjectResultMain.getString("opportunity_created_date"));
                        if (jsonObjectResultMain.has("credit_line_amount")) {
                            mortgageDetailsHolder.setCreditLineAmt(new BigDecimal(jsonObjectResultMain.getString("credit_line_amount")).toPlainString());
                        } else {
                            mortgageDetailsHolder.setCreditLineAmt("0");
                        }

                        if (jsonObjectResultMain.has("mortgage_opportunity_id")) {
                            opportunityIdAlreadyAvailable = jsonObjectResultMain.getString("mortgage_opportunity_id");
                            if (CommonConstants.mDebug) Log.v("TAG", opportunityIdAlreadyAvailable);
                        }

                        if (jsonObjectResultMain.has("mortgage_opportunity_type")) {
                            opportunityNameAlreadyAvialble = jsonObjectResultMain.getString("mortgage_opportunity_type");
                            if (CommonConstants.mDebug)
                                Log.v("TAG", jsonObjectResultMain.getString("mortgage_opportunity_type"));
                        }

                        //CR 4 calculation for variable interest rate.
                        Dprime_rate_adjustment_percentage = Double.parseDouble(prime_rate_adjustment_percentage);
                        if (prime_rate_plus_minus.equalsIgnoreCase("+")) {
                            calculatedValue = primeRate + Dprime_rate_adjustment_percentage;
                        } else {
                            calculatedValue = primeRate + (-Dprime_rate_adjustment_percentage);
                        }
                        calculatedValue = calculatedValue * 100;
                        calculatedValue = Math.round(calculatedValue);
                        calculatedValue = calculatedValue / 100;
                        mortgageDetailsHolder.setMortgageStatus(jsonObjectResultMain.getString("mortgage_opportunity_status"));
                        mortgageDetailsHolder.setUpdatedAt(jsonObjectResultMain.getString("updated_at"));

                        JSONArray jsonArrayThumbnailData = jsonObjectResultMain.getJSONArray("attachments");
                        for (int i = 0; i < jsonArrayThumbnailData.length(); i++) {
                            JSONObject jsonObjectRow = jsonArrayThumbnailData.getJSONObject(0);
                            doc_id = jsonObjectRow.getString("doc_id");
                        }
                        if (isAdded()) {
                            setMortgageDetails();
                            showPrimeRateInformation();
                            ViewDialog.hideProgress();
                        }
                    } else if (jsonObjectResult.getString("status").equalsIgnoreCase("false")) {
                        JSONObject jsonObjectError = jsonObjectResult.getJSONObject("error");
                        String code = jsonObjectError.getString("code");
                        if (code.equals("10005")) {
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, final String reason, String details) {
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        if (isAdded()) {
                            if (value) {
                                getCurrentPrimeRate();
                                getMortgageDetails();

                            }
                        }
                    }

                    @Override
                    public void onDisconnect() {
                        Log.v("TAG", "onDisconnect");
                        ViewDialog.hideProgress();
                        if (CommonConstants.mDebug) Log.v("TAG", "" + reason);
                        if (isAdded()) {
                            if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(PrimaryResidenceFragment.this.getActivity())) {
                                ToastMessage toastMessage = new ToastMessage(PrimaryResidenceFragment.this.getContext());
                                toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                            }
                        }
                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        if (status) {
                            getCurrentPrimeRate();
                            getMortgageDetails();
                        }
                    }
                });

            }
        });
    }


    public void showPrimeRateInformation() {
        if (current_term_rate_type.equalsIgnoreCase("Fixed")) {
            informationTextView.setVisibility(View.GONE);
            originalTextView.setVisibility(View.VISIBLE);
        } else if (current_term_rate_type.equalsIgnoreCase("Variable")) {
            interestRateTV.setText("Current Interest Rate");
            originalTextView.setVisibility(View.GONE);
            interestRateTextView.setText(String.valueOf(f.format(calculatedValue)) + "%");
            informationTextView.setVisibility(View.VISIBLE);
            if (informationTextView.isShown()) {
                showPopUp();
            }
        }
    }

    public void showPopUp() {
        informationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double Dprime_rate_adjustment_percentage = Double.parseDouble(prime_rate_adjustment_percentage);
                ViewDialog.showPopUpPrimeRate(PrimaryResidenceFragment.this.getActivity(), String.valueOf(f.format(calculatedValue)) + "%", String.valueOf(f.format(primeRate)) + " %", f.format(Dprime_rate_adjustment_percentage) + " %", prime_rate_plus_minus);
            }
        });
    }

    public void setMortgageDetails() {
        if (mortgageDetailsHolder.getAmortizedBalance().contains("0") && mortgageDetailsHolder.getAmortizedBalance().length() == 1) {
            dollarTextView.setVisibility(View.INVISIBLE);
            amortizedBalanceTextView.setText("- -");
        } else {
            amortizedBalanceTextView.setText(df.format(Double.valueOf(mortgageDetailsHolder.getAmortizedBalance()).doubleValue()));
        }
        if (!mortgageDetailsHolder.getInterestRate().contains(".")) {
            interestRateTextView.setText(mortgageDetailsHolder.getInterestRate() + ".00" + "%");
        } else {
            double total = Double.parseDouble(mortgageDetailsHolder.getInterestRate());
            total = total * 100;
            total = Math.round(total);
            total = total / 100;
            interestRateTextView.setText(String.valueOf(f.format(total)) + "%");
        }
        paymentTextView.setText(df.format(Double.valueOf(mortgageDetailsHolder.getPayment().toString().trim()).doubleValue()));
        paymentFrequencyTextView.setText(mortgageDetailsHolder.getPaymentFrequency());
        maturityDateTextview.setText(mortgageDetailsHolder.getMaturityDate());
        maturityDateTextview.setText(GlobalMethods.convertMilliSecondsToDate(mortgageDetailsHolder.getMaturityDate()));
        amortizationTextView.setText(mortgageDetailsHolder.getAmortization() +" "+ period);
        if (mortgageDetailsHolder.getTerm().equalsIgnoreCase("6")) {
            termTextview.setText(mortgageDetailsHolder.getTerm() + getResources().getString(R.string.months));
        } else {
            t = calculateYears(Integer.parseInt(mortgageDetailsHolder.getTerm()));
            if (CommonConstants.mDebug) Log.v("TAG", "tt" + t);
            if (t <= 1) {
                termTextview.setText(calculateYears(Integer.parseInt(mortgageDetailsHolder.getTerm())) + getResources().getString(R.string.year));
            } else {
                termTextview.setText(calculateYears(Integer.parseInt(mortgageDetailsHolder.getTerm())) + getResources().getString(R.string.years));
            }

        }
        term = Integer.parseInt(mortgageDetailsHolder.getTerm()) / 12;
        statusTextView.setText(mortgageDetailsHolder.getStatus());
        typeTextView.setText(mortgageDetailsHolder.getType());
        postalCodeTextview.setText(mortgageDetailsHolder.getPostalCode());
        if (mortgageDetailsHolder.getCreditLineAmt().contains("0") && mortgageDetailsHolder.getCreditLineAmt().length() == 1) {
            creditLineAmtTextView.setText(" - -");
        } else {
            creditLineAmtTextView.setText("$" + df.format(Double.valueOf(mortgageDetailsHolder.getCreditLineAmt()).doubleValue()));
        }
        //   creditLineAmtTextView.addTextChangedListener(new CustomTextWatcher(creditLineAmtTextView));
        lastUpdateTextView.setText("Last Updated on " + GlobalMethods.convertMilliSecondsToDate(mortgageDetailsHolder.getUpdatedAt()));

        mortgage_opportunity_status = mortgageDetailsHolder.getMortgageStatus();
        if (CommonConstants.mDebug) Log.v("TAG", mortgage_opportunity_status);
        if (CommonConstants.mDebug) Log.v("TAG", mortgageDetailsHolder.getOpprCreatedDate());

        if (mortgage_opportunity_status.equals("open") && mortgageDetailsHolder.getOpprCreatedDate().equals("")) {
            textViewCaseFour.setVisibility(View.VISIBLE);
        } else if (mortgage_opportunity_status.equalsIgnoreCase("open")) {
            relativeLayoutCaseThree.setVisibility(View.VISIBLE);

            relativeLayoutCaseThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(PrimaryResidenceFragment.this.getActivity())) {
//                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                        ViewDialog.showNetworkPopup(PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.app_cant_perform_operation), getResources().getString(R.string.no_network_connection));
                    } else {
                        if (opportunityName.equalsIgnoreCase("Variable Rate")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprThreeVariableRateNotificationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        } else if (opportunityName.equalsIgnoreCase("Saving")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpportunityOneSavingsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        } else if (opportunityName.equalsIgnoreCase("Approaching Maturity")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprTwoApporchingMaturityActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        } else if (opportunityName.equalsIgnoreCase("Prime Rate Change")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprFourPrimeRateChangeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);

                        }
                    }
                }
            });
        } else if (mortgage_opportunity_status.equalsIgnoreCase("available")) {
            relativeLayoutCaseOne.setVisibility(View.VISIBLE);
            relativeLayoutCaseOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(PrimaryResidenceFragment.this.getActivity())) {
//                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                        ViewDialog.showNetworkPopup(PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.app_cant_perform_operation), getResources().getString(R.string.no_network_connection));
                    } else {
                        if (opportunityName.equalsIgnoreCase("Variable Rate")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprThreeVariableRateNotificationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        } else if (opportunityName.equalsIgnoreCase("Saving")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpportunityOneSavingsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        } else if (opportunityName.equalsIgnoreCase("Approaching Maturity")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprTwoApporchingMaturityActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        } else if (opportunityName.equalsIgnoreCase("Prime Rate Change")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprFourPrimeRateChangeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        }
                    }
                }
            });
        } else if (mortgage_opportunity_status.equalsIgnoreCase("close")) {
            relativeLayoutCaseTwo.setVisibility(View.VISIBLE);
            relativeLayoutCaseTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(PrimaryResidenceFragment.this.getActivity())) {
//                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                        ViewDialog.showNetworkPopup(PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.app_cant_perform_operation), getResources().getString(R.string.no_network_connection));
                    } else {
                        if (opportunityName.equalsIgnoreCase("Variable Rate")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprThreeVariableRateNotificationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        } else if (opportunityName.equalsIgnoreCase("Saving")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpportunityOneSavingsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        } else if (opportunityName.equalsIgnoreCase("Approaching Maturity")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprTwoApporchingMaturityActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        } else if (opportunityName.equalsIgnoreCase("Prime Rate Change")) {
                            Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprFourPrimeRateChangeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("opportunity_id", opportunityId);
                            intent.putExtra("activity_title", title);
                            intent.putExtra("mortgage_id", mortgageID);
                            startActivity(intent);
                        }
                    }
                }
            });
        }

    }


    public int calculateYears(int months) {
        if (months >= 12) {
            months = months / 12;
            return months;
        } else {
            return months;
        }
    }

    public void onDisconnect() {
        try {
            ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                @Override
                public void onConnect(boolean value) {
                    getCurrentPrimeRate();
                    getMortgageDetails();
                }

                @Override
                public void onDisconnect() {
                    Log.v("TAG", "onDisconnect");
                    ViewDialog.hideProgress();
                    if (isAdded()) {
                        if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection(PrimaryResidenceFragment.this.getActivity())) {
                            toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                        }
                    }
                }

                @Override
                public void onException(Exception e) {

                }

                @Override
                public void onInternetStatusChanged(boolean status) {
                    getCurrentPrimeRate();
                    getMortgageDetails();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public Bitmap base64(ImageView imageView, String lenderString) {

        byte[] decodedString = Base64.decode(lenderString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        imageView.setImageBitmap(bitmap);
        return bitmap;
    }

    public void editMortgageButtonClick(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // newly added for new CR changes.
                if (GlobalMethods.haveNetworkConnection(PrimaryResidenceFragment.this.getActivity()) && MeteorSingleton.getInstance().isConnected()) {
                    if (mortgage_opportunity_status != null && mortgage_opportunity_status.equalsIgnoreCase("close")) {
                        ViewDialog.showAlertPopUp(PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.edit_mortgage_restriction), getResources().getString(R.string.help_screen_one));
                    } else {
                        editActivityOpened = true;
                        GlobalMethods.currentMortgageID = mortgageID;

                        ViewDialog.showProgress(R.string.help_screen_one, PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.please_wait));
                        Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), EditMortgageActivity.class);
                        intent.putExtra("MortgageID", mortgageID);
                        intent.putExtra("lenderLogo", lenderLogo);
                        intent.putExtra("lenderid", lenderid);
                        intent.putExtra("term", t);
                        startActivity(intent);
                        ViewDialog.hideProgress();
                    }
                } else {
                    ViewDialog.showNetworkPopup(PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.app_cant_perform_operation), getResources().getString(R.string.no_network_connection));
                }
            }
        });

    }

    public void analyzeNowBtnClick() {

        btn_analyze_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                analyzeNowAPI();
                if (GlobalMethods.haveNetworkConnection(PrimaryResidenceFragment.this.getActivity()) && MeteorSingleton.getInstance().isConnected()) {
                    ViewDialog.showProgress(R.string.help_screen_one, PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.please_wait));
                    if (flag) {
                        btn_analyze_now.setEnabled(false);
                        analyzeNowAPI();
                    }
                    flag = true;
                } else {
//                    toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                    ViewDialog.showNetworkPopup(PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.app_cant_perform_operation), getResources().getString(R.string.no_network_connection));
                }
            }
        });
    }

    public void btnBackToDashboardClick() {
        btn_backToDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDashboardFragment myDashboardFragment = new MyDashboardFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, myDashboardFragment).commit();
            }
        });

    }

    public void analyzeNowAPI() {
        if (CommonConstants.mDebug) Log.v("TAG", "on Analyze btn click");
        HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
        stringObjectHashMap.put("mortgage_id", mortgageID);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("analyzeMortgage", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v("TAG", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                        final JSONObject jsonObjectData = jsonObject.getJSONObject("data");

                        LayoutInflater inflater = ((Activity) PrimaryResidenceFragment.this.getActivity()).getLayoutInflater();
                        final View myDialog = inflater.inflate(R.layout.custom_dialog_oppr_available, null);
                        alertDialogBuilder.setView(myDialog);
                        alertDialogBuilder.setCancelable(false);
                        alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        if (ViewDialog.kProgressHUD.isShowing()) {
                            ViewDialog.hideProgress();
                        }
                        TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
                        TextView text = (TextView) myDialog.findViewById(R.id.text_dialog);
                        TextView close = (TextView) myDialog.findViewById(R.id.closeButton);
                        close.setVisibility(View.INVISIBLE);

                        text.setText(getResources().getString(R.string.we_found_better_option_for_your_mortgage) + title + getResources().getString(R.string.mortgage));
                        titleTextView.setText(R.string.title_oppo_available_dialog);
                        Button viewLaterButton = (Button) myDialog.findViewById(R.id.btn_view_later);
                        Button viewNowButton = (Button) myDialog.findViewById(R.id.btn_view_now);

                        viewLaterButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                globalMethods.replaceFragment(new MyDashboardFragment(), PrimaryResidenceFragment.this.getActivity(), null);
                            }
                        });

                        viewNowButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (GlobalMethods.haveNetworkConnection(PrimaryResidenceFragment.this.getActivity()) && MeteorSingleton.getInstance().isConnected()) {
                                        if (!MeteorSingleton.getInstance().isConnected())
                                            MeteorSingleton.getInstance().reconnect();
                                        if (jsonObjectData.getString("code").equalsIgnoreCase("16110")) {
                                            JSONObject jsonObjectResult = jsonObjectData.getJSONObject("result");
                                            if (jsonObjectResult.getString("opportunity_type").equalsIgnoreCase("Variable Rate")) {
                                                opportunityId = jsonObjectResult.getString("opportunity_id");
                                                Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprThreeVariableRateNotificationActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("opportunity_id", opportunityId);
                                                intent.putExtra("activity_title", title);
                                                startActivity(intent);
                                            } else if (jsonObjectResult.getString("opportunity_type").equalsIgnoreCase("Saving")) {
                                                opportunityId = jsonObjectResult.getString("opportunity_id");
                                                Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpportunityOneSavingsActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("opportunity_id", opportunityId);
                                                intent.putExtra("activity_title", title);
                                                startActivity(intent);
                                            } else if (jsonObjectResult.getString("opportunity_type").equalsIgnoreCase("Approaching Maturity")) {
                                                opportunityId = jsonObjectResult.getString("opportunity_id");
                                                Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprTwoApporchingMaturityActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("opportunity_id", opportunityId);
                                                intent.putExtra("activity_title", title);
                                                startActivity(intent);
                                            } else if (jsonObjectResult.getString("opportunity_type").equalsIgnoreCase("Prime Rate Change")) {
                                                opportunityId = jsonObjectResult.getString("opportunity_id");
                                                Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprFourPrimeRateChangeActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.putExtra("opportunity_id", opportunityId);
                                                intent.putExtra("activity_title", title);
                                                startActivity(intent);
                                            }
                                        } else if (jsonObjectData.getString("code").equalsIgnoreCase("16109")) {
                                            if (jsonObjectData.getString("message").equalsIgnoreCase("Opportunity already available")) {
                                                if (opportunityNameAlreadyAvialble.equalsIgnoreCase("Variable Rate")) {
                                                    Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprThreeVariableRateNotificationActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("opportunity_id", opportunityIdAlreadyAvailable);
                                                    intent.putExtra("activity_title", title);
                                                    startActivity(intent);
                                                } else if (opportunityNameAlreadyAvialble.equalsIgnoreCase("Saving")) {
                                                    Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpportunityOneSavingsActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.putExtra("opportunity_id", opportunityIdAlreadyAvailable);
                                                    intent.putExtra("activity_title", title);
                                                    startActivity(intent);
                                                } else if (opportunityNameAlreadyAvialble.equalsIgnoreCase("Approaching Maturity")) {
                                                    Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprTwoApporchingMaturityActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.putExtra("opportunity_id", opportunityIdAlreadyAvailable);
                                                    intent.putExtra("activity_title", title);
                                                    startActivity(intent);
                                                } else if (opportunityNameAlreadyAvialble.equalsIgnoreCase("Prime Rate Change")) {
                                                    Intent intent = new Intent(PrimaryResidenceFragment.this.getActivity(), OpprFourPrimeRateChangeActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.putExtra("opportunity_id", opportunityIdAlreadyAvailable);
                                                    intent.putExtra("activity_title", title);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                        alertDialog.dismiss();
                                    } else {
//                                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                                        alertDialog.dismiss();
                                        ViewDialog.showNetworkPopup(PrimaryResidenceFragment.this.getActivity(), getResources().getString(R.string.app_cant_perform_operation), getResources().getString(R.string.no_network_connection));
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        if (jsonObjectData.getString("code").equalsIgnoreCase("16108")) {
                            alertDialog.dismiss();
                            lastUpdateTextView.setVisibility(View.GONE);
                            showAlertPopUp(PrimaryResidenceFragment.this.getActivity(), "Mortgage looks good at this time. We will keep you posted.", "Monitor My Mortgage");

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(String error, final String reason, String details) {
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean value) {
                        if (value) {
                            analyzeNowBtnClick();
                        }
                    }

                    @Override
                    public void onDisconnect() {
                        Log.v("TAG", "onDisconnect");
                        ToastMessage toastMessage = new ToastMessage(PrimaryResidenceFragment.this.getContext());
                        toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
//                        ViewDialog.showAlertPopUp(PrimaryResidenceFragment.this.getActivity(), reason, getResources().getString(R.string.error));
                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        if (status) {
                            analyzeNowBtnClick();
                        }
                    }
                });

            }
        });
    }


    public void showAlertPopUp(Activity activity, final String msg, String title) {
        try {
            alertDialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = ((Activity) activity).getLayoutInflater();
            final View myDialog = inflater.inflate(R.layout.custom_dailog, null);
            alertDialogBuilder.setView(myDialog);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.setCancelable(false);
            TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
            TextView text = (TextView) myDialog.findViewById(R.id.text_dialog);
            TextView close = (TextView) myDialog.findViewById(R.id.closeButton);
            close.setVisibility(View.INVISIBLE);


            text.setText(msg);
            titleTextView.setText(title);
            Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    globalMethods.replaceFragment(new MyDashboardFragment(), PrimaryResidenceFragment.this.getActivity(), null);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
