package com.app.monitormymortgage.BaseClasses;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.monitormymortgage.Activity.LoginActivity;
import com.app.monitormymortgage.Common.CommonConstants;
import com.app.monitormymortgage.Common.DialogManager;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.R;
import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.ErisConnectionListener;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.Preferences;
import com.eris.androidddp.ResultListener;

import java.util.HashMap;


public class BaseFragment extends Fragment implements ErisConnectionListener {

    boolean facebookUserLoginFlag;
    boolean googleUserLoginFlag;
    private static final String TAG = BaseActivity.class.getSimpleName();
    Context context;

    GlobalMethods globalMethods;
    ToastMessage toastMessage;
    DialogManager dialogManager;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("GoogleUserLoginFlag", false);
        ErisCollectionManager.getInstance().setConnectionListener(this);
        context=this.getContext();
        globalMethods=new GlobalMethods(context);
        toastMessage=new ToastMessage(context);
        dialogManager=new DialogManager(context);
    }


    public void hideDialog()
    {

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


    public void showCustomToast(String message)
    {
        toastMessage.showToastMsg(message, Toast.LENGTH_SHORT);
    }

    public void showProgress(Context context)
    {
        ViewDialog.showProgress(R.string.help_screen_one, (Activity) context, context.getResources().getString(R.string.please_wait));
    }

    public void hideProgress()
    {
        ViewDialog.hideProgress();
    }

    /*Get Global Method class object.*/
    protected GlobalMethods getGlobalMethods() {
        return globalMethods;
    }

    private String getLoginToken() {
        return getSharedPreferences().getString(Preferences.Keys.LOGIN_TOKEN, null);
    }

    private SharedPreferences getSharedPreferences() {
        return getContext().getSharedPreferences(Preferences.FILE_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false);
    }


    @Override
    public void onConnect(boolean value) {
        recreateSession();
//        MyDashboardFragment.getAllMasters();
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
