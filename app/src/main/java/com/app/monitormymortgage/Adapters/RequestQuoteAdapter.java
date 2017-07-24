package com.app.monitormymortgage.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.monitormymortgage.DataHolderClasses.RequestQuoteData;
import com.app.monitormymortgage.R;

import java.util.List;

/**
 * Created by Sangram on 12/01/17.
 */

public class RequestQuoteAdapter extends BaseAdapter {
    Context context;
    private Activity activity;
    private String[] data;
    List<RequestQuoteData> requestQuoteDataList;

    public RequestQuoteAdapter(Context context, List<RequestQuoteData> requestQuoteDataList) {
        this.context = context;
        this.requestQuoteDataList = requestQuoteDataList;
    }

    static class ViewHolderItem {
        ImageView mortgageImage;
        TextView mortgageName;
        TextView mortgageStatus;
    }


    @Override
    public int getCount() {
        return this.requestQuoteDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RequestQuoteData mortgageHolder = this.requestQuoteDataList.get(position);
        RequestQuoteAdapter.ViewHolderItem holder = new RequestQuoteAdapter.ViewHolderItem();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.request_quote_cell, null);
            holder.mortgageImage = (ImageView) convertView.findViewById(R.id.lenderLogoImageView);
            holder.mortgageName = (TextView) convertView.findViewById(R.id.mortgageNameTextView);
            holder.mortgageStatus = (TextView) convertView.findViewById(R.id.mortgageStatusTextView);
            convertView.setTag(holder);
        } else {
            holder = (RequestQuoteAdapter.ViewHolderItem) convertView.getTag();
        }
        holder.mortgageName.setText(mortgageHolder.getTitle());
        if (mortgageHolder.getMortgage_opportunity_status().equalsIgnoreCase("close")) {
            holder.mortgageStatus.setTextColor(ContextCompat.getColor(context,R.color.color_broker_responce_pending));
            holder.mortgageStatus.setText("{fa-user} Broker Response Pending");
        }

        if (mortgageHolder.getQuote_type_code().equalsIgnoreCase("1"))
        {
            holder.mortgageImage.setImageResource(R.drawable.preapproval);
        }else if (mortgageHolder.getQuote_type_code().equalsIgnoreCase("2")){
            holder.mortgageImage.setImageResource(R.drawable.purchase);
        }
        return convertView;
    }
}
