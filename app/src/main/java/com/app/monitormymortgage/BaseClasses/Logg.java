package com.app.monitormymortgage.BaseClasses;

import android.util.Log;

/**
 * Created by Sangram on 29/06/17.
 */

public class Logg {


    /* Disable printLog flag if want to disable Logs.*/
    public static boolean printLog = true;

    public static void i(Object obj, String message) {
        if (printLog) {
            try {
                Log.i(getClassName(obj), message);
            } catch (Exception ignored) {
            }
        }
    }

    public static void d(Object obj, String message) {
        if (printLog) {
            try {
                Log.d(getClassName(obj), message);
            } catch (Exception ignored) {
            }
        }
    }

    public static void v(Object obj, String message) {
        if (printLog) {
            try {
                Log.v(getClassName(obj), message);
            } catch (Exception ignored) {
            }
        }
    }

    private static String getClassName(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        } else {
            return obj.getClass().getSimpleName();
        }
    }
}
