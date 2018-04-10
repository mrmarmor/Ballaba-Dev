package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachment;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 10/04/2018.
 */

public class AttachmentsRecyclerAdapter extends RecyclerView.Adapter<AttachmentsRecyclerAdapter.ViewHolder>{

    //private Resources res;
    //private ArrayList<PropertyAttachment> items;
    private ArrayList<String> attachIds;
    private Context context;
    public ArrayList<AttachmentsRecyclerAdapter.ViewHolder> parsedItems;
    //public HashMap<String, String> parsedItems;

    public AttachmentsRecyclerAdapter(Context context, ArrayList<String>/*ArrayList<PropertyAttachment>*/ attachIds) {
        this.context = context;
        this.attachIds = attachIds;
        this.parsedItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public AttachmentsRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View chipsView = LayoutInflater.from(context).inflate(R.layout.chips_item, parent, false);
        AttachmentsRecyclerAdapter.ViewHolder viewHolder = new AttachmentsRecyclerAdapter.ViewHolder(new TextView(context));
        parsedItems.add(viewHolder);
        //res = context.getResources();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentsRecyclerAdapter.ViewHolder holder, int position) {
        //PropertyAttachmentAddonEntity a = PropertyAttachmentsAddonsHolder.getInstance().getAttachments();
        PropertyAttachment.Type propertyAttachment = PropertyAttachment.Type.getTypeById(attachIds.get(position));

        if (propertyAttachment != null) {
            holder.textView.setText(propertyAttachment.getTitle());
            holder.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(propertyAttachment.getIcon(), 0, 0, 0);
            holder.textView.setPaddingRelative(8, 0, 0, 0);
            //holder.formattedTitle = propertyAttachment.formattedTitle;
            //holder.originalTitle = propertyAttachment.title;
            //holder.attachment.id = propertyAttachment.id;
        }

    }

    @Override
    public int getItemCount() {
        return attachIds.size();
    }

  /*  public String getOriginalTitleByFormatted(String formatted){
        for (PropertyAttachment item : items) {
            if(item.formattedTitle.equals(formatted)){
                return item.title;
            }
        }
        return null;
    }*/

    /*public AttachmentsRecyclerAdapter.ViewHolder getHolderByOriginalTitle(String title){
        for (AttachmentsRecyclerAdapter.ViewHolder holder : parsedItems) {
            if(holder.originalTitle.equals(title)){
                return holder;
            }
        }
        return null;
    }*/

    /*public AttachmentsRecyclerAdapter.ViewHolder getHolderByFormattedTitle(String formattedTitle){
        for (AttachmentsRecyclerAdapter.ViewHolder holder : parsedItems) {
            if(holder.formattedTitle.equals(formattedTitle)){
                return holder;
            }
        }
        return null;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        //public PropertyAttachment attachment;
        //public String formattedTitle, originalTitle, id, type;
        //public @DrawableRes int icon;

        public ViewHolder(TextView itemView) {
            super(itemView);
            textView = itemView;
        }
    }
}
