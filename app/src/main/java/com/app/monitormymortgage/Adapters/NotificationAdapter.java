package com.app.monitormymortgage.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.monitormymortgage.Common.GlobalMethods;
import com.app.monitormymortgage.DataHolderClasses.NotificationHolder;
import com.app.monitormymortgage.R;

import java.util.List;

/**
 * Created by Sangram on 22/05/16.
 */
public class NotificationAdapter extends BaseAdapter {
    Context context;
    private Activity activity;
    private String[] data;
    List<NotificationHolder> notificationHolderList;

    public NotificationAdapter(Context context, List<NotificationHolder> notificationHolderList) {
        this.context = context;
        this.notificationHolderList = notificationHolderList;
    }

    @Override
    public int getCount() {
        return this.notificationHolderList.size();
    }

    static class ViewHolderItem {
        TextView mortgageNameTextView;
        TextView opportunityNameTextView;
        TextView updatedAtTextView;
        ImageView navigationTextView;
        TextView remeinderTextView;
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
        NotificationHolder notificationHolder = this.notificationHolderList.get(position);
        ViewHolderItem viewHolderItem = new ViewHolderItem();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.notification_list_cell, null);
            viewHolderItem.mortgageNameTextView = (TextView) convertView.findViewById(R.id.mortgageNameTextView);
            viewHolderItem.opportunityNameTextView = (TextView) convertView.findViewById(R.id.opportunityNameTextView);
            viewHolderItem.updatedAtTextView = (TextView) convertView.findViewById(R.id.updatedAtTextView);
            viewHolderItem.navigationTextView = (ImageView) convertView.findViewById(R.id.navigationTextView);
            viewHolderItem.remeinderTextView = (TextView) convertView.findViewById(R.id.remeinderTextView);
            convertView.setTag(viewHolderItem);
        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }
        viewHolderItem.mortgageNameTextView.setText(notificationHolder.getMortgageTitle());

        if (notificationHolder.getOpportunityTitle().equalsIgnoreCase("Saving")) {
            viewHolderItem.opportunityNameTextView.setText("Savings" + context.getString(R.string.opportunity));
        } else if (notificationHolder.getNotification_type_code().equalsIgnoreCase("3")) {
            viewHolderItem.opportunityNameTextView.setText(notificationHolder.getNotification_title());
        } else {
            viewHolderItem.opportunityNameTextView.setText(notificationHolder.getOpportunityTitle() + context.getString(R.string.opportunity));
        }

        if (notificationHolder.getType().equalsIgnoreCase("2")) {
            viewHolderItem.remeinderTextView.setText(context.getString(R.string.remindar));
        }else if(notificationHolder.getType().equalsIgnoreCase("4"))
        {
            viewHolderItem.remeinderTextView.setText(context.getString(R.string.remindar));
        }else if (notificationHolder.getType().equalsIgnoreCase("3")) {
            viewHolderItem.remeinderTextView.setText("");
        } else {
            viewHolderItem.remeinderTextView.setText(context.getString(R.string.available));
        }

        if (notificationHolder.getOpporCreatedDate().length() > 0)
            viewHolderItem.updatedAtTextView.setText(GlobalMethods.convertMilliSecondsToDate(notificationHolder.getOpporCreatedDate()));
        else
            viewHolderItem.updatedAtTextView.setText("");

        return convertView;

    }


//    public String convertMilliSecondsToDate(String milliseconds) {
//        long millisecond = Long.parseLong(milliseconds);
//        String dateString = DateFormat.format("MMM d,yyyy", new Date(millisecond)).toString();
//        Log.v("TAG", dateString);
//        return dateString;
//    }
}
