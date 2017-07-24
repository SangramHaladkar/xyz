package com.app.monitormymortgage.Common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.app.monitormymortgage.R;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class GlobalMethods {

    private ObjectMapper mapper;
    private Context mContext;
    public static String currentMortgageID;
    private String facebookUserLoginId;
    private boolean facebookUserLoginFlag;
    private String googleUserLoginId;
    private boolean googleUserLoginFlag;
    private String username;


    /*Constructor initialise Object Mapper for Json Parsing. */
    public GlobalMethods(Context context) {
        mapper = new ObjectMapper();
        mContext = context;
    }


    /* Check Internet Connection */
    public static boolean haveNetworkConnection(Activity activity) {

        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null &&
                        activeNetworkInfo.getType() == networkType)
                    return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;

    }

    /*
    * Email validation
    * @param email
    * */
    public static boolean isValidEmailId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    /*
    * Convert UTC date milliseconds to Date Format 'MMM d,YYYY'
    * @param milliseconds
    * */
    public static String convertMilliSecondsToDate(String milliseconds) {
        long millisecond = Long.parseLong(milliseconds);
        String dateString = DateFormat.format("MMM d, yyyy", new Date(millisecond)).toString();
        if (CommonConstants.mDebug) Log.v("TAG", dateString);
        return dateString;
    }

    /**
     * replace old fragment with new one
     * if no fragment in back-stack  create it
     *
     * @param fragment
     */
    public void replaceFragment(Fragment fragment, FragmentActivity contextFragment, Bundle bundle) {

        try {
//            String backStateName = fragment.getClass().getName();
//            String fragmentTag = backStateName;
            FragmentManager manager = contextFragment.getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container, fragment);
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * convert json to list object
     *
     * @param jasondata
     * @param target
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException if type=List<User>
     *                                List<User> userlist=   convertJsonToPOJO(jsonString,User.class);
     */
    public <T> T convertJsonToPOJO(String jasondata, Class<?> target) throws IOException, ClassNotFoundException {
        return mapper.readValue(jasondata, mapper.getTypeFactory().constructCollectionType(List.class, Class.forName(target.getName())));
    }


    /*
    * getUserId() - based on flag
    *
    * User id stored in Shared Prefernces.
    * returned based on flag.
    * */
    public String getUserId() {
        this.getStoredUserId();
        String userId = "";
        if (facebookUserLoginFlag) {
            userId = facebookUserLoginId;
        } else if (googleUserLoginFlag) {
            userId = googleUserLoginId;
        } else {
            userId = username;
        }
        return userId;
    }


    /*
    * Get Shared Prefernce Data.
    *
    * */
    public void getStoredUserId() {
        /* Get Shared Preferences get user id (normal, social) based on flag. */
        facebookUserLoginId = PreferenceManager.getDefaultSharedPreferences(mContext).getString("FacebookUserLoginId", "");
        facebookUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("FacebookUserLoginFlag", false);
        googleUserLoginId = PreferenceManager.getDefaultSharedPreferences(mContext).getString("GoogleUserLoginId", "");
        googleUserLoginFlag = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("GoogleUserLoginFlag", false);
        username = PreferenceManager.getDefaultSharedPreferences(mContext).getString("username", "");
    }


    /*
    * Convert base64 string into Image bitmap
    * @param - base64String
    * please note that base64 string is splitted string
    * */
    public Bitmap base64(String base64String) {
        try {
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    * Split string based on split parameter.
    * Returned splitted string
    * */
    public String spiltString(String originalString, String splitParam) {
        try {
            String newConvertedString = "";
            String[] parts = originalString.split(splitParam);
            if (parts.length > 0) {
                String part1 = parts[0];
                return newConvertedString = parts[1];
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
