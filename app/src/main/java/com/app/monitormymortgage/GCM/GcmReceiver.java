package com.app.monitormymortgage.GCM;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


/**
 * Created by Phil Inglis, DevBBQ on 2017-02-14.
 */

public class GcmReceiver extends WakefulBroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("GCM", "In Receive Method of Broadcast Receiver");
        ComponentName cn = new ComponentName(context.getPackageName(), RegistrationIntentService.class.getName());
        startWakefulService(context, intent.setComponent(cn));
        setResultCode(Activity.RESULT_OK);
    }
}
