package com.app.monitormymortgage.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.DataHolderClasses.MortgageArrayHolder;
import com.app.monitormymortgage.R;

import java.util.*;

/**
 * Created by Sangram on 28/06/17.
 */

public class MortgageRecyclerViewAdapter extends RecyclerView.Adapter<MortgageRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    List<MortgageArrayHolder> mortgageArrayHolderList;
    GlobalMethods globalMethods;

    public MortgageRecyclerViewAdapter(Context mContext, List<MortgageArrayHolder> mortgageArrayHolderList) {
        this.mContext = mContext;
        this.mortgageArrayHolderList = mortgageArrayHolderList;
        globalMethods = new GlobalMethods(mContext);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mortgageNameTV;
        TextView mortgageStatusTV;
        TextView otherTV;
        ImageView mortgageImageView;

        public ViewHolder(View view) {
            super(view);
            mortgageNameTV = (TextView) view.findViewById(R.id.mortgageNameTextView);
            mortgageStatusTV = (TextView) view.findViewById(R.id.mortgageStatusTextView);
            otherTV = (TextView) view.findViewById(R.id.otherTextView);
            mortgageImageView = (ImageView) view.findViewById(R.id.lenderLogoImageView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mortgage_list_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        try {
            holder.mortgageNameTV.setText(mortgageArrayHolderList.get(position).getTitle());

            /*
            * Set Status based on condition.
            * */
            if (mortgageArrayHolderList.get(position).getMortgage_opportunity_status().equalsIgnoreCase("open") && !mortgageArrayHolderList.get(position).getOpportunity_created_at().equalsIgnoreCase("")) {
                holder.mortgageStatusTV.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                holder.mortgageStatusTV.setTextSize(12f);
                holder.mortgageStatusTV.setText("{fa-history} Last Opportunity " + GlobalMethods.convertMilliSecondsToDate(mortgageArrayHolderList.get(position).getOpportunity_created_at()));
            } else if (mortgageArrayHolderList.get(position).getMortgage_opportunity_status().equalsIgnoreCase("close")) {
                holder.mortgageStatusTV.setTextColor(ContextCompat.getColor(mContext, R.color.color_broker_responce_pending));
                holder.mortgageStatusTV.setTypeface(holder.mortgageStatusTV.getTypeface(), Typeface.ITALIC);
                holder.mortgageStatusTV.setText("{fa-user} Broker Response Pending");
            } else if (mortgageArrayHolderList.get(position).getMortgage_opportunity_status().equalsIgnoreCase("available")) {
                holder.mortgageStatusTV.setTextColor(ContextCompat.getColor(mContext, R.color.color_opportunity_available));
                holder.mortgageStatusTV.setText("{fa-dollar} Opportunity Available");
            } else {
                holder.mortgageStatusTV.setText("");
            }

            /*
            * Set Image based on condition
            * */
            if (mortgageArrayHolderList.get(position).getLender_id() != null && mortgageArrayHolderList.get(position).getLender_id().equalsIgnoreCase("1000")) {
                holder.otherTV.setText("Other");
                holder.otherTV.setVisibility(View.VISIBLE);
                holder.mortgageImageView.setVisibility(View.GONE);
            } else if (mortgageArrayHolderList.get(position).getLender_id() != null) {
                holder.otherTV.setVisibility(View.GONE);
                holder.mortgageImageView.setVisibility(View.VISIBLE);
                try {
                    holder.mortgageImageView.setImageBitmap(globalMethods.base64(globalMethods.spiltString(mortgageArrayHolderList.get(position).getLender_logo(), ",")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (mortgageArrayHolderList.get(position).getLender_id() == null) {
                holder.mortgageImageView.setVisibility(View.VISIBLE);
                holder.otherTV.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return mortgageArrayHolderList.size();
    }


}
