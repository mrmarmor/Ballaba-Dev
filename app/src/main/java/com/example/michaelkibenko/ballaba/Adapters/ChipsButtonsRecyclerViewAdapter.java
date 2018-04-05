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
    public ChipsButtonsRecyclerViewAdapter(Context context, ArrayList<PropertyAttachmentAddonEntity> items, View.OnClickListener itemOnClickListener) {
        this.context = context;
        this.items = items;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ChipsButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chipsView = LayoutInflater.from(context).inflate(R.layout.chips_item, parent, false);
        return new ChipsButtonViewHolder(chipsView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChipsButtonViewHolder holder, int position) {
        holder.chips.setText(items.get(position).formattedTitle);
        if(holder.chips.getTag() != null) {
            if (!holder.chips.getTag().equals(UiUtils.ChipsButtonStates.PRESSED)) {
                holder.chips.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
            }
        }else{
            holder.chips.setTag(UiUtils.ChipsButtonStates.NOT_PRESSED);
        }

        holder.chips.setOnClickListener(this.itemOnClickListener);
//        holder.chips.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                uiUtils.onChipsButtonClick((Button)v);
//            }
//        });
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ChipsButtonViewHolder extends RecyclerView.ViewHolder{
        public Button chips;
        public ChipsButtonViewHolder(View itemView) {
            super(itemView);
            chips = (Button)itemView;
        }
    }
}
