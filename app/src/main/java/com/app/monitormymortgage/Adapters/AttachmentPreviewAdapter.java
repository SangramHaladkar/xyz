package com.app.monitormymortgage.Adapters;

/**
 * Created by Sangram on 02/06/16.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.monitormymortgage.DataHolderClasses.AttachmentFileData;
import com.app.monitormymortgage.R;

import java.util.ArrayList;
import java.util.List;

public class AttachmentPreviewAdapter extends RecyclerView.Adapter<AttachmentPreviewAdapter.MyViewHolder> {

    ArrayList<AttachmentFileData> filesDataList;
    Context aContext;
    private RecyclerViewListeners recyclerViewListeners;

    public interface RecyclerViewListeners {
        void onRecycleItemClick(ArrayList<AttachmentFileData> filesDataList, int position);
       void onCloseIconClick(ArrayList<AttachmentFileData> filesDataList, int position);
    }

    public void setCallBack(RecyclerViewListeners listener) {
        this.recyclerViewListeners = listener;
    }

    public AttachmentPreviewAdapter(Context context, ArrayList<AttachmentFileData> filesData) {
        this.filesDataList = filesData;
        this.aContext = context;
    }

    public void setFilesDataList(List<AttachmentFileData> filesDataLst) {
        if (this.filesDataList != null) {
            this.filesDataList.clear();
            this.filesDataList.addAll(filesDataLst);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(aContext).inflate(R.layout.custom_image_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //getFileBase64String()
        String temp=filesDataList.get(position).getDocId();
        String lenderStr=filesDataList.get(position).getThumbNailBase64String();
        if(lenderStr.contains("data:image/")) {
            String[] arrayString = filesDataList.get(holder.getAdapterPosition()).getThumbNailBase64String().split("\\,");
            if (arrayString.length > 0) {
                lenderStr = arrayString[1];
            }
            holder.imageViewFile.setImageBitmap(decodeBase64String(lenderStr));
            Log.v("TAG doc id",filesDataList.get(position).getDocId());
        }else if(lenderStr.contains("pdf"))
        {
            holder.imageViewFile.setImageResource(R.drawable.pdf);
        }else
        {
            holder.imageViewFile.setImageBitmap(decodeBase64String(filesDataList.get(position).getThumbNailBase64String()));
        }
        holder.imageViewFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewListeners.onRecycleItemClick(filesDataList, position);
            }
        });
//        holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                recyclerViewListeners.onCloseIconClick(filesDataList, position);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return filesDataList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final ImageView imageViewFile;
        public final ImageView cancelBtn;


        public MyViewHolder(View view) {
            super(view);
            mView = itemView;
            imageViewFile = (ImageView) itemView.findViewById(R.id.imageViewFile);
            cancelBtn = (ImageView) itemView.findViewById(R.id.cancelBtn);
            cancelBtn.setVisibility(View.GONE);
        }
    }
    private Bitmap decodeBase64String(String base64String){
        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }




}
