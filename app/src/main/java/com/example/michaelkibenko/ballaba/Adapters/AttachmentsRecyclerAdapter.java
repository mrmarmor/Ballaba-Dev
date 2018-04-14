package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.example.michaelkibenko.ballaba.Entities.BallabaPropertyFull;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachment;
import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.Holders.PropertyAttachmentsAddonsHolder;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.databinding.ActivityPropertyDescriptionBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsBinding;
import com.example.michaelkibenko.ballaba.databinding.PropertyDescriptionAttachmentsExtendedBinding;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 10/04/2018.
 */
//TODO this class is probably useless
public class AttachmentsRecyclerAdapter {//extends RecyclerView.Adapter<AttachmentsRecyclerAdapter.ViewHolder>{

    //private Resources res;
    //private ArrayList<PropertyAttachment> items;
    /*private ArrayList<String> attachIds;
    private Context context;
    private PropertyDescriptionAttachmentsBinding binder;
    private PropertyDescriptionAttachmentsExtendedBinding binderExt;
    public ArrayList<AttachmentsRecyclerAdapter.ViewHolder> parsedItems;
    //public HashMap<String, String> parsedItems;

    public AttachmentsRecyclerAdapter(Context context, PropertyDescriptionAttachmentsBinding binder
            , PropertyDescriptionAttachmentsExtendedBinding binderExt, ArrayList<String>*//*ArrayList<PropertyAttachment>*//* attachIds) {
        this.context = context;
        this.binder = binder;
        this.binderExt = binderExt;
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
        PropertyAttachment.Type propertyAttachment = PropertyAttachment.Type.getTypeById(attachIds.get(position) );

        if (propertyAttachment != null) {
            holder.textView.setText(propertyAttachment.getTitle());
            holder.textView.setCompoundDrawablesRelativeWithIntrinsicBounds(propertyAttachment.getIcon(), 0, 0, 0);
            holder.textView.setPaddingRelative(8, 0, 0, 0);
            holder.textView.setTextAppearance(R.style.property_description_textViews);
            holder.textView.setPaddingRelative(0, 20, 0, 0);

            if (position > attachIds.size() / 2) {
                holder.textView.setX(-binder.propertyDescriptionAttachmentsSizeTextView.getX()/2);
            } else {
                holder.textView.setPaddingRelative(16, 20, 0, 0);
            }
        }

    }

    @Override
    public int getItemCount() {
        return attachIds.size();
    }

  *//*  public String getOriginalTitleByFormatted(String formatted){
        for (PropertyAttachment item : items) {
            if(item.formattedTitle.equals(formatted)){
                return item.title;
            }
        }
        return null;
    }*//*

    *//*public AttachmentsRecyclerAdapter.ViewHolder getHolderByOriginalTitle(String title){
        for (AttachmentsRecyclerAdapter.ViewHolder holder : parsedItems) {
            if(holder.originalTitle.equals(title)){
                return holder;
            }
        }
        return null;
    }*//*

    *//*public AttachmentsRecyclerAdapter.ViewHolder getHolderByFormattedTitle(String formattedTitle){
        for (AttachmentsRecyclerAdapter.ViewHolder holder : parsedItems) {
            if(holder.formattedTitle.equals(formattedTitle)){
                return holder;
            }
        }
        return null;
    }*//*

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        //public PropertyAttachment attachment;
        //public String formattedTitle, originalTitle, id, type;
        //public @DrawableRes int icon;

        public ViewHolder(TextView itemView) {
            super(itemView);
            textView = itemView;
        }
    }*/
}
