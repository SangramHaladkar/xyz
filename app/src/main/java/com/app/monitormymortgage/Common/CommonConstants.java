package com.app.monitormymortgage.Common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

/**
 * Created by Sangram on 14/04/16.
 */
public class CommonConstants {
    // TODO: Enable or disable LOG for improve performance - build release only.

    public static final boolean mDebug = false;
    public static boolean dashboardReplaceFlag = true;

    public static boolean flag=false;
    ObjectMapper mapper;

    public CommonConstants() {
        mapper=new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
    }

    //TODO:change or replace server URL's
    public enum URLS {
        stagingTekyzURLNew("app.monitormymortgage.com", "staging"),
        devSAASUrl("app.monitormymortgage.com", "dev");

        private String URL;
        private String enviromentVariable;

        URLS(final String Url, final String enviromentVariable) {
            this.URL = Url;
            this.enviromentVariable = enviromentVariable;
        }

        public String getURL() {
            return URL;
        }
    }

    public static void registerIntercom(String userId)
    {
        Registration registration = Registration.create().withUserId(userId);
        Intercom.client().registerIdentifiedUser(registration);
    }


//    public static final String stagingTekyzURL = "app.monitormymortgage.com";
//    public static final String stagingTekyzURLNew = "m3.staging2.tekyz.com";
    //public static final String stagingTekyzURLNew = "m3-staging.devbbq.com";
    public static final String stagingTekyzURLNew = "app.monitormymortgage.com";
    public static final String stagingURLDev = "staging.monitormymortgage.com";
    public static final String devSAASUrl = "app.monitormymortgage.com";
    public static final String localURLUJMachine = "10.0.1.60:3000";
    public static final String localURLSarangMachine = "10.0.1.39:3000";
    public static final String privacyPolicyURL = "http://" + stagingTekyzURLNew + "/privacy-policy.html";
    public static final String termsAndConditionURL = "http://" + stagingTekyzURLNew + "/terms-condition.html";
    public static final String faqURL = "http://" + stagingTekyzURLNew + "/faq.html";

    public static final URLS CONNECTIONURL = URLS.devSAASUrl;
    public static final String CONNECTIONURLfinal = CONNECTIONURL.getURL();
    // Final strings.
    public static final String requestFrom = "android";
    public static final String language = "en";


    public static final String passwordSimplificationRegex = "^.*(?=.{8,25})(?=.*\\d)(?=.*[A-Z])(?=.*[a-zA-Z]).*$";
    public static final String opprtuNotificationPref = "Email";
    public static final String contactPref = "Email";
    public static final String notifySavingPerMonth = "50";
    public static final String notifySavingOverTerm = "5000";
    public static final String notifyRateIncrease = "10";
    public static final String notifyUntilMaturity = "120";


    public static String countryCode = "";

    /* Get User Data */

    public static String contactPreference="";



}
