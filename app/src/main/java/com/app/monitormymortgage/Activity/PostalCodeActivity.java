package com.app.monitormymortgage.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.ResultListener;
import com.app.monitormymortgage.BaseClasses.BaseActivity;
import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.BaseClasses.ViewDialog;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PostalCodeActivity extends BaseActivity {
    private ListView lv;
    ArrayAdapter<String> adapter;
    EditText inputSearch;
    ArrayList<String> productList;
    String postalCode;
    TextView closeButton;
    String USDollars = "";
    String lastThree = "";
    private static final char space = ' ';
    TextView postalCodeNotFoundTextView;
    ToastMessage toastMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postal_code);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toastMessage = new ToastMessage(this);
        productList = new ArrayList<>();
        closeButton = (TextView) findViewById(R.id.closeButton);
        postalCodeNotFoundTextView = (TextView) findViewById(R.id.postalCodeNotFoundTextView);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Hide Keyboard.
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                finish();
            }
        });

        lv = (ListView) findViewById(R.id.list_view);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        Intent intent = this.getIntent();
        if (intent != null) {
            String strdata = intent.getExtras().getString("USDollars");
            if (CommonConstants.mDebug) Log.v("TAG", strdata);
            if (strdata != null) {
                if (strdata.equals("US Dollars")) {
                    inputSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
//                inputSearch.setKeyListener(new DigitsKeyListener());
                } else {
                    inputSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            } else {
                inputSearch.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(inputSearch, InputMethodManager.SHOW_IMPLICIT);
        adapter = new ArrayAdapter<String>(PostalCodeActivity.this, R.layout.list_item, R.id.product_name, productList);
        lv.setAdapter(adapter);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                if (cs.length() > 2) {
                    if (productList.size() <= 0) {
                        if (GlobalMethods.haveNetworkConnection(PostalCodeActivity.this)) {
                            getPostalCode(cs.toString());
                            ViewDialog.showProgress(R.string.help_screen_one, PostalCodeActivity.this, getResources().getString(R.string.please_wait));
                        } else {
                            toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                            inputSearch.setText("");
//                            ViewDialog.showAlertPopUp(PostalCodeActivity.this, getResources().getString(R.string.no_network_found), getResources().getString(R.string.error));
                        }

                    } else {
                        PostalCodeActivity.this.adapter.getFilter().filter(cs.toString());
                    }
                } else if (cs.length() == 0) {
                    productList.clear();
                    adapter = new ArrayAdapter<String>(PostalCodeActivity.this, R.layout.list_item, R.id.product_name, productList);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove spacing char
                if (s.length() > 0 && (s.length() % 4) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.

                if (s.length() > 0 && (s.length() % 4) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space

                    if (!Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                postalCode = parent.getAdapter().getItem(position).toString();
                /* Hide Keyboard */
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                /* Send postal code data through intent */
                if (CommonConstants.mDebug) Log.v("TAG", postalCode);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("postal_code", postalCode);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    public void getPostalCode(final String value) {
        if (lastThree.isEmpty()) {
            lastThree = value;
        }
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("searchkey", value);
        stringObjectHashMap.put("lang", "en");
        stringObjectHashMap.put("req_from", "android");

        ErisCollectionManager.getInstance().callMethod("getPostalCode", new Object[]{stringObjectHashMap}, new ResultListener() {
            @Override
            public void onSuccess(String result) {
                if (CommonConstants.mDebug) Log.v("TAG", result);
                String str = "";
                try {
                    JSONObject jsonObjectMain = new JSONObject(result);
                    JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                    JSONArray jsonArrayResult = jsonObjectData.getJSONArray("result");
                    for (int i = 0; i < jsonArrayResult.length(); i++) {
                        str = jsonArrayResult.getString(i);
                        productList.add(str);
                    }

                    adapter = new ArrayAdapter<String>(PostalCodeActivity.this, R.layout.list_item, R.id.product_name, productList);
                    lv.setAdapter(adapter);
                    if (ViewDialog.kProgressHUD.isShowing()) {
                        ViewDialog.hideProgress();
                    }
                    PostalCodeActivity.this.adapter.getFilter().filter(value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error, String reason, String details) {
                if (CommonConstants.mDebug) Log.v("TAG", reason);
                ErisCollectionManager.getInstance().reconnectMeteor(new ErisConnectionListener() {
                    @Override
                    public void onConnect(boolean reason) {
                        getPostalCode(value);
                    }

                    @Override
                    public void onDisconnect() {

                    }

                    @Override
                    public void onException(Exception e) {

                    }

                    @Override
                    public void onInternetStatusChanged(boolean status) {
                        getPostalCode(value);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
