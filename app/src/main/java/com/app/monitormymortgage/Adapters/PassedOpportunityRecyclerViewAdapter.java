package com.app.monitormymortgage.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.monitormymortgage.BaseClasses.ToastMessage;
import com.app.monitormymortgage.Common.AlertDialogManager;
import com.app.monitormymortgage.Common.DialogManager;
import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.DataHolderClasses.OpportunityDataPassed;
import com.app.monitormymortgage.DataHolderClasses.ResultPassedOpportunity;
import com.app.monitormymortgage.R;
import com.eris.androidddp.ErisCollectionManager;
import com.eris.androidddp.MeteorSingleton;
import com.eris.androidddp.ResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Sangram on 26/06/17.
 */

public class PassedOpportunityRecyclerViewAdapter extends RecyclerView.Adapter<PassedOpportunityRecyclerViewAdapter.PassedOpportunityViewHolder> {

    private List<ResultPassedOpportunity> passedOpportunitiesList;
    private Context mContext;
    private String communication_pref;
    private String userId;
    DialogManager dialogManager;
    ToastMessage toastMessage;

    public PassedOpportunityRecyclerViewAdapter(List<ResultPassedOpportunity> passedOpportunitiesList, Context mContext, String communication_pref, String userId) {
        this.passedOpportunitiesList = passedOpportunitiesList;
        this.mContext = mContext;
        this.communication_pref = communication_pref;
        this.userId = userId;
        dialogManager = new DialogManager(mContext);
        toastMessage = new ToastMessage(mContext);
    }


    static class PassedOpportunityViewHolder extends RecyclerView.ViewHolder {
        TextView mortgageNameTV;
        Button contactBrokerBtn;
        RecyclerView opportunityRecyclerView;

        PassedOpportunityViewHolder(View view) {
            super(view);
            mortgageNameTV = (TextView) view.findViewById(R.id.mortgageNameTV);
            contactBrokerBtn = (Button) view.findViewById(R.id.btn_contact_broker);
            opportunityRecyclerView = (RecyclerView) view.findViewById(R.id.opportunityRecyclerView);
        }
    }

    @Override
    public PassedOpportunityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lapsed_opportunity_cell, parent, false);
        return new PassedOpportunityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PassedOpportunityViewHolder holder, int position) {

        try {
            ResultPassedOpportunity resultPassedOpportunity = passedOpportunitiesList.get(position);
            holder.mortgageNameTV.setText(passedOpportunitiesList.get(position).getTitle());
              /* Set tag to button and send data.*/
            holder.contactBrokerBtn.setTag(passedOpportunitiesList.get(position));

            /*  Recycler View - Opportunity List. */
            // Set layout manager to Recycler view.
            holder.opportunityRecyclerView.setHasFixedSize(true);
            holder.opportunityRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

            // Pass adapter to recycler view.
            List<OpportunityDataPassed> list = passedOpportunitiesList.get(position).getOpportunityDataPassedList();
            OpportunityRecyclerViewAdapter opportunityAdapter = new OpportunityRecyclerViewAdapter(list, mContext);
            holder.opportunityRecyclerView.setAdapter(opportunityAdapter);

            if (passedOpportunitiesList.get(position).getMortgage_opportunity_status().equalsIgnoreCase("close")) {
                holder.contactBrokerBtn.setClickable(false);
                holder.contactBrokerBtn.setText(mContext.getResources().getString(R.string.broker_response_pending));
            } else {
                holder.contactBrokerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /* Get tagged data. */
                        if (!MeteorSingleton.getInstance().isConnected() && !GlobalMethods.haveNetworkConnection((Activity) mContext)) {
                            toastMessage.showToastMsg(R.string.no_network_found, Toast.LENGTH_SHORT);
                        } else {
                            ResultPassedOpportunity data = (ResultPassedOpportunity) view.getTag();
                            showContactPref(data.get_id(), data);
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return passedOpportunitiesList.size();
    }


    private void showContactPref(final String id, final ResultPassedOpportunity data) {

        communication_pref = getAlertDialogManager().showContactPref(mContext, R.layout.custom_dialog_broker_cnt, mContext.getResources().getString(R.string.how_broker_connect), "", new AlertDialogManager.onSingleButtonClickListner() {
            @Override
            public void onPositiveClick() {
                HashMap<String, Object> stringObjectHashMap = new HashMap<String, Object>();
                stringObjectHashMap.put("mortgage_id", id);
                stringObjectHashMap.put("user_id", userId);
                stringObjectHashMap.put("communication_pref", communication_pref);
                stringObjectHashMap.put("lang", "en");
                stringObjectHashMap.put("req_from", "android");

                ErisCollectionManager.getInstance().callMethod("contactBroker", new Object[]{stringObjectHashMap}, new ResultListener() {
                    @Override
                    public void onSuccess(String result) {
                        Log.v("TAG", result);
                        data.setMortgage_opportunity_status("close");
                        try {
                            JSONObject jsonObjectMain = new JSONObject(result);
                            JSONObject jsonObjectData = jsonObjectMain.getJSONObject("data");
                            if (jsonObjectData.getString("code").equalsIgnoreCase("16850")) {
                                showConfirmation(jsonObjectData.getString("message"));
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
        });
    }

    private AlertDialogManager getAlertDialogManager() {
        return dialogManager.getAlertDialogManager();
    }


    private void showConfirmation(String message) {
        getAlertDialogManager().showAlertPopUp(mContext, message, mContext.getResources().getString(R.string.help_screen_one), new AlertDialogManager.onTwoButtonClickListner() {
            @Override
            public void onNegativeClick() {
                notifyDataSetChanged();
            }

            @Override
            public void onPositiveClick() {
                notifyDataSetChanged();
            }
        });

    }


}
