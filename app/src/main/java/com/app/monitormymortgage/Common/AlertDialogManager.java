package com.app.monitormymortgage.Common;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.app.monitormymortgage.R;

/**
 * Created by Sangram on 26/06/17.
 */

public class AlertDialogManager {


    private Context context;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    LayoutInflater inflater;
    String communication_pref;


    public AlertDialogManager(Context context) {
        this.alertDialog = new AlertDialog.Builder(context).create();
        this.context = context;
        this.inflater = ((Activity) context).getLayoutInflater();
        communication_pref=CommonConstants.contactPreference;
    }

    public interface onSingleButtonClickListner {
        void onPositiveClick();
    }

    public interface onTwoButtonClickListner extends onSingleButtonClickListner {
        void onNegativeClick();
    }

    public void showAlertPopUp(Context context, final String msg, String title, final onTwoButtonClickListner twoButtonClickListner) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
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
                    if (twoButtonClickListner != null) {
                        twoButtonClickListner.onNegativeClick();
                    }
                    alertDialog.dismiss();
                }
            });

            text.setText(msg);
            titleTextView.setText(title);
            Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (twoButtonClickListner != null) {
                        twoButtonClickListner.onPositiveClick();
                    }
                    alertDialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String showContactPref(final Context context, int layout, String title, String message, final onSingleButtonClickListner clickListner) {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View myDialog = inflater.inflate(layout, null);  // R.layout.custom_dialog_broker_cnt
        alertDialogBuilder.setView(myDialog);
        alertDialogBuilder.setCancelable(false);

        TextView titleTextView = (TextView) myDialog.findViewById(R.id.titleTextView);
        TextView close = (TextView) myDialog.findViewById(R.id.closeButton);

        final CheckBox phnCheckBox = (CheckBox) myDialog.findViewById(R.id.radioButtonPhn);
        final CheckBox emailCheckBox = (CheckBox) myDialog.findViewById(R.id.radioButtonEmail);
        final TextView errorTextView = (TextView) myDialog.findViewById(R.id.errorTextView);


        titleTextView.setText(title);  // R.string.how_broker_connect

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Button dialogButton = (Button) myDialog.findViewById(R.id.btn_dialog);

        phnCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communication_pref = "Phone";
                errorTextView.setVisibility(View.INVISIBLE);
            }
        });
        emailCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                communication_pref = "Email";
                errorTextView.setVisibility(View.INVISIBLE);
            }
        });


        if (communication_pref != null && communication_pref.equalsIgnoreCase("Email")) {
            emailCheckBox.setChecked(true);
        } else if (communication_pref != null && communication_pref.equalsIgnoreCase("Phone")) {
            phnCheckBox.setChecked(true);
        } else if (communication_pref != null && communication_pref.equalsIgnoreCase("Both")) {
            emailCheckBox.setChecked(true);
            phnCheckBox.setChecked(true);
        }

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phnCheckBox.isChecked() && !emailCheckBox.isChecked()) {
                    errorTextView.setVisibility(View.VISIBLE);
                    errorTextView.setText(context.getString(R.string.select_contact_prefernce));
                } else {
                    if (phnCheckBox.isChecked() && emailCheckBox.isChecked()) {
                        communication_pref = "Phone,Email";
                    } else if (phnCheckBox.isChecked()) {
                        communication_pref = "Phone";
                    } else if (emailCheckBox.isChecked()) {
                        communication_pref = "Email";
                    }

                    if (clickListner!=null)
                    {
                        clickListner.onPositiveClick();
                        alertDialog.dismiss();
                    }
                }
            }
        });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        return communication_pref;
    }


}
