package com.example.michaelkibenko.ballaba.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.michaelkibenko.ballaba.Entities.PropertyAttachmentAddonEntity;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.UiUtils;

import java.util.ArrayList;

public class ChipsButtonsRecyclerViewAdapter extends RecyclerView.Adapter<ChipsButtonsRecyclerViewAdapter.ChipsButtonViewHolder>{

    private ArrayList<PropertyAttachmentAddonEntity> items;
    private Context context;
    private View.OnClickListener itemOnClickListener;
    public ArrayList<ChipsButtonViewHolder> parsedItems;
    public ChipsButtonsRecyclerViewAdapter(Context context, ArrayList<PropertyAttachmentAddonEntity> items, View.OnClickListener itemOnClickListener) {
        this.context = context;
        this.items = items;
        this.parsedItems = new ArrayList<>();
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ChipsButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chipsView = LayoutInflater.from(context).inflate(R.layout.chips_item, parent, false);
        ChipsButtonViewHolder chipsButtonViewHolder = new ChipsButtonViewHolder(chipsView);
        parsedItems.add(chipsButtonViewHolder);
        return chipsButtonViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChipsButtonViewHolder holder, int position) {
        PropertyAttachmentAddonEntity propertyAttachmentAddonEntity = items.get(position);
        holder.chips.setText(propertyAttachmentAddonEntity.formattedTitle);
        holder.formattedTitle = propertyAttachmentAddonEntity.formattedTitle;
        holder.originalTitle = propertyAttachmentAddonEntity.title;
        holder.id = propertyAttachmentAddonEntity.id;
        if(holder.chips.getTag() != null) {
            if (!holder.chips.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                holder.chips.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
            }
        }else{
            holder.chips.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
        }

        holder.chips.setOnClickListener(this.itemOnClickListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public String getOriginalTitleByFormatted(String formatted){
        for (PropertyAttachmentAddonEntity item : items) {
            if(item.formattedTitle.equals(formatted)){
                return item.title;
            }
        }
        return null;
    }

    public ChipsButtonViewHolder getHolderByOriginalTitle(String title){
        for (ChipsButtonViewHolder holder : parsedItems) {
            if(holder.originalTitle.equals(title)){
                return holder;
            }
        }
        return null;
    }

    public ChipsButtonViewHolder getHolderByFormattedTitle(String formattedTitle){
        for (ChipsButtonViewHolder holder : parsedItems) {
            if(holder.formattedTitle.equals(formattedTitle)){
                return holder;
            }
        }
        return null;
    }

    public class ChipsButtonViewHolder extends RecyclerView.ViewHolder{
        public Button chips;
        public String formattedTitle;
        public String originalTitle;
        public String id;
        public ChipsButtonViewHolder(View itemView) {
            super(itemView);
            chips = (Button)itemView;
        }
    }
}
