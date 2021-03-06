package com.app.monitormymortgage.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.app.monitormymortgage.DataHolderClasses.ObjectItem;
import com.app.monitormymortgage.R;

import java.util.List;

/**
 * Created by Sangram on 02/05/16.
 */
public class ArrayAdapterItem extends BaseAdapter{

    Context mContext;
    int layoutResourceId;
    List<Bitmap> objectItemListBitmap;
    List<ObjectItem> objectItemList;

    public ArrayAdapterItem(Context mContext, int layoutResourceId,List<Bitmap> ItemList,List<ObjectItem> objectItems) {
    this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.objectItemListBitmap = ItemList;
        this.objectItemList=objectItems;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        ObjectItem objectItem = objectItemList.get(position);


        // get the TextView and then set the text (item name) and tag (item ID) values
        ImageView textViewItem = (ImageView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setImageBitmap(objectItem.getImageBitmap());
        return convertView;

    }
    @Override
    public int getCount() {
        return this.objectItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.objectItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



}
