package com.app.monitormymortgage.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.DataHolderClasses.OpportunityDataPassed;
import com.app.monitormymortgage.R;
import java.util.*;

/**
 * Created by Sangram on 27/06/17.
 */

class OpportunityRecyclerViewAdapter extends RecyclerView.Adapter<OpportunityRecyclerViewAdapter.ViewHolder> {


    private List<OpportunityDataPassed> passedArrayList;
    private Context context;


    /* Opportunity Adapter constructor
    * @param OpportunityData ArrayList
    * */
    OpportunityRecyclerViewAdapter(List<OpportunityDataPassed> passedArrayList, Context context) {
        this.passedArrayList = passedArrayList;
        this.context=context;
    }

    /*
    * ViewHolder class
    * */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView opportunityTV;

        ViewHolder(View view) {
            super(view);
            opportunityTV=(TextView)view.findViewById(R.id.opportunityTV);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.opportunity_cell, parent, false);
        return new OpportunityRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.opportunityTV.setText(passedArrayList.get(position).getType()+ context.getResources().getString(R.string.expired_on) + GlobalMethods.convertMilliSecondsToDate(passedArrayList.get(position).getStatus_updated_at()));
    }

    @Override
    public int getItemCount() {
        return passedArrayList.size();
    }


}
