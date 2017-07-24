package com.app.monitormymortgage.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Sangram on 26/06/17.
 */

/* Alert Dialog and Progress Dialog. */
public class DialogManager {

    private AlertDialogManager alertDialogManager;

    private Context mContext;

    public DialogManager(Context mContext) {
        this.mContext = mContext;
    }

    protected ProgressDialog progressDialog;

    private int activeBusyTasks = 0;


    public AlertDialogManager getAlertDialogManager() {
        if (this.alertDialogManager != null) {
            return this.alertDialogManager;
        } else {
            this.alertDialogManager = new AlertDialogManager(mContext);
            return this.alertDialogManager;
        }
    }

    /**
     * Show the busy progress overlay with a default message.
     * <p/>
     * Note that the show and hide calls need to be balanced.
     */
    public void showBusyProgress() {
        this.showBusyProgress(null);
    }

    /**
     * Show the busy progress overlay with the specified message
     * <p/>
     * Note that the show and hide calls need to be balanced.
     */
    public void showBusyProgress(String message) {
        this.activeBusyTasks++;
        try {
            if (this.progressDialog == null) {
                this.progressDialog = new ProgressDialog(mContext);
                this.progressDialog.setCancelable(false);
                this.progressDialog.show();
            }

            if (message == null) {
                this.progressDialog.setMessage("Please wait....");
            } else {
                this.progressDialog.setMessage(message);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show the busy progress overlay with the specified message
     * <p/>
     * Note that the show and hide calls need to be balanced.
     */
    public void showBusyProgress(int message) {
        this.activeBusyTasks++;
        try {
            if (this.progressDialog == null) {
                this.progressDialog = new ProgressDialog(mContext);
                this.progressDialog.setCancelable(false);
                this.progressDialog.show();
            }

            if (message == 0) {
                this.progressDialog.setMessage("Please wait....");
            } else {
                this.progressDialog.setMessage(mContext.getResources().getString(message));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hide the busy progress overlay.
     * <p/>
     * Note that the show and hide calls need to be balanced.
     */
    public void hideBusyProgress() {
        this.activeBusyTasks--;
        try {
            if (this.activeBusyTasks < 0) {
                this.activeBusyTasks = 0;
            }

            if (this.activeBusyTasks == 0) {
                if (this.progressDialog != null) {
                    this.progressDialog.dismiss();
                    this.progressDialog = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
    * Use of Busy Progress Overlay
    * <p/>
    * DilaogManager dialogManager = new DialogManager(context);
    * dialogManager.showBusyProgress(message);
    */


}
