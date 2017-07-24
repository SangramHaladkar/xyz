package com.app.monitormymortgage.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.DataHolderClasses.MortgageHolder;
import com.app.monitormymortgage.R;

import java.util.List;

/**
 * Created by Sangram on 05/05/16.
 */
public class MortgageAdapter extends BaseAdapter {

    Context context;
    private Activity activity;
    private String[] data;
    List<MortgageHolder> mortgageHolderList;

    public MortgageAdapter(Context context, List<MortgageHolder> mortgageHolderList) {
        this.context = context;
        this.mortgageHolderList = mortgageHolderList;
    }

    @Override
    public int getCount() {
        return this.mortgageHolderList.size();
    }

    static class ViewHolderItem {
        ImageView mortgageImage;
        TextView mortgageName;
        TextView mortgageStatus;
        TextView otherTextView;
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
        MortgageHolder mortgageHolder = this.mortgageHolderList.get(position);
        ViewHolderItem holder = new ViewHolderItem();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mortgage_list_cell, null);
            holder.mortgageImage = (ImageView) convertView.findViewById(R.id.lenderLogoImageView);
            holder.mortgageName = (TextView) convertView.findViewById(R.id.mortgageNameTextView);
            holder.mortgageStatus = (TextView) convertView.findViewById(R.id.mortgageStatusTextView);
            holder.otherTextView = (TextView) convertView.findViewById(R.id.otherTextView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderItem) convertView.getTag();
        }
        holder.mortgageName.setText(mortgageHolder.getMortgageTitle());
        holder.mortgageStatus.setText(mortgageHolder.getMortgageOppStatus());

        if (mortgageHolder.getLenderId() != null && mortgageHolder.getLenderId().equalsIgnoreCase("1000")) {
            holder.otherTextView.setText("Other");
            holder.otherTextView.setVisibility(View.VISIBLE);
            holder.mortgageImage.setVisibility(View.GONE);
        } else if (mortgageHolder.getLenderId() != null) {
            holder.otherTextView.setVisibility(View.GONE);
            holder.mortgageImage.setVisibility(View.VISIBLE);
            try {
                holder.mortgageImage.setImageBitmap(base64(mortgageHolder.getLenderLogo()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (mortgageHolder.getLenderId() == null) {
            holder.mortgageImage.setVisibility(View.VISIBLE);
            holder.otherTextView.setVisibility(View.GONE);
            if (mortgageHolder.getQuote_type_code() != null && mortgageHolder.getQuote_type_code().equalsIgnoreCase("1")) {
                holder.mortgageImage.setImageResource(R.drawable.preapproval);
            } else if (mortgageHolder.getQuote_type_code() != null && mortgageHolder.getQuote_type_code().equalsIgnoreCase("2")) {
                holder.mortgageImage.setImageResource(R.drawable.purchase);
            }
        }


        if (mortgageHolder.getMortgageOppStatus().equalsIgnoreCase("open") && !mortgageHolder.getOppCreatedDate().equalsIgnoreCase("")) {
            holder.mortgageStatus.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.mortgageStatus.setTextSize(12f);
            holder.mortgageStatus.setText("{fa-history} Last Opportunity " + GlobalMethods.convertMilliSecondsToDate(mortgageHolder.getOppCreatedDate()));
        } else if (mortgageHolder.getMortgageOppStatus().equalsIgnoreCase("close")) {
            holder.mortgageStatus.setTextColor(ContextCompat.getColor(context, R.color.color_broker_responce_pending));
            holder.mortgageStatus.setTypeface(holder.mortgageStatus.getTypeface(), Typeface.ITALIC);
            holder.mortgageStatus.setText("{fa-user} Broker Response Pending");
        } else if (mortgageHolder.getMortgageOppStatus().equalsIgnoreCase("available")) {
            holder.mortgageStatus.setTextColor(ContextCompat.getColor(context, R.color.color_opportunity_available));
            holder.mortgageStatus.setText("{fa-dollar} Opportunity Available");
        } else {
            holder.mortgageStatus.setText("");
        }
        return convertView;
    }

    public Bitmap base64(String base64String) {

        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

//    public String convertMilliSecondsToDate(String milliseconds) {
//        long millisecond = Long.parseLong(milliseconds);
//        String dateString = DateFormat.format("MMM d,yyyy", new Date(millisecond)).toString();
//        if (CommonConstants.mDebug) Log.v("TAG", dateString);
//        return dateString;
//    }
}